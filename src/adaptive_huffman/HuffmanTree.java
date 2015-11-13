package adaptive_huffman;

import util.BinaryFileInputStream;
import util.BinaryFileOutputStream;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;

class HuffmanTree {
    private Node root;
    private HashMap<Byte, Node> lookup;
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
        // leave room for the byteCount
        fs.write(0, 32);
        int nextByteInt;
        int byteCount = 0;
        while ((nextByteInt = is.read()) != -1) {
            if (byteCount == Integer.MAX_VALUE)
                throw new IllegalArgumentException("sorry, file too big!");
            byteCount++;
            byte nextByte = (byte) nextByteInt;
            Boolean[] code = codeFromByte(lookup.containsKey(nextByte) ? nextByte : null);
            for (boolean b : code) {
                fs.write(b ? 1 : 0);
            }
            if (!lookup.containsKey(nextByte))
                fs.write(nextByteInt, 8);

            this.update(nextByte);
        }
        fs.close();

        RandomAccessFile raf = new RandomAccessFile(outputFileName, "rw");
        raf.seek(0);
        raf.writeInt(byteCount);
        raf.close();
    }

    private Boolean[] codeFromByte(Byte b) {
        Node n = lookup.get(b);
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
        // read the byte count using RandomAccessFile since BinaryFileStream stores bits in the
        // reverse order.
        RandomAccessFile raf = new RandomAccessFile(inputFileName, "r");
        int byteCount = raf.readInt();
        raf.close();

        BinaryFileInputStream fe = new BinaryFileInputStream(inputFileName);
        fe.read(32);
        for (int c = 0; c < byteCount; c++) {
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
                byte nextByte = (byte) fe.read(8);
//                System.out.println("new symbol: " + nextByte);
                if (nextByte == -1)
                    throw new IllegalStateException("not actually bad but most likely wrong");
                this.update(nextByte);
                fs.write(nextByte, 8);
            } else {
//                System.out.println(l.getValue());
                this.update(l.getValue());
                fs.write(l.getValue(), 8);
            }
        }
    }

    public void update(byte b) {
        Node node;
        if (this.lookup.containsKey(b)) {
            node = lookup.get(b);
        } else {
            Leaf voidLeaf = (Leaf) this.lookup.get(null);
            Tree voidLeafParent = (Tree) voidLeaf.getParent();
            Leaf newLeaf = new Leaf(1, null, b); // parent null for now
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

            this.lookup.put(b, newLeaf);
            node = voidLeafParent;
        }

//            System.out.println("here");
        while (node != null) {
            Node initialNode = node;
//            System.out.println(node);
//            System.out.println(node.getParent());
//            System.out.println("blah");
//            System.out.println(Arrays.toString(gallegerOrder.toArray()));

            for (int i = 0; i < gallegerOrder.size(); i++) {
//                System.out.println(i);
                Node n = gallegerOrder.get(i);
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

                        Tree originalNParent = (Tree) n.getParent();
                        Tree originalNodeParent = (Tree) node.getParent();

                        replaceChildOrUpdateRoot(originalNParent, n, node);
                        replaceChildOrUpdateRoot(originalNodeParent, node, n);

                        int leafIndex = gallegerOrder.indexOf(node);
                        Collections.swap(gallegerOrder, leafIndex, i);
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

        for (int i = 0; i < gallegerOrder.size() - 1; i++) {
            if (gallegerOrder.get(i).getWeight() < gallegerOrder.get(i + 1).getWeight()) {
                System.out.println(Arrays.toString(gallegerOrder.toArray()));
                throw new IllegalStateException("galleger order broken");
            }
        }

    }
}
