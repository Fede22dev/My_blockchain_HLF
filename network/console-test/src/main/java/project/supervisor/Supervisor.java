package project.supervisor;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

import static project.Main.*;

public class Supervisor {
    public static void payRent() throws IOException {
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
        request.setHeader("Authorization", "Bearer " + TOKEN.get(port));
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

    public static void payDeposit() throws IOException {
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
        request.setHeader("Authorization", "Bearer " + TOKEN.get(port));
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

    public static void payBill() throws IOException {
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
        request.setHeader("Authorization", "Bearer " + TOKEN.get(port));
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

    public static void payCondominiumFees() throws IOException {
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
        request.setHeader("Authorization", "Bearer " + TOKEN.get(port));
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

    public static void readAllPaymentType() throws IOException {
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
                request.setHeader("Authorization", "Bearer " + TOKEN.get(port));
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
            } catch (NumberFormatException e) {
                System.err.println("Only number");
            }
        }
    }
}
