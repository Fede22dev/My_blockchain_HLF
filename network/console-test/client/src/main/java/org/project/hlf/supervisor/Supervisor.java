package org.project.hlf.supervisor;

import org.project.models.MyRequest;
import org.project.models.MyResponse;
import org.project.server.ServerInstance;

import java.io.IOException;

import static org.project.ClientConstants.*;

public class Supervisor {
    public static void payRent() throws IOException {
        System.out.print("[Peer PORT]: ");
        String port = CONSOLE.readLine().strip();
        System.out.print("[Channel num]: ");
        String channel = CONSOLE.readLine().strip();
        String request = "http://localhost:" + port + "/invoke/home" + channel + "/chaincode" + channel;
        System.out.print("Payment from: ");
        String payFrom = CONSOLE.readLine().strip();
        System.out.print("Payment to: ");
        String payTo = CONSOLE.readLine().strip();
        System.out.print("Value of rent: ");
        String rent = CONSOLE.readLine().strip();
        String body = "{ \"method\": \"HouseSupervisorContract:payRent\", \"args\": [ \"" + payFrom + "\", \"" + payTo + "\", \"" + rent + "\" ] }";

        MyResponse myResponse = ServerInstance.getInstance().payRent(new MyRequest(request, body, port));

        System.out.println(myResponse.response().replaceAll("\\\\", ""));
        System.out.println(ANSI_BLUE + "TOTAL EXECUTION TIME: " + myResponse.executionTime() + ANSI_RESET);
    }

    public static void payDeposit() throws IOException {
        System.out.print("[Peer PORT]: ");
        String port = CONSOLE.readLine().strip();
        System.out.print("[Channel num]: ");
        String channel = CONSOLE.readLine().strip();
        String request = "http://localhost:" + port + "/invoke/home" + channel + "/chaincode" + channel;
        System.out.print("Payment from: ");
        String payFrom = CONSOLE.readLine().strip();
        System.out.print("Payment to: ");
        String payTo = CONSOLE.readLine().strip();
        System.out.print("Value of deposit: ");
        String deposit = CONSOLE.readLine().strip();
        String body = "{ \"method\": \"HouseSupervisorContract:payDeposit\", \"args\": [ \"" + payFrom + "\", \"" + payTo + "\", \"" + deposit + "\" ] }";

        MyResponse myResponse = ServerInstance.getInstance().payDeposit(new MyRequest(request, body, port));

        System.out.println(myResponse.response().replaceAll("\\\\", ""));
        System.out.println(ANSI_BLUE + "TOTAL EXECUTION TIME: " + myResponse.executionTime() + ANSI_RESET);
    }

    public static void payBill() throws IOException {
        System.out.print("[Peer PORT]: ");
        String port = CONSOLE.readLine().strip();
        System.out.print("[Channel num]: ");
        String channel = CONSOLE.readLine().strip();
        String request = "http://localhost:" + port + "/invoke/home" + channel + "/chaincode" + channel;
        System.out.print("Payment from: ");
        String payFrom = CONSOLE.readLine().strip();
        System.out.print("Payment to: ");
        String payTo = CONSOLE.readLine().strip();
        System.out.print("Value of bill: ");
        String bill = CONSOLE.readLine().strip();
        String body = "{ \"method\": \"HouseSupervisorContract:payBills\", \"args\": [ \"" + payFrom + "\", \"" + payTo + "\", \"" + bill + "\" ] }";

        MyResponse myResponse = ServerInstance.getInstance().payBills(new MyRequest(request, body, port));

        System.out.println(myResponse.response().replaceAll("\\\\", ""));
        System.out.println(ANSI_BLUE + "TOTAL EXECUTION TIME: " + myResponse.executionTime() + ANSI_RESET);
    }

    public static void payCondominiumFees() throws IOException {
        System.out.print("[Peer PORT]: ");
        String port = CONSOLE.readLine().strip();
        System.out.print("[Channel num]: ");
        String channel = CONSOLE.readLine().strip();
        String request = "http://localhost:" + port + "/invoke/home" + channel + "/chaincode" + channel;
        System.out.print("Payment from: ");
        String payFrom = CONSOLE.readLine().strip();
        System.out.print("Payment to: ");
        String payTo = CONSOLE.readLine().strip();
        System.out.print("Value of deposit: ");
        String condominiumFees = CONSOLE.readLine().strip();
        String body = "{ \"method\": \"HouseSupervisorContract:payCondominiumFees\", \"args\": [ \"" + payFrom + "\", \"" + payTo + "\", \"" + condominiumFees + "\" ] }";

        MyResponse myResponse = ServerInstance.getInstance().payCondominiumFees(new MyRequest(request, body, port));

        System.out.println(myResponse.response().replaceAll("\\\\", ""));
        System.out.println(ANSI_BLUE + "TOTAL EXECUTION TIME: " + myResponse.executionTime() + ANSI_RESET);
    }

    public static void readAllPaymentType() throws IOException {
        System.out.print("[Peer PORT]: ");
        String port = CONSOLE.readLine().strip();
        System.out.print("[Channel num]: ");
        String channel = CONSOLE.readLine().strip();
        String request = "http://localhost:" + port + "/query/home" + channel + "/chaincode" + channel;

        boolean numOk = true;
        while (numOk) {
            try {
                System.out.print("Insert type [1 = rents] [2 = bills] [3 = deposits] [4 = condominium fees]: ");
                int type = Integer.parseInt(CONSOLE.readLine().strip());
                numOk = false;
                System.out.print("Start month: ");
                String startMonth = CONSOLE.readLine().strip();
                System.out.print("Start year: ");
                String startYear = CONSOLE.readLine().strip();
                System.out.print("End month: ");
                String endMonth = CONSOLE.readLine().strip();
                System.out.print("End year: ");
                String endYear = CONSOLE.readLine().strip();
                String body = "{ \"method\": \"HouseSupervisorContract:readAllPaymentType\", \"args\": [ \"" + TYPOLOGY[type - 1] + "\", \"" + startMonth + "\", \"" + startYear + "\", \"" + endMonth + "\", \"" + endYear + "\" ] }";

                MyResponse myResponse = ServerInstance.getInstance().readAllPaymentType(new MyRequest(request, body, port));

                System.out.println(myResponse.response().replaceAll("\\\\", ""));
                System.out.println(ANSI_BLUE + "TOTAL EXECUTION TIME: " + myResponse.executionTime() + ANSI_RESET);
            } catch (NumberFormatException e) {
                System.err.println("Only number");
            }
        }
    }
}
