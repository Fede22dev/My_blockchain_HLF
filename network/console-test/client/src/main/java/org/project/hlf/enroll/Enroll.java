package org.project.hlf.enroll;

import org.project.models.MyRequest;
import org.project.models.MyResponse;
import org.project.server.ServerInstance;

import java.io.IOException;

import static org.project.ClientConstants.*;

public class Enroll {
    public static void enroll() throws IOException {
        System.out.print("[Peer PORT]: ");
        String port = CONSOLE.readLine().strip();
        String request = "http://localhost:" + port + "/user/enroll";
        String body = "{\"id\": \"admin\", \"secret\": \"adminpw\"}";
        MyResponse myResponse = ServerInstance.getInstance().enroll(new MyRequest(request, body, port));

        System.out.println(myResponse.response());
        System.out.println(ANSI_BLUE + "TOTAL EXECUTION TIME: " + myResponse.executionTime() + ANSI_RESET);
    }
}
