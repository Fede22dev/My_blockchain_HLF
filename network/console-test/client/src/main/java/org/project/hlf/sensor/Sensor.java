package org.project.hlf.sensor;

import org.project.models.MyRequest;
import org.project.models.MyResponse;
import org.project.server.ServerInstance;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.project.ClientConstants.*;

public class Sensor {
    public static void readAllHouseData(final boolean dataType) throws IOException {
        String request = "http://localhost:" + TENANT_PORT + "/query/home1/chaincode1";

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

        boolean dateOk = true;
        while (dateOk) {
            try {
                long startMillis = sdf.parse(startDate).getTime();
                long endMillis = sdf.parse(endDate).getTime();
                dateOk = false;

                String body;
                if (dataType) {
                    body = "{ \"method\": \"HouseSensorContract:readAllHouseWeather\", \"args\": [ \"" + startMillis + "\", \"" + endMillis + "\" ] }";
                } else {
                    body = "{ \"method\": \"HouseSensorContract:readAllHouseElectricity\", \"args\": [ \"" + startMillis + "\", \"" + endMillis + "\" ] }";
                }

                MyResponse myResponse = ServerInstance.getInstance().readAllHouseData(new MyRequest(request, body, TENANT_PORT));

                System.out.println(myResponse.response().replaceAll("\\\\", ""));
                System.out.println(ANSI_BLUE + "TOTAL EXECUTION TIME: " + myResponse.executionTime() + ANSI_RESET);
            } catch (ParseException e) {
                System.err.println("Date not valid");
            }
        }
    }
}
