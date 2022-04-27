package org.project.hlf.sensor;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.project.models.MyResponse;
import org.project.server.ServerImpl;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static org.project.server.ServerImpl.*;

public class Sensor {
    public static void startWeatherSensor() {
        ThreadLocalRandom tlr = ThreadLocalRandom.current();
        Request request = Request.Post("http://localhost:" + SENSORPORT + "/invoke/home1/chaincode1");
        request.setHeader("Content-Type", "application/x-www-form-urlencoded");
        Executors.newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(() -> {
                    try {
                        request.setHeader("Authorization", "Bearer " + ServerImpl.getToken(SENSORPORT));
                        String body = "{ \"method\": \"HouseSensorContract:insertHouseWeather\", \"args\": [\"" + tlr.nextDouble(10, 35) + "\", \"" + tlr.nextDouble(0, 100) + "\" ] }";
                        request.bodyString(body, ContentType.APPLICATION_FORM_URLENCODED);
                        executeRequest(request);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }, 0, 10000, TimeUnit.MILLISECONDS);
    }

    public static void startElectricitySensor() {
        ThreadLocalRandom tlr = ThreadLocalRandom.current();
        Request request = Request.Post("http://localhost:" + SENSORPORT + "/invoke/home1/chaincode1");
        request.setHeader("Content-Type", "application/x-www-form-urlencoded");
        Executors.newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(() -> {
                    try {
                        double washingMachineKw = tlr.nextDouble(0, 4.00001);
                        double fridgeKw = tlr.nextDouble(0, 4.00001);
                        double dishwasherKw = tlr.nextDouble(0, 4.00001);
                        double useElectricity = washingMachineKw + fridgeKw + dishwasherKw;

                        request.setHeader("Authorization", "Bearer " + ServerImpl.getToken(SENSORPORT));
                        String body = "{ \"method\": \"HouseSensorContract:insertHouseElectricity\", \"args\": [\"" + useElectricity + "\", \"" + washingMachineKw + "\", \"" + fridgeKw + "\", \"" + dishwasherKw + "\"] }";
                        request.bodyString(body, ContentType.APPLICATION_FORM_URLENCODED);
                        executeRequest(request);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }, 0, 10000, TimeUnit.MILLISECONDS);
    }

    private static void executeRequest(@NotNull Request request) throws IOException {
        long startTime = System.nanoTime();
        Response response = request.execute();
        long endTime = System.nanoTime();
        HttpResponse httpResponse = response.returnResponse();
        HttpEntity entity = httpResponse.getEntity();
        if (entity != null) {
            String html = EntityUtils.toString(entity);
            System.out.println(html.replaceAll("\\\\", ""));
            System.out.println(ANSI_BLUE + "EXECUTION TIME: " + ((double) (endTime - startTime) / OBL) + " sec" + ANSI_RESET);
        }
    }

    @Contract("_, _ -> new")
    public static @NotNull MyResponse readAllHouseData(@NotNull Request request, String port) throws IOException {
        request.setHeader("Authorization", "Bearer " + ServerImpl.getToken(port));
        long startTime = System.nanoTime();
        Response response = request.execute();
        long endTime = System.nanoTime();
        return new MyResponse(response, ((double) (endTime - startTime) / OBL));
    }
}