package org.project.server.Interface;

import org.apache.http.client.fluent.Request;
import org.project.models.MyResponse;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerSensors extends Remote {
    MyResponse readAllHouseData(Request request, String port) throws RemoteException;

    String benchmarkSensorInvoke() throws RemoteException;

    String benchmarkSensorQuery() throws RemoteException;
}
