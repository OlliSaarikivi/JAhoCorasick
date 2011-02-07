import java.util.*;

interface MultiplePatternMatcher {
	String getName();
    void initialize(byte[][] patterns);
    List<List<Integer>>  match(byte[] text);
}