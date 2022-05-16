package org.project.hlf.sensor;

import org.apache.http.HttpEntity;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.NotNull;
import org.project.models.MyRequest;
import org.project.models.MyResponse;
import org.project.server.TokenManager;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static org.project.ServerConstants.*;
import static org.project.hlf.Utils.executeRequest;

public class Sensor {
    public static void startWeatherSensor() {
        ThreadLocalRandom tlr = ThreadLocalRandom.current();
        Request request = Request.Post("http://localhost:" + SENSOR_PORT + "/invoke/home1/chaincode1");
        request.setHeader("Content-Type", "application/x-www-form-urlencoded");
        Executors.newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(() -> {
                    try {
                        request.setHeader("Authorization", "Bearer " + TokenManager.getToken(SENSOR_PORT));
                        String body = "{ \"method\": \"HouseSensorContract:insertHouseWeather\", \"args\": [\"" + tlr.nextDouble(MIN_TEMPERATURE, MAX_TEMPERATURE) + "\", \"" + tlr.nextDouble(MIN_HUMIDITY, MAX_HUMIDITY) + "\" ] }";
                        request.bodyString(body, ContentType.APPLICATION_FORM_URLENCODED);
                        executeRequestInsertDataSensor(request);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }, SECONDS_DELAY_START_SENSOR, SECONDS_INSERT_DATA_SENSOR, TimeUnit.SECONDS);
    }

    public static void startElectricitySensor() {
        ThreadLocalRandom tlr = ThreadLocalRandom.current();
        Request request = Request.Post("http://localhost:" + SENSOR_PORT + "/invoke/home1/chaincode1");
        request.setHeader("Content-Type", "application/x-www-form-urlencoded");
        Executors.newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(() -> {
                    try {
                        double washingMachineKw = tlr.nextDouble(MIN_ABSORPTION_KW, MAX_ABSORPTION_KW);
                        double fridgeKw = tlr.nextDouble(MIN_ABSORPTION_KW, MAX_ABSORPTION_KW);
                        double dishwasherKw = tlr.nextDouble(MIN_ABSORPTION_KW, MAX_ABSORPTION_KW);
                        double useElectricity = washingMachineKw + fridgeKw + dishwasherKw;

                        request.setHeader("Authorization", "Bearer " + TokenManager.getToken(SENSOR_PORT));
                        String body = "{ \"method\": \"HouseSensorContract:insertHouseElectricity\", \"args\": [\"" + useElectricity + "\", \"" + washingMachineKw + "\", \"" + fridgeKw + "\", \"" + dishwasherKw + "\"] }";
                        request.bodyString(body, ContentType.APPLICATION_FORM_URLENCODED);
                        executeRequestInsertDataSensor(request);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }, SECONDS_DELAY_START_SENSOR, SECONDS_INSERT_DATA_SENSOR, TimeUnit.SECONDS);
    }

    private static void executeRequestInsertDataSensor(@NotNull final Request request) throws IOException {
        long startTime = System.nanoTime();
        Response response = request.execute();
        long endTime = System.nanoTime();
        HttpEntity entity = response.returnResponse().getEntity();
        if (entity != null) {
            System.out.println(EntityUtils.toString(entity).replaceAll("\\\\", ""));
            System.out.println(ANSI_BLUE + "EXECUTION TIME: " + ((double) (endTime - startTime) / ONE_BILION) + " sec" + ANSI_RESET);
        }
    }

    public static @NotNull MyResponse readAllHouseData(@NotNull final MyRequest myRequest) throws IOException {
        return executeRequest(myRequest);
    }
}
