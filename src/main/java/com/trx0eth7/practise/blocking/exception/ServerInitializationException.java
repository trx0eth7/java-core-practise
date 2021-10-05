package com.trx0eth7.practise.blocking.exception;

/**
 * @author vasilev
 */
public class ServerInitializationException extends RuntimeException {
    public ServerInitializationException() {
    }

    public ServerInitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
