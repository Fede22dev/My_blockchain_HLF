package org.project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.text.ParseException;

import static org.project.hlf.enroll.Enroll.benchmarkEnroll;
import static org.project.hlf.enroll.Enroll.enroll;
import static org.project.hlf.sensor.Sensor.readAllHouseData;
import static org.project.hlf.sensor.SensorBenchmark.benchmarkSensorInvoke;
import static org.project.hlf.sensor.SensorBenchmark.benchmarkSensorQuery;
import static org.project.hlf.supervisor.Supervisor.*;
import static org.project.hlf.supervisor.SupervisorBenchmark.benchmarkSupervisorInvoke;
import static org.project.hlf.supervisor.SupervisorBenchmark.benchmarkSupervisorQuery;

public class Main {
    public final static String ANSI_RESET = "\u001B[0m";
    public final static String ANSI_BLUE = "\u001B[34m";
    public final static String[] TYPOLOGY = {"rents", "bills", "deposits", "condominium fees"};
    public final static BufferedReader CONSOLE = new BufferedReader(new InputStreamReader(System.in));
    public final static String TENANTPORT = "8802";

    public static void main(String[] args) throws IOException, NotBoundException, ParseException {
        while (true) {
            System.out.print("Command --> [1 = enroll] [2 = invoke supervisor] [3 = query supervisor] [4 = performance] [5 = query house data] [6 = stop]: ");
            switch (CONSOLE.readLine().strip()) {
                case "1" -> enroll();

                case "2" -> {
                    boolean goInvoke = true;
                    while (goInvoke) {
                        System.out.print("Method --> [1 = payRent] [2 = payDeposit] [3 = payBill] [4 = payCondominiumFees] [5 = stop invoke]: ");
                        switch (CONSOLE.readLine().strip()) {
                            case "1" -> payRent();
                            case "2" -> payDeposit();
                            case "3" -> payBill();
                            case "4" -> payCondominiumFees();
                            case "5" -> goInvoke = false;
                        }
                    }
                }

                case "3" -> {
                    boolean goQuery = true;
                    while (goQuery) {
                        System.out.print("Method --> [1 = readAllPaymentType] [2 = stop query]: ");
                        switch (CONSOLE.readLine().strip()) {
                            case "1" -> readAllPaymentType();
                            case "2" -> goQuery = false;
                        }
                    }
                }

                case "4" -> {
                    boolean goPerformance = true;
                    while (goPerformance) {
                        System.out.print("Method --> [1 = bench enroll] [2 = bench supervisor invoke] [3 = bench supervisor query] [4 = bench sensor invoke] [5 = bench sensor query] [6 = stop bench]: ");
                        switch (CONSOLE.readLine().strip()) {
                            case "1" -> benchmarkEnroll();
                            case "2" -> benchmarkSupervisorInvoke();
                            case "3" -> benchmarkSupervisorQuery();
                            case "4" -> benchmarkSensorInvoke();
                            case "5" -> benchmarkSensorQuery();
                            case "6" -> goPerformance = false;
                        }
                    }
                }

                case "5" -> {
                    boolean goQuery = true;
                    while (goQuery) {
                        System.out.print("Method --> [1 = read electricity data] [2 = read weather data] [3 = stop query]: ");
                        switch (CONSOLE.readLine().strip()) {
                            case "1" -> readAllHouseData(false);
                            case "2" -> readAllHouseData(true);
                            case "3" -> goQuery = false;
                        }
                    }
                }

                case "6" -> System.exit(0);
            }
        }
    }
}
