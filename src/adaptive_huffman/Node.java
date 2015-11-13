package adaptive_huffman;

public abstract class Node {
    private int weight;
    private Tree parent;

    public Node(int weight, Tree parent) {
        this.weight = weight;
        this.parent = parent;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Tree getParent() {
        return parent;
    }

    public void setParent(Tree parent) {
        this.parent = parent;
    }

    public void incrementWeight() {
        this.weight++;
    }

    @Override
    public String toString() {
        return super.toString() + " " + (this.parent == null ? null : this.parent.getAddress()) + " " + this.weight;
    }

    public String getAddress() {
        return super.toString();
    }
}
