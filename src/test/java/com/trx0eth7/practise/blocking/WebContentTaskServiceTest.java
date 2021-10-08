package com.trx0eth7.practise.blocking;

import com.trx0eth7.practise.blocking.server.service.WebCrawlerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

/**
 * @author vasilev
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WebContentTaskServiceTest {

    private WebCrawlerService webCrawlerService;

    @BeforeAll
    void beforeAll() {
        webCrawlerService = new WebCrawlerService();
    }

    @BeforeEach
    void setUp() {
        System.out.println("setUp");
    }

    @Test
    void submitTest() {
        var task1 = new WebCrawlerService.DownloadTask("https://jsonplaceholder.typicode.com/posts");
        var task2 = new WebCrawlerService.DownloadTask("https://jsonplaceholder.typicode.com/users");
        var task3 = new WebCrawlerService.DownloadTask("https://jsonplaceholder.typicode.com/photos");

        webCrawlerService.submit(task1);
        webCrawlerService.submit(task2);
        webCrawlerService.submit(task3);

        long taskDoneId = -1;

        do {
            var tasks = webCrawlerService.getInfo();

            for (var task : tasks) {
                if (task.isDone()) {
                    taskDoneId = task.getId();
                }
            }

        } while (taskDoneId < 0);

        Assertions.assertTrue(webCrawlerService.isDone(taskDoneId));
        Assertions.assertNotNull(webCrawlerService.getNow(taskDoneId));
        Assertions.assertEquals("null", webCrawlerService.getNow(taskDoneId));
    }
}