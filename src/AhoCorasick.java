import java.util.*;

public class AhoCorasick implements MultiplePatternMatcher {
    public static class TrieImpl {
        public static final int ARRAY = 0;
        public static final int PRIVATE_HASHMAP = 1;
        public static final int SHARED_HASHMAP = 2;
    }

    private ACTrie trie;
    private int trieImpl;
    private boolean flatten;

    public AhoCorasick(int trieImpl, boolean flatten) {
        this.trieImpl = trieImpl;
        this.flatten = flatten;
    }

    public String getName() {
        String name = "";
        if (flatten)
            name += "Flattened ";
        switch (trieImpl) {
        case TrieImpl.ARRAY:
            name += "Array ";
            break;
        case TrieImpl.PRIVATE_HASHMAP:
            name += "Private HashMap ";
            break;
        case TrieImpl.SHARED_HASHMAP:
            name += "Shared HashMap ";
            break;
        default:
            name += "Unkown ";
            break;
        }
        name += "Aho-Corasick";
        return name;
    }

    public void prepare(List<byte[]> patterns, byte[] text) {
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
        case TrieImpl.SHARED_HASHMAP:
            factory = new ACTrie.TrieNodeFactory() {
                HashMap<SharedHashMapTrieNode.NodeSymbolPair, TrieNode> children
                    = new HashMap<SharedHashMapTrieNode.NodeSymbolPair, TrieNode>();
                public TrieNode createNode() {
                    return new SharedHashMapTrieNode(children);
                }
            };
            break;
        default:
            throw new IllegalArgumentException("Unknown trie implementation: " + trieImpl);
        }

        trie = new ACTrie(patterns, text, factory, flatten);
    }

    public List<int[]> search() {
        if (trie == null)
            throw new IllegalStateException("Called match before initializing.");

        return trie.match();
    }
}