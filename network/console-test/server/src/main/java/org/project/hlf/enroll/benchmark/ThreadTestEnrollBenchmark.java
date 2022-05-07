package org.project.hlf.enroll.benchmark;

import org.apache.http.HttpEntity;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static org.project.server.ServerImpl.*;

class ThreadTestEnrollBenchmark extends Thread {
    private final String key;
    private final Request request;
    private final ScheduledExecutorService executor;
    private final List<Double> times;
    private ScheduledFuture<?> future;

    ThreadTestEnrollBenchmark(String key) {
        this.key = key;
        request = Request.Post("http://localhost:" + TENANTPORT + "/user/enroll");
        executor = Executors.newScheduledThreadPool(5);
        times = new ArrayList<>();
        start();
    }

    @Override
    public void run() {
        try {
            test();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void test() throws InterruptedException {
        String body = "{\"id\": \"admin\", \"secret\": \"adminpw\"}";
        request.bodyString(body, ContentType.APPLICATION_FORM_URLENCODED);
        request.setHeader("Authorization", "Bearer");
        request.setHeader("Content-Type", "application/x-www-form-urlencoded");

        startNewExecutor();
        Thread.sleep(1000 * MINTESTBENCHMARK);
        future.cancel(false);
        executor.shutdown();

        EnrollDataBenchmark.putTimes(key, times);
    }

    private void startNewExecutor() {
        future = executor.scheduleAtFixedRate(() -> {
            try {
                long startTime = System.nanoTime();
                Response response = request.execute();
                long endTime = System.nanoTime();
                HttpEntity entity = response.returnResponse().getEntity();
                if (entity != null) {
                    double time = (double) (endTime - startTime) / OBL;
                    System.out.println("ENROLL: " + key + " -> " + Thread.currentThread().getName() + " -> " + executor + " -> " + EntityUtils.toString(entity) + " -> " + time);
                    times.add(time);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, 0, RATETESTMILLIS, TimeUnit.MILLISECONDS);
    }
}
