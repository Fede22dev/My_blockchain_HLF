package project.weather;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

import static project.Main.*;

public class Weather {
    public static void insertHouseData() {
        Request request = Request.Post("http://localhost:" + "8802" + "/invoke/home" + "1" + "/chaincode" + "1");
        ThreadLocalRandom tlr = ThreadLocalRandom.current();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    String body = "{ \"method\": \"HouseWeatherContract:insertHouseData\", \"args\": [\"" + tlr.nextDouble(10, 35) + "\", \"" + tlr.nextDouble(0, 100) + "\", \"" + tlr.nextDouble(0, 3.3) + "\"] }";
                    request.bodyString(body, ContentType.APPLICATION_FORM_URLENCODED);
                    request.setHeader("Authorization", "Bearer " + TOKEN.get("8802"));
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
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 5000);
    }
}
