package adaptive_huffman;

import rle.RunLengthUtil;
import util.BinaryFileInputStream;
import util.BinaryFileOutputStream;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.*;

class HuffmanTree {
    private Node root;
    private HashMap<Integer, Node> lookup;
    private ArrayList<Node> gallegerOrder;

    public HuffmanTree() {
        this.root = new Leaf(0, null, null); // voidSymbol
        this.lookup = new HashMap<>();
        this.lookup.put(null, this.root);
        this.gallegerOrder = new ArrayList<>();
        this.gallegerOrder.add(this.root);
    }

    private void replaceChildWith(Tree parent, Node currentChild, Node newChild) {
        newChild.setParent(parent);
        if (parent.getLeft() == currentChild) {
            parent.setLeft(newChild);
        } else if (parent.getRight() == currentChild) {
            parent.setRight(newChild);
        } else {
//            System.out.println(parent);
//            System.out.println(currentChild);
            throw new IllegalStateException("parent does not have specified child");
        }
    }

    private void replaceChildOrUpdateRoot(Tree parent, Node currentChild, Node newChild) {
        if (parent == null) {
            this.root = newChild;
            newChild.setParent(null);
        } else {
            replaceChildWith(parent, currentChild, newChild);
        }
    }

    public void encode(BufferedInputStream is, String outputFileName) throws IOException {
        BinaryFileOutputStream fs = new BinaryFileOutputStream(outputFileName);
        int nextInt;
        while ((nextInt = RunLengthUtil.readTwoBytes(is)) != -1) {
            if (nextInt < 0 || nextInt > RunLengthUtil.MAX_VALID_VALUE)
                throw new IllegalStateException("Attempting to write invalid value " + nextInt);

            writeCodeForInt(nextInt, fs);
            this.update(nextInt);
        }
        writeCodeForInt(RunLengthUtil.EOF, fs);
        fs.close();
    }

    private void writeCodeForInt(int i, BinaryFileOutputStream fs) throws IOException {
        Boolean[] code = codeFromByte(lookup.containsKey(i) ? i : null);
        for (boolean b : code) {
            fs.write(b ? 1 : 0);
        }

        System.out.println(i);

        if (!lookup.containsKey(i))
            fs.write(i, 9);
    }

    private Boolean[] codeFromByte(Integer i) {
        Node n = lookup.get(i);
        List<Boolean> l = new LinkedList<>();

        while (n.getParent() != null) {
            Tree t = n.getParent();
            l.add(0, t.getLeft() == n);
            n = n.getParent();
        }

        Boolean[] returnArray = new Boolean[l.size()];
        l.toArray(returnArray);
        return returnArray;
    }

    public void decode(String inputFileName, BinaryFileOutputStream fs) throws IOException {
        BinaryFileInputStream fe = new BinaryFileInputStream(inputFileName);
        while (true) {
            Node currentNode = this.root;
            while (currentNode instanceof Tree) {
                Tree t = (Tree) currentNode;
                int nextBit = fe.read();
                if (nextBit == 1) {
                    currentNode = t.getLeft();
                } else if (nextBit == 0) {
                    currentNode = t.getRight();
                } else if (nextBit == -1 && currentNode == this.root) {
                    return; // reached EOF and have not started reading the next symbol
                } else {
                    System.out.println(currentNode);
                    throw new IllegalStateException("Reached EOF although still traversing tree");
                }
            }

            Leaf l = (Leaf) currentNode;
            if (l.isVoidSymbol()) {
                int nextInt = fe.read(9);
                System.out.println("new symbol: " + nextInt);
                if (this.lookup.containsKey(nextInt))
                    throw new IllegalStateException("received void symbol although we know the following symbol");
                if (nextInt < -1 || nextInt > RunLengthUtil.MAX_VALID_VALUE)
                    throw new IllegalStateException("read invalid character with value " + nextInt);

                if (nextInt == RunLengthUtil.EOF)
                    return;

                this.update(nextInt);
                fs.write(nextInt, 16);
            } else {
//                System.out.println(l.getValue());
                this.update(l.getValue());
                fs.write(l.getValue(), 16);
            }
        }
    }

    public void update(int i) {
        Node node;
        if (this.lookup.containsKey(i)) {
            node = lookup.get(i);
        } else {
            Leaf voidLeaf = (Leaf) this.lookup.get(null);
            Tree voidLeafParent = voidLeaf.getParent();
            Leaf newLeaf = new Leaf(1, null, i); // parent null for now
            Tree newTree = new Tree(1, voidLeafParent, newLeaf, voidLeaf);
            newLeaf.setParent(newTree);
            voidLeaf.setParent(newTree);

            if (voidLeafParent != null) {
                if (voidLeafParent.getLeft() == voidLeaf)
                    voidLeafParent.setLeft(newTree);
                else if (voidLeafParent.getRight() == voidLeaf)
                    voidLeafParent.setRight(newTree);
                else
                    throw new IllegalStateException("sth wrong");
            } else {
                this.root = newTree;
            }

//            replaceChildOrUpdateRoot(voidLeafParent, voidLeaf, newTree);

            int voidIndex = gallegerOrder.indexOf(voidLeaf);
            gallegerOrder.set(voidIndex, newTree);
            gallegerOrder.add(voidIndex + 1, newLeaf);
            gallegerOrder.add(voidIndex + 2, voidLeaf);
//            System.out.println(Arrays.toString(gallegerOrder.toArray()));

            this.lookup.put(i, newLeaf);
            node = voidLeafParent;
        }

//            System.out.println("here");
        while (node != null) {
            Node initialNode = node;
//            System.out.println(node);
//            System.out.println(node.getParent());
//            System.out.println("blah");
//            System.out.println(Arrays.toString(gallegerOrder.toArray()));

            for (int c = 0; c < gallegerOrder.size(); c++) {
//                System.out.println(i);
                Node n = gallegerOrder.get(c);
                if (n.getWeight() == node.getWeight()) {
//                        System.out.println("bleh");
//                        System.out.println(n);
                    if (n != node) {

                        // The algorithm will try to swap a child and parent. This is expected behavior
                        // if the sibling of the child is a void node and thus has count 0.
                        if (n.getParent() == node || node.getParent() == n) {
                            Node child = n.getParent() == node ? n : node;
                            Tree parent = child.getParent();
                            if (parent.getLeft() == lookup.get(null) || parent.getRight() == lookup.get(null))
                                continue;

                            System.out.println(node);
                            System.out.println(n);
                            throw new IllegalStateException("trying to swap child and parent");
                        }

                        Tree originalNParent = n.getParent();
                        Tree originalNodeParent = node.getParent();

                        replaceChildOrUpdateRoot(originalNParent, n, node);
                        replaceChildOrUpdateRoot(originalNodeParent, node, n);

                        int leafIndex = gallegerOrder.indexOf(node);
                        Collections.swap(gallegerOrder, leafIndex, c);
                    }
                    node.incrementWeight();
                    node = node.getParent();
                    break;
                }
            }
            if (initialNode == node) {
//                System.out.println(Arrays.toString(gallegerOrder.toArray()));
//                System.out.println(initialNode);
                throw new IllegalStateException("something went wrong");
            }
        }
//            System.out.println("out of node null loop");

        for (int c = 0; c < gallegerOrder.size() - 1; c++) {
            if (gallegerOrder.get(c).getWeight() < gallegerOrder.get(c + 1).getWeight()) {
                System.out.println(Arrays.toString(gallegerOrder.toArray()));
                throw new IllegalStateException("galleger order broken");
            }
        }

    }
}
