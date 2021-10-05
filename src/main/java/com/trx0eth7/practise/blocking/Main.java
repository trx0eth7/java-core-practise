package com.trx0eth7.practise.blocking;

/**
 * @author vasilev
 */
public class Main {
    public static void main(String[] args) {
        var tcpServer = new TCPCommandServer(8083, 10000);

        System.out.println("Server running: " + tcpServer.isRunning());

        tcpServer.run();

        System.out.println("Server run");
        System.out.println("Server running: " + tcpServer.isRunning());
    }

}
