package org.project.hlf.enroll.benchmark;

import com.opencsv.CSVWriter;
import org.jetbrains.annotations.NotNull;

import java.io.FileWriter;
import java.io.IOException;

public class EnrollBenchmark {
    public static @NotNull String benchmarkEnroll() throws InterruptedException, IOException {
        for (int i = 0; i < 3; i++) {
            MainEnrollBenchmark.main(new String[]{String.valueOf(i + 1)});
        }

        CSVWriter writer = new CSVWriter(new FileWriter("/media/sf_Passaggio_File/bench_enroll.csv"));
        writer.writeNext(new String[]{"enroll time"});

        EnrollDataBenchmark.getMultiListTimes().forEach(time ->
                writer.writeNext(new String[]{String.valueOf(time).replace(".", ",")})
        );

        writer.close();
        EnrollDataBenchmark.clear();

        return "BENCHMARK ENROLL ESEGUITO";
    }
}
