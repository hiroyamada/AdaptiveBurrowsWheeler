package util;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Inspired by code written by Prof. Jean-Pierre Tillich.
 * Translated class, method, and variable names into English from French.
 */
public class BinaryFileOutputStream {
    private int c; // current character
    private int n; // number of bits that are written
    private FileOutputStream fileOutputStream;

    public BinaryFileOutputStream(String s) throws IOException {
        fileOutputStream = new FileOutputStream(s);
        n = 0;
        c = 0;
    }

    // writes the  i least significant bits of c
    public void write(int c, int i) throws IOException {
        for (; i > 0; --i) {
            write(c & 1);
            c >>= 1;
        }
    }

    // writes bit b
    public void write(int b) throws IOException {
        c ^= (b << n);
        ++n;
        if (n == 8)
            flush();
    }

    public void close() throws IOException {
        flush();
        fileOutputStream.close();
    }

    public boolean emptyBuffer() {
        return (n == 0);
    }

    public void flush() throws IOException {
        fileOutputStream.write(c);
        n = 0;
        c = 0;
    }
}
