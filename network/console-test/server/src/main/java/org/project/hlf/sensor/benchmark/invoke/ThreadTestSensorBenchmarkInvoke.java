package org.project.hlf.sensor.benchmark.invoke;

import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.project.hlf.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.project.server.ServerImpl.*;

class ThreadTestSensorBenchmarkInvoke extends Thread {
    private final String key;
    private final Request request;
    private final ScheduledExecutorService executor;
    private final List<Double> times;
    private ScheduledFuture<?> future;

    ThreadTestSensorBenchmarkInvoke(String key) {
        this.key = key;
        request = Request.Post("http://localhost:" + SENSORPORT + "/invoke/home1/chaincode1");
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
        startNewExecutor();
        Thread.sleep(1000 * MINTESTBENCHMARK);
        future.cancel(false);

        SensorInvokeDataBenchmark.putTimes(key, times);
    }

    private void startNewExecutor() {
        future = executor.scheduleAtFixedRate(() -> {
            try {
                ThreadLocalRandom tlr = ThreadLocalRandom.current();
                String body = "{ \"method\": \"HouseSensorContract:insertHouseWeather\", \"args\": [\"" + tlr.nextDouble(10, 35) + "\", \"" + tlr.nextDouble(0, 100) + "\" ] }";
                request.bodyString(body, ContentType.APPLICATION_FORM_URLENCODED);
                Utils.execRequestInvokeBenchmark(request, key, times);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, 0, RATETESTMILLIS, TimeUnit.MILLISECONDS);
    }
}
