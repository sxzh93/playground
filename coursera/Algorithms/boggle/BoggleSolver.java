import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashSet;

public class BoggleSolver {
    private final Trie words;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        words = new Trie();
        for (String word : dictionary) {
            words.put(word, word);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        int cols = board.cols();
        int rows = board.rows();
        boolean[][] visited = new boolean[rows][cols];
        HashSet<String> result = new HashSet<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                dfs(board, visited, i, j, rows, cols, words.getRoot(), result);
            }
        }
        return result;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (!words.contains(word) || word.length() < 3) {
            return 0;
        }
        int wordLength = word.length();
        if (wordLength <= 4) {
            return 1;
        }
        else if (wordLength == 5) {
            return 2;
        }
        else if (wordLength == 6) {
            return 3;
        }
        else if (wordLength == 7) {
            return 5;
        }
        else {
            return 11;
        }
    }

    private void dfs(BoggleBoard board, boolean[][] visited, int i, int j, int rows, int cols,
                     Node curr, HashSet<String> result) {
        visited[i][j] = true;
        char ch = board.getLetter(i, j);
        Node x = curr.move(ch);
        if (x != null && ch == 'Q') {
            x = x.move('U');
        }
        if (x != null) {
            if (x.getVal() != null && x.getVal().length() >= 3) {
                result.add(x.getVal());
            }

            for (int offx = -1; offx <= 1; offx++) {
                for (int offy = -1; offy <= 1; offy++) {
                    int nextI = i + offx;
                    int nextJ = j + offy;
                    if (isValidIndex(nextI, nextJ, rows, cols) && !visited[nextI][nextJ]) {
                        dfs(board, visited, nextI, nextJ, rows, cols, x, result);
                    }
                }
            }
        }

        visited[i][j] = false;
    }

    private static boolean isValidIndex(int i, int j, int rows, int cols) {
        return i >= 0 && i < rows && j >= 0 && j < cols;
    }

    public static void main(String[] args) {
        In in = new In("dictionary-yawl.txt");
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard("board-antidisestablishmentarianisms.txt");
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
