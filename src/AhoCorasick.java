import java.util.*;

class AhoCorasick implements MultiplePatternMatcher {

    static class ACTrie {
        interface TrieNodeFactory {
            TrieNode createNode();
        }

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

        static class ArrayTrieNode implements TrieNode{
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

        private TrieNode root;
        private int numPatterns;

        ACTrie(byte[][] patterns, TrieNodeFactory nodeFactory) {
            numPatterns = patterns.length;

            root = nodeFactory.createNode();

            // Construct trie
            for (int patternIndex = 0; patternIndex < patterns.length; ++patternIndex) {
                byte[] pattern = patterns[patternIndex];
                TrieNode current = root;
                int symbolIndex = 0;

                TrieNode next;
                while (symbolIndex < pattern.length && (next = current.getChild(pattern[symbolIndex])) != null) {
                    current = next;
                    symbolIndex += 1;
                }

                while (symbolIndex < pattern.length) {
                    TrieNode newNode = nodeFactory.createNode();
                    newNode.setSymbol(pattern[symbolIndex]);
                    current.setChild(pattern[symbolIndex], newNode);
                    current = newNode;
                    symbolIndex += 1;
                }

                current.addPatternIndex(patternIndex);
            }

            // Update trie
            Queue<TrieNode> queue = new LinkedList<TrieNode>();
            for (int symbol = Byte.MIN_VALUE; symbol <= Byte.MAX_VALUE; ++symbol) {
                TrieNode child;
                if ((child = root.getChild((byte)symbol)) == null) {
                    root.setChild((byte)symbol, root);
                } else {
                    queue.add(child);
                    child.setFail(root);
                }
            }

            TrieNode current;
            while ((current = queue.poll()) != null) {
                Iterator<TrieNode> childIterator = current.getChildIterator();
                while (childIterator.hasNext()) {
                    TrieNode child = childIterator.next();
                    byte symbol = child.getSymbol();
                    TrieNode w = current.getFail();

                    TrieNode fail;
                    while ((fail = w.getChild(symbol)) == null)
                        w = w.getFail();
                    
                    child.setFail(fail);
                    child.addPatternIndices(fail.getPatternIndices());
                    queue.add(child);
                }
            }
        }

        List<List<Integer>> match(byte[] text) {
            List<List<Integer>> matches = new ArrayList<List<Integer>>();
            for (int i = 0; i < numPatterns; ++i) {
                matches.add(new ArrayList<Integer>());
            }

            TrieNode current = root;
            for (int i = 0; i < text.length; ++i) {
                TrieNode next;
                while ((next = current.getChild(text[i])) == null) {
                    current = current.getFail();
                }
                current = next;
                for (int patternIndex : current.getPatternIndices()) {
                    matches.get(patternIndex).add(i);
                }
            }

            return matches;
        }
    }

    private ACTrie trie;

    public void initialize(byte[][] patterns) {
        trie = new ACTrie(patterns, new ACTrie.TrieNodeFactory() {
            public ACTrie.TrieNode createNode() {
                return new ACTrie.ArrayTrieNode();
            }
        });
    }

    public List<List<Integer>> match(byte[] text) {
        if (trie == null)
            throw new IllegalStateException("Called match before initializing.");

        return trie.match(text);
    }
}