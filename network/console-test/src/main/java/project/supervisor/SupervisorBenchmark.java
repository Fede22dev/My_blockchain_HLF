package project.supervisor;

import com.opencsv.CSVWriter;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static project.Main.*;

public class SupervisorBenchmark {
    public static void benchmarkSupervisorQuery() {
        new Timer().schedule(new TimerTask() {
            final long t0 = System.currentTimeMillis();
            final ArrayList<Double> times = new ArrayList<>();
            int i = 1;

            @Override
            public void run() {
                if (System.currentTimeMillis() - t0 > 1000 * 60) {
                    cancel();
                    try {
                        CSVWriter writer = new CSVWriter(new FileWriter("/media/sf_Passaggio_File/bench_supervisor_query.csv"));
                        writer.writeNext(new String[]{"query supervisor time"});

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
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    try {
                        Request request = Request.Post("http://localhost:" + TENANTPORT + "/query/home1/chaincode1");
                        request.setHeader("Authorization", "Bearer " + TOKEN.get(TENANTPORT));
                        request.setHeader("Content-Type", "application/x-www-form-urlencoded");
                        String body = "{ \"method\": \"HouseSupervisorContract:queryBenchmark\", \"args\": [\"" + i + "\"] }";
                        request.bodyString(body, ContentType.APPLICATION_FORM_URLENCODED);
                        long startTime = System.nanoTime();
                        Response response = request.execute();
                        long endTime = System.nanoTime();
                        HttpResponse httpResponse = response.returnResponse();
                        HttpEntity entity = httpResponse.getEntity();
                        if (entity != null) {
                            String html = EntityUtils.toString(entity);
                            System.out.print("\r" + "QUERY: " + i + " " + html);
                            i++;
                            times.add((double) (endTime - startTime) / OBL);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }, 0, 1000);
    }

    public static void benchmarkSupervisorInvoke() {
        new Timer().schedule(new TimerTask() {
            final long t0 = System.currentTimeMillis();
            final ArrayList<Double> times = new ArrayList<>();
            int i = 1;

            @Override
            public void run() {
                if (System.currentTimeMillis() - t0 > 1000 * 60) {
                    cancel();
                    try {
                        CSVWriter writer = new CSVWriter(new FileWriter("/media/sf_Passaggio_File/bench_supervisor_invoke.csv"));
                        writer.writeNext(new String[]{"invoke supervisor time"});

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
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    try {
                        Request request = Request.Post("http://localhost:" + TENANTPORT + "/invoke/home1/chaincode1");
                        request.setHeader("Authorization", "Bearer " + TOKEN.get(TENANTPORT));
                        request.setHeader("Content-Type", "application/x-www-form-urlencoded");
                        String body = "{ \"method\": \"HouseSupervisorContract:invokeBenchmark\", \"args\": [ \"" + i + "\" ] }";
                        request.bodyString(body, ContentType.APPLICATION_FORM_URLENCODED);
                        long startTime = System.nanoTime();
                        Response response = request.execute();
                        long endTime = System.nanoTime();
                        HttpResponse httpResponse = response.returnResponse();
                        HttpEntity entity = httpResponse.getEntity();
                        if (entity != null) {
                            String html = EntityUtils.toString(entity);
                            System.out.print("\r" + "INVOKE: " + i + " " + html);
                            i++;
                            times.add((double) (endTime - startTime) / OBL);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }, 0, 1000);
    }
}
