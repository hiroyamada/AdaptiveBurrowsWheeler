package rle;

import java.io.IOException;
import java.io.InputStream;

public class RunLengthUtil {
    public static final int RUNA = 256;
    public static final int RUNB = 257;
    public static final int EOF = 258;

    public static final int MAX_VALID_VALUE = 258;



    public static int readTwoBytes(InputStream inputStream) throws IOException {
        byte[] bytes = new byte[2];
        int numRead = inputStream.read(bytes);
        if (numRead == -1)
            return -1;
        if (numRead == 1)
            throw new IllegalStateException("file ends in the middle of character");

        int returnValue = (bytes[1] & 0xff) | ((bytes[0] & 0xff) << 8);
        if (returnValue < 0 || returnValue > MAX_VALID_VALUE) {
            System.err.println(bytes[0]);
            System.err.println(bytes[1]);
            System.err.println(Integer.toBinaryString(returnValue));
            throw new IllegalStateException("read invalid character with value " + returnValue);
        }
        return returnValue;
    }
}
