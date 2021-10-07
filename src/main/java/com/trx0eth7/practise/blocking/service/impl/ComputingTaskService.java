package com.trx0eth7.practise.blocking.service.impl;

import com.trx0eth7.practise.blocking.command.Command;
import com.trx0eth7.practise.blocking.command.CommandHandlerBuilder;
import com.trx0eth7.practise.blocking.command.CommandType;
import com.trx0eth7.practise.blocking.service.AbstractTaskService;
import com.trx0eth7.practise.blocking.service.task.SumTask;

import java.io.PrintWriter;
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
    public void send(Command command, PrintWriter sender) {
        new CommandHandlerBuilder()
                .match(CommandType.SEND_SUM, args -> {
                    // TODO exception
                    var number = Long.parseLong(args);
                    var future = executor.submit(new SumTask(number));
                    var id = taskCounter.getAndIncrement();

                    tasks.put(id, future);
                    sender.write("Task id: " + id);
                })
                .match(CommandType.RECEIVE_SUM, args -> {
                    // TODO exception
                    var id = Long.parseLong(args);
                    var future = tasks.remove(id);

                    try {
                        var sum = future.get();
                        sender.write("Sum: " + sum);
                    } catch (InterruptedException | ExecutionException e) {
                        sender.write("undefined");
                    }
                })
                .build()
                .handle(command);
    }
}
