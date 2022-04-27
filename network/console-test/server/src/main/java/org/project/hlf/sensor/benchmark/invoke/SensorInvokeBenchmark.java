package org.project.hlf.sensor.benchmark.invoke;

import com.opencsv.CSVWriter;
import org.jetbrains.annotations.NotNull;

import java.io.FileWriter;
import java.io.IOException;

public class SensorInvokeBenchmark {
    public static @NotNull String benchmarkSensorInvoke() throws IOException, InterruptedException {
        for (int i = 0; i < 3; i++) {
            MainSensorBenchmarkInvoke.main(new String[]{String.valueOf(i + 1)});
        }

        CSVWriter writer = new CSVWriter(new FileWriter("/media/sf_Passaggio_File/bench_sensor_invoke.csv"));
        writer.writeNext(new String[]{"invoke sensor time"});

        SensorInvokeDataBenchmark.getMultiListTimes().forEach(time ->
                writer.writeNext(new String[]{String.valueOf(time).replace(".", ",")})
        );

        writer.close();
        SensorInvokeDataBenchmark.clear();

        return "BENCHMARK SENSOR INVOKE ESEGUITO";
    }
}
