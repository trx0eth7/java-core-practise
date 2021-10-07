package com.trx0eth7.practise.blocking.command;

import java.util.function.Consumer;

/**
 * @author vasilev
 */
public class CommandHandler {
    private final CommandType commandType;
    private final Consumer<String> handler;

    private CommandHandler next;

    public CommandHandler(CommandType commandType, Consumer<String> handler) {
        this.commandType = commandType;
        this.handler = handler;
    }

    public void handle(Command command) {
        if (command.getType() == commandType) {
            handler.accept(command.getArgs());
        } else if (next != null) {
            next.handle(command);
        }
    }

    public CommandHandler orElse(CommandHandler next) {
        this.next = next;
        return next;
    }
}
