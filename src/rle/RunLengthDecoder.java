package rle;

import util.BinaryFileInputStream;
import util.IllegalCharacterException;

import static rle.RunLengthAlphabetUtil.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RunLengthDecoder {
    private static RunLengthAlphabetUtil alphabetUtil = new RunLengthAlphabetUtil();

    public static void main(String[] args) {
        runLengthDecode(args[0], args[1]);
    }

    public static void runLengthDecode(String inPath, String outPath) {
        try (BinaryFileInputStream fileInputStream = new BinaryFileInputStream(inPath);
             FileOutputStream fileOutputStream = new FileOutputStream(outPath)) {
            int currentCharacter = -1;
            int nextByte;
            List<Integer> counter = new ArrayList<>();
            while ((nextByte = alphabetUtil.readNextCharacterFromSource(fileInputStream)) != alphabetUtil.getEOF()) {
                if (nextByte == RUNA || nextByte == RUNB) {
                    counter.add(nextByte);
                } else {
                    writeCurrentCharacters(counter, currentCharacter, fileOutputStream);
                    currentCharacter = nextByte;
                    counter.clear();
                }
            }
            writeCurrentCharacters(counter, currentCharacter, fileOutputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalCharacterException e) {
            e.printStackTrace();
        }
    }

    private static void writeCurrentCharacters(List<Integer> counter, int currentCharacter, FileOutputStream fileOutputStream) throws IOException {
        if (counter.size() == 0 && currentCharacter != -1) {
            fileOutputStream.write(currentCharacter);
        }

        for (int i = 0; i < rleToInt(counter); i++) {
            fileOutputStream.write(currentCharacter);
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
