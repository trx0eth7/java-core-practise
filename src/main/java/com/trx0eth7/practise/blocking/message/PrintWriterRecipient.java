package com.trx0eth7.practise.blocking.message;

import java.io.PrintWriter;

/**
 * @author vasilev
 */
public class PrintWriterRecipient implements MessageRecipient {
    private final PrintWriter out;

    public PrintWriterRecipient(PrintWriter out) {
        this.out = out;
    }

    @Override
    public void reply(String message) {
        out.write(message);
    }
}
