import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
    private static final int INFINITY = Integer.MAX_VALUE;
    private final Digraph digraph;

    // Constructor takes a digraph (not necessarily a DAG).
    public SAP(Digraph G) {
        this.digraph = new Digraph(G);
    }

    // Length of the shortest ancestral path between v and w; -1 if no such path.
    public int length(int v, int w) {
        return findSap(new BreadthFirstDirectedPaths(digraph, v),
                       new BreadthFirstDirectedPaths(digraph, w), /* returnLength= */true);
    }

    // A common ancestor of v and w that participates in the shortest ancestral
    // path; -1 if no such path.
    public int ancestor(int v, int w) {
        return findSap(new BreadthFirstDirectedPaths(digraph, v),
                       new BreadthFirstDirectedPaths(digraph, w), /* returnLength= */ false);
    }

    // Length of the shortest ancestral path between any vertex in v and any vertex
    // in w; -1 if no such path.
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        try {
            return findSap(new BreadthFirstDirectedPaths(digraph, v),
                           new BreadthFirstDirectedPaths(digraph, w), /* returnLength= */true);
        }
        catch (IllegalArgumentException e) {
            if (e.getMessage().equals("zero vertices")) {
                return -1;
            }
            else {
                throw new IllegalArgumentException(e);
            }
        }
    }

    // A common ancestor that participates in shortest ancestral path; -1 if no
    // such path.
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        try {
            return findSap(new BreadthFirstDirectedPaths(digraph, v),
                           new BreadthFirstDirectedPaths(digraph, w), /* returnLength= */ false);
        }
        catch (IllegalArgumentException e) {
            if (e.getMessage().equals("zero vertices")) {
                return -1;
            }
            else {
                throw new IllegalArgumentException(e);
            }
        }
    }

    // Returns length iff returnLength set to true else returns ancestor.
    private int findSap(BreadthFirstDirectedPaths bfsFromV, BreadthFirstDirectedPaths bfsFromW,
                        boolean returnLength) {
        int shortestLength = INFINITY;
        int ancestor = -1;
        for (int i = 0; i < this.digraph.V(); i++) {
            if (bfsFromV.hasPathTo(i) && bfsFromW.hasPathTo(i)) {
                int curLength = bfsFromV.distTo(i) + bfsFromW.distTo(i);
                if (curLength < shortestLength) {
                    shortestLength = curLength;
                    ancestor = i;
                }
            }
        }
        if (returnLength) {
            return shortestLength < INFINITY ? shortestLength : -1;
        }
        return ancestor;
    }

    // Do unit testing of this class.
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph digraph = new Digraph(in);
        SAP sap = new SAP(digraph);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
