package org.project.server.Interface;

import org.project.models.MyRequest;
import org.project.models.MyResponse;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerEnroll extends Remote {
    MyResponse enroll(MyRequest request) throws RemoteException;

    String benchmarkEnroll() throws RemoteException;
}
