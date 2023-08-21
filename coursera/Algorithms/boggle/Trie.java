public class Trie {
    private Node root;

    public Trie() {
    }

    public Node getRoot() {
        return root;
    }


    public String get(String key) {
        Node x = get(root, key, 0);
        if (x == null) {
            return null;
        }
        return x.getVal();
    }

    public void put(String key, String val) {
        root = put(root, key, val, 0);
    }

    public boolean contains(String key) {
        return get(key) != null;
    }

    private Node get(Node x, String key, int d) {
        if (x == null) {
            return null;
        }
        if (d == key.length()) {
            return x;
        }
        char c = key.charAt(d);
        return get(x.getNext()[getCharOffset(c)], key, d + 1);
    }

    private Node put(Node x, String key, String val, int d) {
        if (x == null) {
            x = new Node();
        }
        if (d == key.length()) {
            x.setVal(val);
            return x;
        }
        char c = key.charAt(d);
        x.getNext()[getCharOffset(c)] = put(x.getNext()[getCharOffset(c)], key, val, d + 1);
        return x;
    }

    private int getCharOffset(char c) {
        return c - 'A';
    }

    public static void main(String[] args) {

    }
}
