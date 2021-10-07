package com.trx0eth7.practise.blocking.service.impl;

import com.trx0eth7.practise.blocking.command.Command;
import com.trx0eth7.practise.blocking.command.CommandHandlerBuilder;
import com.trx0eth7.practise.blocking.command.CommandType;
import com.trx0eth7.practise.blocking.service.TaskService;

import java.io.PrintWriter;

/**
 * @author vasilev
 */
public class DefaultTaskService implements TaskService<String> {
    public static final String DEFAULT_MESSAGE = "There is no at least one registered service for this command\n" +
            "example: {\"type\":\"URL\",\"args\":\"https://jsonplaceholder.typicode.com/comments\"}";

    @Override
    public void send(Command command, PrintWriter sender) {
        new CommandHandlerBuilder()
                .match(CommandType.DEFAULT, args -> sender.write(DEFAULT_MESSAGE))
                .match(null, args -> sender.write(DEFAULT_MESSAGE))
                .build()
                .handle(command);
    }
}
