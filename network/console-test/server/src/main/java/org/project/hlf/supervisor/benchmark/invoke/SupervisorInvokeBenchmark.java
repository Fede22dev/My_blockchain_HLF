package org.project.hlf.supervisor.benchmark.invoke;

import com.opencsv.CSVWriter;
import org.jetbrains.annotations.NotNull;

import java.io.FileWriter;
import java.io.IOException;

public class SupervisorInvokeBenchmark {
    public static @NotNull String benchmarkSupervisorInvoke() throws InterruptedException, IOException {
        for (int i = 0; i < 3; i++) {
            MainSupervisorBenchmarkInvoke.main(new String[]{String.valueOf(i + 1)});
        }

        CSVWriter writer = new CSVWriter(new FileWriter("/media/sf_Passaggio_File/bench_supervisor_invoke.csv"));
        writer.writeNext(new String[]{"invoke supervisor time"});

        SupervisorInvokeDataBenchmark.getMultiListTimes().forEach(time ->
                writer.writeNext(new String[]{String.valueOf(time).replace(".", ",")})
        );

        writer.close();
        SupervisorInvokeDataBenchmark.clear();

        return "BENCHMARK SUPERVISOR INVOKE ESEGUITO";
    }
}
