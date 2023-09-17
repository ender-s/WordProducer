# WordProducer
A Java library that produces all word permutations at variable lengths for given alphabets in a distributed manner such that produced words can then be used by multiple consumer threads concurrently.

# Sample Usage

```java
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import enders.wordproducer.manager.WordProducerManager;

public class Main {
    public static void main(String[] args) {
        List<String> symbolList = "abc0123"
                .chars()
                .mapToObj(e -> String.valueOf((char) e))
                .collect(Collectors.toList());

        WordProducerManager wpm = new WordProducerManager.Builder()
                .setNumberOfQueues(3)
                .setThreadsPerQueueAuto()
                .putSymbolsToRange(1, 4, symbolList)
                .build();

        List<BlockingQueue<String>> queues = wpm.produce();
        ExecutorService es = Executors.newFixedThreadPool(queues.size());
        for (BlockingQueue<String> q : queues) {
            es.execute(() -> {
                while (!wpm.isCompleted()) {
                    try {
                        String word = q.poll(100, TimeUnit.MILLISECONDS);
                        if (word != null) {
                            System.out.println(word);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        es.shutdown();
    }
}
```