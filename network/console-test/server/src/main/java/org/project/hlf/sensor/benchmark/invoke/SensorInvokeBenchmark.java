package org.project.hlf.sensor.benchmark.invoke;

import com.opencsv.CSVWriter;
import org.jetbrains.annotations.NotNull;
import org.project.hlf.Utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static org.project.server.ServerImpl.MINTESTBENCHMARK;
import static org.project.server.ServerImpl.RATETESTMILLIS;

public class SensorInvokeBenchmark {
    private static final ThreadTestSensorBenchmarkInvoke[] threads = new ThreadTestSensorBenchmarkInvoke[3];


    public static @NotNull String benchmarkSensorInvoke() throws IOException, InterruptedException {
        for (int i = 0; i < 3; i++) {
            threads[i] = new ThreadTestSensorBenchmarkInvoke(String.valueOf(i));
        }

        for (int i = 0; i < 3; i++) {
            threads[i].join();
        }

        CSVWriter writer = new CSVWriter(new FileWriter("/media/sf_Passaggio_File/bench_sensor_invoke_" + "rate" + 1000 / RATETESTMILLIS + "_dur" + MINTESTBENCHMARK + ".csv"));
        writer.writeNext(new String[]{"invoke time 1", "invoke time 2", "invoke time 3"});

        HashMap<String, List<Double>> hashMap = SensorInvokeDataBenchmark.getMultiListTimes();
        Utils.writeCSV(writer, hashMap);
        SensorInvokeDataBenchmark.clear();

        return "BENCHMARK SENSOR INVOKE ESEGUITO";
    }
}
