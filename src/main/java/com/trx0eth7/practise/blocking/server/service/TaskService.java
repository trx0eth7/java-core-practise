package com.trx0eth7.practise.blocking.server.service;

import com.trx0eth7.practise.blocking.message.MessageRecipient;
import com.trx0eth7.practise.blocking.command.Command;

/**
 * @author vasilev
 */
public interface TaskService {

    void send(Command command, MessageRecipient sender);
}
