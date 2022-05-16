package org.project.hlf.enroll;

import org.project.server.ServerInstance;

import java.io.IOException;

public class EnrollBenchmark {
    public static void benchmarkEnroll() throws IOException {
        System.out.println(ServerInstance.getInstance().benchmarkEnroll());
    }
}
