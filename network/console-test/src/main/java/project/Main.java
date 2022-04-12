package project;

import com.opencsv.CSVWriter;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import static project.supervisor.Supervisor.*;
import static project.supervisor.SupervisorBenchmark.invokeBenchmark;
import static project.supervisor.SupervisorBenchmark.queryBenchmark;
import static project.weather.Weather.readAllHouseData;
import static project.weather.Weather.startInsertHouseData;

public class Main {
    public final static String ANSI_RESET = "\u001B[0m";
    public final static String ANSI_BLUE = "\u001B[34m";
    public final static long OBL = 1_000_000_000;
    public final static long OMIN = 60;
    public final static String[] TYPOLOGY = {"rents", "bills", "deposits", "condominium fees"};
    public final static BufferedReader CONSOLE = new BufferedReader(new InputStreamReader(System.in));
    public final static HashMap<String, String> TOKEN = new HashMap<>();

    public static void main(String[] args) throws IOException {
        while (true) {
            System.out.print("Command --> [1 = enroll] [2 = invoke] [3 = query] [4 = performance evaluation supervisor] [5 = start insert house data] [6 = read house data] [7 = stop]: ");
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

                case "5" -> startInsertHouseData();

                case "6" -> {
                    boolean goQuery = true;
                    while (goQuery) {
                        System.out.print("Method --> [1 = readAllHouseData] [2 = stop query]: ");
                        switch (CONSOLE.readLine().strip()) {
                            case "1" -> {
                                try {
                                    readAllHouseData();
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }

                            case "2" -> goQuery = false;
                        }
                    }
                }

                case "7" -> System.exit(0);
            }
        }
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
            for (int i = 0; i < 250; i++) {
                long startTime = System.nanoTime();
                Response response = request.execute();
                long endTime = System.nanoTime();
                HttpResponse httpResponse = response.returnResponse();
                HttpEntity entity = httpResponse.getEntity();
                if (entity != null) {
                    String html = EntityUtils.toString(entity);
                    System.out.print("\r" + "ENROLL: " + (i + 1) + " -> " + html);
                    times.add((double) (endTime - startTime) / OBL);
                }
            }

            CSVWriter writer = new CSVWriter(new FileWriter("/media/sf_Passaggio_File/benchenroll.csv"));
            writer.writeNext(new String[]{"enroll time"});

            double total = 0;
            for (double time : times) {
                total += time;
                writer.writeNext(new String[]{String.valueOf(time).replace(".", ",")});

            }
            writer.close();
            double average = total / times.size();
            System.out.println("\n" + ANSI_BLUE + "TOTAL EXECUTION TIME: " + total + " sec" + ANSI_RESET);
            System.out.println(ANSI_BLUE + "AVERAGE EXECUTION TIME: " + average + " sec" + ANSI_RESET);
            System.out.println(ANSI_BLUE + "TRANSACTION PER MINUTE: " + OMIN / average + " transaction/min" + ANSI_RESET);
        }
    }
}
