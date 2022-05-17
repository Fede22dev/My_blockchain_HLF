package org.project.server;

import org.project.hlf.enroll.Enroll;
import org.project.hlf.enroll.benchmark.EnrollBenchmark;
import org.project.hlf.sensor.Sensor;
import org.project.hlf.sensor.benchmark.invoke.SensorInvokeBenchmark;
import org.project.hlf.sensor.benchmark.query.SensorQueryBenchmark;
import org.project.hlf.supervisor.Supervisor;
import org.project.hlf.supervisor.benchmark.invoke.SupervisorInvokeBenchmark;
import org.project.hlf.supervisor.benchmark.query.SupervisorQueryBenchmark;
import org.project.models.MyRequest;
import org.project.models.MyResponse;
import org.project.server.Interface.Server;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import static org.project.ServerConstants.SENSOR_PORT;
import static org.project.ServerConstants.TENANT_PORT;

public class ServerImpl extends UnicastRemoteObject implements Server {
    public ServerImpl() throws RemoteException {
        super();
        TokenManager.timerEnroll(SENSOR_PORT);
        TokenManager.timerEnroll(TENANT_PORT);

        Sensor.startWeatherSensor();
        Sensor.startElectricitySensor();
    }

    @Override
    public synchronized MyResponse enroll(final MyRequest myRequest) throws RemoteException {
        try {
            return Enroll.enroll(myRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized String benchmarkEnroll() throws RemoteException {
        try {
            return EnrollBenchmark.benchmarkEnroll();
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized MyResponse readAllHouseData(final MyRequest myRequest) throws RemoteException {
        try {
            return Sensor.readAllHouseData(myRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized String benchmarkSensorInvoke() throws RemoteException {
        try {
            return SensorInvokeBenchmark.benchmarkSensorInvoke();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized String benchmarkSensorQuery() throws RemoteException {
        try {
            return SensorQueryBenchmark.benchmarkSensorQuery();
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized MyResponse payRent(final MyRequest myRequest) throws RemoteException {
        try {
            return Supervisor.payRent(myRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized MyResponse payDeposit(final MyRequest myRequest) throws RemoteException {
        try {
            return Supervisor.payDeposit(myRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized MyResponse payBills(final MyRequest myRequest) throws RemoteException {
        try {
            return Supervisor.payBills(myRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized MyResponse payCondominiumFees(final MyRequest myRequest) throws RemoteException {
        try {
            return Supervisor.payCondominiumFees(myRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized MyResponse readAllPaymentType(final MyRequest myRequest) throws RemoteException {
        try {
            return Supervisor.readAllPaymentType(myRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized String benchmarkSupervisorInvoke() throws RemoteException {
        try {
            return SupervisorInvokeBenchmark.benchmarkSupervisorInvoke();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized String benchmarkSupervisorQuery() throws RemoteException {
        try {
            return SupervisorQueryBenchmark.benchmarkSupervisorQuery();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
