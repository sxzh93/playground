class Node {
    private static final int R = 26;

    private String val;
    private Node[] next = new Node[R];

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public Node[] getNext() {
        return next;
    }

    public void setNext(Node[] next) {
        this.next = next;
    }

    public Node move(char c) {
        return next[getCharOffset(c)];
    }

    private int getCharOffset(char c) {
        return c - 'A';
    }
}
