package org.project.server.Interface;

import org.project.models.MyRequest;
import org.project.models.MyResponse;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerSupervisor extends Remote {
    MyResponse payRent(MyRequest myRequest) throws RemoteException;

    MyResponse payDeposit(MyRequest myRequest) throws RemoteException;

    MyResponse payBill(MyRequest myRequest) throws RemoteException;

    MyResponse payCondominiumFees(MyRequest myRequest) throws RemoteException;

    MyResponse readAllPaymentType(MyRequest myRequest) throws RemoteException;

    String benchmarkSupervisorInvoke() throws RemoteException;

    String benchmarkSupervisorQuery() throws RemoteException;
}
