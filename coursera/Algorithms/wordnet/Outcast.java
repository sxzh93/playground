import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private final WordNet wordNet;

    // Constructor takes a WordNet object.
    public Outcast(WordNet wordnet) {
        this.wordNet = wordnet;
    }

    // Given an array of WordNet nouns, return an outcast.
    public String outcast(String[] nouns) {
        String outcastNoun = "";
        int maxDistance = -1;
        for (int i = 0; i < nouns.length; i++) {
            int currDistance = 0;
            for (int j = 0; j < nouns.length; j++) {
                currDistance += wordNet.distance(nouns[i], nouns[j]);
            }
            if (currDistance > maxDistance) {
                maxDistance = currDistance;
                outcastNoun = nouns[i];
            }
        }
        return outcastNoun;
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
