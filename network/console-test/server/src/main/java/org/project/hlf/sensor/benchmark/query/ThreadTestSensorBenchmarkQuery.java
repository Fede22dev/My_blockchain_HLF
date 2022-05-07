package org.project.hlf.sensor.benchmark.query;

import org.apache.http.HttpEntity;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.project.server.ServerImpl;

import java.io.IOException;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static org.project.server.ServerImpl.*;

class ThreadTestSensorBenchmarkQuery extends Thread {
    private final String key;
    private final Request request;
    private final ScheduledExecutorService executor;
    private final List<Double> times;
    private ScheduledFuture<?> future;

    ThreadTestSensorBenchmarkQuery(String key) {
        this.key = key;
        request = Request.Post("http://localhost:" + SENSORPORT + "/query/home1/chaincode1");
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
        request.setHeader("Authorization", "Bearer " + ServerImpl.getToken(SENSORPORT));
        request.setHeader("Content-Type", "application/x-www-form-urlencoded");
        LocalDateTime todayMidnight = LocalDateTime.of(LocalDate.now(ZoneId.of("Europe/Rome")), LocalTime.MIDNIGHT);
        LocalDateTime tomorrowMidnight = todayMidnight.plusDays(1);
        long todayMillis = todayMidnight.toInstant(ZoneOffset.of("+1")).toEpochMilli();
        long tomorrowMillis = tomorrowMidnight.toInstant(ZoneOffset.of("+1")).toEpochMilli();
        String body = "{ \"method\": \"HouseSensorContract:readAllHouseWeather\", \"args\": [\"" + todayMillis + "\", \"" + tomorrowMillis + "\"] }";
        request.bodyString(body, ContentType.APPLICATION_FORM_URLENCODED);

        startNewExecutor();
        Thread.sleep(1000 * MINTESTBENCHMARK);
        future.cancel(false);
        executor.shutdown();

        SensorQueryDataBenchmark.putTimes(key, times);
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
                    System.out.println("QUERY: " + key + " -> " + Thread.currentThread().getName() + " -> " + executor + " -> " + EntityUtils.toString(entity) + " -> " + time);
                    times.add(time);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, 0, RATETESTMILLIS, TimeUnit.MILLISECONDS);
    }
}
