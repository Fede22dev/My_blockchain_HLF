package org.project.hlf.sensor;

import org.project.server.ServerInstance;

import java.rmi.RemoteException;

public class SensorBenchmark {
    public static void benchmarkSensorQuery() throws RemoteException {
        System.out.println(ServerInstance.getInstance().benchmarkSensorQuery());
    }

    public static void benchmarkSensorInvoke() throws RemoteException {
        System.out.println(ServerInstance.getInstance().benchmarkSensorInvoke());
    }
}
