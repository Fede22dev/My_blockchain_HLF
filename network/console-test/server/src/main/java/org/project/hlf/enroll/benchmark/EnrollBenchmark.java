package org.project.hlf.enroll.benchmark;

import com.opencsv.CSVWriter;
import org.jetbrains.annotations.NotNull;
import org.project.hlf.Utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.project.ServerConstants.*;

public class EnrollBenchmark {
    private static final ThreadEnrollBenchmark[] THREAD_ENROLL_BENCHMARKS = new ThreadEnrollBenchmark[NUMBER_OF_PROCESS];

    public static @NotNull String benchmarkEnroll() throws InterruptedException, IOException {
        for (int i = 0; i < NUMBER_OF_PROCESS; i++) {
            THREAD_ENROLL_BENCHMARKS[i] = new ThreadEnrollBenchmark(String.valueOf(i));
        }

        for (int i = 0; i < NUMBER_OF_PROCESS; i++) {
            THREAD_ENROLL_BENCHMARKS[i].join();
        }

        CSVWriter writer = new CSVWriter(new FileWriter(ENROLL_CSV));
        writer.writeNext(new String[]{"enroll time 1", "enroll time 2", "enroll time 3"});

        Map<String, List<Double>> hashMap = EnrollDataBenchmark.getMultiListTimes();
        Utils.writeCSV(writer, hashMap);
        EnrollDataBenchmark.clear();

        return "BENCHMARK ENROLL ESEGUITO";
    }
}
