import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    private static final int R = 256;

    // Apply move-to-front encoding, reading from standard input and writing to standard output.
    public static void encode() {
        char[] chars = new char[R];
        for (char ch = 0; ch < R; ch++) {
            chars[ch] = ch;
        }

        while (!BinaryStdIn.isEmpty()) {
            char ch = BinaryStdIn.readChar();
            int code = 0;
            while (chars[code] != ch) {
                code++;
            }
            BinaryStdOut.write((char) code);
            for (int i = code; i > 0; i--) {
                chars[i] = chars[i - 1];
            }
            chars[0] = ch;
        }

        BinaryStdOut.close();
    }

    // Apply move-to-front decoding, reading from standard input and writing to standard output.
    public static void decode() {
        char[] chars = new char[R];
        for (char ch = 0; ch < R; ch++) {
            chars[ch] = ch;
        }

        while (!BinaryStdIn.isEmpty()) {
            int idx = BinaryStdIn.readChar();
            char ch = chars[idx];
            BinaryStdOut.write(ch);
            for (int i = idx; i > 0; i--) {
                chars[i] = chars[i - 1];
            }
            chars[0] = ch;
        }

        BinaryStdOut.close();
    }

    // If args[0] is "-", apply move-to-front encoding.
    // If args[0] is "+", apply move-to-front decoding.
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            MoveToFront.encode();
        }
        else if (args[0].equals("+")) {
            MoveToFront.decode();
        }
    }
}
