import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    private final static String ANSI_RESET = "\u001B[0m";
    private final static String ANSI_BLUE = "\u001B[34m";
    private final static long OBL = 1_000_000_000;
    private final static long OMIN = 60;
    private final static String[] TYPOLOGY = {"rents", "bills", "deposits", "condominium fees"};
    private final static BufferedReader CONSOLE = new BufferedReader(new InputStreamReader(System.in));
    private final static HashMap<String, String> TOKEN = new HashMap<>();

    public static void main(String[] args) throws IOException {
        while (true) {
            System.out.print("Command --> [1 = enroll] [2 = invoke] [3 = query] [4 = performance evaluation] [5 = stop]: ");
            switch (CONSOLE.readLine().strip()) {
                case "1" -> {
                    try {
                        enroll(true);
                    } catch (ConnectException e) {
                        System.err.println("Connection refused");
                    }
                }

                case "2" -> {
                    boolean goInvoke = true;
                    while (goInvoke) {
                        System.out.print("Method --> [1 = payRent] [2 = payDeposit] [3 = payBill] [4 = payCondominiumFees] [5 = stop invoke]: ");
                        switch (CONSOLE.readLine().strip()) {
                            case "1" -> {
                                try {
                                    payRent();
                                } catch (ConnectException e) {
                                    System.err.println("Connection refused");
                                }
                            }

                            case "2" -> {
                                try {
                                    payDeposit();
                                } catch (ConnectException e) {
                                    System.err.println("Connection refused");
                                }
                            }

                            case "3" -> {
                                try {
                                    payBill();
                                } catch (ConnectException e) {
                                    System.err.println("Connection refused");
                                }
                            }

                            case "4" -> {
                                try {
                                    payCondominiumFees();
                                } catch (ConnectException e) {
                                    System.err.println("Connection refused");
                                }
                            }

                            case "5" -> goInvoke = false;
                        }
                    }
                }

                case "3" -> {
                    boolean goQuery = true;
                    while (goQuery) {
                        System.out.print("Method --> [1 = readAllPaymentType] [2 = stop query]: ");
                        switch (CONSOLE.readLine().strip()) {
                            case "1" -> {
                                try {
                                    readAllPaymentType();
                                } catch (ConnectException e) {
                                    System.err.println("Connection refused");
                                }
                            }

                            case "2" -> goQuery = false;
                        }
                    }
                }

                case "4" -> {
                    boolean goPerformance = true;
                    while (goPerformance) {
                        System.out.print("Method --> [1 = enroll] [2 = invoke] [3 = query] [4 = stop performance]: ");
                        switch (CONSOLE.readLine().strip()) {
                            case "1" -> enroll(false);
                            case "2" -> invokeBenchmark();
                            case "3" -> queryBenchmark();
                            case "4" -> goPerformance = false;
                        }
                    }
                }

                case "5" -> System.exit(0);
            }
        }
    }

    private static void invokeBenchmark() throws IOException {
        if (TOKEN.get("8802") == null) {
            Request request = Request.Post("http://localhost:8802/user/enroll");
            String body = "{\"id\": \"admin\", \"secret\": \"adminpw\"}";
            request.bodyString(body, ContentType.APPLICATION_FORM_URLENCODED);
            request.setHeader("Authorization", "Bearer");
            request.setHeader("Content-Type", "application/x-www-form-urlencoded");
            Response response = request.execute();
            HttpResponse httpResponse = response.returnResponse();
            HttpEntity entity = httpResponse.getEntity();
            if (entity != null) {
                String html = EntityUtils.toString(entity);
                JSONObject object = new JSONObject(html);
                String tokenAdmin = object.getString("token");
                TOKEN.put("8802", tokenAdmin);
            }
        }

        Request request = Request.Post("http://localhost:8802/invoke/home1/chaincode1");
        request.setHeader("Authorization", "Bearer " + TOKEN.get("8802"));
        request.setHeader("Content-Type", "application/x-www-form-urlencoded");
        ArrayList<Double> times = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            String body = "{ \"method\": \"HouseSupervisorContract:invokeBenchmark\", \"args\": [ \"" + (i + 1) + "\" ] }";
            request.bodyString(body, ContentType.APPLICATION_FORM_URLENCODED);
            long startTime = System.nanoTime();
            Response response = request.execute();
            long endTime = System.nanoTime();
            HttpResponse httpResponse = response.returnResponse();
            HttpEntity entity = httpResponse.getEntity();
            if (entity != null) {
                String html = EntityUtils.toString(entity);
                System.out.println(html);
                times.add((double) (endTime - startTime) / OBL);
            }
        }

        double total = 0;
        for (double time : times) {
            total += time;
        }
        double average = total / times.size();
        System.out.println(ANSI_BLUE + "TOTAL EXECUTION TIME: " + total + " sec" + ANSI_RESET);
        System.out.println(ANSI_BLUE + "AVERAGE EXECUTION TIME: " + average + " sec" + ANSI_RESET);
        System.out.println(ANSI_BLUE + "TRANSACTION PER MINUTE: " + OMIN / average + " transaction/min" + ANSI_RESET);
    }

    private static void queryBenchmark() throws IOException {
        Request request = Request.Post("http://localhost:8802/query/home1/chaincode1");
        request.setHeader("Authorization", "Bearer " + TOKEN.get("8802"));
        request.setHeader("Content-Type", "application/x-www-form-urlencoded");

        ArrayList<Double> times = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            String body = "{ \"method\": \"HouseSupervisorContract:queryBenchmark\", \"args\": [\"" + (i + 1) + "\"] }";
            request.bodyString(body, ContentType.APPLICATION_FORM_URLENCODED);
            long startTime = System.nanoTime();
            Response response = request.execute();
            long endTime = System.nanoTime();
            HttpResponse httpResponse = response.returnResponse();
            HttpEntity entity = httpResponse.getEntity();
            if (entity != null) {
                String html = EntityUtils.toString(entity);
                System.out.println(html);
                times.add((double) (endTime - startTime) / OBL);
            }
        }

        double total = 0;
        for (double time : times) {
            total += time;
        }
        double average = total / times.size();
        System.out.println(ANSI_BLUE + "TOTAL EXECUTION TIME: " + total + " sec" + ANSI_RESET);
        System.out.println(ANSI_BLUE + "AVERAGE EXECUTION TIME: " + average + " sec" + ANSI_RESET);
        System.out.println(ANSI_BLUE + "TRANSACTION PER MINUTE: " + OMIN / average + " transaction/min" + ANSI_RESET);
    }

    private static void enroll(boolean operationType) throws IOException {
        if (operationType) {
            System.out.print("[Peer PORT]: ");
            String port = CONSOLE.readLine().strip();
            Request request = Request.Post("http://localhost:" + port + "/user/enroll");
            String body = "{\"id\": \"admin\", \"secret\": \"adminpw\"}";
            request.bodyString(body, ContentType.APPLICATION_FORM_URLENCODED);
            request.setHeader("Authorization", "Bearer");
            request.setHeader("Content-Type", "application/x-www-form-urlencoded");
            long startTime = System.nanoTime();
            Response response = request.execute();
            long endTime = System.nanoTime();
            HttpResponse httpResponse = response.returnResponse();
            HttpEntity entity = httpResponse.getEntity();
            if (entity != null) {
                String html = EntityUtils.toString(entity);
                System.out.println(html);
                System.out.println(ANSI_BLUE + "EXECUTION TIME: " + ((double) (endTime - startTime) / OBL) + " sec" + ANSI_RESET);
                JSONObject object = new JSONObject(html);
                String tokenAdmin = object.getString("token");
                TOKEN.put(port, tokenAdmin);
            }
        } else {
            System.out.print("[Peer PORT]: ");
            String port = CONSOLE.readLine().strip();
            Request request = Request.Post("http://localhost:" + port + "/user/enroll");
            String body = "{\"id\": \"admin\", \"secret\": \"adminpw\"}";
            request.bodyString(body, ContentType.APPLICATION_FORM_URLENCODED);
            request.setHeader("Authorization", "Bearer");
            request.setHeader("Content-Type", "application/x-www-form-urlencoded");
            ArrayList<Double> times = new ArrayList<>();
            for (int i = 0; i < 100; i++) {
                long startTime = System.nanoTime();
                Response response = request.execute();
                long endTime = System.nanoTime();
                HttpResponse httpResponse = response.returnResponse();
                HttpEntity entity = httpResponse.getEntity();
                if (entity != null) {
                    String html = EntityUtils.toString(entity);
                    System.out.println(html);
                    times.add((double) (endTime - startTime) / OBL);
                }
            }
            double total = 0;
            for (double time : times) {
                total += time;
            }
            double average = total / times.size();
            System.out.println(ANSI_BLUE + "TOTAL EXECUTION TIME: " + total + " sec" + ANSI_RESET);
            System.out.println(ANSI_BLUE + "AVERAGE EXECUTION TIME: " + average + " sec" + ANSI_RESET);
            System.out.println(ANSI_BLUE + "TRANSACTION PER MINUTE: " + OMIN / average + " transaction/min" + ANSI_RESET);
        }
    }

    private static void payRent() throws IOException {
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

    private static void payDeposit() throws IOException {
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

    private static void payBill() throws IOException {
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

    private static void payCondominiumFees() throws IOException {
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

    private static void readAllPaymentType() throws IOException {
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