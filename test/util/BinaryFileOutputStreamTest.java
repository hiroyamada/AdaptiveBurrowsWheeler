package util;

import static org.mockito.Mockito.*;

import java.io.FileOutputStream;

public class BinaryFileOutputStreamTest {
    @org.junit.Test
    public void testWrite() throws Exception {
        FileOutputStream fileOutputStream = mock(FileOutputStream.class);
        BinaryFileOutputStream binaryFileOutputStream = new BinaryFileOutputStream(fileOutputStream);

        binaryFileOutputStream.write(0);
        binaryFileOutputStream.write(1);
        binaryFileOutputStream.write(0);
        binaryFileOutputStream.write(1);
        binaryFileOutputStream.flush();

        verify(fileOutputStream).write(0b01010000);

        binaryFileOutputStream.write(0b11101010, 4);
        binaryFileOutputStream.flush();

        verify(fileOutputStream).write(0b10100000);

        binaryFileOutputStream.write(0b11101010, 5);
        binaryFileOutputStream.write(0b11011010, 5);

        verify(fileOutputStream).write(0b01010110);

        binaryFileOutputStream.flush();

        verify(fileOutputStream).write(0b10000000);
    }

    @org.junit.Test
    public void testWrite1() throws Exception {

    }

    @org.junit.Test
    public void testClose() throws Exception {

    }

    @org.junit.Test
    public void testEmptyBuffer() throws Exception {

    }

    @org.junit.Test
    public void testFlush() throws Exception {

    }
}