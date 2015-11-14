package adaptive_huffman;

class Leaf extends Node {
    private final Integer value;

    public Leaf(int weight, Tree parent, Integer value) {
        super(weight, parent);
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public boolean isVoidSymbol() {
        return this.value == null;
    }

    @Override
    public String toString() {
        return super.toString() + " " + this.value;
    }
}
