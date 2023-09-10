package enders.wordproducer.util;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class Counter
{
    /**
     * Calculates and returns the count of all possible words
     * that may be generated using the given symbolMap.
     * @param symbolMap mapping of integers specifying lengths to
     *                  symbol lists consisting of Strings
     * @return the count of all possible words that may be generated
     *         using the given symbolMap
     */
    public static BigInteger countWords(HashMap<Integer, List<String>> symbolMap)
    {
        BigInteger result = BigInteger.ZERO;
        for (Entry<Integer, List<String>> entry: symbolMap.entrySet())
        {
            int length = entry.getKey();
            BigInteger symbolCount = BigInteger.valueOf(entry.getValue().size());
            result = result.add(symbolCount.pow(length));
        }
        return result;
    }

    /**
     * Calculates and returns the count of all possible words
     * that may be generated of length {@code <length>} using the
     * symbol list associated with length {@code <length>}.
     * @param symbolMap mapping of integers specifying lengths to
     *                  symbol lists consisting of Strings
     * @param length the length of the words (must exist in symbolMap)
     * @return the count of all possible words
     */
    public static BigInteger countWords(HashMap<Integer, List<String>> symbolMap, int length)
    {
        return BigInteger.valueOf(symbolMap.get(length).size()).pow(length);
    }
}
