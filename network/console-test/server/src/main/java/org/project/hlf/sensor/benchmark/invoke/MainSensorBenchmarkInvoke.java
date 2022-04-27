package org.project.hlf.sensor.benchmark.invoke;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.NotNull;
import org.project.server.ServerImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static org.project.server.ServerImpl.*;

class MainSensorBenchmarkInvoke {
    private static final ArrayList<Double> times = new ArrayList<>();
    private static final ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);
    private static final Request request = Request.Post("http://localhost:" + SENSORPORT + "/invoke/home1/chaincode1");

    private static synchronized void addTime(double time) {
        times.add(time);
    }

    public static void main(String @NotNull [] args) throws InterruptedException {
        request.setHeader("Authorization", "Bearer " + ServerImpl.getToken(SENSORPORT));
        request.setHeader("Content-Type", "application/x-www-form-urlencoded");

        startNewExecutor();
        Thread.sleep(1000 * MINTEST);
        executor.shutdownNow();

        SensorInvokeDataBenchmark.putTimes(args[0], times);
    }

    private static void startNewExecutor() {
        executor.scheduleAtFixedRate(() -> {
            try {
                ThreadLocalRandom tlr = ThreadLocalRandom.current();
                String body = "{ \"method\": \"HouseSensorContract:insertHouseWeather\", \"args\": [\"" + tlr.nextDouble(10, 35) + "\", \"" + tlr.nextDouble(0, 100) + "\" ] }";
                request.bodyString(body, ContentType.APPLICATION_FORM_URLENCODED);
                long startTime = System.nanoTime();
                Response response = request.execute();
                long endTime = System.nanoTime();
                HttpResponse httpResponse = response.returnResponse();
                HttpEntity entity = httpResponse.getEntity();
                if (entity != null) {
                    String html = EntityUtils.toString(entity);
                    System.out.println("INVOKE: " + " " + html);
                    addTime((double) (endTime - startTime) / OBL);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, 0, RATETESTMILLIS, TimeUnit.MILLISECONDS);
    }
}
