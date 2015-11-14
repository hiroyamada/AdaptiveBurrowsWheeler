package adaptive_huffman;

import rle.RunLengthAlphabetUtil;
import util.AlphabetUtil;
import util.BinaryFileInputStream;
import util.BinaryFileOutputStream;
import util.IllegalCharacterException;

import java.io.FileNotFoundException;
import java.io.IOException;

public class HuffmanEncoder {
    public static void main(String[] args) {
        huffmanEncode(args[0], args[1], new RunLengthAlphabetUtil());
    }

    public static void huffmanEncode(String inPath, String outPath, AlphabetUtil alphabetUtil) {
        try (BinaryFileInputStream input = new BinaryFileInputStream(inPath);
             BinaryFileOutputStream output = new BinaryFileOutputStream(outPath)) {
            new HuffmanTree(alphabetUtil).encode(input, output);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalCharacterException e) {
            e.printStackTrace();
        }
    }
}
