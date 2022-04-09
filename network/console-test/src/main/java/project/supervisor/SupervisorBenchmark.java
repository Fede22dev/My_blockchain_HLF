package project.supervisor;

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

import static project.Main.*;

public class SupervisorBenchmark {
    public static void queryBenchmark() throws IOException {
        Request request = Request.Post("http://localhost:8802/query/home1/chaincode1");
        request.setHeader("Authorization", "Bearer " + TOKEN.get("8802"));
        request.setHeader("Content-Type", "application/x-www-form-urlencoded");

        ArrayList<Double> times = new ArrayList<>();
        for (int i = 0; i < 250; i++) {
            String body = "{ \"method\": \"HouseSupervisorContract:queryBenchmark\", \"args\": [\"" + (i + 1) + "\"] }";
            request.bodyString(body, ContentType.APPLICATION_FORM_URLENCODED);
            long startTime = System.nanoTime();
            Response response = request.execute();
            long endTime = System.nanoTime();
            HttpResponse httpResponse = response.returnResponse();
            HttpEntity entity = httpResponse.getEntity();
            if (entity != null) {
                String html = EntityUtils.toString(entity);
                System.out.print("\r" + "QUERY: " + (i + 1) + html);
                times.add((double) (endTime - startTime) / OBL);
            }
        }

        CSVWriter writer = new CSVWriter(new FileWriter("/media/sf_Passaggio_File/benchquery.csv"));
        writer.writeNext(new String[]{"query time"});

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

    public static void invokeBenchmark() throws IOException {
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
        for (int i = 0; i < 250; i++) {
            String body = "{ \"method\": \"HouseSupervisorContract:invokeBenchmark\", \"args\": [ \"" + (i + 1) + "\" ] }";
            request.bodyString(body, ContentType.APPLICATION_FORM_URLENCODED);
            long startTime = System.nanoTime();
            Response response = request.execute();
            long endTime = System.nanoTime();
            HttpResponse httpResponse = response.returnResponse();
            HttpEntity entity = httpResponse.getEntity();
            if (entity != null) {
                String html = EntityUtils.toString(entity);
                System.out.print("\r" + html);
                times.add((double) (endTime - startTime) / OBL);
            }
        }

        CSVWriter writer = new CSVWriter(new FileWriter("/media/sf_Passaggio_File/benchinvoke.csv"));
        writer.writeNext(new String[]{"invoke time"});

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
