import java.util.*;

public class ArrayTrieNode implements TrieNode{
    private final TrieNode[] children = new TrieNode[1<<Byte.SIZE];
    private TrieNode fail;
    private Set<Integer> patternIndices = new HashSet<Integer>();
    private byte symbol;

    public TrieNode getChild(byte symbol) {
        return children[(int)symbol-Byte.MIN_VALUE];
    }

    public Iterator<TrieNode> getChildIterator() {
        return new Iterator<TrieNode>() {
            int i = 0;
            TrieNode next;
            {
                scanToNext();
            }

            void scanToNext() {
                next = null;
                do {
                    i += 1;
                } while (i < children.length && (next = children[i]) == null);
            }

            public boolean hasNext() {
                return next != null;
            }

            public TrieNode next() {
                if (next == null)
                    throw new NoSuchElementException();

                TrieNode current = next;
                scanToNext();
                return current;
            }

            public void remove () {
                throw new UnsupportedOperationException();
            }
        };
    }

    public void setChild(byte symbol, TrieNode child) {
        children[(int)symbol-Byte.MIN_VALUE] = child;
    }

    public TrieNode getFail() {
        return fail;
    }

    public void setFail(TrieNode fail) {
        this.fail = fail;
    }

    public byte getSymbol() {
        return symbol;
    }

    public void setSymbol(byte symbol) {
        this.symbol = symbol;
    }

    public void addPatternIndex(int patternIndex) {
        patternIndices.add(patternIndex);
    }

    public void addPatternIndices(Collection<Integer> indices) {
        patternIndices.addAll(indices);
    }

    public Set<Integer> getPatternIndices() {
        return patternIndices;
    }
}