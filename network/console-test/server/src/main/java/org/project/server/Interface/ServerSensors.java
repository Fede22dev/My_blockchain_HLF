package org.project.server.Interface;

import org.project.models.MyRequest;
import org.project.models.MyResponse;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerSensors extends Remote {
    MyResponse readAllHouseData(MyRequest myRequest) throws RemoteException;

    String benchmarkSensorInvoke() throws RemoteException;

    String benchmarkSensorQuery() throws RemoteException;
}
