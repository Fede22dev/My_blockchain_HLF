package project.sensor;

import com.opencsv.CSVWriter;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.time.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

import static project.Main.*;
import static project.sensor.Sensor.startWeatherSensor;
import static project.sensor.Sensor.stopWeatherSensor;

public class SensorBenchmark {
    public static void benchmarkSensorQuery() {
        new Timer().schedule(new TimerTask() {
            final ArrayList<Double> times = new ArrayList<>();
            final long t0 = System.currentTimeMillis();
            int i = 1;

            @Override
            public void run() {
                if (System.currentTimeMillis() - t0 > 1000 * 60) {
                    cancel();
                    try {
                        CSVWriter writer = new CSVWriter(new FileWriter("/media/sf_Passaggio_File/bench_sensor_query.csv"));
                        writer.writeNext(new String[]{"query sensor time"});

                        double total = 0;
                        for (double time : times) {
                            total += time;
                            writer.writeNext(new String[]{String.valueOf(time).replace(".", ",")});
                        }
                        writer.close();
                        double average = total / times.size();
                        System.out.println("\n" + ANSI_BLUE + "TOTAL EXECUTION TIME: " + total + " sec" + ANSI_RESET);
                        System.out.println(ANSI_BLUE + "AVERAGE EXECUTION TIME: " + average + " sec" + ANSI_RESET);
                        System.out.println(ANSI_BLUE + "TRANSACTION PER MINUTE: " + OMIN / average + " transaction/min" + ANSI_RESET);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    try {
                        Request request = Request.Post("http://localhost:" + TENANTPORT + "/query/home1/chaincode1");
                        request.setHeader("Authorization", "Bearer " + TOKEN.get(TENANTPORT));
                        request.setHeader("Content-Type", "application/x-www-form-urlencoded");

                        LocalDateTime todayMidnight = LocalDateTime.of(LocalDate.now(ZoneId.of("Europe/Rome")), LocalTime.MIDNIGHT);
                        LocalDateTime tomorrowMidnight = todayMidnight.plusDays(1);
                        long todayMillis = todayMidnight.toInstant(ZoneOffset.of("+1")).toEpochMilli();
                        long tomorrowMillis = tomorrowMidnight.toInstant(ZoneOffset.of("+1")).toEpochMilli();

                        String body = "{ \"method\": \"HouseSensorContract:readAllHouseWeather\", \"args\": [\"" + todayMillis + "\", \"" + tomorrowMillis + "\"] }";
                        request.bodyString(body, ContentType.APPLICATION_FORM_URLENCODED);
                        long startTime = System.nanoTime();
                        Response response = request.execute();
                        long endTime = System.nanoTime();
                        HttpResponse httpResponse = response.returnResponse();
                        HttpEntity entity = httpResponse.getEntity();
                        if (entity != null) {
                            String html = EntityUtils.toString(entity);
                            System.out.print("\r" + "QUERY: " + i + " " + html);
                            i++;
                            times.add((double) (endTime - startTime) / OBL);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }, 0, 1000);
    }

    public static void benchmarkSensorInvoke() {
        stopWeatherSensor();
        new Timer().schedule(new TimerTask() {
            final ThreadLocalRandom tlr = ThreadLocalRandom.current();
            final ArrayList<Double> times = new ArrayList<>();
            final long t0 = System.currentTimeMillis();
            int i = 1;

            @Override
            public void run() {
                if (System.currentTimeMillis() - t0 > 1000 * 60) {
                    cancel();
                    try {
                        CSVWriter writer = new CSVWriter(new FileWriter("/media/sf_Passaggio_File/bench_sensor_invoke.csv"));
                        writer.writeNext(new String[]{"invoke sensor time"});

                        double total = 0;
                        for (double time : times) {
                            total += time;
                            writer.writeNext(new String[]{String.valueOf(time).replace(".", ",")});
                        }
                        writer.close();
                        double average = total / times.size();
                        System.out.println("\n" + ANSI_BLUE + "TOTAL EXECUTION TIME: " + total + " sec" + ANSI_RESET);
                        System.out.println(ANSI_BLUE + "AVERAGE EXECUTION TIME: " + average + " sec" + ANSI_RESET);
                        System.out.println(ANSI_BLUE + "TRANSACTION PER MINUTE: " + OMIN / average + " transaction/min" + ANSI_RESET);

                        startWeatherSensor(false);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    try {
                        Request request = Request.Post("http://localhost:" + SENSORPORT + "/invoke/home1/chaincode1");
                        request.setHeader("Authorization", "Bearer " + TOKEN.get(SENSORPORT));
                        request.setHeader("Content-Type", "application/x-www-form-urlencoded");
                        String body = "{ \"method\": \"HouseSensorContract:insertHouseWeather\", \"args\": [\"" + tlr.nextDouble(10, 35) + "\", \"" + tlr.nextDouble(0, 100) + "\" ] }";
                        request.bodyString(body, ContentType.APPLICATION_FORM_URLENCODED);
                        long startTime = System.nanoTime();
                        Response response = request.execute();
                        long endTime = System.nanoTime();
                        HttpResponse httpResponse = response.returnResponse();
                        HttpEntity entity = httpResponse.getEntity();
                        if (entity != null) {
                            String html = EntityUtils.toString(entity);
                            System.out.print("\r" + "INVOKE: " + i + " " + html);
                            i++;
                            times.add((double) (endTime - startTime) / OBL);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }, 0, 125);
    }
}
