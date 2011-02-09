import java.util.*;

public class Main {
    public static void main(String[] args) {
        byte[][] patterns = new byte[200][];
        for (int i = 0; i < patterns.length; ++i) {
            patterns[i] = getRandomArray(20);
        }
        byte[] text = getRandomArray(5000000);


        /*
        text = "AGAGACTCTTTTCTGAAAACTGTATTATGAAACATTTGCTAATGATGCTTTTCACAGGAGTAATAAAAATTTGATTTAGAAAATGTGCTTAAGTATTCTG".getBytes();
        byte[] pattern = "AACATTTGCTAATGATGCTTTTCA".getBytes();
        byte[] pattern2 = "TESTI".getBytes();
        byte[] pattern3 = "TAA".getBytes();
        patterns = new byte[][]{pattern3, pattern2,pattern};
        */
/*
        MultiplePatternMatcher mpm = new AhoCorasick(AhoCorasick.TrieImpl.SHARED_HASHMAP, false);

        long start, end;

        for (int i = 0; i < 2; ++i) {
            start = System.currentTimeMillis();
            mpm.prepare(patterns, text);
            end = System.currentTimeMillis();
            printTime("Trie construction", start, end);
        }

        List<int[]> matches = null;
        for (int i = 0; i < 10; ++i) {
            start = System.currentTimeMillis();
            matches = mpm.search();
            end = System.currentTimeMillis();
            printTime("Matching", start, end);
        }
*/
        //printMatches(matches);
    }

    static void printTime(String phase, long start, long end) {
        System.out.println(phase + " took " + (end - start) + " milliseconds to complete.");
    }

    static void printMatches(List<List<Integer>> matches) {
        for (int i = 0; i < matches.size(); ++i) {
            List<Integer> positions = matches.get(i);
            System.out.print("Pattern " + i + ":");
            for (int j = 0; j < positions.size(); ++j) {
                System.out.print(" " + positions.get(j));
            }
            System.out.println();
        }
    }

    static byte[] getRandomArray(int length) {
        byte[] array = new byte[length];
        for (int i = 0; i < array.length; ++i) {
            array[i] = (byte)(Math.random()*256-127);
        }
        return array;
    }
}