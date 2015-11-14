package adaptive_bw;

import adaptive_huffman.HuffmanAlphabetUtil;
import adaptive_huffman.HuffmanDecoder;
import adaptive_huffman.HuffmanEncoder;
import bw.BWDetransformer;
import bw.BWTransformer;
import m2f.M2FDecoder;
import m2f.M2FEncoder;
import rle.RunLengthAlphabetUtil;
import rle.RunLengthDecoder;
import rle.RunLengthEncoder;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class AdaptiveBurrowsWheeler {
    private static final String TMP_DIR_PATH = "tmpDir/";
    private static final String BW_TRANSFORMED_PATH = TMP_DIR_PATH + "bw_transformed";
    private static final String M2F_ENCODED_PATH = TMP_DIR_PATH + "m2f_encoded";
    private static final String RUN_LENGTH_ENCODED_PATH = TMP_DIR_PATH + "rl_encoded";

    private static final String HUFFMAN_DECODED_PATH = TMP_DIR_PATH + "huffman_decoded";
    private static final String RUN_LENGTH_DECODED_PATH = TMP_DIR_PATH + "rl_decoded";
    private static final String M2F_DECODED_PATH = TMP_DIR_PATH + "m2f_decoded";

    public static void main(String[] args) {
        File tmpDir = new File(TMP_DIR_PATH);
        tmpDir.mkdir();

        try {
            ArrayList<String> argsList = new ArrayList<>(Arrays.asList(args));
            boolean decodeMode = false;
            if (argsList.get(0).equals("-d")) {
                decodeMode = true;
                argsList.remove(0);
            }

            boolean withRLE = false;
            if (argsList.get(0).equals("-r")) {
                withRLE = true;
                argsList.remove(0);
            }

            if (decodeMode) {
                decode(argsList.get(0), argsList.get(1), withRLE);
            } else {
                encode(argsList.get(0), argsList.get(1), withRLE);
            }

        } finally {
            recursiveDelete(tmpDir);
        }
    }

    public static void encode(String inPath, String outPath, boolean withRLE) {
        BWTransformer.bwTransform(inPath, BW_TRANSFORMED_PATH);
        M2FEncoder.m2fEncode(BW_TRANSFORMED_PATH, M2F_ENCODED_PATH);

        if (withRLE) {
            RunLengthEncoder.runLengthEncode(M2F_ENCODED_PATH, RUN_LENGTH_ENCODED_PATH);
            HuffmanEncoder.huffmanEncode(RUN_LENGTH_ENCODED_PATH, outPath, new RunLengthAlphabetUtil());
        } else {
            HuffmanEncoder.huffmanEncode(M2F_ENCODED_PATH, outPath, new HuffmanAlphabetUtil());
        }
    }

    public static void decode(String inPath, String outPath, boolean withRLE) {
        if (withRLE) {
            HuffmanDecoder.huffmanDecode(inPath, HUFFMAN_DECODED_PATH, new RunLengthAlphabetUtil());
            RunLengthDecoder.runLengthDecode(HUFFMAN_DECODED_PATH, RUN_LENGTH_DECODED_PATH);
        } else {
            HuffmanDecoder.huffmanDecode(inPath, HUFFMAN_DECODED_PATH, new HuffmanAlphabetUtil());
        }
        M2FDecoder.m2fDecode(RUN_LENGTH_DECODED_PATH, M2F_DECODED_PATH);
        BWDetransformer.bwDetransform(M2F_DECODED_PATH, outPath);
    }

    static void recursiveDelete(File file) {
        if (!file.exists())
            return;
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                recursiveDelete(f);
            }
        }
        file.delete();
    }
}
