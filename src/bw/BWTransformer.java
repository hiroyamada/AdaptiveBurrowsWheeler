package bw;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class BWTransformer {

    public static final int N = 250000;

    public static void main(String[] args) {
        bwTransform(args[0], args[1]);
    }

    public static void bwTransform(String inPath, String outPath) {
        try (FileInputStream fileInputStream = new FileInputStream(inPath);
             FileOutputStream fileOutputStream = new FileOutputStream(outPath)) {
            boolean endReached = false;

            while (!endReached) {
                byte[] bytes = new byte[N];
                int numRead = fileInputStream.read(bytes);

                if (numRead == -1)
                    break;
                if (numRead != N) {
                    endReached = true;
                    bytes = Arrays.copyOfRange(bytes, 0, numRead);
                }

                Integer[] V = new Integer[bytes.length];
                for (int i = 0; i < bytes.length; i++) {
                    V[i] = i;
                }
                final byte[] finalBytes = bytes;
                Arrays.sort(V, (o1, o2) -> {
                    int res;
                    for (int i = 0; i < finalBytes.length; i++) {
                        if ((res = Integer.compare(
                                finalBytes[(o1 + i) % finalBytes.length], finalBytes[(o2 + i) % finalBytes.length])) != 0)
                            return res;
                    }
                    return 0;
                });

                for (int i = 0; i < bytes.length; i++) {
                    if (V[i] == 0) {
                        byte[] originalIndexBytes = ByteBuffer.allocate(4).putInt(i).array();
                        fileOutputStream.write(originalIndexBytes);
                        break;
                    }
                }

                for (int v : V) {
                    fileOutputStream.write(bytes[(v + bytes.length - 1) % bytes.length]);
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}