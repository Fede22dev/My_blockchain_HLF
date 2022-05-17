package org.project.server.Interface;

import org.project.models.MyRequest;
import org.project.models.MyResponse;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerSupervisor extends Remote {
    MyResponse payRent(MyRequest request) throws RemoteException;

    MyResponse payDeposit(MyRequest request) throws RemoteException;

    MyResponse payBills(MyRequest request) throws RemoteException;

    MyResponse payCondominiumFees(MyRequest request) throws RemoteException;

    MyResponse readAllPaymentType(MyRequest request) throws RemoteException;

    String benchmarkSupervisorInvoke() throws RemoteException;

    String benchmarkSupervisorQuery() throws RemoteException;
}
