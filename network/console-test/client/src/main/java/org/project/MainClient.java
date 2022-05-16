package org.project;

import org.project.hlf.enroll.Enroll;
import org.project.hlf.enroll.EnrollBenchmark;
import org.project.hlf.sensor.Sensor;
import org.project.hlf.sensor.SensorBenchmark;
import org.project.hlf.supervisor.Supervisor;
import org.project.hlf.supervisor.SupervisorBenchmark;

import java.io.IOException;

import static org.project.ClientConstants.CONSOLE;

public class MainClient {
    public static void main(final String[] args) throws IOException {
        while (true) {
            System.out.print("Command --> [1 = enroll] [2 = invoke supervisor] [3 = query supervisor] [4 = performance] [5 = query house data] [6 = stop]: ");
            switch (CONSOLE.readLine().strip()) {
                case "1" -> Enroll.enroll();

                case "2" -> {
                    boolean goInvoke = true;
                    while (goInvoke) {
                        System.out.print("Method --> [1 = payRent] [2 = payDeposit] [3 = payBill] [4 = payCondominiumFees] [5 = stop invoke]: ");
                        switch (CONSOLE.readLine().strip()) {
                            case "1" -> Supervisor.payRent();
                            case "2" -> Supervisor.payDeposit();
                            case "3" -> Supervisor.payBill();
                            case "4" -> Supervisor.payCondominiumFees();
                            case "5" -> goInvoke = false;
                            default -> System.out.println("Command not recognized");
                        }
                    }
                }

                case "3" -> {
                    boolean goQuery = true;
                    while (goQuery) {
                        System.out.print("Method --> [1 = readAllPaymentType] [2 = stop query]: ");
                        switch (CONSOLE.readLine().strip()) {
                            case "1" -> Supervisor.readAllPaymentType();
                            case "2" -> goQuery = false;
                            default -> System.out.println("Command not recognized");
                        }
                    }
                }

                case "4" -> {
                    boolean goPerformance = true;
                    while (goPerformance) {
                        System.out.print("Method --> [1 = bench enroll] [2 = bench supervisor invoke] [3 = bench supervisor query] [4 = bench sensor invoke] [5 = bench sensor query] [6 = stop bench]: ");
                        switch (CONSOLE.readLine().strip()) {
                            case "1" -> EnrollBenchmark.benchmarkEnroll();
                            case "2" -> SupervisorBenchmark.benchmarkSupervisorInvoke();
                            case "3" -> SupervisorBenchmark.benchmarkSupervisorQuery();
                            case "4" -> SensorBenchmark.benchmarkSensorInvoke();
                            case "5" -> SensorBenchmark.benchmarkSensorQuery();
                            case "6" -> goPerformance = false;
                            default -> System.out.println("Command not recognized");
                        }
                    }
                }

                case "5" -> {
                    boolean goQuery = true;
                    while (goQuery) {
                        System.out.print("Method --> [1 = read electricity data] [2 = read weather data] [3 = stop query]: ");
                        switch (CONSOLE.readLine().strip()) {
                            case "1" -> Sensor.readAllHouseData(false);
                            case "2" -> Sensor.readAllHouseData(true);
                            case "3" -> goQuery = false;
                            default -> System.out.println("Command not recognized");
                        }
                    }
                }

                case "6" -> System.exit(0);

                default -> System.out.println("Command not recognized");
            }
        }
    }
}
