package org.project.hlf.supervisor.benchmark.query;

import com.opencsv.CSVWriter;
import org.jetbrains.annotations.NotNull;
import org.project.hlf.Utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static org.project.server.ServerImpl.MINTESTBENCHMARK;
import static org.project.server.ServerImpl.RATETESTMILLIS;

public class SupervisorQueryBenchmark {
    private static final ThreadTestSupervisorBenchmarkQuery[] threads = new ThreadTestSupervisorBenchmarkQuery[3];

    public static @NotNull String benchmarkSupervisorQuery() throws InterruptedException, IOException {
        for (int i = 0; i < 3; i++) {
            threads[i] = new ThreadTestSupervisorBenchmarkQuery(String.valueOf(i));
        }

        for (int i = 0; i < 3; i++) {
            threads[i].join();
        }

        CSVWriter writer = new CSVWriter(new FileWriter("/media/sf_Passaggio_File/bench_supervisor_query_" + "rate" + RATETESTMILLIS + "_dur" + MINTESTBENCHMARK + ".csv"));
        writer.writeNext(new String[]{"query time 1", "query time 2", "query time 3"});

        HashMap<String, ArrayList<Double>> hashMap = SupervisorQueryDataBenchmark.getMultiListTimes();
        Utils.writeCSV(writer, hashMap);
        SupervisorQueryDataBenchmark.clear();

        return "BENCHMARK SUPERVISOR QUERY ESEGUITO";
    }
}
