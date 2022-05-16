package org.project.hlf.sensor.benchmark.query;

import com.opencsv.CSVWriter;
import org.jetbrains.annotations.NotNull;
import org.project.hlf.Utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.project.ServerConstants.*;

public class SensorQueryBenchmark {
    private static final ThreadSensorBenchmarkQuery[] THREAD_SENSOR_BENCHMARK_QUERIES = new ThreadSensorBenchmarkQuery[NUMBER_OF_PROCESS];

    public static @NotNull String benchmarkSensorQuery() throws InterruptedException, IOException {
        for (int i = 0; i < NUMBER_OF_PROCESS; i++) {
            THREAD_SENSOR_BENCHMARK_QUERIES[i] = new ThreadSensorBenchmarkQuery(String.valueOf(i));
        }

        for (int i = 0; i < NUMBER_OF_PROCESS; i++) {
            THREAD_SENSOR_BENCHMARK_QUERIES[i].join();
        }

        CSVWriter writer = new CSVWriter(new FileWriter("/media/sf_Passaggio_File/bench_sensor_query_" + "tps" + ONE_THOUSAND / MILLIS_RATE_REQUEST_BENCHMARK + "_dur" + SECONDS_DURATION_BENCHMARK + ".csv"));
        writer.writeNext(new String[]{"query time 1", "query time 2", "query time 3"});

        Map<String, List<Double>> hashMap = SensorQueryDataBenchmark.getMultiListTimes();
        Utils.writeCSV(writer, hashMap);
        SensorQueryDataBenchmark.clear();

        return "BENCHMARK SENSOR QUERY ESEGUITO";
    }
}
