package bw;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class BWTransformer {

    public static final int N = 250000;
//    static final int k = 4;

    public static void main(String[] args) {
        String fileName = args[0];
        try (FileInputStream fileInputStream = new FileInputStream(fileName)) {
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

//            byte[] tmp = new byte[endIndex + 1 + k];
//            for (int i = 0; i < endIndex + 1; i++) {
//                tmp[i] = bytes[i];
//            }
//
//            bytes = tmp;
//
//            int[] W = new int[N];
//
//            for (int i = 0; i < N; i++) {
//                for (int j = i; j < i + k; j++) {
//                    W[i] = (W[i] << 8) | bytes[j];
//                }
////                System.out.println(Integer.toHexString(W[i]));
//            }
//
//            Integer[] V = new Integer[N];
//            for (int i = 0; i < N; i++) {
//                V[i] = i;
//            }
//
//            // Initial sort. Paper does this with radix.
//            Arrays.sort(V, (o1, o2) -> Integer.compare(W[o1] >> 16, W[o2] >> 16));

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
                        System.out.write(originalIndexBytes);
                        break;
                    }
                }

                for (int v : V) {
                    System.out.write(bytes[(v + bytes.length - 1) % bytes.length]);
                }
            }
            System.out.flush();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}