import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
    private static final int R = 256;

    // Apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output.
    public static void transform() {
        String inputString = BinaryStdIn.readString();
        CircularSuffixArray circularSuffixArray = new CircularSuffixArray(inputString);
        for (int i = 0; i < circularSuffixArray.length(); i++) {
            if (circularSuffixArray.index(i) == 0) {
                BinaryStdOut.write(i);
                break;
            }
        }
        for (int i = 0; i < circularSuffixArray.length(); i++) {
            BinaryStdOut.write(inputString.charAt(
                    circularMinus1(circularSuffixArray.index(i), circularSuffixArray.length())));
        }
        BinaryStdOut.close();
    }

    // Apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output.
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        String t = BinaryStdIn.readString();

        // Compute next[].
        int[] next = new int[t.length()];
        int[] count = new int[R + 1];
        char[] s = t.toCharArray();
        for (char ch : s) {
            count[ch + 1] += 1;
        }
        for (int i = 0; i < R; i++) {
            count[i + 1] += count[i];
        }
        for (int i = 0; i < s.length; i++) {
            next[count[s[i]]++] = i;
        }

        // Restore input string from first and next[].
        int curr = next[first];
        for (int i = 0; i < t.length(); i++) {
            BinaryStdOut.write(s[curr]);
            curr = next[curr];
        }
        BinaryStdOut.close();
    }

    private static int circularMinus1(int idx, int len) {
        return (idx + len - 1) % len;
    }

    // If args[0] is "-", apply Burrows-Wheeler transform.
    // If args[0] is "+", apply Burrows-Wheeler inverse transform.
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            BurrowsWheeler.transform();
        }
        else if (args[0].equals("+")) {
            BurrowsWheeler.inverseTransform();
        }
    }
}
