import java.util.*;

interface MultiplePatternMatcher {
	/*
	 * Returns a discriptive name for the matcher.
	 */
	String getName();

	/*
	 * Initializes the matcher with an array of patterns and text to match.
	 */
    void prepare(List<byte[]> patterns, byte[] data);

    /*
     * Matches the initialized patterns on the provided text. Returns a list
     * containing an entry for each match. Each match is a two element array
     * containing the index of the match and the index of the pattern.
     */
    List<int[]>  search();
}