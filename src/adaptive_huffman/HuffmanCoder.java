package adaptive_huffman;

import util.BinaryFileOutputStream;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class HuffmanCoder {
    public static void main(String[] args) {
        BufferedInputStream input;
        try {
            input = new BufferedInputStream(new FileInputStream(args[0]));
            new HuffmanTree().encode(input, args[1]);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
