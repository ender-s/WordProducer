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
        private boolean threadsPerQueueSetAuto;
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
            if (numberOfQueues <= 0)
            {
                throw new RuntimeException("Number of queues must be positive!");
            }

            this.numberOfQueues = numberOfQueues;
            if (threadsPerQueueSetAuto)
            {
                setThreadsPerQueueAuto();
            }
            return this;
        }

        /**
         * Sets the number of producer threads per queue
         * @param threadsPerQueue the number of producer threads per queue
         * @return the Builder
         */
        public Builder setThreadsPerQueue(int threadsPerQueue)
        {
            if (threadsPerQueue <= 0)
            {
                throw new RuntimeException("Number of threads per queue must be positive!");
            }
            threadsPerQueueSetAuto = false;
            this.threadsPerQueue = threadsPerQueue;
            return this;
        }

        /**
         * Sets the number of threads per queue automatically
         * by taking number of logical cores found in the system into account.
         * @return the Builder
         */
        public Builder setThreadsPerQueueAuto()
        {
            threadsPerQueueSetAuto = true;
            int maxThreads = Runtime.getRuntime().availableProcessors();
            threadsPerQueue = maxThreads / numberOfQueues;
            if (threadsPerQueue == 0)
            {
                threadsPerQueue = 1;
            }

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
            checkSymbols(symbols);
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
            checkSymbols(symbols);
            for (int i = lengthStart; i <= lengthEnd; i++)
            {
                symbolMap.put(i, symbols);
            }
            return this;
        }

        /**
         * Checks if the given symbols list is empty or not,
         * and throws RuntimeException if it is empty.
         * @param symbols the symbol list to be checked
         */
        private void checkSymbols(List<String> symbols)
        {
            if (symbols.size() == 0)
            {
                throw new RuntimeException("Symbols list cannot be empty!");
            }
        }

        /**
         * Performs validation to check whether
         * it is safe to build and return the WordProducerManager.
         */
        private void validate()
        {
            if (symbolMap.size() == 0)
            {
                throw new RuntimeException("No symbol has been set so far!");
            }
        }

        /**
         * Returns the WordProducerManager built by using the attributes
         * that have been set so far.
         * @return the built WordProducerManager
         */
        public WordProducerManager build()
        {
            validate();
            return new WordProducerManager(this);
        }
    }

    /**
     * Accessor method for numberOfQueues.
     * @return the number of queues
     */
    public int getNumberOfQueues()
    {
        return numberOfQueues;
    }

    /**
     * Accessor method for threadsPerQueue.
     * @return the number of producer threads per queue
     */
    public int getThreadsPerQueue()
    {
        return threadsPerQueue;
    }

    /**
     * Accessor method for symbolMap.
     * @return the symbol map consisting of length-symbol list associations
     */
    public HashMap<Integer, List<String>> getSymbolMap()
    {
        return symbolMap;
    }
}
