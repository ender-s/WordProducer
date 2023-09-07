package distributor;

import manager.WordProducerManager;
import producer.ProducerAssignment;
import producer.WordProducer;
import util.Counter;
import util.IntervalManager;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class WordDistributor
{
    private List<WordProducer> wordProducers;
    private List<BlockingQueue<String>> queues;

    private int numberOfQueues;
    private int threadsPerQueue;
    private int threadCount;
    private HashMap<Integer, List<String>> symbolMap;

    /**
     * Constructor of WordDistributor class
     * @param wordProducerManager the word producer manager that holds
     *                            information related to word producing process
     */
    public WordDistributor(WordProducerManager wordProducerManager)
    {
        numberOfQueues = wordProducerManager.getNumberOfQueues();
        threadsPerQueue = wordProducerManager.getThreadsPerQueue();
        threadCount = threadsPerQueue * numberOfQueues;
        symbolMap = wordProducerManager.getSymbolMap();

        queues = new ArrayList<>();
        initializeWordProducers();
    }

    /**
     * Initializes word producers by taking into account
     * numberOfQueues and threadsPerQueue. Queues are created and
     * assigned to related producers.
     */
    private void initializeWordProducers()
    {
        wordProducers = new ArrayList<>();

        for (int i = 0; i < numberOfQueues; i++)
        {
            BlockingQueue<String> queue = new ArrayBlockingQueue<>(1024);
            queues.add(queue);
            for (int j = 0; j < threadsPerQueue; j++)
            {
                wordProducers.add(new WordProducer(queue));
            }
        }
    }

    /**
     * Distributes all words as tasks to different producers equally.
     */
    public void distribute()
    {
        BigInteger wordCount = Counter.countWords(symbolMap);
        BigInteger wordsPerThread = wordCount.divide(BigInteger.valueOf(threadCount));

        int[] lengths = Arrays.stream(symbolMap.keySet().toArray()).mapToInt(i -> (int) i).toArray();
        int lengthCounter = 0;
        int currentLength = lengths[lengthCounter];
        BigInteger wordCountForCurrentLength = Counter.countWords(symbolMap, currentLength);
        BigInteger selectedWordCountForCurrentLength = BigInteger.ZERO;

        int threadCounter = 0;
        BigInteger wordCountForThread = BigInteger.ZERO;
        while (threadCounter < threadCount)
        {
            BigInteger remainingWordCountForCurrentLength = wordCountForCurrentLength
                    .subtract(selectedWordCountForCurrentLength);
            BigInteger remainingWordCountForCurrentThread = wordsPerThread.subtract(wordCountForThread);
            if (remainingWordCountForCurrentLength.compareTo(remainingWordCountForCurrentThread) > 0)
            {
                assign(threadCounter, currentLength, selectedWordCountForCurrentLength,
                        selectedWordCountForCurrentLength
                                .add(remainingWordCountForCurrentThread).subtract(BigInteger.ONE));

                selectedWordCountForCurrentLength = selectedWordCountForCurrentLength
                        .add(remainingWordCountForCurrentThread);
                wordCountForThread = BigInteger.ZERO;
                threadCounter++;
            }
            else
            {
                assign(threadCounter, currentLength, selectedWordCountForCurrentLength,
                        selectedWordCountForCurrentLength.add(remainingWordCountForCurrentLength)
                                .subtract(BigInteger.ONE));
                wordCountForThread = wordCountForThread.add(remainingWordCountForCurrentLength);
                selectedWordCountForCurrentLength = BigInteger.ZERO;
                lengthCounter++;
                if (lengthCounter >= lengths.length)
                {
                    wordCountForCurrentLength = BigInteger.ZERO;
                    break;
                }

                if (wordCountForThread.equals(wordsPerThread))
                {
                    threadCounter++;
                    wordCountForThread = BigInteger.ZERO;
                }

                currentLength = lengths[lengthCounter];
                wordCountForCurrentLength = Counter.countWords(symbolMap, currentLength);
            }
        }

        threadCounter = 0;
        BigInteger remainingWordCountForCurrentLength = wordCountForCurrentLength
                .subtract(selectedWordCountForCurrentLength);

        if (remainingWordCountForCurrentLength.compareTo(BigInteger.valueOf(threadCount)) >= 0)
        {
            throw new RuntimeException("Unexpected condition!: Remaining word count " +
                    "is greater than or equal to thread count");
        }

        while (selectedWordCountForCurrentLength.compareTo(wordCountForCurrentLength) < 0)
        {
            assign(threadCounter, currentLength, selectedWordCountForCurrentLength,
                    selectedWordCountForCurrentLength);

            selectedWordCountForCurrentLength = selectedWordCountForCurrentLength.add(BigInteger.ONE);
            threadCounter++;
        }
        validate();
    }

    /**
     * Checks if the distributed task assignments cover
     * all possible words correctly.
     */
    private void validate()
    {
        HashMap<Integer, IntervalManager> coverageMap = new HashMap<>();
        for (WordProducer wordProducer: wordProducers)
        {
            for (ProducerAssignment producerAssignment: wordProducer.getAssignments())
            {
                int length = producerAssignment.getLength();
                BigInteger startIndex = producerAssignment.getStartIndex();
                BigInteger endIndex = producerAssignment.getEndIndex();
                if (!coverageMap.containsKey(length))
                {
                    coverageMap.put(length, new IntervalManager());
                }

                coverageMap.get(length).addInterval(startIndex, endIndex);
            }
        }

        for (Map.Entry<Integer, IntervalManager> entry: coverageMap.entrySet())
        {
            Integer length = entry.getKey();
            IntervalManager intervalManager = entry.getValue();
            BigInteger realCount = intervalManager.getCount();
            BigInteger requiredCount = Counter.countWords(symbolMap, length);

            if (!realCount.equals(requiredCount))
            {
                throw new RuntimeException(String.format("Validation for word distribution failed! Length: %d" +
                        " | Required count: %d | Real count: %d", length, requiredCount, realCount));
            }
        }
    }

    /**
     * Assigns the given task to the thread (word producer) whose index is specified.
     * @param threadIndex the index of the thread (word producer) to which the given task is assigned
     * @param length length of the words to be produced
     * @param lowerBound lower bound of the
     *                   word interval (the interval that defines the task along with length)
     * @param upperBound upper bound of the
     *                   word interval (the interval that defines the task along with length)
     */
    private void assign(int threadIndex, int length, BigInteger lowerBound, BigInteger upperBound)
    {
        wordProducers.get(threadIndex).addAssignment(
                new ProducerAssignment(symbolMap.get(length), length, lowerBound, upperBound));
    }

    /**
     * Accessor method for wordProducers.
     * @return a List consisting of word producers to which the tasks are assigned
     */
    public List<WordProducer> getWordProducers()
    {
        return wordProducers;
    }

    /**
     * Accessor method for queues.
     * @return a List consisting of queues where the produced words are to be put
     */
    public List<BlockingQueue<String>> getQueues()
    {
        return queues;
    }
}
