package com.trx0eth7.practise.blocking;

import com.trx0eth7.practise.blocking.server.TaskServer;
import com.trx0eth7.practise.blocking.server.service.impl.ComputingTaskService;
import com.trx0eth7.practise.blocking.server.service.impl.WebContentTaskService;

/**
 * @author vasilev
 */
public class Main {
    public static void main(String[] args) {
        var tcpServer = new TaskServer(8083, 10_000);

        tcpServer.addService(new ComputingTaskService());
        tcpServer.addService(new WebContentTaskService());

        System.out.println("Server running: " + tcpServer.isRunning());
        System.out.println("Server run");

        tcpServer.run();

        System.out.println("Server running: " + tcpServer.isRunning());
    }

}
