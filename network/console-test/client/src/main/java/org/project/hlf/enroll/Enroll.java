package org.project.hlf.enroll;

import org.apache.http.util.EntityUtils;
import org.project.Main;
import org.project.models.MyRequest;
import org.project.models.MyResponse;
import org.project.server.ServerReference;

import java.io.IOException;
import java.rmi.NotBoundException;

public class Enroll {
    public static void enroll() throws IOException, NotBoundException {
        System.out.print("[Peer PORT]: ");
        String port = Main.CONSOLE.readLine().strip();
        String request = "http://localhost:" + port + "/user/enroll";
        String body = "{\"id\": \"admin\", \"secret\": \"adminpw\"}";
        MyResponse myResponse = ServerReference.getServer().enroll(new MyRequest(request, body, port));

        System.out.println(EntityUtils.toString(myResponse.response().returnResponse().getEntity()));
        System.out.println("TOTAL EXECUTION TIME: " + myResponse.executionTime());
    }

    public static void benchmarkEnroll() throws IOException, NotBoundException {
        System.out.println(ServerReference.getServer().benchmarkEnroll());
    }
}
