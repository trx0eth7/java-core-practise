package com.trx0eth7.practise.blocking.server.service.impl;

import com.trx0eth7.practise.blocking.command.Command;
import com.trx0eth7.practise.blocking.command.CommandHandlerBuilder;
import com.trx0eth7.practise.blocking.command.CommandType;
import com.trx0eth7.practise.blocking.message.MessageRecipient;
import com.trx0eth7.practise.blocking.server.service.TaskService;

/**
 * @author vasilev
 */
public class DefaultTaskService implements TaskService {
    public static final String DEFAULT_MESSAGE = "There is no at least one registered service for this command\n" +
            "example: {\"type\":\"URL\",\"args\":\"https://jsonplaceholder.typicode.com/comments\"}";

    @Override
    public void send(Command command, MessageRecipient sender) {
        CommandHandlerBuilder.create()
                .match(CommandType.DEFAULT, this::onDefaultHandle)
                .match(null, this::onDefaultHandle)
                .build()
                .handle(command, sender);
    }

    private void onDefaultHandle(MessageRecipient sender, String args) {
        sender.reply(DEFAULT_MESSAGE);
    }
}
