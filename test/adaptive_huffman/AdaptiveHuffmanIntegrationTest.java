package adaptive_huffman;

import org.junit.Test;

import static util.TestUtils.compareFiles;

public class AdaptiveHuffmanIntegrationTest {
    private static String ORIGINAL_FILE_PATH = "binary_file";
    private static String HUFFMAN_ENCODED_FILE_PATH = "huffman_encoded.txt";
    private static String HUFFMAN_DECODED_FILE_PATH = "huffman_decoded.txt";

    @Test
    public void testCompressAndDecompress() throws Exception {
        HuffmanEncoder.huffmanEncode(ORIGINAL_FILE_PATH, HUFFMAN_ENCODED_FILE_PATH, new HuffmanAlphabetUtil());
        HuffmanDecoder.huffmanDecode(HUFFMAN_ENCODED_FILE_PATH, HUFFMAN_DECODED_FILE_PATH, new HuffmanAlphabetUtil());

        compareFiles(ORIGINAL_FILE_PATH, HUFFMAN_DECODED_FILE_PATH);
    }
}
