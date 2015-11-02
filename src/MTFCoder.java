import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;

public class MTFCoder {
    public static void main(String[] args) {
        String fileName = args[0];

        LinkedList<Integer> characters = new LinkedList<>();

        for (int i = 0; i < 256; i++) {
            characters.add(i);
        }

        try (FileInputStream fileInputStream = new FileInputStream(fileName)) {
            int nextByte;
            while ((nextByte = fileInputStream.read()) != -1) {
                int currentIndex = characters.indexOf(nextByte);
                System.out.write(currentIndex);
                characters.remove(currentIndex);
                characters.add(0, nextByte);
            }
            System.out.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
