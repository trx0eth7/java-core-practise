package com.trx0eth7.practise.blocking;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.LongStream;

/**
 * @author vasilev
 */
public class SumComputingService {
    private final ForkJoinPool executor;
    private final Map<Long, ForkJoinTask<Long>> tasks;
    private final AtomicLong taskId = new AtomicLong();

    public SumComputingService() {
        executor = new ForkJoinPool();
        tasks = new ConcurrentHashMap<>();
    }

    public long submit(SumComputingService.RecursiveSumTask task) {
        var future = executor.submit(task);
        var id = taskId.getAndIncrement();

        tasks.put(id, future);

        return id;
    }

    static class RecursiveSumTask extends RecursiveTask<Long> {
        private final long number;

        public RecursiveSumTask(long number) {
            this.number = number;
        }

        @Override
        protected Long compute() {
            return LongStream.range(0L, number + 1).sum();
        }
    }
}
