package rle;

import util.AlphabetUtil;
import util.BinaryFileInputStream;
import util.BinaryFileOutputStream;
import util.IllegalCharacterException;

import java.io.IOException;

public class RunLengthAlphabetUtil implements AlphabetUtil {
    static final int RUNA = 256;
    static final int RUNB = 257;
    static final int EOF = 258;

    private static final int MAX_VALID_VALUE = 258;

    private static final int NUM_BITS_PER_CHARACTER = 9;

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
        int readValue = binaryFileInputStream.read(9);
        if (readValue == -1)
            return EOF;

        if (!validCharacter(readValue))
            throw new IllegalCharacterException("Read invalid character with value " + readValue);

        return readValue;
    }

    @Override
    public int readNextCharacterFromCode(BinaryFileInputStream binaryFileInputStream) throws IOException, IllegalCharacterException {
        // source and code have same schema for RLE alphabet
        return readNextCharacterFromSource(binaryFileInputStream);
    }

    @Override
    public boolean validCharacter(int character) {
        return character >= 0 && character <= MAX_VALID_VALUE;
    }

    @Override
    public void writeCharacterToCode(int character, BinaryFileOutputStream binaryFileOutputStream)
            throws IOException, IllegalCharacterException {
        if (!validCharacter(character))
            throw new IllegalCharacterException("Writing illegal character with value " + character);

        binaryFileOutputStream.write(character, NUM_BITS_PER_CHARACTER);
    }

    @Override
    public void writeCharacterToSource(int character, BinaryFileOutputStream binaryFileOutputStream) throws IOException, IllegalCharacterException {
        // source and code have same schema for RLE alphabet
        writeCharacterToCode(character, binaryFileOutputStream);
    }

    @Override
    public void onEOF(BinaryFileOutputStream binaryFileOutputStream) throws IOException, IllegalCharacterException {
        writeCharacterToCode(EOF, binaryFileOutputStream);
    }
}
