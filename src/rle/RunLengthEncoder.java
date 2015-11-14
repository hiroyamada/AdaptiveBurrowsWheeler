package rle;

import util.BinaryFileOutputStream;
import util.IllegalCharacterException;

import static rle.RunLengthAlphabetUtil.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class RunLengthEncoder {
    private static RunLengthAlphabetUtil alphabetUtil = new RunLengthAlphabetUtil();

    public static void main(String[] args) {
        runLengthEncode(args[0], args[1]);
    }

    public static void runLengthEncode(String inPath, String outPath) {
        try (FileInputStream fileInputStream = new FileInputStream(inPath);
             BinaryFileOutputStream binaryFileOutputStream = new BinaryFileOutputStream(outPath)) {
            int prevByte = -1;
            int prevCount = 0;
            int nextByte;
            while ((nextByte = fileInputStream.read()) != -1) {
                if (nextByte == prevByte) {
                    prevCount++;
                } else {
                    writePrevious(prevByte, prevCount, binaryFileOutputStream);
                    prevByte = nextByte;
                    prevCount = 1;
                }
            }
            writePrevious(prevByte, prevCount, binaryFileOutputStream);
            alphabetUtil.writeCharacterToCode(alphabetUtil.getEOF(), binaryFileOutputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalCharacterException e) {
            e.printStackTrace();
        }
    }

    private static void writePrevious(int prevByte, int prevCount, BinaryFileOutputStream output)
            throws IOException, IllegalCharacterException {
        if (prevCount >= 1) {
            alphabetUtil.writeCharacterToCode(prevByte, output);
            if (prevCount >= 2) {
                for (int i : intToRle(prevCount))
                    alphabetUtil.writeCharacterToCode(i, output);
            }
        }
    }

    private static int[] intToRle(int i) {
        i += 1;
        int numDigits = (int) Math.floor(Math.log(i) / Math.log(2));

        i -= (int) Math.pow(2, numDigits);
        int currentPlace = (int) Math.pow(2, numDigits - 1);

        int[] res = new int[numDigits];
        for (int j = res.length - 1; j >= 0; j--) {
            if (i >= currentPlace) {
                i -= currentPlace;
                res[j] = RUNB;
            } else {
                res[j] = RUNA;
            }
            currentPlace /= 2;
        }
        return res;
    }
}
