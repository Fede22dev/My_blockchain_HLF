package org.project.server;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import static org.project.ServerConstants.*;

public class TokenManager {
    private final static HashMap<String, String> TOKEN = new HashMap<>();

    public static synchronized String getToken(final String port) {
        return TOKEN.get(port);
    }

    public static synchronized void putToken(final String port, final String token) {
        TOKEN.put(port, token);
    }

    static void timerEnroll(final String port) {
        final Request request = Request.Post("http://localhost:" + port + "/user/enroll");
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    String body = "{\"id\": \"admin\", \"secret\": \"adminpw\"}";
                    request.bodyString(body, ContentType.APPLICATION_FORM_URLENCODED);
                    request.setHeader("Authorization", "Bearer");
                    request.setHeader("Content-Type", "application/x-www-form-urlencoded");
                    Response response = request.execute();
                    HttpResponse httpResponse = response.returnResponse();
                    HttpEntity entity = httpResponse.getEntity();
                    if (entity != null) {
                        String html = EntityUtils.toString(entity);
                        System.out.println(ANSI_PURPLE + "ENROLL TIMER " + port + ": " + html + ANSI_RESET);
                        JSONObject object = new JSONObject(html);
                        String tokenAdmin = object.getString("token");
                        putToken(port, tokenAdmin);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 0, ONE_THOUSAND * MINUTE_TIMER_ENROLL);
    }
}
