package org.project.hlf.supervisor.benchmark.invoke;

import com.opencsv.CSVWriter;
import org.jetbrains.annotations.NotNull;
import org.project.hlf.Utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.project.ServerConstants.*;

public class SupervisorInvokeBenchmark {
    private static final ThreadSupervisorBenchmarkInvoke[] THREAD_SUPERVISOR_BENCHMARK_INVOKES = new ThreadSupervisorBenchmarkInvoke[NUMBER_OF_PROCESS];

    public static @NotNull String benchmarkSupervisorInvoke() throws InterruptedException, IOException {
        for (int i = 0; i < NUMBER_OF_PROCESS; i++) {
            THREAD_SUPERVISOR_BENCHMARK_INVOKES[i] = new ThreadSupervisorBenchmarkInvoke(String.valueOf(i));
        }

        for (int i = 0; i < NUMBER_OF_PROCESS; i++) {
            THREAD_SUPERVISOR_BENCHMARK_INVOKES[i].join();
        }

        CSVWriter writer = new CSVWriter(new FileWriter("/media/sf_Passaggio_File/bench_supervisor_invoke_" + "tps" + ONE_THOUSAND / MILLIS_RATE_REQUEST_BENCHMARK + "_dur" + SECONDS_DURATION_BENCHMARK + ".csv"));
        writer.writeNext(new String[]{"invoke time 1", "invoke time 2", "invoke time 3"});

        Map<String, List<Double>> hashMap = SupervisorInvokeDataBenchmark.getMultiListTimes();
        Utils.writeCSV(writer, hashMap);
        SupervisorInvokeDataBenchmark.clear();

        return "BENCHMARK SUPERVISOR INVOKE ESEGUITO";
    }
}
