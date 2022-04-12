package project.weather;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

import static project.Main.*;

public class Weather {
    private static final String SENSORPORT = "8804";


    public static void startInsertHouseData() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    Request request = Request.Post("http://localhost:" + SENSORPORT + "/user/enroll");
                    String body = "{\"id\": \"admin\", \"secret\": \"adminpw\"}";
                    request.bodyString(body, ContentType.APPLICATION_FORM_URLENCODED);
                    request.setHeader("Authorization", "Bearer");
                    request.setHeader("Content-Type", "application/x-www-form-urlencoded");
                    Response response = request.execute();
                    HttpResponse httpResponse = response.returnResponse();
                    HttpEntity entity = httpResponse.getEntity();
                    if (entity != null) {
                        String html = EntityUtils.toString(entity);
                        //System.out.println(html);
                        JSONObject object = new JSONObject(html);
                        String tokenAdmin = object.getString("token");
                        TOKEN.put(SENSORPORT, tokenAdmin);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 1000 * 60 * 8);

        Request request = Request.Post("http://localhost:" + SENSORPORT + "/invoke/home" + "1" + "/chaincode" + "1");
        ThreadLocalRandom tlr = ThreadLocalRandom.current();
        request.setHeader("Authorization", "Bearer " + TOKEN.get(SENSORPORT));
        request.setHeader("Content-Type", "application/x-www-form-urlencoded");
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    String body = "{ \"method\": \"HouseWeatherContract:insertHouseData\", \"args\": [\"" + tlr.nextDouble(10, 35) + "\", \"" + tlr.nextDouble(0, 100) + "\", \"" + tlr.nextDouble(0, 3.3) + "\"] }";
                    request.bodyString(body, ContentType.APPLICATION_FORM_URLENCODED);
                    request.execute();
                    /*long startTime = System.nanoTime();
                    Response response = request.execute();
                    long endTime = System.nanoTime();
                    HttpResponse httpResponse = response.returnResponse();
                    System.out.println(httpResponse.getStatusLine());
                    HttpEntity entity = httpResponse.getEntity();
                    if (entity != null) {
                        String html = EntityUtils.toString(entity);
                        System.out.println(html.replaceAll("\\\\", ""));
                        System.out.println(ANSI_BLUE + "EXECUTION TIME: " + ((double) (endTime - startTime) / OBL) + " sec" + ANSI_RESET);
                    }*/
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 1000, 5000);
    }

    public static void readAllHouseData() throws IOException, ParseException {
        Request request = Request.Post("http://localhost:" + SENSORPORT + "/query/home" + "1" + "/chaincode" + "1");
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

        String body = "{ \"method\": \"HouseWeatherContract:readAllHouseData\", \"args\": [ \"" + startMillis + "\", \"" + endMillis + "\" ] }";
        request.bodyString(body, ContentType.APPLICATION_FORM_URLENCODED);
        request.setHeader("Authorization", "Bearer " + TOKEN.get(SENSORPORT));
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
