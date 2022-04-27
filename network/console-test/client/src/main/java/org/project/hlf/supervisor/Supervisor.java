package org.project.hlf.supervisor;

import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.project.models.MyResponse;
import org.project.server.ServerReference;

import java.io.IOException;
import java.rmi.NotBoundException;

import static org.project.Main.CONSOLE;
import static org.project.Main.TYPOLOGY;

public class Supervisor {
    public static void payRent() throws IOException, NotBoundException {
        System.out.print("[Peer PORT]: ");
        String port = CONSOLE.readLine().strip();
        System.out.print("[Channel num]: ");
        String channel = CONSOLE.readLine().strip();
        Request request = Request.Post("http://localhost:" + port + "/invoke/home" + channel + "/chaincode" + channel);
        System.out.print("Payment from: ");
        String payFrom = CONSOLE.readLine().strip();
        System.out.print("Payment to: ");
        String payTo = CONSOLE.readLine().strip();
        System.out.print("Value of rent: ");
        String rent = CONSOLE.readLine().strip();
        String body = "{ \"method\": \"HouseSupervisorContract:payRent\", \"args\": [ \"" + payFrom + "\", \"" + payTo + "\", \"" + rent + "\" ] }";
        request.bodyString(body, ContentType.APPLICATION_FORM_URLENCODED);
        request.setHeader("Content-Type", "application/x-www-form-urlencoded");

        MyResponse myResponse = ServerReference.getServer().payRent(request, port);

        System.out.println(EntityUtils.toString(myResponse.response().returnResponse().getEntity()));
        System.out.println("TOTAL EXECUTION TIME: " + myResponse.executionTime());
    }

    public static void payDeposit() throws IOException, NotBoundException {
        System.out.print("[Peer PORT]: ");
        String port = CONSOLE.readLine().strip();
        System.out.print("[Channel num]: ");
        String channel = CONSOLE.readLine().strip();
        Request request = Request.Post("http://localhost:" + port + "/invoke/home" + channel + "/chaincode" + channel);
        System.out.print("Payment from: ");
        String payFrom = CONSOLE.readLine().strip();
        System.out.print("Payment to: ");
        String payTo = CONSOLE.readLine().strip();
        System.out.print("Value of deposit: ");
        String deposit = CONSOLE.readLine().strip();
        String body = "{ \"method\": \"HouseSupervisorContract:payDeposit\", \"args\": [ \"" + payFrom + "\", \"" + payTo + "\", \"" + deposit + "\" ] }";
        request.bodyString(body, ContentType.APPLICATION_FORM_URLENCODED);
        request.setHeader("Content-Type", "application/x-www-form-urlencoded");

        MyResponse myResponse = ServerReference.getServer().payDeposit(request, port);

        System.out.println(EntityUtils.toString(myResponse.response().returnResponse().getEntity()));
        System.out.println("TOTAL EXECUTION TIME: " + myResponse.executionTime());
    }

    public static void payBill() throws IOException, NotBoundException {
        System.out.print("[Peer PORT]: ");
        String port = CONSOLE.readLine().strip();
        System.out.print("[Channel num]: ");
        String channel = CONSOLE.readLine().strip();
        Request request = Request.Post("http://localhost:" + port + "/invoke/home" + channel + "/chaincode" + channel);
        System.out.print("Payment from: ");
        String payFrom = CONSOLE.readLine().strip();
        System.out.print("Payment to: ");
        String payTo = CONSOLE.readLine().strip();
        System.out.print("Value of bill: ");
        String bill = CONSOLE.readLine().strip();
        String body = "{ \"method\": \"HouseSupervisorContract:payBills\", \"args\": [ \"" + payFrom + "\", \"" + payTo + "\", \"" + bill + "\" ] }";
        request.bodyString(body, ContentType.APPLICATION_FORM_URLENCODED);
        request.setHeader("Content-Type", "application/x-www-form-urlencoded");

        MyResponse myResponse = ServerReference.getServer().payBill(request, port);

        System.out.println(EntityUtils.toString(myResponse.response().returnResponse().getEntity()));
        System.out.println("TOTAL EXECUTION TIME: " + myResponse.executionTime());
    }

    public static void payCondominiumFees() throws IOException, NotBoundException {
        System.out.print("[Peer PORT]: ");
        String port = CONSOLE.readLine().strip();
        System.out.print("[Channel num]: ");
        String channel = CONSOLE.readLine().strip();
        Request request = Request.Post("http://localhost:" + port + "/invoke/home" + channel + "/chaincode" + channel);
        System.out.print("Payment from: ");
        String payFrom = CONSOLE.readLine().strip();
        System.out.print("Payment to: ");
        String payTo = CONSOLE.readLine().strip();
        System.out.print("Value of deposit: ");
        String condominiumFees = CONSOLE.readLine().strip();
        String body = "{ \"method\": \"HouseSupervisorContract:payCondominiumFees\", \"args\": [ \"" + payFrom + "\", \"" + payTo + "\", \"" + condominiumFees + "\" ] }";
        request.bodyString(body, ContentType.APPLICATION_FORM_URLENCODED);
        request.setHeader("Content-Type", "application/x-www-form-urlencoded");

        MyResponse myResponse = ServerReference.getServer().payCondominiumFees(request, port);

        System.out.println(EntityUtils.toString(myResponse.response().returnResponse().getEntity()));
        System.out.println("TOTAL EXECUTION TIME: " + myResponse.executionTime());
    }

    public static void readAllPaymentType() throws IOException, NotBoundException {
        System.out.print("[Peer PORT]: ");
        String port = CONSOLE.readLine().strip();
        System.out.print("[Channel num]: ");
        String channel = CONSOLE.readLine().strip();
        Request request = Request.Post("http://localhost:" + port + "/query/home" + channel + "/chaincode" + channel);
        boolean num = true;
        while (num) {
            try {
                System.out.print("Insert type [1 = rents] [2 = bills] [3 = deposits] [4 = condominium fees]: ");
                int type = Integer.parseInt(CONSOLE.readLine().strip());
                num = false;
                System.out.print("Start month: ");
                String startMonth = CONSOLE.readLine().strip();
                System.out.print("Start year: ");
                String startYear = CONSOLE.readLine().strip();
                System.out.print("End month: ");
                String endMonth = CONSOLE.readLine().strip();
                System.out.print("End year: ");
                String endYear = CONSOLE.readLine().strip();
                String body = "{ \"method\": \"HouseSupervisorContract:readAllPaymentType\", \"args\": [ \"" + TYPOLOGY[type - 1] + "\", \"" + startMonth + "\", \"" + startYear + "\", \"" + endMonth + "\", \"" + endYear + "\" ] }";
                request.bodyString(body, ContentType.APPLICATION_FORM_URLENCODED);
                request.setHeader("Content-Type", "application/x-www-form-urlencoded");

                MyResponse myResponse = ServerReference.getServer().readAllPaymentType(request, port);

                System.out.println(EntityUtils.toString(myResponse.response().returnResponse().getEntity()));
                System.out.println("TOTAL EXECUTION TIME: " + myResponse.executionTime());

            } catch (NumberFormatException e) {
                System.err.println("Only number");
            }
        }
    }
}