package org.project.hlf.enroll.benchmark;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
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

class TestEnrollBenchmark {
    private static final ArrayList<Double> times = new ArrayList<>();
    private static final ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);
    private static final Request request = Request.Post("http://localhost:" + TENANTPORT + "/user/enroll");
    private static ScheduledFuture<?> future;

    private static synchronized void addTime(double time) {
        times.add(time);
    }

    private static void startNewExecutor() {
        future = executor.scheduleAtFixedRate(() -> {
            try {
                long startTime = System.nanoTime();
                Response response = request.execute();
                long endTime = System.nanoTime();
                HttpResponse httpResponse = response.returnResponse();
                HttpEntity entity = httpResponse.getEntity();
                if (entity != null) {
                    String html = EntityUtils.toString(entity);
                    System.out.println("ENROLL: " + html);
                    addTime((double) (endTime - startTime) / OBL);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, 0, RATETESTMILLIS, TimeUnit.MILLISECONDS);
    }

    static void testEnroll(String key) throws InterruptedException {
        String body = "{\"id\": \"admin\", \"secret\": \"adminpw\"}";
        request.bodyString(body, ContentType.APPLICATION_FORM_URLENCODED);
        request.setHeader("Authorization", "Bearer");
        request.setHeader("Content-Type", "application/x-www-form-urlencoded");

        startNewExecutor();
        Thread.sleep(1000 * MINTEST);
        future.cancel(false);

        EnrollDataBenchmark.putTimes(key, times);
    }
}
