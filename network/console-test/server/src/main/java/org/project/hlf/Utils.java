package org.project.hlf;

import com.opencsv.CSVWriter;
import org.apache.http.HttpEntity;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.project.models.MyRequest;
import org.project.models.MyResponse;
import org.project.server.ServerImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Stream;

import static org.project.server.ServerImpl.OBL;

public class Utils {
    public static @NotNull MyResponse executeRequest(@NotNull MyRequest myRequest) throws IOException {
        Request request = Request.Post(myRequest.request());
        request.bodyString(myRequest.body(), ContentType.APPLICATION_FORM_URLENCODED);
        request.setHeader("Content-Type", "application/x-www-form-urlencoded");
        request.setHeader("Authorization", "Bearer " + ServerImpl.getToken(myRequest.port()));
        long startTime = System.nanoTime();
        Response response = request.execute();
        long endTime = System.nanoTime();
        return new MyResponse(EntityUtils.toString(response.returnResponse().getEntity()), ((double) (endTime - startTime) / OBL));
    }

    public static void execRequestInvokeBenchmark(@NotNull Request request, String key, ArrayList<Double> times) throws IOException {
        long startTime = System.nanoTime();
        Response response = request.execute();
        long endTime = System.nanoTime();
        HttpEntity entity = response.returnResponse().getEntity();
        if (entity != null) {
            double time = (double) (endTime - startTime) / OBL;
            System.out.println("INVOKE: " + key + " " + EntityUtils.toString(entity) + " " + time);
            times.add(time);
        }
    }

    public static void writeCSV(CSVWriter writer, @NotNull HashMap<String, ArrayList<Double>> hashMap) throws IOException {
        int list0Length = hashMap.get("0").size();
        int list1Length = hashMap.get("1").size();
        int list2Length = hashMap.get("2").size();

        int max = Stream.of(list0Length, list1Length, list2Length).max(Integer::compareTo).get();
        for (int i = 0; i < max; i++) {
            writer.writeNext(new String[]{String.valueOf(i >= list0Length ? "" : hashMap.get("0").get(i)).replace(".", ","),
                    String.valueOf(i >= list1Length ? "" : hashMap.get("1").get(i)).replace(".", ","),
                    String.valueOf(i >= list2Length ? "" : hashMap.get("2").get(i)).replace(".", ",")}
            );
        }

        writer.close();
    }
}
