package org.project.hlf.sensor;

import org.project.models.MyRequest;
import org.project.models.MyResponse;
import org.project.server.ServerReference;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.project.MainClient.*;

public class Sensor {
    public static void readAllHouseData(boolean dataType) throws IOException, ParseException, NotBoundException {
        String request = "http://localhost:" + TENANTPORT + "/query/home1/chaincode1";
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

        MyResponse myResponse = ServerReference.getServer().readAllHouseData(new MyRequest(request, body, TENANTPORT));

        System.out.println(myResponse.response());
        System.out.println(ANSI_BLUE + "TOTAL EXECUTION TIME: " + myResponse.executionTime() + ANSI_RESET);
    }
}
