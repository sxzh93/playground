/**
 * WordNet is a semantic lexicon for the English language that computational linguists and cognitive scientists use
 * extensively. We build WordNet digraph: each vertex v is an integer that represents a synset, and each directed edge
 * v->w represents that w is a hypernym of v. The WordNet digraph is a rooted DAG: it is acyclic and has one vertex—the
 * root—that is an ancestor of every other vertex. However, it is not necessarily a tree because a synset can have more
 * than one hypernym.
 */
package prince;


public class WordNet {


    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernym) throws IllegalArgumentException {
        if (synsets == null || hypernym == null) {
            throw new IllegalArgumentException("any argument to the constructor or an instance is null");
        }

    }

    // return all WorkNet nouns
    public Iterable<String> nouns() {

    }

    // is the word a WordNet noun?
    public boolean isNoun() {

    }

    // distance between nounA and nounB
    public int distance(String nounA, String nounB) {

    }

    // a synset(second field of synsets.txt) that is the common ancestor of nounA and nounB in a shortest ancestral path
    public String sap(String nounA, String nounB) {

    }

    // do unit testing of this class
    public static void main(String[] args) {

    }
}
