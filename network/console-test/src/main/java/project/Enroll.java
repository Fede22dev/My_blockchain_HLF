package project;

import com.opencsv.CSVWriter;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static project.Main.*;

/**
 * In questa classe vengono dichiarati tutti i metodi per effettuare
 * l'enroll degli utenti di tutti gli attori delle organizzazioni
 */
public class Enroll {
    public static void enroll() throws IOException {
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
    }

    /**
     * Metodo usato per effettuare il test delle prestazioni per quanto riguarda
     * l'operazione di enroll.
     */
    public static void benchmarkEnroll() throws IOException {
        System.out.print("[Peer PORT]: ");
        String port = CONSOLE.readLine().strip();
        Request request = Request.Post("http://localhost:" + port + "/user/enroll");
        String body = "{\"id\": \"admin\", \"secret\": \"adminpw\"}";
        request.bodyString(body, ContentType.APPLICATION_FORM_URLENCODED);
        request.setHeader("Authorization", "Bearer");
        request.setHeader("Content-Type", "application/x-www-form-urlencoded");

        new Timer().schedule(new TimerTask() {
            final long t0 = System.currentTimeMillis();
            final ArrayList<Double> times = new ArrayList<>();
            int i = 1;

            @Override
            public void run() {
                if (System.currentTimeMillis() - t0 > 1000 * 60) {
                    cancel();
                    try {
                        CSVWriter writer = new CSVWriter(new FileWriter("/media/sf_Passaggio_File/bench_enroll.csv"));
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
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    try {
                        long startTime = System.nanoTime();
                        Response response = request.execute();
                        long endTime = System.nanoTime();
                        HttpResponse httpResponse = response.returnResponse();
                        HttpEntity entity = httpResponse.getEntity();
                        if (entity != null) {
                            String html = EntityUtils.toString(entity);
                            System.out.print("\r" + "ENROLL: " + i + " -> " + html);
                            times.add((double) (endTime - startTime) / OBL);
                            i++;
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }, 0, 1000);
    }

    /**
     * Metodo chiamato dalla classe sensor e dalla classe SupervisorBenchmark.
     * Provvisto di timer per poter rilasciare il token ogni 8 minuti, in modo
     * da permettere ai test di continuare ad oltranza.
     *
     * @param port port
     */
    public static void timerEnroll(String port) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    Request request = Request.Post("http://localhost:" + port + "/user/enroll");
                    String body = "{\"id\": \"admin\", \"secret\": \"adminpw\"}";
                    request.bodyString(body, ContentType.APPLICATION_FORM_URLENCODED);
                    request.setHeader("Authorization", "Bearer");
                    request.setHeader("Content-Type", "application/x-www-form-urlencoded");
                    Response response = request.execute();
                    HttpResponse httpResponse = response.returnResponse();
                    HttpEntity entity = httpResponse.getEntity();
                    if (entity != null) {
                        String html = EntityUtils.toString(entity);
                        System.out.println("\nENROLL TIMER " + port + ": " + html);
                        JSONObject object = new JSONObject(html);
                        String tokenAdmin = object.getString("token");
                        TOKEN.put(port, tokenAdmin);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 1000 * 60 * 8);
    }
}
