package com.trx0eth7.practise.blocking.command;

import com.trx0eth7.practise.blocking.message.MessageRecipient;

import java.util.function.BiConsumer;

/**
 * @author vasilev
 */
public final class CommandHandlerBuilder {

    private CommandHandler head;
    private CommandHandler tail;

    private CommandHandlerBuilder() {
    }

    public static CommandHandlerBuilder create() {
        return new CommandHandlerBuilder();
    }

    public CommandHandlerBuilder match(CommandType commandType, BiConsumer<MessageRecipient, String> handler) {
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
