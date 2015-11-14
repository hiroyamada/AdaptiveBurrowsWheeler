package bw;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;

public class BWDetransformer {

    public static void main(String[] args) {
        bwDetransform(args[0], args[1]);
    }

    public static void bwDetransform(String inPath, String outPath) {
        try (FileInputStream fileInputStream = new FileInputStream(inPath);
             FileOutputStream fileOutputStream = new FileOutputStream(outPath)) {
            boolean endReached = false;
            while (!endReached) {
                byte[] originalIndexBytes = new byte[4];
                byte[] L = new byte[BWTransformer.N];

                int originalIndexBytesRead = fileInputStream.read(originalIndexBytes);
                if (originalIndexBytesRead == 0)
                    break;
                if (originalIndexBytesRead != 4)
                    throw new IllegalStateException("Less that 4 bytes allocated to the original index.");

                int originalIndex = ByteBuffer.wrap(originalIndexBytes).getInt();

                int numRead = fileInputStream.read(L);

                if (numRead == -1)
                    throw new IllegalStateException("Original Index found but not content.");
                if (numRead != BWTransformer.N) {
                    endReached = true;
                    L = Arrays.copyOfRange(L, 0, numRead);
                }

                HashMap<Byte, Integer> charCount = new HashMap<>();
                HashMap<Byte, Integer> C = new HashMap<>();
                int[] P = new int[L.length];

                for (int i = 0; i < L.length; i++) {
                    if (!charCount.containsKey(L[i]))
                        charCount.put(L[i], 0);
                    P[i] = charCount.get(L[i]);
                    charCount.put(L[i], P[i] + 1);
                }

                int currentCount = 0;
                for (int i = Byte.MIN_VALUE; i < Byte.MAX_VALUE + 1; i++) {
                    C.put((byte) i, currentCount);
                    currentCount += charCount.get((byte) i) == null ? 0 : charCount.get((byte) i);
                }

                int[] T = new int[L.length];
                for (int i = 0; i < T.length; i++) {
                    T[i] = P[i] + C.get(L[i]);
                }

                byte[] result = new byte[L.length];
                int previousIndex = originalIndex;
                for (int i = L.length - 1; i >= 0; i--) {
                    if (i != L.length - 1) {
                        previousIndex = T[previousIndex];
                    }
                    result[i] = L[previousIndex];
                }
                fileOutputStream.write(result);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
