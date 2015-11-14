package adaptive_huffman;

import org.junit.Test;
import rle.RunLengthDecoder;
import rle.RunLengthEncoder;

import java.io.FileInputStream;

import static org.junit.Assert.assertEquals;

public class RunLengthHuffmanIntegrationTest {
    private static String ORIGINAL_FILE_PATH = "donner_unformated.txt";
    private static String RUN_LENGTH_ENCODED_FILE_PATH = "run_length_encoded.txt";
    private static String HUFFMAN_ENCODED_FILE_PATH = "huffman_encoded.txt";
    private static String HUFFMAN_DECODED_FILE_PATH = "huffman_decoded.txt";
    private static String RUN_LENGTH_DECODED_FILE_PATH = "run_length_decoded.txt";

    @Test
    public void testCompressAndDecompress() throws Exception {
        String[] runLengthEncoderArgs = {ORIGINAL_FILE_PATH, RUN_LENGTH_ENCODED_FILE_PATH};
        RunLengthEncoder.main(runLengthEncoderArgs);

        String[] huffmanCoderArgs = {RUN_LENGTH_ENCODED_FILE_PATH, HUFFMAN_ENCODED_FILE_PATH};
        HuffmanEncoder.main(huffmanCoderArgs);

        String[] huffmanDecoderArgs = {HUFFMAN_ENCODED_FILE_PATH, HUFFMAN_DECODED_FILE_PATH};
        HuffmanDecoder.main(huffmanDecoderArgs);

        String[] runLengthDecoderArgs = {HUFFMAN_DECODED_FILE_PATH, RUN_LENGTH_DECODED_FILE_PATH};
        RunLengthDecoder.main(runLengthDecoderArgs);


        compareFiles(RUN_LENGTH_ENCODED_FILE_PATH, HUFFMAN_DECODED_FILE_PATH);
        compareFiles(ORIGINAL_FILE_PATH, RUN_LENGTH_DECODED_FILE_PATH);
    }

    private void compareFiles(String s1, String s2) throws Exception {
        FileInputStream f1 = new FileInputStream(s1);
        FileInputStream f2 = new FileInputStream(s2);


        int f1NextByte = f1.read();
        int f2NextByte = f2.read();

        while (f1NextByte != -1 && f2NextByte != -1) {
            assertEquals(f1NextByte, f2NextByte);
            f1NextByte = f1.read();
            f2NextByte = f2.read();
        }

        assertEquals(f1NextByte, -1);
        assertEquals(f2NextByte, -1);
    }

}
