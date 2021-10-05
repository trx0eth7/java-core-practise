package com.trx0eth7.practise.blocking;

import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.Data;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author vasilev
 */
class WebCrawlerService {
    private final ExecutorService executor;
    private final BlockingQueue<Runnable> workQueue;
    private final ThreadFactory factory;
    private final Map<Long, Future<String>> tasks;
    private final AtomicLong taskId = new AtomicLong();

    public WebCrawlerService() {
        factory = new DefaultThreadFactory("downloader", false, Thread.NORM_PRIORITY);
        workQueue = new LinkedBlockingQueue<>();
        executor = new ThreadPoolExecutor(10, 100, 0L, TimeUnit.MILLISECONDS, workQueue, factory);
        tasks = new ConcurrentHashMap<>();
    }

    public long submit(DownloadTask task) {
        var future = executor.submit(task);
        var id = taskId.getAndIncrement();
        tasks.put(id, future);

        return id;
    }

    public String getNow(long id) {
        var future = tasks.remove(id);

        if (future == null) {
            System.out.println("Future[" + id + "] was not found");
            return "null";
        }

        try {
            return future.get();
        } catch (InterruptedException e) {
            System.out.println("Thread was interrupted");
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            System.out.println("Thread was failed by execution");
        }

        return "null";
    }

    public String getWaiting(long id, long timeout, TimeUnit unit) {
        var future = tasks.remove(id);

        if (future == null) {
            System.out.println("Future[" + id + "] was not found");
            return "null";
        }

        try {
            return future.get(timeout, unit);
        } catch (InterruptedException e) {
            System.out.println("Thread was interrupted");
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            System.out.println("Thread was failed by execution");
        } catch (TimeoutException e) {
            System.out.println("Thread was failed by timeout");
        }

        return "null";
    }

    public boolean isDone(long id) {
        var future = tasks.get(id);

        if (future == null) {
            System.out.println("Future[" + id + "] was not found");
            return false;
        }

        return future.isDone();
    }

    public List<TaskInfo> getInfo() {
        var data = new ArrayList<TaskInfo>();

        for (var task : tasks.entrySet()) {
            var id = task.getKey();
            var future = task.getValue();

            data.add(new TaskInfo(id, future.isDone()));
        }

        return data;
    }

    static final class DownloadTask implements Callable<String> {
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

    @Data
    static class TaskInfo {
        private long id;
        private boolean done;

        TaskInfo(long id, boolean done) {
            this.id = id;
            this.done = done;
        }
    }

    static abstract class HttpClientHolder {
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
}
