package producer;

import generator.DistributableGenerator;
import generator.NumberSystemGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class WordProducer implements Runnable
{
    private final BlockingQueue<String> queue;
    private final List<ProducerAssignment> assignments;

    /**
     * Constructor of WordProducer class
     * @param queue a {@code BlockingQueue<String>} where the producer
     *              put the produced words
     */
    public WordProducer(BlockingQueue<String> queue)
    {
        this.queue = queue;
        assignments = new ArrayList<>();
    }

    /**
     * Adds the given ProducerAssignment to the assignments list of the producer
     * @param producerAssignment the ProducerAssignment to be added to the assignments list
     */
    public void addAssignment(ProducerAssignment producerAssignment)
    {
        assignments.add(producerAssignment);
    }

    /**
     * Performs produce tasks found in assignments list.
     */
    @Override
    public void run()
    {
        for (ProducerAssignment producerAssignment: assignments)
        {
            DistributableGenerator generator = new NumberSystemGenerator(producerAssignment.getSymbols(),
                    producerAssignment.getStartIndex(), producerAssignment.getEndIndex(),
                    producerAssignment.getLength());
            for (String word: generator)
            {
                try
                {
                    queue.put(word);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
