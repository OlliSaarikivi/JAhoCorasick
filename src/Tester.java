import java.io.*;
import java.util.*;

/**
 * University of Helsinki, Department of Computer Science
 * Project in String Processing Algorithms, Spring 2011
 *
 * Author: llatvala (lari.latvala@helsinki.fi) and osaariki (olli.saarikivi@iki.fi)
 * Date: 18.1.2011
 * Time: 19:39:34
 */
public class Tester {
    public static final String ENCODING_ISO8BIT = "8859_1";

    /*
     * program <data-file> <output-file> <data-length> <warmup-runs> <test-runs> random-equal-length <seed> <num-patterns> <pattern-length>
     *  OR
     * program <data-file> <output-file> <data-length> <warmup-runs> <test-runs> user-supplied <pattern> [<pattern>...]
     */
    public static void main(String[] args)
    {
        MultiplePatternMatcher[] matchers = new MultiplePatternMatcher[] {
            new AhoCorasick(AhoCorasick.TrieImpl.ARRAY, false),
            new AhoCorasick(AhoCorasick.TrieImpl.ARRAY, true),
            new AhoCorasick(AhoCorasick.TrieImpl.PRIVATE_HASHMAP, false),
            new AhoCorasick(AhoCorasick.TrieImpl.SHARED_HASHMAP, false),
        };

        try {
            int bytesToRead = Integer.parseInt(args[2]);
            log("Reading " + bytesToRead + " bytes from data file.");

            byte[] testData = readBytesFromFile(args[0], 0, bytesToRead); 

            String dataFilePath, outputFilePath;
            List<byte[]> patterns = new ArrayList<byte[]>();
            if (args[5].equals("random-equal-length")) {
                log("Picking equal length patterns at random from data.");
                long seed = Long.parseLong(args[6]);
                log("Using seed: " + seed);
                int numPatterns = Integer.parseInt(args[7]);
                int patternLength = Integer.parseInt(args[8]);
                log("Picking " + numPatterns + " patterns of length " + patternLength + ".");

                Random random = new Random(seed);
                for (int i = 0; i < numPatterns; ++i) {
                    patterns.add(getRandomFactor(testData, patternLength, random));
                }
            } else if (args[5].equals("user-supplied")) {
                log("Using user supplied patterns.");
                for (int i = 6; i < args.length; ++i) {
                    patterns.add(stringToByteArray(args[i]));
                }
            }

            int warmupRuns = Integer.parseInt(args[3]);
            log("Running " + warmupRuns + " warmup runs.");
            for (int i = 0; i < warmupRuns; ++i) {
                for (MultiplePatternMatcher matcher : matchers) {
                    test(matcher, patterns, testData);
                }
            }

            int testRuns = Integer.parseInt(args[4]);
            log("Running " + testRuns + " test runs.");
            List<long[][]> testRunsResults = new ArrayList<long[][]>();
            for (int i = 0; i < testRuns; ++i) {
                long[][] matchersResults = new long[matchers.length][];
                for (int j = 0; j < matchers.length; ++j) {
                    matchersResults[j] = test(matchers[j], patterns, testData);
                }
                testRunsResults.add(matchersResults);
            }

            log("Writing output.");
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(args[1])));
            boolean first = true;
            for (MultiplePatternMatcher matcher : matchers) {
                if (first)
                    first = false;
                else
                    out.print(',');
                out.print(matcher.getName()+" prepare," + matcher.getName() + " search");
            }
            out.println();
            for (long[][] matchersResults : testRunsResults) {
                first = true;
                for (long[] result : matchersResults) {
                    if (first)
                        first = false;
                    else
                        out.print(',');
                    out.print(result[0]+","+result[1]);
                }
                out.println();
            }
            out.close();

        } catch (NumberFormatException e) {
            log(e.getMessage());
            System.exit(1);
        } catch (IOException e) {
            log(e.getMessage());
            System.exit(1);
        }
    }

    public static byte[] getRandomFactor(byte[] data, int length, Random random) {
        if (length > data.length)
            throw new IllegalArgumentException();

        byte[] factor = new byte[length];
        int start = random.nextInt(data.length - factor.length + 1);
        for (int i = 0; i < factor.length; ++i) {
            factor[i] = data[start + i];
        }
        return factor;
    }

    public static long[] test(MultiplePatternMatcher matcher, List<byte[]> testPatterns, byte[] testData)
    {
        long[] runTimes = new long[2];

        long startTime = 0;
        long endTime = 0;
               
        startTime = System.currentTimeMillis();
        matcher.prepare(testPatterns, testData);
        endTime = System.currentTimeMillis();
        log(matcher.getName()+ ": Preprocessing took " + (endTime - startTime) + " ms.");
        runTimes[0] = endTime - startTime;
        
        startTime = System.currentTimeMillis();
        List<int[]> results = matcher.search();
        endTime = System.currentTimeMillis();
        log(matcher.getName() + ": Search completed in " + (endTime - startTime) + " ms. Found " + results.size() + " matches.");
        runTimes[1] = endTime - startTime;

        int checksum = 0;
        for (int[] result : results) {
            checksum += result[0] + result[1];
        }
        log("Checksum for matches: " + checksum);

        return runTimes; 
    }
    
    
    /**
     * Converts a string to byte array. Only uses the first byte and discards the second for each char.
     * @param string
     * @return
     */
    public static byte[] stringToByteArray(String string) {
        try {
            byte[] bytes = string.getBytes(ENCODING_ISO8BIT);
            return bytes;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            System.exit(1);
            throw new RuntimeException();
        }
    }

    /**
     * 
     * @param bytes
     * @return
     */
    public static String byteArrayToString(byte[] bytes) {
        try {
            return new String(bytes, ENCODING_ISO8BIT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            System.exit(1);
            throw new RuntimeException();
        }
    }
    
    /**
     * 
     * @param arrayOne
     * @param arrayTwo
     * @return
     */
    public static boolean byteArrayIsEqual(byte[] arrayOne, byte[] arrayTwo) {
        if (arrayOne.length == arrayTwo.length) {
            for (int i = 0; i < arrayOne.length; i++) {
                if (arrayOne[i] != arrayTwo[i]) return false;
            }
            return true;
        }
        return false;
    }

    public static byte[] readBytesFromFile(String filePath, int offset, int length) throws IOException
    {
        FileInputStream fis = new FileInputStream(filePath);
        byte[] buffer = new byte[length];
        fis.read(buffer, offset, length);
        fis.close();
        return buffer;
    }

    public static void log(String message) {
    	System.out.println(message);
    }
}
