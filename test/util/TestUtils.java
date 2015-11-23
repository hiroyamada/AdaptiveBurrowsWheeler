package util;

import java.io.FileInputStream;

import static org.junit.Assert.assertEquals;

public class TestUtils {
    public static void compareFiles(String s1, String s2) throws Exception {
        FileInputStream f1 = new FileInputStream(s1);
        FileInputStream f2 = new FileInputStream(s2);


        int f1NextByte = f1.read();
        int f2NextByte = f2.read();

        while (f1NextByte != -1 && f2NextByte != -1) {
            assertEquals(f1NextByte, f2NextByte);
            f1NextByte = f1.read();
            f2NextByte = f2.read();
        }

        assertEquals(f1NextByte, -1);
        assertEquals(f2NextByte, -1);
    }
}
