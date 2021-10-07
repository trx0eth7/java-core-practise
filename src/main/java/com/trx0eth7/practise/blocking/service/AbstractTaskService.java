package com.trx0eth7.practise.blocking.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author vasilev
 */
public abstract class AbstractTaskService<T> implements TaskService<T> {
    protected ExecutorService executor;
    protected Map<Long, Future<T>> tasks = new ConcurrentHashMap<>();
    protected AtomicLong taskCounter = new AtomicLong();

}
