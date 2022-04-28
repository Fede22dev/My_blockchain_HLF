package org.project.hlf;

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

import static org.project.server.ServerImpl.OBL;

public class Utils {
    @Contract("_ -> new")
     public static @NotNull MyResponse execRequest(@NotNull MyRequest myRequest) throws IOException {
        Request request = Request.Post(myRequest.request());
        request.bodyString(myRequest.body(), ContentType.APPLICATION_FORM_URLENCODED);
        request.setHeader("Content-Type", "application/x-www-form-urlencoded");
        request.setHeader("Authorization", "Bearer " + ServerImpl.getToken(myRequest.port()));
        long startTime = System.nanoTime();
        Response response = request.execute();
        long endTime = System.nanoTime();
        return new MyResponse(EntityUtils.toString(response.returnResponse().getEntity()), ((double) (endTime - startTime) / OBL));
    }
}
