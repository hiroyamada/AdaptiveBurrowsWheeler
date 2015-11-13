package adaptive_huffman;

class Leaf extends Node {
    private final Byte value;

    public Leaf(int weight, Tree parent, Byte value) {
        super(weight, parent);
        this.value = value;
    }

    public byte getValue() {
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
