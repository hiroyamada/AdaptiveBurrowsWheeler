package adaptive_huffman;

class Tree extends Node {
    private Node left;
    private Node right;

    public Tree(int weight, Tree parent, Node left, Node right) {
        super(weight, parent);
        this.left = left;
        this.right = right;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    @Override
    public String toString() {
        return super.toString() + " " + this.left.getAddress() + " " + this.right.getAddress();
    }
}
