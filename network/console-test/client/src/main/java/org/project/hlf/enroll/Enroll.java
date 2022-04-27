package org.project.hlf.enroll;

import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.project.Main;
import org.project.models.MyResponse;
import org.project.server.ServerReference;

import java.io.IOException;
import java.rmi.NotBoundException;

public class Enroll {
    public static void enroll() throws IOException, NotBoundException {
        System.out.print("[Peer PORT]: ");
        String port = Main.CONSOLE.readLine().strip();
        Request request = Request.Post("http://localhost:" + port + "/user/enroll");
        String body = "{\"id\": \"admin\", \"secret\": \"adminpw\"}";
        request.bodyString(body, ContentType.APPLICATION_FORM_URLENCODED);
        request.setHeader("Authorization", "Bearer");
        request.setHeader("Content-Type", "application/x-www-form-urlencoded");
        MyResponse myResponse = ServerReference.getServer().enroll(request, port);

        System.out.println(EntityUtils.toString(myResponse.response().returnResponse().getEntity()));
        System.out.println("TOTAL EXECUTION TIME: " + myResponse.executionTime());
    }

    public static void benchmarkEnroll() throws IOException, NotBoundException {
        System.out.println(ServerReference.getServer().benchmarkEnroll());
    }
}
