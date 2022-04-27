package org.project.hlf.enroll.benchmark;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.project.server.ServerImpl.OBL;

public class MainEnrollBenchmark {
    private static final ArrayList<Double> times = new ArrayList<>();
    private static final ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);
    private static final Request request = Request.Post("http://localhost:8802/user/enroll");

    private static synchronized void addTime(double time) {
        times.add(time);
    }

    public static void main(String @NotNull [] args) throws InterruptedException {
        String body = "{\"id\": \"admin\", \"secret\": \"adminpw\"}";
        request.bodyString(body, ContentType.APPLICATION_FORM_URLENCODED);
        request.setHeader("Authorization", "Bearer");
        request.setHeader("Content-Type", "application/x-www-form-urlencoded");

        startNewExecutor();
        Thread.sleep(1000 * 60);
        executor.shutdownNow();

        EnrollDataBenchmark.putTimes(args[0], times);
    }

    private static void startNewExecutor() {
        executor.scheduleAtFixedRate(() -> {
            try {
                long startTime = System.nanoTime();
                Response response = request.execute();
                long endTime = System.nanoTime();
                HttpResponse httpResponse = response.returnResponse();
                HttpEntity entity = httpResponse.getEntity();
                if (entity != null) {
                    String html = EntityUtils.toString(entity);
                    System.out.print("\r" + "ENROLL: " + " -> " + html);
                    addTime((double) (endTime - startTime) / OBL);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, 0, 500, TimeUnit.MILLISECONDS);
    }
}