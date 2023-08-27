package generator;

import util.NumberSystem;

import java.util.Iterator;
import java.util.List;

public class NumberSystemGenerator extends DistributableGenerator
{
    private final int base;

    /**
     * Constructor of NumberSystemGenerator class
     * @param generationPool a List of Strings that defines the symbols used for word generation.
     * @param lowerBound the lower bound of the generator.
     * @param upperBound the upper bound of the generator.
     *                   Words mapped by indices between lowerBound and upperBound (both inclusive)
     *                   are produced by the generator.
     * @param digitCount the number of symbols to be used in produced words.
     */
    public NumberSystemGenerator(List<String> generationPool, int lowerBound, int upperBound, int digitCount)
    {
        super(generationPool, lowerBound, upperBound, digitCount);
        this.base = generationPool.size();
    }

    protected class NumberSystemGeneratorIterator extends DistributableGeneratorIterator
    {
        @Override
        public String next()
        {
            String result = generateWord(currentIndex);
            currentIndex++;
            return result;
        }
    }

    @Override
    protected String generateWord(int index)
    {
        StringBuilder stringBuilder = new StringBuilder();
        for(int symbolIndex: NumberSystem.decimalToBaseN(index, base, digitCount))
        {
            stringBuilder.append(generationPool.get(symbolIndex));
        }
        return stringBuilder.toString();
    }

    @Override
    public Iterator<String> iterator()
    {
        return new NumberSystemGeneratorIterator();
    }
}
