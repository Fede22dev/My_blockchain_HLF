package org.project.hlf.supervisor.benchmark.invoke;

import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.project.hlf.Utils;
import org.project.server.TokenManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static org.project.ServerConstants.*;

class ThreadSupervisorBenchmarkInvoke extends Thread {
    private final String key;
    private final Request request;
    private final ScheduledExecutorService executor;
    private final List<Double> times;
    private ScheduledFuture<?> future;

    ThreadSupervisorBenchmarkInvoke(final String key) {
        this.key = key;
        request = Request.Post("http://localhost:" + TENANT_PORT + "/invoke/home1/chaincode1");
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
        String body = "{ \"method\": \"HouseSupervisorContract:invokeBenchmark\", \"args\": [] }";
        request.bodyString(body, ContentType.APPLICATION_FORM_URLENCODED);

        startNewExecutor();
        Thread.sleep(ONE_THOUSAND * SECONDS_DURATION_BENCHMARK);
        future.cancel(false);
        executor.shutdown();

        SupervisorInvokeDataBenchmark.putTimes(key, times);
    }

    private void startNewExecutor() {
        future = executor.scheduleAtFixedRate(() -> {
            try {
                Utils.execRequestInvokeBenchmark(request, key, executor, times);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, 0, MILLIS_RATE_REQUEST_BENCHMARK, TimeUnit.MILLISECONDS);
    }
}
