package com.trx0eth7.practise.blocking.command;

import com.trx0eth7.practise.blocking.message.MessageRecipient;

import java.util.function.BiConsumer;

/**
 * @author vasilev
 */
public class CommandHandler {
    private final CommandType commandType;
    private final BiConsumer<MessageRecipient, String> handler;

    private CommandHandler next;

    public CommandHandler(CommandType commandType, BiConsumer<MessageRecipient, String> handler) {
        this.commandType = commandType;
        this.handler = handler;
    }

    public void handle(Command command, MessageRecipient sender) {
        if (command.getType() == commandType) {
            handler.accept(sender, command.getArgs());
        } else if (next != null) {
            next.handle(command, sender);
        }
    }

    public CommandHandler orElse(CommandHandler next) {
        this.next = next;
        return next;
    }
}
