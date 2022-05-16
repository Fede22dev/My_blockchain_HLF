package org.project.hlf.supervisor;

import org.project.server.ServerInstance;

import java.rmi.RemoteException;

public class SupervisorBenchmark {
    public static void benchmarkSupervisorQuery() throws RemoteException {
        System.out.println(ServerInstance.getInstance().benchmarkSupervisorQuery());
    }

    public static void benchmarkSupervisorInvoke() throws RemoteException {
        System.out.println(ServerInstance.getInstance().benchmarkSupervisorInvoke());
    }
}
