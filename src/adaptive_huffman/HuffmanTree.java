package adaptive_huffman;

import util.AlphabetUtil;
import util.BinaryFileInputStream;
import util.BinaryFileOutputStream;
import util.IllegalCharacterException;

import java.io.IOException;
import java.util.*;

class HuffmanTree {
    private Node root;
    private HashMap<Integer, Node> lookup;
    private ArrayList<Node> gallegerOrder;
    private AlphabetUtil alphabetUtil;

    public HuffmanTree(AlphabetUtil alphabetUtil) {
        this.root = new Leaf(0, null, null); // voidSymbol
        this.lookup = new HashMap<>();
        this.lookup.put(null, this.root);
        this.gallegerOrder = new ArrayList<>();
        this.gallegerOrder.add(this.root);
        this.alphabetUtil = alphabetUtil;
    }

    private void replaceChildWith(Tree parent, Node currentChild, Node newChild) {
        newChild.setParent(parent);
        if (parent.getLeft() == currentChild) {
            parent.setLeft(newChild);
        } else if (parent.getRight() == currentChild) {
            parent.setRight(newChild);
        } else {
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

    public void encode(BinaryFileInputStream is, BinaryFileOutputStream fs) throws IOException, IllegalCharacterException {
        int nextInt;
        while ((nextInt = alphabetUtil.readNextCharacterFromSource(is)) != alphabetUtil.getEOF()) {
            writeCodeForInt(nextInt, fs);
            this.update(nextInt);
        }
        writeCodeForInt(alphabetUtil.getEOF(), fs);
    }

    private void writeCodeForInt(int i, BinaryFileOutputStream fs) throws IOException, IllegalCharacterException {
        Boolean[] code = codeFromByte(lookup.containsKey(i) ? i : null);
        for (boolean b : code) {
            fs.write(b ? 1 : 0);
        }

        if (!lookup.containsKey(i))
            alphabetUtil.writeCharacterToCode(i, fs);
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

    public void decode(BinaryFileInputStream fe, BinaryFileOutputStream fs) throws IOException, IllegalCharacterException {
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
                int nextInt = alphabetUtil.readNextCharacterFromCode(fe);
                if (this.lookup.containsKey(nextInt))
                    throw new IllegalStateException("received void symbol although we know the following symbol");

                this.update(nextInt);

                if (alphabetUtil.isEOF(nextInt)) {
                    alphabetUtil.onEOF(fs);
                    return;
                }

                alphabetUtil.writeCharacterToSource(nextInt, fs);

            } else {
                this.update(l.getValue());
                alphabetUtil.writeCharacterToSource(l.getValue(), fs);
            }
        }
    }

    private void update(int i) {
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

            int voidIndex = gallegerOrder.indexOf(voidLeaf);
            gallegerOrder.set(voidIndex, newTree);
            gallegerOrder.add(voidIndex + 1, newLeaf);
            gallegerOrder.add(voidIndex + 2, voidLeaf);

            this.lookup.put(i, newLeaf);
            node = voidLeafParent;
        }

        while (node != null) {
            Node initialNode = node;

            for (int c = 0; c < gallegerOrder.size(); c++) {
                Node n = gallegerOrder.get(c);
                if (n.getWeight() == node.getWeight()) {
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
                throw new IllegalStateException("something went wrong");
            }
        }

        for (int c = 0; c < gallegerOrder.size() - 1; c++) {
            if (gallegerOrder.get(c).getWeight() < gallegerOrder.get(c + 1).getWeight()) {
                System.out.println(Arrays.toString(gallegerOrder.toArray()));
                throw new IllegalStateException("galleger order broken");
            }
        }

    }
}
