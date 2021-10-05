package com.trx0eth7.practise.blocking;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.trx0eth7.practise.blocking.exception.ServerInitializationException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;

/**
 * @author vasilev
 */
public class TCPCommandServer {
    private volatile boolean running;
    private WebCrawlerService webCrawlerService;
    private SumComputingService sumComputingService;
    private ServerSocket serverSocket;
    private Thread runThread;
    private Gson gson;

    public TCPCommandServer(int port, int socketTimeout) {
        this.webCrawlerService = new WebCrawlerService();
        this.sumComputingService = new SumComputingService();
        this.gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(socketTimeout);
        } catch (IOException e) {
            throw new ServerInitializationException("Socket initialization error", e);
        }
    }

    public synchronized void run() {
        if (runThread != null) {
            throw new IllegalStateException("Tcp server has already run");
        }

        runThread = new Thread(this::internalAccept);
        running = true;

        runThread.setName("TCPServer-run");
        runThread.start();
    }

    public boolean isRunning() {
        return runThread != null && running;
    }

    private void internalAccept() {
        while (running) {
            try (var socket = serverSocket.accept();
                 var in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 var out = new PrintWriter(socket.getOutputStream());
            ) {
                var text = in.readLine();
                var response = handleTextAsCommand(text);

                out.write(response);
            } catch (SocketTimeoutException e) {
                // TODO
            } catch (IOException e) {
                // TODO
            }
        }
    }

    private String handleTextAsCommand(String text) {
        try {
            var command = gson.fromJson(text, Command.class);

            switch (command.getType()) {
                case SEND_URL:
                    return "task url id: " + webCrawlerService.submit(new WebCrawlerService.DownloadTask(command.getArgs()));
                case RECEIVE_URL:
                    var taskId = Long.parseLong(command.getArgs());
                    var done = webCrawlerService.isDone(taskId);
                    return "result: " + (done ? webCrawlerService.getNow(taskId) : "not ready");
                case SEND_SUM:
                    var number = Long.parseLong(command.getArgs());
                    return "task sum id: " + sumComputingService.submit(new SumComputingService.RecursiveSumTask(number));
                case RECEIVE_SUM:
                default:
                    return "not implemented";
            }
        } catch (JsonSyntaxException e) {
            return "command is not valid, example: {\"type\":\"URL\",\"args\":\"https://jsonplaceholder.typicode.com/comments\"}";
        } catch (Exception e) {
            return "undefine";
        }
    }

}
