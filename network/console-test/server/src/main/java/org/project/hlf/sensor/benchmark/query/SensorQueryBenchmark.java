package org.project.hlf.sensor.benchmark.query;

import com.opencsv.CSVWriter;
import org.jetbrains.annotations.NotNull;

import java.io.FileWriter;
import java.io.IOException;

public class SensorQueryBenchmark {
    public static @NotNull String benchmarkSensorQuery() throws InterruptedException, IOException {
        for (int i = 0; i < 3; i++) {
            MainSensorBenchmarkQuery.main(new String[]{String.valueOf(i + 1)});
        }

        CSVWriter writer = new CSVWriter(new FileWriter("/media/sf_Passaggio_File/bench_sensor_query.csv"));
        writer.writeNext(new String[]{"query sensor time"});

        SensorQueryDataBenchmark.getMultiListTimes().forEach(time ->
                writer.writeNext(new String[]{String.valueOf(time).replace(".", ",")})
        );

        writer.close();
        SensorQueryDataBenchmark.clear();

        return "BENCHMARK SENSOR QUERY ESEGUITO";
    }
}
