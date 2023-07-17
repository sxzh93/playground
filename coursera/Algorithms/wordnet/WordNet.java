import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashMap;

public class WordNet {
    private final ArrayList<String> synsetArr;
    private final HashMap<String, SET<Integer>> nounToSynsetIds;
    private final SAP sap;

    // Constructor takes the name of the two input files.
    public WordNet(String synsets, String hypernyms) {
        if (synsets.isEmpty() || hypernyms.isEmpty()) {
            throw new IllegalArgumentException("All input parameters must not be empty.");
        }

        synsetArr = new ArrayList<>();
        nounToSynsetIds = new HashMap<>();

        readSynsets(synsets);
        Digraph digraph = getDigraph(hypernyms);
        verifyDigraphIsDag(digraph);
        sap = new SAP(digraph);
    }

    // Returns all WordNet nouns.
    public Iterable<String> nouns() {
        return nounToSynsetIds.keySet();
    }

    // Is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null || word.isEmpty()) {
            throw new IllegalArgumentException("All input parameters must not be empty.");
        }
        return nounToSynsetIds.containsKey(word);
    }

    // Distance between nounA and nounB (defined below).
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException("All input parameters must be WordNet noun.");
        }

        return sap.length(nounToSynsetIds.get(nounA), nounToSynsetIds.get(nounB));
    }

    // A synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in the shortest ancestral path (defined below).
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException("All input parameters must be WordNet noun.");
        }

        int synsetId = sap.ancestor(nounToSynsetIds.get(nounA), nounToSynsetIds.get(nounB));
        return synsetArr.get(synsetId);
    }

    private void readSynsets(String synsets) {
        In synsetInput = new In(synsets);
        while (synsetInput.hasNextLine()) {
            String synsetLine = synsetInput.readLine();
            String[] fields = synsetLine.split(",");
            int synsetId = Integer.parseInt(fields[0]);
            String[] synsetNouns = fields[1].split(" ");

            // Update synsetArr.
            synsetArr.add(synsetId, fields[1]);

            // Update synsetNouns.
            for (String synsetNoun : synsetNouns) {
                if (nounToSynsetIds.containsKey(synsetNoun)) {
                    nounToSynsetIds.get(synsetNoun).add(synsetId);
                }
                else {
                    SET<Integer> newSynset = new SET<>();
                    newSynset.add(synsetId);
                    nounToSynsetIds.put(synsetNoun, newSynset);
                }
            }
        }
    }

    private Digraph getDigraph(String hypernyms) {
        Digraph digraph = new Digraph(synsetArr.size());
        In hypernymInput = new In(hypernyms);

        while (hypernymInput.hasNextLine()) {
            String hypernymLine = hypernymInput.readLine();
            String[] fields = hypernymLine.split(",");

            int synsetId = Integer.parseInt(fields[0]);
            for (int i = 1; i < fields.length; i++) {
                int hypernymId = Integer.parseInt(fields[i]);
                digraph.addEdge(synsetId, hypernymId);
            }
        }

        return digraph;
    }

    private void verifyDigraphIsDag(Digraph digraph) {
        int zeroOutdegreeCnt = 0;
        for (int i = 0; i < digraph.V(); i++) {
            if (digraph.outdegree(i) == 0) {
                zeroOutdegreeCnt += 1;
            }
        }
        if (zeroOutdegreeCnt != 1) {
            throw new IllegalArgumentException("Input digraph must be DAG.");
        }
    }

    // Do unit testing of this class.
    public static void main(String[] args) {
        WordNet wordNet = new WordNet("synsets100-subgraph.txt", "hypernyms100-subgraph.txt");
        StdOut.println(wordNet.nouns().toString());
        StdOut.println(wordNet.distance("gluten", "gluten"));
        StdOut.println(wordNet.sap("gluten", "gluten"));
    }
}
