package org.project.hlf.enroll;

import org.apache.http.HttpEntity;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;
import org.project.models.MyRequest;
import org.project.models.MyResponse;
import org.project.server.TokenManager;

import java.io.IOException;

import static org.project.ServerConstants.ONE_BILION;

public class Enroll {
    public static @Nullable MyResponse enroll(@NotNull final MyRequest myRequest) throws IOException {
        Request request = Request.Post(myRequest.request());
        request.bodyString(myRequest.body(), ContentType.APPLICATION_FORM_URLENCODED);
        request.setHeader("Authorization", "Bearer");
        request.setHeader("Content-Type", "application/x-www-form-urlencoded");
        long startTime = System.nanoTime();
        Response response = request.execute();
        long endTime = System.nanoTime();
        HttpEntity entity = response.returnResponse().getEntity();
        if (entity != null) {
            String html = EntityUtils.toString(entity);
            JSONObject object = new JSONObject(html);
            String token = object.getString("token");
            TokenManager.putToken(myRequest.port(), token);
            return new MyResponse(html, ((double) (endTime - startTime) / ONE_BILION));
        }
        return null;
    }
}
