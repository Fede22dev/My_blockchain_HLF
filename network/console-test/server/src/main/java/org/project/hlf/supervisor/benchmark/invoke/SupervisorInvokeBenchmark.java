package org.project.hlf.supervisor.benchmark.invoke;

import com.opencsv.CSVWriter;
import org.jetbrains.annotations.NotNull;
import org.project.hlf.Utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.project.server.ServerImpl.MINTESTBENCHMARK;
import static org.project.server.ServerImpl.RATETESTMILLIS;

public class SupervisorInvokeBenchmark {
    private static final ThreadTestSupervisorBenchmarkInvoke[] threads = new ThreadTestSupervisorBenchmarkInvoke[3];

    public static @NotNull String benchmarkSupervisorInvoke() throws InterruptedException, IOException {
        for (int i = 0; i < 3; i++) {
            threads[i] = new ThreadTestSupervisorBenchmarkInvoke(String.valueOf(i));
        }

        for (int i = 0; i < 3; i++) {
            threads[i].join();
        }

        CSVWriter writer = new CSVWriter(new FileWriter("/media/sf_Passaggio_File/bench_supervisor_invoke_" + "rate" + 1000 / RATETESTMILLIS + "_dur" + MINTESTBENCHMARK + ".csv"));
        writer.writeNext(new String[]{"invoke time 1", "invoke time 2", "invoke time 3"});

        Map<String, List<Double>> hashMap = SupervisorInvokeDataBenchmark.getMultiListTimes();
        Utils.writeCSV(writer, hashMap);
        SupervisorInvokeDataBenchmark.clear();

        return "BENCHMARK SUPERVISOR INVOKE ESEGUITO";
    }
}
