package manager;

import java.util.HashMap;
import java.util.List;

public final class WordProducerManager
{
    private int numberOfQueues;
    private int threadsPerQueue;
    private HashMap<Integer, List<String>> symbolMap;

    /**
     * Constructor of WordProducerManager class
     * @param builder the Builder that builds WordProducerManager
     */
    private WordProducerManager(Builder builder)
    {
        this.numberOfQueues = builder.numberOfQueues;
        this.threadsPerQueue = builder.threadsPerQueue;
        this.symbolMap = builder.symbolMap;
    }

    public static final class Builder
    {
        private int numberOfQueues;
        private int threadsPerQueue;
        private HashMap<Integer, List<String>> symbolMap;

        /**
         * Constructor of Builder of WordProducerManager
         */
        public Builder()
        {
            symbolMap = new HashMap<>();
        }

        /**
         * Sets the number of output queues to be used
         * @param numberOfQueues the number of output queues to be used
         * @return the Builder
         */
        public Builder setNumberOfQueues(int numberOfQueues)
        {
            this.numberOfQueues = numberOfQueues;
            return this;
        }

        /**
         * Sets the number of producer threads per queue
         * @param threadsPerQueue the number of producer threads per queue
         * @return the Builder
         */
        public Builder setThreadsPerQueue(int threadsPerQueue)
        {
            this.threadsPerQueue = threadsPerQueue;
            return this;
        }

        /**
         * Sets the symbol list for the words to be produced of length <length>
         * @param length the length of the words to be produced with symbol list <symbols>.
         *               Note that a symbol consisting of multiple letters still contributes 1 to the length.
         * @param symbols the symbol list for the words to be produced of length <length>
         * @return the Builder
         */
        public Builder putSymbols(int length, List<String> symbols)
        {
            symbolMap.put(length, symbols);
            return this;
        }

        /**
         * Sets the given symbol list for all the words to be produced of lengths between
         * {@code <lengthStart>} and {@code <lengthEnd>} (both inclusive).
         * @param lengthStart the lower bound of the length range
         * @param lengthEnd the upper bound of the length range
         * @param symbols the symbol list for the words to be produced of lengths between
         *                {@code <lengthStart>} and {@code <lengthEnd>} (both inclusive).
         * @return the Builder
         */
        public Builder putSymbolsToRange(int lengthStart, int lengthEnd, List<String> symbols)
        {
            for (int i = lengthStart; i <= lengthEnd; i++)
            {
                symbolMap.put(i, symbols);
            }
            return this;
        }

        /**
         * Returns the WordProducerManager built by using the attributes
         * that have been set so far.
         * @return the built WordProducerManager
         */
        public WordProducerManager build()
        {
            return new WordProducerManager(this);
        }
    }
}
