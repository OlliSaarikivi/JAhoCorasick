import java.util.*;

interface MultiplePatternMatcher {
    void initialize(byte[][] patterns);
    List<List<Integer>>  match(byte[] text);
}