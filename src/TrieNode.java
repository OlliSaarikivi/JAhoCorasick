import java.util.*;

interface TrieNode {
    TrieNode getChild(byte symbol);
    Iterator<TrieNode> getChildIterator();
    void setChild(byte symbol, TrieNode child);
    TrieNode getFail();
    void setFail(TrieNode fail);
    byte getSymbol();
    void setSymbol(byte symbol);
    void addPatternIndex(int patternIndex);
    void addPatternIndices(Collection<Integer> indices);
    Set<Integer> getPatternIndices();
}