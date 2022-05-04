package org.project.server;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
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
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class ServerImpl extends UnicastRemoteObject implements Server {

    public final static String ANSI_RESET = "\u001B[0m";
    public final static String ANSI_BLUE = "\u001B[34m";
    public final static long OBL = 1_000_000_000;
    //public final static String LANDLORDPORT = "8801";
    public final static String TENANTPORT = "8802";
    //public final static String GUESTPORT = "8803";
    public final static String SENSORPORT = "8804";
    public final static long MINTEST = 30;
    public final static long RATETESTMILLIS = 500; // 500 = 2 transaction per second
    private final static HashMap<String, String> TOKEN = new HashMap<>();

    public ServerImpl() throws RemoteException {
        super();
        timerEnroll(SENSORPORT);
        timerEnroll(TENANTPORT);

        Sensor.startWeatherSensor();
        Sensor.startElectricitySensor();
    }

    public static synchronized String getToken(String port) {
        return TOKEN.get(port);
    }

    public static synchronized void putToken(String port, String token) {
        TOKEN.put(port, token);
    }

    private void timerEnroll(String port) {
        final Request request = Request.Post("http://localhost:" + port + "/user/enroll");
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    String body = "{\"id\": \"admin\", \"secret\": \"adminpw\"}";
                    request.bodyString(body, ContentType.APPLICATION_FORM_URLENCODED);
                    request.setHeader("Authorization", "Bearer");
                    request.setHeader("Content-Type", "application/x-www-form-urlencoded");
                    Response response = request.execute();
                    HttpResponse httpResponse = response.returnResponse();
                    HttpEntity entity = httpResponse.getEntity();
                    if (entity != null) {
                        String html = EntityUtils.toString(entity);
                        System.out.println("\nENROLL TIMER " + port + ": " + html);
                        JSONObject object = new JSONObject(html);
                        String tokenAdmin = object.getString("token");
                        putToken(port, tokenAdmin);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 1000 * 60 * 8);
    }

    @Override
    public synchronized MyResponse enroll(MyRequest myRequest) throws RemoteException {
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
    public synchronized MyResponse readAllHouseData(MyRequest myRequest) throws RemoteException {
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
    public synchronized MyResponse payRent(MyRequest myRequest) throws RemoteException {
        try {
            return Supervisor.payRent(myRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized MyResponse payDeposit(MyRequest myRequest) throws RemoteException {
        try {
            return Supervisor.payDeposit(myRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized MyResponse payBill(MyRequest myRequest) throws RemoteException {
        try {
            return Supervisor.payBill(myRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized MyResponse payCondominiumFees(MyRequest myRequest) throws RemoteException {
        try {
            return Supervisor.payCondominiumFees(myRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized MyResponse readAllPaymentType(MyRequest myRequest) throws RemoteException {
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
