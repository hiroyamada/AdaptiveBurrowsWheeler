package adaptive_huffman;

import org.junit.Test;
import rle.RunLengthDecoder;
import rle.RunLengthEncoder;

import java.io.FileInputStream;

import static org.junit.Assert.assertEquals;
import static util.TestUtils.compareFiles;

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


}
