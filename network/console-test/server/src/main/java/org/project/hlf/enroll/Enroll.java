package org.project.hlf.enroll;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;
import org.project.models.MyRequest;
import org.project.models.MyResponse;
import org.project.server.ServerImpl;

import java.io.IOException;

import static org.project.server.ServerImpl.OBL;

public class Enroll {
    public static @Nullable MyResponse enroll(@NotNull MyRequest myRequest) throws IOException {
        Request request = Request.Post(myRequest.request());
        request.bodyString(myRequest.body(), ContentType.APPLICATION_FORM_URLENCODED);
        request.setHeader("Authorization", "Bearer");
        request.setHeader("Content-Type", "application/x-www-form-urlencoded");
        long startTime = System.nanoTime();
        Response response = request.execute();
        long endTime = System.nanoTime();
        HttpResponse httpResponse = response.returnResponse();
        HttpEntity entity = httpResponse.getEntity();
        if (entity != null) {
            String html = EntityUtils.toString(entity);
            JSONObject object = new JSONObject(html);
            String token = object.getString("token");
            ServerImpl.putToken(myRequest.port(), token);
            return new MyResponse(EntityUtils.toString(response.returnResponse().getEntity()), ((double) (endTime - startTime) / OBL));
        }
        return null;
    }
}
