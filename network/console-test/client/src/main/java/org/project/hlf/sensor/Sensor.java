package org.project.hlf.sensor;

import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.project.models.MyResponse;
import org.project.server.ServerReference;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.project.Main.CONSOLE;
import static org.project.Main.TENANTPORT;

public class Sensor {
    public static void readAllHouseData(boolean dataType) throws IOException, ParseException, NotBoundException {
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
        request.setHeader("Content-Type", "application/x-www-form-urlencoded");

        MyResponse myResponse = ServerReference.getServer().enroll(request, TENANTPORT);

        System.out.println(EntityUtils.toString(myResponse.response().returnResponse().getEntity()));
        System.out.println("TOTAL EXECUTION TIME: " + myResponse.executionTime());
    }
}
