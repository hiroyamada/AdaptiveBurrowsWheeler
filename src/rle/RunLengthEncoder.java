package rle;

import static rle.RunLengthUtil.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class RunLengthEncoder {
    public static void main(String[] args) {
        String fileName = args[0];

        try (FileInputStream fileInputStream = new FileInputStream(fileName)) {
            int prevByte = -1;
            int prevCount = 0;
            int nextByte;
            while ((nextByte = fileInputStream.read()) != -1) {
                if (nextByte == prevByte) {
                    prevCount++;
                } else {
                    writePrevious(prevByte, prevCount);
                    prevByte = nextByte;
                    prevCount = 1;
                }
            }
            writePrevious(prevByte, prevCount);
            System.out.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writePrevious(int prevByte, int prevCount) throws IOException {
        if (prevCount >= 1) {
            System.out.write(intToByteArray(prevByte));
            if (prevCount >= 2) {
                for (int i : intToRle(prevCount))
                    System.out.write(intToByteArray(i));
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

    private static byte[] intToByteArray(int x) {
        if (x < 0 || x > 257)
            throw new IllegalArgumentException("b must be a valid byte value. Got " + x);

        byte[] ret = new byte[2];
        ret[0] = (byte) (x & 0xff);
        ret[1] = (byte) ((x >> 8) & 0xff);

        return ret;
    }
}
