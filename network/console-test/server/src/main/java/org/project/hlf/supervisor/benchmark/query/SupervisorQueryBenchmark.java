package org.project.hlf.supervisor.benchmark.query;

import com.opencsv.CSVWriter;
import org.jetbrains.annotations.NotNull;

import java.io.FileWriter;
import java.io.IOException;

public class SupervisorQueryBenchmark {
    public static @NotNull String benchmarkSupervisorQuery() throws IOException, InterruptedException {
        for (int i = 0; i < 3; i++) {
            MainSupervisorBenchmarkQuery.main(new String[]{String.valueOf(i + 1)});
        }

        CSVWriter writer = new CSVWriter(new FileWriter("/media/sf_Passaggio_File/bench_supervisor_query.csv"));
        writer.writeNext(new String[]{"invoke supervisor time"});

        SupervisorQueryDataBenchmark.getMultiListTimes().forEach(time ->
                writer.writeNext(new String[]{String.valueOf(time).replace(".", ",")})
        );

        writer.close();
        SupervisorQueryDataBenchmark.clear();

        return "BENCHMARK SUPERVISOR INVOKE ESEGUITO";
    }
}
