package com.trx0eth7.practise.blocking.service;

import com.trx0eth7.practise.blocking.command.Command;

import java.io.PrintWriter;

/**
 * @author vasilev
 */
public interface TaskService<T> {

    void send(Command command, PrintWriter sender);
}
