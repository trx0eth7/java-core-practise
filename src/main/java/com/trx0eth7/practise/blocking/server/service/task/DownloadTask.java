package com.trx0eth7.practise.blocking.server.service.task;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.Callable;

/**
 * @author vasilev
 */
public final class DownloadTask implements Callable<String> {
    private final String url;

    public DownloadTask(String url) {
        this.url = url;
    }

    @Override
    public String call() throws Exception {
        var client = HttpClientHolder.getInstance();
        var request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofMinutes(2))
                .GET()
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8))
                .thenApply(HttpResponse::body)
                .get();
    }
}
