package org.project.hlf.enroll;

import org.project.MainClient;
import org.project.models.MyRequest;
import org.project.models.MyResponse;
import org.project.server.ServerReference;

import java.io.IOException;
import java.rmi.NotBoundException;

import static org.project.MainClient.ANSI_BLUE;
import static org.project.MainClient.ANSI_RESET;

public class Enroll {
    public static void enroll() throws IOException, NotBoundException {
        System.out.print("[Peer PORT]: ");
        String port = MainClient.CONSOLE.readLine().strip();
        String request = "http://localhost:" + port + "/user/enroll";
        String body = "{\"id\": \"admin\", \"secret\": \"adminpw\"}";
        MyResponse myResponse = ServerReference.getServer().enroll(new MyRequest(request, body, port));

        System.out.println(myResponse.response());
        System.out.println(ANSI_BLUE + "TOTAL EXECUTION TIME: " + myResponse.executionTime() + ANSI_RESET);
    }

    public static void benchmarkEnroll() throws IOException, NotBoundException {
        System.out.println(ServerReference.getServer().benchmarkEnroll());
    }
}
