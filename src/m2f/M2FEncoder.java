package m2f;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;

public class M2FEncoder {
    public static void main(String[] args) {
        m2fEncode(args[0], args[1]);
    }

    public static void m2fEncode(String inPath, String outPath) {
        LinkedList<Integer> characters = new LinkedList<>();

        for (int i = 0; i < 256; i++) {
            characters.add(i);
        }

        try (FileInputStream fileInputStream = new FileInputStream(inPath);
             FileOutputStream fileOutputStream = new FileOutputStream(outPath)) {
            int nextByte;
            while ((nextByte = fileInputStream.read()) != -1) {
                int currentIndex = characters.indexOf(nextByte);
                fileOutputStream.write(currentIndex);
                characters.remove(currentIndex);
                characters.add(0, nextByte);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
