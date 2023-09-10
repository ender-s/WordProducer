package enders.wordproducer.producer;

import java.math.BigInteger;
import java.util.List;

public class ProducerAssignment
{
    private final List<String> symbols;
    private final int length;
    private final BigInteger startIndex;
    private final BigInteger endIndex;

    /**
     * Constructor of ProducerAssignment class.
     * @param symbols the symbol list to be used for producing words
     * @param length the length of the produced words. Note that the term
     *               length does not refer to number of characters
     *               found in a word. It refers to the number of symbols found in a word,
     *               where a symbol might consist of multiple characters.
     * @param startIndex the lower bound index (inclusive) for the interval defining the
     *                   words to be produced. Since each word is referred to by
     *                   a decimal number, it is possible to divide the set of
     *                   possible words of length n defined by a symbol list
     *                   to parts.
     * @param endIndex the upper bound index (inclusive)
     */
    public ProducerAssignment(List<String> symbols, int length, BigInteger startIndex, BigInteger endIndex)
    {
        this.symbols = symbols;
        this.length = length;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    /**
     * Accessor method for symbols.
     * @return the list of symbols to be used for producing words
     */
    public List<String> getSymbols()
    {
        return symbols;
    }

    /**
     * Accessor method for length.
     * @return the length of the words to be produced
     */
    public int getLength()
    {
        return length;
    }

    /**
     * Accessor method for startIndex.
     * @return the lower bound (inclusive) of the interval of the words to be produced
     */
    public BigInteger getStartIndex()
    {
        return startIndex;
    }

    /**
     * Accessor method for endIndex.
     * @return the upper bound (exclusive) of the interval of the words to be produced
     */
    public BigInteger getEndIndex()
    {
        return endIndex;
    }
}
