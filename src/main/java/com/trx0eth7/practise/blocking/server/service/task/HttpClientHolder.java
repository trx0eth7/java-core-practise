package com.trx0eth7.practise.blocking.server.service.task;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.concurrent.Executors;

/**
 * @author vasilev
 */
public abstract class HttpClientHolder {
    private static HttpClient INSTANCE;

    public static HttpClient getInstance() {
        if (INSTANCE != null) {
            return INSTANCE;
        }

        synchronized (HttpClientHolder.class) {
            if (INSTANCE == null) {
                INSTANCE = HttpClient.newBuilder()
                        .version(HttpClient.Version.HTTP_2)
                        .followRedirects(HttpClient.Redirect.NORMAL)
                        .connectTimeout(Duration.ofSeconds(20))
                        .executor(Executors.newFixedThreadPool(10))
                        .build();
            }

            return INSTANCE;
        }
    }
}
