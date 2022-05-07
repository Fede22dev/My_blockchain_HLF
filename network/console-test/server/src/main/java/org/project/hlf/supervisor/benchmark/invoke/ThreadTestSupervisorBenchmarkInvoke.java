package org.project.hlf.supervisor.benchmark.invoke;

import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.project.hlf.Utils;
import org.project.server.ServerImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static org.project.server.ServerImpl.*;

class ThreadTestSupervisorBenchmarkInvoke extends Thread {
    private final String key;
    private final Request request;
    private final ScheduledExecutorService executor;
    private final List<Double> times;
    private ScheduledFuture<?> future;

    ThreadTestSupervisorBenchmarkInvoke(String key) {
        this.key = key;
        request = Request.Post("http://localhost:" + TENANTPORT + "/invoke/home1/chaincode1");
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
        request.setHeader("Authorization", "Bearer " + ServerImpl.getToken(TENANTPORT));
        request.setHeader("Content-Type", "application/x-www-form-urlencoded");
        String body = "{ \"method\": \"HouseSupervisorContract:invokeBenchmark\", \"args\": [] }";
        request.bodyString(body, ContentType.APPLICATION_FORM_URLENCODED);

        startNewExecutor();
        Thread.sleep(1000 * MINTESTBENCHMARK);
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
        }, 0, RATETESTMILLIS, TimeUnit.MILLISECONDS);
    }
}
