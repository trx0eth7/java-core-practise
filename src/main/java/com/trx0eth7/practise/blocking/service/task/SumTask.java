package com.trx0eth7.practise.blocking.service.task;

import java.util.concurrent.Callable;
import java.util.concurrent.RecursiveTask;
import java.util.stream.LongStream;

/**
 * @author vasilev
 */
public class SumTask extends RecursiveTask<Long> implements Callable<Long> {
    private final long number;

    public SumTask(long number) {
        this.number = number;
    }

    @Override
    protected Long compute() {
        return LongStream.range(0L, number + 1).sum();
    }

    @Override
    public Long call() throws Exception {
        return compute();
    }
}
