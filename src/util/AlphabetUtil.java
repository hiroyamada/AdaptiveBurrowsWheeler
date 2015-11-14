package util;

import java.io.IOException;

public interface AlphabetUtil {
    /**
     * Determines whether the passed character denotes an EOF in this alphabet.
     * @param character
     * @return Whether the character denotes an EOF.
     */
    boolean isEOF(int character);

    /**
     * Retrieve the EOF character in this alphabet.
     * @return The EOF character.
     */
    int getEOF();

    /**
     * Read the next alphabet from bufferedInputStream. Returns the EOF symbol defined by the alphabet
     * (not necessarily -1) when reaches the end of file. To be used by the encoder.
     *
     * @param binaryFileInputStream Stream from the source.
     * @return The next alphabet.
     */
    int readNextCharacterFromSource(BinaryFileInputStream binaryFileInputStream) throws IOException, IllegalCharacterException;

    /**
     * Read the next alphabet from bufferedInputStream. Returns the EOF symbol defined by the alphabet
     * (not necessarily -1) when reaches the end of file. To be used by the decoder.
     *
     * @param binaryFileInputStream Stream from the code.
     * @return The next alphabet.
     */
    int readNextCharacterFromCode(BinaryFileInputStream binaryFileInputStream) throws IOException, IllegalCharacterException;

    /**
     * Return whether the passed in integer is a valid alphabet.
     *
     * @param character The candidate integer to be examined.
     * @return Whether the alphabet is valid.
     */
    boolean validCharacter(int character);

    /**
     * Write the passed character to output stream connected to code.
     *
     * @param character              The character to be written.
     * @param binaryFileOutputStream The output stream to be written in.
     */
    void writeCharacterToCode(int character, BinaryFileOutputStream binaryFileOutputStream)
            throws IOException, IllegalCharacterException;

    /**
     * Write the passed character to output stream connected to source.
     *
     * @param character              The character to be written.
     * @param binaryFileOutputStream The output stream to be written in.
     */
    void writeCharacterToSource(int character, BinaryFileOutputStream binaryFileOutputStream)
            throws IOException, IllegalCharacterException;

    /**
     * To be called when the end of file is reached. For some cases, this may mean writing out an EOF
     * symbol.
     *
     * @throws IOException
     * @throws IllegalCharacterException
     */
    void onEOF(BinaryFileOutputStream binaryFileOutputStream) throws IOException, IllegalCharacterException;
}
