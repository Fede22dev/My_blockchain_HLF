package org.project.hlf.sensor.benchmark.invoke;

import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.project.hlf.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.project.ServerConstants.*;

class ThreadSensorBenchmarkInvoke extends Thread {
    private final String key;
    private final Request request;
    private final ScheduledExecutorService executor;
    private final List<Double> times;
    private ScheduledFuture<?> future;

    ThreadSensorBenchmarkInvoke(final String key) {
        this.key = key;
        request = Request.Post("http://localhost:" + SENSOR_PORT + "/invoke/home1/chaincode1");
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
        startNewExecutor();
        Thread.sleep(ONE_THOUSAND * SECONDS_DURATION_BENCHMARK);
        future.cancel(false);
        executor.shutdown();

        SensorInvokeDataBenchmark.putTimes(key, times);
    }

    private void startNewExecutor() {
        future = executor.scheduleAtFixedRate(() -> {
            try {
                ThreadLocalRandom tlr = ThreadLocalRandom.current();
                String body = "{ \"method\": \"HouseSensorContract:insertHouseWeather\", \"args\": [\"" + tlr.nextDouble(MIN_TEMPERATURE, MAX_TEMPERATURE) + "\", \"" + tlr.nextDouble(MIN_HUMIDITY, MAX_HUMIDITY) + "\" ] }";
                request.bodyString(body, ContentType.APPLICATION_FORM_URLENCODED);
                Utils.executeRequestInvokeBenchmark(request, key, executor, times);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, 0, MILLIS_RATE_REQUEST_BENCHMARK, TimeUnit.MILLISECONDS);
    }
}
