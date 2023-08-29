package manager;

import java.util.HashMap;
import java.util.List;

public final class WordProducerManager
{
    private int numberOfQueues;
    private int threadsPerQueue;
    private HashMap<Integer, List<String>> symbolMap;

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

        public Builder()
        {
            symbolMap = new HashMap<>();
        }

        public Builder setNumberOfQueues(int numberOfQueues)
        {
            this.numberOfQueues = numberOfQueues;
            return this;
        }

        public Builder setThreadsPerQueue(int threadsPerQueue)
        {
            this.threadsPerQueue = threadsPerQueue;
            return this;
        }

        public Builder putSymbols(int length, List<String> symbols)
        {
            symbolMap.put(length, symbols);
            return this;
        }

        public Builder putSymbolsToRange(int lengthStart, int lengthEnd, List<String> symbols)
        {
            for (int i = lengthStart; i <= lengthEnd; i++)
            {
                symbolMap.put(i, symbols);
            }
            return this;
        }

        public WordProducerManager build()
        {
            return new WordProducerManager(this);
        }
    }
}
