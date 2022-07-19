package org.project.server;

import org.project.server.Interface.Server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class ServerInstance {
    private static Server server = null;

    private static void init() {
        if (server == null) {
            try {
                server = (Server) Naming.lookup("rmi://localhost:" + Server.PORT + "/" + Server.NAME);
                System.out.println("SERVER CONNESSO");
            } catch (MalformedURLException | NotBoundException | RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public static Server getInstance() {
        init();
        return server;
    }
}
