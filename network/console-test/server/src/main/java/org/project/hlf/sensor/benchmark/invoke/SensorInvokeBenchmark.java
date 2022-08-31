package org.project.hlf.sensor.benchmark.invoke;

import com.opencsv.CSVWriter;
import org.jetbrains.annotations.NotNull;
import org.project.hlf.Utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.project.ServerConstants.*;

public class SensorInvokeBenchmark {
    private static final ThreadSensorBenchmarkInvoke[] THREAD_SENSOR_BENCHMARK_INVOKES = new ThreadSensorBenchmarkInvoke[NUMBER_OF_PROCESS];

    public static @NotNull String benchmarkSensorInvoke() throws IOException, InterruptedException {
        for (int i = 0; i < NUMBER_OF_PROCESS; i++) {
            THREAD_SENSOR_BENCHMARK_INVOKES[i] = new ThreadSensorBenchmarkInvoke(String.valueOf(i));
        }

        for (int i = 0; i < NUMBER_OF_PROCESS; i++) {
            THREAD_SENSOR_BENCHMARK_INVOKES[i].join();
        }

        CSVWriter writer = new CSVWriter(new FileWriter(SENSOR_INVOKE_CSV));
        writer.writeNext(new String[]{"invoke time 1", "invoke time 2", "invoke time 3"});

        Map<String, List<Double>> hashMap = SensorInvokeDataBenchmark.getMultiListTimes();
        Utils.writeCSV(writer, hashMap);
        SensorInvokeDataBenchmark.clear();

        return "BENCHMARK SENSOR INVOKE ESEGUITO";
    }
}
