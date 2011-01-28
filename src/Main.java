import java.util.*;

public class Main {
    public static void main(String[] args) {
        byte[][] patterns = new byte[5][];
        for (int i = 0; i < patterns.length; ++i) {
            patterns[i] = getRandomArray(2);
        }
        byte[] text = getRandomArray(1000);



        text = "AGAGACTCTTTTCTGAAAACTGTATTATGAAACATTTGCTAATGATGCTTTTCACAGGAGTAATAAAAATTTGATTTAGAAAATGTGCTTAAGTATTCTG".getBytes();
        byte[] pattern = "AACATTTGCTAATGATGCTTTTCA".getBytes();
        byte[] pattern2 = "TESTI".getBytes();
        byte[] pattern3 = "TAA".getBytes();
        patterns = new byte[][]{pattern3, pattern2,pattern};


        MultiplePatternMatcher mpm = new AhoCorasick();
        mpm.initialize(patterns);
        List<List<Integer>> matches = mpm.match(text);
        printMatches(matches);
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