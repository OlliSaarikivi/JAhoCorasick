import java.util.*;

public class SharedHashMapTrieNode implements TrieNode{
    public class NodeSymbolPair {
        public TrieNode node;
        public byte symbol;

        public int hashCode() {
            return node.hashCode() + symbol;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof NodeSymbolPair)) return false;
            NodeSymbolPair other = (NodeSymbolPair) obj;
            return (node == other.node) && (symbol == other.symbol);
        }
    }

    private HashMap<NodeSymbolPair, TrieNode> children;
    private NodeSymbolPair indexingPair = new NodeSymbolPair();
    private TrieNode fail;
    private Set<Integer> patternIndices = new HashSet<Integer>();
    private byte symbol;

    public SharedHashMapTrieNode(HashMap<NodeSymbolPair, TrieNode> children) {
        this.children = children;
        indexingPair.node = this;
    }

    public TrieNode getChild(byte symbol) {
        indexingPair.symbol = symbol;
        return children.get(indexingPair);
    }

    public Iterator<TrieNode> getChildIterator() {
        return new Iterator<TrieNode>() {
            int symbol = Byte.MIN_VALUE - 1;
            TrieNode next;
            {
                scanToNext();
            }

            void scanToNext() {
                next = null;
                do {
                    symbol += 1;
                } while (symbol <= Byte.MAX_VALUE && (next = getChild((byte)symbol)) == null);
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
        NodeSymbolPair keyPair = new NodeSymbolPair();
        keyPair.node = this;
        keyPair.symbol = symbol;
        children.put(keyPair, child);
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