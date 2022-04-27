package org.project.server.Interface;

import org.apache.http.client.fluent.Request;
import org.project.models.MyResponse;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerEnroll extends Remote {
    MyResponse enroll(Request request, String port) throws RemoteException;

    String benchmarkEnroll() throws RemoteException;
}
