package com.trx0eth7.practise.blocking.service.impl;

import com.trx0eth7.practise.blocking.command.Command;
import com.trx0eth7.practise.blocking.command.CommandHandlerBuilder;
import com.trx0eth7.practise.blocking.command.CommandType;
import com.trx0eth7.practise.blocking.service.AbstractTaskService;
import com.trx0eth7.practise.blocking.service.task.DownloadTask;
import io.netty.util.concurrent.DefaultThreadFactory;

import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author vasilev
 */
public class WebContentTaskService extends AbstractTaskService<String> {
    private final BlockingQueue<Runnable> workQueue;
    private final ThreadFactory factory;

    public WebContentTaskService() {
        factory = new DefaultThreadFactory("downloader", false, Thread.NORM_PRIORITY);
        workQueue = new LinkedBlockingQueue<>();
        executor = new ThreadPoolExecutor(10, 100, 0L, TimeUnit.MILLISECONDS, workQueue, factory);
    }

    @Override
    public void send(Command command, PrintWriter sender) {
        new CommandHandlerBuilder()
                .match(CommandType.SEND_URL, args -> {
                    var future = executor.submit(new DownloadTask(args));
                    var id = taskCounter.getAndIncrement();

                    tasks.put(id, future);
                    sender.write("Task id: " + id);
                })
                .match(CommandType.RECEIVE_URL, args -> {
                    // TODO exception
                    var id = Long.parseLong(args);
                    var future = tasks.remove(id);

                    try {
                        var content = future.get();
                        sender.write("Sum: " + content);
                    } catch (InterruptedException | ExecutionException e) {
                        sender.write("undefined");
                    }
                })
                .build()
                .handle(command);
    }
}
