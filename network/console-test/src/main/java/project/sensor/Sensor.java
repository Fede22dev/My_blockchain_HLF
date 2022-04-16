package project.sensor;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

import static project.Main.*;

public class Sensor {
    private static Timer timerWeather = null;
    private static Timer timerElectricity = null;

    public static void startWeatherSensor(Boolean debugPrint) {
        if (timerWeather != null) {
            timerWeather.cancel();
        }

        timerWeather = new Timer();
        timerWeather.schedule(new TimerTask() {
            final ThreadLocalRandom tlr = ThreadLocalRandom.current();

            @Override
            public void run() {
                try {
                    Request request = Request.Post("http://localhost:" + SENSORPORT + "/invoke/home1/chaincode1");
                    request.setHeader("Authorization", "Bearer " + TOKEN.get(SENSORPORT));
                    request.setHeader("Content-Type", "application/x-www-form-urlencoded");
                    String body = "{ \"method\": \"HouseSensorContract:insertHouseWeather\", \"args\": [\"" + tlr.nextDouble(10, 35) + "\", \"" + tlr.nextDouble(0, 100) + "\" ] }";
                    request.bodyString(body, ContentType.APPLICATION_FORM_URLENCODED);

                    if (debugPrint) {
                        long startTime = System.nanoTime();
                        Response response = request.execute();
                        long endTime = System.nanoTime();
                        HttpResponse httpResponse = response.returnResponse();
                        System.out.println(httpResponse.getStatusLine());
                        HttpEntity entity = httpResponse.getEntity();
                        if (entity != null) {
                            String html = EntityUtils.toString(entity);
                            System.out.println(html.replaceAll("\\\\", ""));
                            System.out.println(ANSI_BLUE + "EXECUTION TIME: " + ((double) (endTime - startTime) / OBL) + " sec" + ANSI_RESET);
                        }
                    } else {
                        request.execute();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 5000);
    }

    public static void stopWeatherSensor() {
        timerWeather.cancel();
        timerWeather = null;
    }

    public static void startElectricitySensor(Boolean debugPrint) {
        if (timerElectricity != null) {
            timerElectricity.cancel();
        }

        timerElectricity = new Timer();
        timerElectricity.schedule(new TimerTask() {
            final ThreadLocalRandom tlr = ThreadLocalRandom.current();

            @Override
            public void run() {
                try {
                    double washingMachineKw = tlr.nextDouble(0, 4.00001);
                    double fridgeKw = tlr.nextDouble(0, 4.00001);
                    double dishwasherKw = tlr.nextDouble(0, 4.00001);
                    double useElectricity = washingMachineKw + fridgeKw + dishwasherKw;

                    Request request = Request.Post("http://localhost:" + SENSORPORT + "/invoke/home" + "1" + "/chaincode" + "1");
                    request.setHeader("Authorization", "Bearer " + TOKEN.get(SENSORPORT));
                    request.setHeader("Content-Type", "application/x-www-form-urlencoded");
                    String body = "{ \"method\": \"HouseSensorContract:insertHouseElectricity\", \"args\": [\"" + useElectricity + "\", \"" + washingMachineKw + "\", \"" + fridgeKw + "\", \"" + dishwasherKw + "\"] }";
                    request.bodyString(body, ContentType.APPLICATION_FORM_URLENCODED);

                    if (debugPrint) {
                        long startTime = System.nanoTime();
                        Response response = request.execute();
                        long endTime = System.nanoTime();
                        HttpResponse httpResponse = response.returnResponse();
                        System.out.println(httpResponse.getStatusLine());
                        HttpEntity entity = httpResponse.getEntity();
                        if (entity != null) {
                            String html = EntityUtils.toString(entity);
                            System.out.println(html.replaceAll("\\\\", ""));
                            System.out.println(ANSI_BLUE + "EXECUTION TIME: " + ((double) (endTime - startTime) / OBL) + " sec" + ANSI_RESET);
                        }
                    } else {
                        request.execute();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 5000);
    }

    public static void readAllHouseData(boolean dataType) throws IOException, ParseException {
        Request request = Request.Post("http://localhost:" + TENANTPORT + "/query/home1/chaincode1");
        System.out.print("Start day: ");
        String startDay = CONSOLE.readLine().strip();
        System.out.print("Start month: ");
        String startMonth = CONSOLE.readLine().strip();
        System.out.print("Start year: ");
        String startYear = CONSOLE.readLine().strip();

        System.out.print("End day: ");
        String endDay = CONSOLE.readLine().strip();
        System.out.print("End month: ");
        String endMonth = CONSOLE.readLine().strip();
        System.out.print("End year: ");
        String endYear = CONSOLE.readLine().strip();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        String startDate = startDay + "-" + startMonth + "-" + startYear + " " + "00:00:00";
        String endDate = endDay + "-" + endMonth + "-" + endYear + " " + "23:59:59";

        long startMillis = sdf.parse(startDate).getTime();
        long endMillis = sdf.parse(endDate).getTime();

        String body;
        if (dataType) {
            body = "{ \"method\": \"HouseSensorContract:readAllHouseWeather\", \"args\": [ \"" + startMillis + "\", \"" + endMillis + "\" ] }";
        } else {
            body = "{ \"method\": \"HouseSensorContract:readAllHouseElectricity\", \"args\": [ \"" + startMillis + "\", \"" + endMillis + "\" ] }";
        }

        request.bodyString(body, ContentType.APPLICATION_FORM_URLENCODED);
        request.setHeader("Authorization", "Bearer " + TOKEN.get(TENANTPORT));
        request.setHeader("Content-Type", "application/x-www-form-urlencoded");
        long startTime = System.nanoTime();
        Response response = request.execute();
        long endTime = System.nanoTime();
        HttpResponse httpResponse = response.returnResponse();
        System.out.println(httpResponse.getStatusLine());
        HttpEntity entity = httpResponse.getEntity();
        if (entity != null) {
            String html = EntityUtils.toString(entity);
            System.out.println(html.replaceAll("\\\\", ""));
            System.out.println(ANSI_BLUE + "EXECUTION TIME: " + ((double) (endTime - startTime) / OBL) + " sec" + ANSI_RESET);
        }
    }
}
