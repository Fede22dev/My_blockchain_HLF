package org.project.hlf.supervisor.benchmark.query;

import org.apache.http.HttpEntity;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.project.server.TokenManager;

import java.io.IOException;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static org.project.ServerConstants.*;

class ThreadSupervisorBenchmarkQuery extends Thread {
    private final String key;
    private final Request request;
    private final ScheduledExecutorService executor;
    private final List<Double> times;
    private ScheduledFuture<?> future;

    ThreadSupervisorBenchmarkQuery(final String key) {
        this.key = key;
        request = Request.Post("http://localhost:" + TENANT_PORT + "/query/home1/chaincode1");
        executor = Executors.newScheduledThreadPool(SIZE_THREAD_POOL);
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
        request.setHeader("Authorization", "Bearer " + TokenManager.getToken(TENANT_PORT));
        request.setHeader("Content-Type", "application/x-www-form-urlencoded");
        LocalDateTime todayMidnight = LocalDateTime.of(LocalDate.now(ZoneId.of("Europe/Rome")), LocalTime.MIDNIGHT);
        LocalDateTime tomorrowMidnight = todayMidnight.plusDays(1);
        long todayMillis = todayMidnight.toInstant(ZoneOffset.of("+1")).toEpochMilli();
        long tomorrowMillis = tomorrowMidnight.toInstant(ZoneOffset.of("+1")).toEpochMilli();
        String body = "{ \"method\": \"HouseSupervisorContract:queryBenchmark\", \"args\": [\"" + todayMillis + "\", \"" + tomorrowMillis + "\"] }";
        request.bodyString(body, ContentType.APPLICATION_FORM_URLENCODED);

        startNewExecutor();
        Thread.sleep(ONE_THOUSAND * SECONDS_DURATION_BENCHMARK);
        future.cancel(false);
        executor.shutdown();

        SupervisorQueryDataBenchmark.putTimes(key, times);
    }

    private void startNewExecutor() {
        future = executor.scheduleAtFixedRate(() -> {
            try {
                long startTime = System.nanoTime();
                Response response = request.execute();
                long endTime = System.nanoTime();
                HttpEntity entity = response.returnResponse().getEntity();
                if (entity != null) {
                    double time = (double) (endTime - startTime) / ONE_BILION;
                    System.out.println("QUERY: " + key + " -> " + Thread.currentThread().getName() + " -> " + executor + " -> " + EntityUtils.toString(entity) + " -> " + time);
                    times.add(time);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, 0, MILLIS_RATE_REQUEST_BENCHMARK, TimeUnit.MILLISECONDS);
    }
}
