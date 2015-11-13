package m2f;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;

public class M2FDecoder {
    public static void main(String[] args) {
        String fileName = args[0];

        LinkedList<Integer> characters = new LinkedList<>();

        for (int i = 0; i < 256; i++) {
            characters.add(i);
        }

        try (FileInputStream fileInputStream = new FileInputStream(fileName)) {
            int nextByte;
            while ((nextByte = fileInputStream.read()) != -1) {
//                int num = (int) 'b';
//                System.out.println(num + " " + characters.get(num));
//                System.out.println("nextByte is " + nextByte);
                int printChar = characters.remove(nextByte);
//                System.out.println("About to move " + printChar + "to head");
                System.out.write(printChar);
                characters.add(0, printChar);
            }
            System.out.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
