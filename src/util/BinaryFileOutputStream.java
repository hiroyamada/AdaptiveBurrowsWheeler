package util;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Inspired by code written by Prof. Jean-Pierre Tillich.
 * Translated class, method, and variable names into English from French.
 */
public class BinaryFileOutputStream implements AutoCloseable {
    private int c; // current character
    private int n; // number of bits that are written
    private FileOutputStream fileOutputStream;

    public BinaryFileOutputStream(String s) throws IOException {
        this(new FileOutputStream(s));
    }

    public BinaryFileOutputStream(FileOutputStream fileOutputStream) {
        this.fileOutputStream = fileOutputStream;
        n = 0;
        c = 0;
    }

    // writes the  i least significant bits of c
    public void write(int c, int i) throws IOException {
        int cFlipped = 0;
        for (int j = 0; j < i; j++) {
            cFlipped = (cFlipped << 1) | (c & 1);
            c >>= 1;
        }

        for (int j = 0; j < i; j++) {
            write(cFlipped & 1);
            cFlipped >>= 1;
        }
    }

    // writes bit b
    public void write(int b) throws IOException {
        c = (c << 1) | (b & 1);
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
        if (n == 0)
            return;
        c <<= 8 - n;
        fileOutputStream.write(c);
        n = 0;
        c = 0;
    }
}
