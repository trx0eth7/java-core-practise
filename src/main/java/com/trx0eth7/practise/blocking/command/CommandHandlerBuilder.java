package com.trx0eth7.practise.blocking.command;

import java.util.function.Consumer;

/**
 * @author vasilev
 */
public class CommandHandlerBuilder {

    private CommandHandler head;
    private CommandHandler tail;


    public CommandHandlerBuilder match(CommandType commandType, Consumer<String> handler) {
        addMatcher(new CommandHandler(commandType, handler));
        return this;
    }

    private void addMatcher(CommandHandler handler) {
        if (tail == null) {
            head = handler;
            tail = handler;
        } else {
            tail = tail.orElse(handler);
        }
    }

    public CommandHandler build() {
        return head;
    }
}
