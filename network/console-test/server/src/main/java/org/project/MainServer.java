package org.project;

import org.project.server.Interface.Server;
import org.project.server.ServerImpl;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

class MainServer {
    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(Server.PORT);
            Naming.rebind("rmi://localhost:" + Server.PORT + "/" + Server.NAME, new ServerImpl());
            System.out.println("REBIND SERVER ESEGUITA");
        } catch (RemoteException | MalformedURLException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}