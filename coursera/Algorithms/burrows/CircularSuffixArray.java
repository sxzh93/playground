import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Collections;

public class CircularSuffixArray {
    private class CircularSuffix implements Comparable<CircularSuffix> {
        private final int start;

        CircularSuffix(int start) {
            this.start = start;
        }

        public int compareTo(CircularSuffix other) {
            for (int i = 0; i < len; i++) {
                if (ss.charAt(start + i) < ss.charAt(other.start + i)) {
                    return -1;
                }
                else if (ss.charAt(start + i) > ss.charAt(other.start + i)) {
                    return 1;
                }
            }
            return 0;
        }
    }

    private final int len;
    private final String ss;
    private final ArrayList<CircularSuffix> circularSuffixes;

    // Circular suffix array of s.
    public CircularSuffixArray(String s) {
        if (s == null) {
            throw new IllegalArgumentException("Input is null.");
        }

        ss = s + s;
        len = s.length();

        circularSuffixes = new ArrayList<>();
        for (int i = 0; i < s.length(); i++) {
            circularSuffixes.add(new CircularSuffix(i));
        }
        Collections.sort(circularSuffixes);
    }

    // Length of s.
    public int length() {
        return this.len;
    }

    // Returns index of ith sorted suffix.
    public int index(int i) {
        if (i < 0 || i >= len) {
            throw new IllegalArgumentException(String.format("Index out of bound: %d.", i));
        }
        return circularSuffixes.get(i).start;
    }

    // Unit testing (required).
    public static void main(String[] args) {
        CircularSuffixArray circularSuffixArray = new CircularSuffixArray("ABRACADABRA!");
        StdOut.println("length: " + circularSuffixArray.length());
        for (int i = 0; i < circularSuffixArray.length(); i++) {
            StdOut.println(circularSuffixArray.index(i));
        }
    }
}
