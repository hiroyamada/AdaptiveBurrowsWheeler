package util;

import static org.mockito.Mockito.*;


import org.junit.Test;

import java.io.FileInputStream;

import static org.junit.Assert.*;

public class BinaryFileInputStreamTest {
    @Test
    public void testRead() throws Exception {
        FileInputStream fileInputStream = mock(FileInputStream.class);
        BinaryFileInputStream binaryFileInputStream = new BinaryFileInputStream(fileInputStream);

        when(fileInputStream.read()).thenReturn(0b11011011);
        assertEquals(0b1101, binaryFileInputStream.read(4));
        assertEquals(0b10111101, binaryFileInputStream.read(8));
    }
}