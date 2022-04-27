package org.project.hlf.supervisor;

import org.project.server.ServerReference;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class SupervisorBenchmark {
    public static void benchmarkSupervisorQuery() throws NotBoundException, RemoteException {
        System.out.println(ServerReference.getServer().benchmarkSupervisorQuery());
    }

    public static void benchmarkSupervisorInvoke() throws NotBoundException, RemoteException {
        System.out.println(ServerReference.getServer().benchmarkSupervisorInvoke());
    }
}
