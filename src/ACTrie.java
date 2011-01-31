import java.util.*;

public class ACTrie {
    interface TrieNodeFactory {
        TrieNode createNode();
    }

    private TrieNode root;
    private int numPatterns;

    public ACTrie(byte[][] patterns, TrieNodeFactory nodeFactory, boolean flatten) {
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

        if (flatten) {
            queue = new LinkedList<TrieNode>();
            Iterator<TrieNode> childIterator = root.getChildIterator();
            while (childIterator.hasNext()) {
                TrieNode child = childIterator.next();
                if (child != root) {
                    queue.add(child);
                }
            }

            while ((current = queue.poll()) != null) {
                childIterator = current.getChildIterator();
                while (childIterator.hasNext()) {
                    TrieNode child = childIterator.next();
                    queue.add(child);
                }

                TrieNode fail = current.getFail();
                for (int symbol = Byte.MIN_VALUE; symbol <= Byte.MAX_VALUE; ++symbol) {
                    if (current.getChild((byte)symbol) == null) {
                        current.setChild((byte)symbol, fail.getChild((byte)symbol));
                    }
                }
            }
        }
    }

    public List<List<Integer>> match(byte[] text) {
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