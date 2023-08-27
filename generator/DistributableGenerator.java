package generator;

import java.util.Iterator;
import java.util.List;

public abstract class DistributableGenerator extends Generator implements Iterable<String>
{

    protected final int lowerBound;
    protected final int upperBound;
    protected final int digitCount;

    /**
     * Constructor of DistributableGenerator class
     * @param generationPool a List of Strings that defines the symbols used for word generation.
     * @param lowerBound the lower bound of the generator.
     * @param upperBound the upper bound of the generator.
     *                   Words mapped by indices between lowerBound and upperBound (both inclusive)
     *                   are produced by the generator.
     * @param digitCount the number of symbols to be used in produced words.
     */
    public DistributableGenerator(List<String> generationPool, int lowerBound, int upperBound, int digitCount)
    {
        super(generationPool);
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.digitCount = digitCount;
    }

    protected abstract class DistributableGeneratorIterator implements Iterator<String>
    {
        protected int currentIndex;

        public DistributableGeneratorIterator()
        {
            this.currentIndex = lowerBound;
        }

        @Override
        public boolean hasNext()
        {
            return currentIndex <= upperBound;
        }
    }

    /**
     * Generates the word mapped by the given index.
     * @param index the index that refers to the target word.
     * @return the word referred to by the given index.
     */
    protected abstract String generateWord(int index);

}
