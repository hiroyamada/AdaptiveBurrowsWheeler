package adaptive_huffman;

import util.BinaryFileInputStream;
import util.BinaryFileOutputStream;

import java.io.IOException;

public class HuffmanDecoder {
    public static void main(String[] args) throws IOException {
        BinaryFileOutputStream output = new BinaryFileOutputStream(args[1]);

        new HuffmanTree().decode(args[0], output);
    }
}
