package util;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Inspired by code written by Prof. Jean-Pierre Tillich.
 * Translated class, method, and variable names into English from French.
 */
public class BinaryFileInputStream implements AutoCloseable{
    private FileInputStream fileInputStream;
    private int cFlipped; // current character flipped
    private int n; // number of bits that are read

    public BinaryFileInputStream(String s) throws IOException {
        this(new FileInputStream(s));
    }

    public BinaryFileInputStream(FileInputStream fileInputStream) {
        this.fileInputStream = fileInputStream;
        n = 0;
        cFlipped = 0;
    }


    // returns k bits as an integer whose value is between 0 and 2^k-1,
    // at the end of file, this method returns -1
    public int read(int k) throws IOException {
        int b = 0, t = 0, i;
        for (i = 0; i < k; ++i) {
            b = read();
            if (b == -1)
                break;
            t = (t << 1) | b;
        }
        if (i == 0)
            return -1;
        else
            return t;
    }

    // returns 0 or 1, 
    // or -1 at the end of file.
    public int read() throws IOException {
        if (n == 0) {
            int c = fileInputStream.read();

            if (c == -1)
                return -1;
            n = 8;

            cFlipped = 0;
            for (int i = 0; i < 8; i++) {
                cFlipped = (cFlipped << 1) | (c & 1);
                c >>= 1;
            }
        }
        n--;
        int b = cFlipped & 1;
        cFlipped >>= 1;
        return b;
    }

    public void close() throws IOException {
        fileInputStream.close();
    }
}
