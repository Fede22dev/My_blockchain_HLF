package org.project.hlf.enroll.benchmark;

import org.apache.http.HttpEntity;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static org.project.server.ServerImpl.*;

class ThreadTestEnrollBenchmark extends Thread {
    private final String key;
    private final Request request;
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);
    private final ArrayList<Double> times = new ArrayList<>();
    private ScheduledFuture<?> future;

    ThreadTestEnrollBenchmark(String key) {
        this.key = key;
        request = Request.Post("http://localhost:" + TENANTPORT + "/user/enroll");
        start();
    }

    @Override
    public void run() {
        try {
            testEnroll();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void testEnroll() throws InterruptedException {
        String body = "{\"id\": \"admin\", \"secret\": \"adminpw\"}";
        request.bodyString(body, ContentType.APPLICATION_FORM_URLENCODED);
        request.setHeader("Authorization", "Bearer");
        request.setHeader("Content-Type", "application/x-www-form-urlencoded");

        startNewExecutor();
        Thread.sleep(1000 * MINTESTBENCHMARK);
        future.cancel(false);

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
                    System.out.println("ENROLL: " + key + " " + EntityUtils.toString(entity) + " " + time);
                    times.add(time);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, 0, RATETESTMILLIS, TimeUnit.MILLISECONDS);
    }
}
