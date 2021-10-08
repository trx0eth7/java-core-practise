package com.trx0eth7.practise.blocking.server.service.impl;

import com.trx0eth7.practise.blocking.command.Command;
import com.trx0eth7.practise.blocking.command.CommandHandlerBuilder;
import com.trx0eth7.practise.blocking.command.CommandType;
import com.trx0eth7.practise.blocking.message.MessageRecipient;
import com.trx0eth7.practise.blocking.server.service.AbstractTaskService;
import com.trx0eth7.practise.blocking.server.service.task.DownloadTask;
import io.netty.util.concurrent.DefaultThreadFactory;

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
    public void send(Command command, MessageRecipient sender) {
        CommandHandlerBuilder.create()
                .match(CommandType.SEND_URL, this::onDownload)
                .match(CommandType.RECEIVE_URL, this::onReceive)
                .build()
                .handle(command, sender);
    }

    // TODO exception
    private void onReceive(MessageRecipient sender, String args) {
        var id = Long.parseLong(args);
        var future = tasks.remove(id);

        try {
            var content = future.get();
            sender.reply("Content: " + content);
        } catch (InterruptedException | ExecutionException e) {
            sender.reply("undefined");
        }
    }

    // TODO exception
    private void onDownload(MessageRecipient sender, String args) {
        var future = executor.submit(new DownloadTask(args));
        var id = taskCounter.getAndIncrement();

        tasks.put(id, future);
        sender.reply("Task id: " + id);

    }
}
