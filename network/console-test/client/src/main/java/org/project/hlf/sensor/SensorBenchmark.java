package org.project.hlf.sensor;

import org.project.server.ServerReference;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class SensorBenchmark {
    public static void benchmarkSensorQuery() throws NotBoundException, RemoteException {
        System.out.println(ServerReference.getServer().benchmarkSensorQuery());
    }

    public static void benchmarkSensorInvoke() throws NotBoundException, RemoteException {
        System.out.println(ServerReference.getServer().benchmarkSensorInvoke());
    }
}
