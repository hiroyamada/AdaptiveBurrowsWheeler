package rle;

import java.io.FileInputStream;
import java.io.IOException;

public class RunLengthUtil {
    public static final int RUNA = 256;
    public static final int RUNB = 257;

    public static int readTwoBytes(FileInputStream fileInputStream) throws IOException {
        byte[] bytes = new byte[2];
        int numRead = fileInputStream.read(bytes);
        if (numRead == -1)
            return -1;
        if (numRead == 1)
            throw new IllegalStateException("file ends in the middle of character");

        return (bytes[0] | (bytes[1] << 8));
    }
}
