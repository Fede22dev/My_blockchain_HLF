package org.project.hlf.enroll;

import org.project.server.ServerReference;

import java.io.IOException;
import java.rmi.NotBoundException;

public class EnrollBenchmark {
    public static void benchmarkEnroll() throws IOException, NotBoundException {
        System.out.println(ServerReference.getServer().benchmarkEnroll());
    }
}
