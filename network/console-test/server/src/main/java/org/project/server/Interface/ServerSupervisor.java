package org.project.server.Interface;

import org.apache.http.client.fluent.Request;
import org.project.models.MyResponse;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerSupervisor extends Remote {
    MyResponse payRent(Request request, String port) throws RemoteException;

    MyResponse payDeposit(Request request, String port) throws RemoteException;

    MyResponse payBill(Request request, String port) throws RemoteException;

    MyResponse payCondominiumFees(Request request, String port) throws RemoteException;

    MyResponse readAllPaymentType(Request request, String port) throws RemoteException;

    String benchmarkSupervisorInvoke() throws RemoteException;

    String benchmarkSupervisorQuery() throws RemoteException;
}
