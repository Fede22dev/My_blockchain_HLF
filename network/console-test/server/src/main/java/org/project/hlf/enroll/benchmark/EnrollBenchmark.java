package org.project.hlf.enroll.benchmark;

import com.opencsv.CSVWriter;
import org.jetbrains.annotations.NotNull;
import org.project.hlf.Utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static org.project.server.ServerImpl.MINTESTBENCHMARK;
import static org.project.server.ServerImpl.RATETESTMILLIS;

public class EnrollBenchmark {
    private static final ThreadTestEnrollBenchmark[] threads = new ThreadTestEnrollBenchmark[3];

    public static @NotNull String benchmarkEnroll() throws InterruptedException, IOException {
        for (int i = 0; i < 3; i++) {
            threads[i] = new ThreadTestEnrollBenchmark(String.valueOf(i));
        }

        for (int i = 0; i < 3; i++) {
            threads[i].join();
        }

        CSVWriter writer = new CSVWriter(new FileWriter("/media/sf_Passaggio_File/bench_enroll_" + "rate" + RATETESTMILLIS + "_dur" + MINTESTBENCHMARK + ".csv"));
        writer.writeNext(new String[]{"enroll time 1", "enroll time 2", "enroll time 3"});

        HashMap<String, ArrayList<Double>> hashMap = EnrollDataBenchmark.getMultiListTimes();
        Utils.writeCSV(writer, hashMap);
        EnrollDataBenchmark.clear();

        return "BENCHMARK ENROLL ESEGUITO";
    }
}
