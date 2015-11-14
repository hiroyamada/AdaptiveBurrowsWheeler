package rle;

import static org.mockito.Mockito.*;


import org.junit.Test;
import util.BinaryFileInputStream;

import java.io.FileInputStream;

import static org.junit.Assert.*;

public class RunLengthIntegrationTest {
    private static String ORIGINAL_FILE_PATH = "donner_unformated.txt";
    private static String ENCODED_FILE_PATH = "run_length_encoded.txt";
    private static String DECODED_FILE_PATH = "run_length_decoded.txt";

    @Test
    public void testCompressAndDecompress() throws Exception {
        String[] encoderArgs = {ORIGINAL_FILE_PATH, ENCODED_FILE_PATH};
        RunLengthEncoder.main(encoderArgs);

        String[] decoderArgs = {ENCODED_FILE_PATH, DECODED_FILE_PATH};
        RunLengthDecoder.main(decoderArgs);

        FileInputStream original = new FileInputStream(ORIGINAL_FILE_PATH);
        FileInputStream decoded = new FileInputStream(DECODED_FILE_PATH);

        int originalNextByte = original.read();
        int decodedNextByte = decoded.read();

        while (originalNextByte != -1 && decodedNextByte != -1) {
            assertEquals(originalNextByte, decodedNextByte);
            originalNextByte = original.read();
            decodedNextByte = decoded.read();
        }

        assertEquals(originalNextByte, -1);
        assertEquals(decodedNextByte, -1);
    }
}
