package adaptive_huffman;

import util.AlphabetUtil;
import util.BinaryFileInputStream;
import util.BinaryFileOutputStream;
import util.IllegalCharacterException;

import java.io.IOException;

public class HuffmanAlphabetUtil implements AlphabetUtil {
    private static final int EOF = 256;

    private static final int MAX_VALID_VALUE = 256;

    private static final int NUM_BITS_PER_CHARACTER_IN_SOURCE = 8;
    private static final int NUM_BITS_PER_CHARACTER_IN_CODE = 9;

    @Override
    public boolean isEOF(int character) {
        return character == EOF;
    }

    @Override
    public int getEOF() {
        return EOF;
    }

    @Override
    public int readNextCharacterFromSource(BinaryFileInputStream binaryFileInputStream)
            throws IOException, IllegalCharacterException {
        return readNextCharacter(binaryFileInputStream, NUM_BITS_PER_CHARACTER_IN_SOURCE);
    }

    @Override
    public int readNextCharacterFromCode(BinaryFileInputStream binaryFileInputStream)
            throws IOException, IllegalCharacterException {
        return readNextCharacter(binaryFileInputStream, NUM_BITS_PER_CHARACTER_IN_CODE);
    }

    private int readNextCharacter(BinaryFileInputStream binaryFileInputStream, int numBits) throws IllegalCharacterException, IOException {
        int readValue = binaryFileInputStream.read(numBits);
        if (readValue == -1)
            return EOF;

        if (!validCharacter(readValue))
            throw new IllegalCharacterException("Read invalid character with value " + readValue);

        return readValue;
    }

    @Override
    public boolean validCharacter(int character) {
        return character >= 0 && character <= MAX_VALID_VALUE;
    }

    @Override
    public void writeCharacterToCode(int character, BinaryFileOutputStream binaryFileOutputStream)
            throws IOException, IllegalCharacterException {
        writeCharacter(character, binaryFileOutputStream, NUM_BITS_PER_CHARACTER_IN_CODE);
    }

    @Override
    public void writeCharacterToSource(int character, BinaryFileOutputStream binaryFileOutputStream)
            throws IOException, IllegalCharacterException {
        writeCharacter(character, binaryFileOutputStream, NUM_BITS_PER_CHARACTER_IN_SOURCE);
    }

    private void writeCharacter(int character, BinaryFileOutputStream binaryFileOutputStream, int numBits)
            throws IOException, IllegalCharacterException {
        if (!validCharacter(character))
            throw new IllegalCharacterException("Writing illegal character with value " + character);

        binaryFileOutputStream.write(character, numBits);
    }

    @Override
    public void onEOF(BinaryFileOutputStream binaryFileOutputStream) throws IOException, IllegalCharacterException {
    }
}
