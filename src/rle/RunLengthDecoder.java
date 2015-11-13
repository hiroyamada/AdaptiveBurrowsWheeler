package rle;

import static rle.RunLengthUtil.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RunLengthDecoder {
    public static void main(String[] args) {
        String fileName = args[0];

        try (FileInputStream fileInputStream = new FileInputStream(fileName)) {
            int currentCharacter = -1;
            int nextByte;
            List<Integer> counter = new ArrayList<>();
            while ((nextByte = readTwoBytes(fileInputStream)) != -1) {
                if (nextByte == RUNA || nextByte == RUNB) {
                    counter.add(nextByte);
                } else {
                    writeCurrentCharacters(counter, currentCharacter);
                    currentCharacter = nextByte;
                    counter.clear();
                }
            }
            writeCurrentCharacters(counter, currentCharacter);
            System.out.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeCurrentCharacters(List<Integer> counter, int currentCharacter) {
        if (counter.size() == 0 && currentCharacter != -1) {
            System.out.write(currentCharacter);
        }

        for (int i = 0; i < rleToInt(counter); i++) {
            System.out.write(currentCharacter);
        }
    }

    private static int rleToInt(List<Integer> a) {
        int currentPlace = 1;
        int res = 0;

        for (Integer i : a) {
            if (i == RUNA) {
                res += currentPlace;
            } else if (i == RUNB) {
                res += currentPlace * 2;
            } else {
                throw new IllegalStateException("value that is not either RUNA or RUNB is passed");
            }
            currentPlace *= 2;
        }

        return res;
    }

}
