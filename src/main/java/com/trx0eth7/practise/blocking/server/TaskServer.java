package com.trx0eth7.practise.blocking.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.trx0eth7.practise.blocking.command.Command;
import com.trx0eth7.practise.blocking.command.CommandType;
import com.trx0eth7.practise.blocking.exception.ServerInitializationException;
import com.trx0eth7.practise.blocking.message.MessageRecipient;
import com.trx0eth7.practise.blocking.message.PrintWriterRecipient;
import com.trx0eth7.practise.blocking.server.service.TaskService;
import com.trx0eth7.practise.blocking.server.service.impl.DefaultTaskService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author vasilev
 */
public class TaskServer {
    private final Object servicesLock = new Object();
    private final List<TaskService> services = new ArrayList<>();

    private volatile boolean running;

    private final ServerSocket serverSocket;
    private final Gson gson;
    private Thread runThread;

    public TaskServer(int port, int socketTimeout) {
        gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(socketTimeout);
        } catch (IOException e) {
            throw new ServerInitializationException("Socket initialization error", e);
        }

        services.add(new DefaultTaskService());
    }

    public void addService(TaskService service) {
        synchronized (servicesLock) {
            services.add(service);
        }
    }

    public void removeService(TaskService service) {
        synchronized (servicesLock) {
            services.remove(service);
        }
    }

    public synchronized void run() {
        if (runThread != null) {
            throw new IllegalStateException("Tcp server has already run");
        }

        runThread = new Thread(this::await);
        running = true;

        runThread.setName("TCPServer-run");
        runThread.start();
    }

    public boolean isRunning() {
        return runThread != null && running;
    }

    private void await() {
        while (running) {
            try (var socket = serverSocket.accept();
                 var in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 var out = new PrintWriter(socket.getOutputStream());
            ) {
                var text = in.readLine();
                tryHandleCommand(text, new PrintWriterRecipient(out));
            } catch (SocketTimeoutException e) {
                // TODO
            } catch (IOException e) {
                // TODO
            }
        }
    }

    private void tryHandleCommand(String commandAsText, MessageRecipient sender) {
        var command = parseCommand(commandAsText);

        for (var service : services) {
            service.send(command, sender);
        }
    }

    private Command parseCommand(String commandAsText) {
        try {
            return gson.fromJson(commandAsText, Command.class);
        } catch (JsonSyntaxException e) {
            return new Command(CommandType.DEFAULT, "");
        }
    }
}
