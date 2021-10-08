package com.trx0eth7.practise.blocking.server.service.impl;

import com.trx0eth7.practise.blocking.command.Command;
import com.trx0eth7.practise.blocking.command.CommandHandlerBuilder;
import com.trx0eth7.practise.blocking.command.CommandType;
import com.trx0eth7.practise.blocking.message.MessageRecipient;
import com.trx0eth7.practise.blocking.server.service.AbstractTaskService;
import com.trx0eth7.practise.blocking.server.service.task.SumTask;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

/**
 * @author vasilev
 */
public class ComputingTaskService extends AbstractTaskService<Long> {

    public ComputingTaskService() {
        executor = new ForkJoinPool();
    }

    @Override
    public void send(Command command, MessageRecipient sender) {
        CommandHandlerBuilder.create()
                .match(CommandType.SEND_SUM, this::onCompute)
                .match(CommandType.RECEIVE_SUM, this::onReceive)
                .build()
                .handle(command, sender);
    }

    // TODO exception
    private void onCompute(MessageRecipient sender, String args) {
        var number = Long.parseLong(args);
        var future = executor.submit(new SumTask(number));
        var id = taskCounter.getAndIncrement();

        tasks.put(id, future);
        sender.reply("Task id: " + id);
    }

    // TODO exception
    private void onReceive(MessageRecipient sender, String args) {
        var id = Long.parseLong(args);
        var future = tasks.remove(id);

        try {
            var sum = future.get();
            sender.reply("Sum: " + sum);
        } catch (InterruptedException | ExecutionException e) {
            sender.reply("undefined");
        }
    }
}
