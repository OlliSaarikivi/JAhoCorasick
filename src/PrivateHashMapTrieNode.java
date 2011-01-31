import java.util.*;

public class PrivateHashMapTrieNode implements TrieNode{
    private final HashMap<Byte, TrieNode> children = new HashMap<Byte, TrieNode>();
    private TrieNode fail;
    private Set<Integer> patternIndices = new HashSet<Integer>();
    private byte symbol;

    public TrieNode getChild(byte symbol) {
        return children.get(symbol);
    }

    public Iterator<TrieNode> getChildIterator() {
        return children.values().iterator();
    }

    public void setChild(byte symbol, TrieNode child) {
        children.put(symbol, child);
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