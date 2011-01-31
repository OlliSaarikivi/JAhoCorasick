import java.util.*;

public class AhoCorasick implements MultiplePatternMatcher {
    public static class TrieImpl {
        public static final int ARRAY = 0;
        public static final int PRIVATE_HASHMAP = 1;
    }

    private ACTrie trie;
    private int trieImpl;
    private boolean flatten;

    public AhoCorasick(int trieImpl, boolean flatten) {
        this.trieImpl = trieImpl;
        this.flatten = flatten;
    }

    public void initialize(byte[][] patterns) {
        ACTrie.TrieNodeFactory factory;
        switch (trieImpl) {
        case TrieImpl.ARRAY:
            factory = new ACTrie.TrieNodeFactory() {
                public TrieNode createNode() {
                    return new ArrayTrieNode();
                }
            };
            break;

        case TrieImpl.PRIVATE_HASHMAP:
            factory = new ACTrie.TrieNodeFactory() {
                public TrieNode createNode() {
                    return new PrivateHashMapTrieNode();
                }
            };
            break;

        default:
            throw new IllegalArgumentException("Unknown trie implementation: " + trieImpl);
        }

        trie = new ACTrie(patterns, factory, flatten);
    }

    public List<List<Integer>> match(byte[] text) {
        if (trie == null)
            throw new IllegalStateException("Called match before initializing.");

        return trie.match(text);
    }
}