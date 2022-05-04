package org.project.hlf.enroll.benchmark;

import com.opencsv.CSVWriter;
import org.jetbrains.annotations.NotNull;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Stream;

public class EnrollBenchmark {
    private static final Thread[] threads = new Thread[3];

    public static @NotNull String benchmarkEnroll() throws InterruptedException, IOException {
        for (int i = 0; i < 3; i++) {
            int finalI = i + 1;
             threads[i] = new Thread(() -> {
                try {
                    TestEnrollBenchmark.testEnroll(String.valueOf(finalI));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        for (int i = 0; i < 3; i++) {
            threads[i].join();
            threads[i].start();
        }

        CSVWriter writer = new CSVWriter(new FileWriter("/media/sf_Passaggio_File/bench_enroll.csv"));
        writer.writeNext(new String[]{"enroll time 1", "enroll time 2", "enroll time 3"});

        HashMap<String, ArrayList<Double>> hashMap = EnrollDataBenchmark.getMultiListTimes();

        int list1Length = hashMap.get("1").size();
        int list2Length = hashMap.get("2").size();
        int list3Length = hashMap.get("3").size();

        int max = Stream.of(list1Length, list2Length, list3Length).max(Integer::compareTo).get();
        for (int i = 0; i < max; i++) {
            writer.writeNext(new String[]{String.valueOf(i > list1Length - 1 ? "" : hashMap.get("1").get(i)).replace(".", ","),
                    String.valueOf(i > list2Length - 1 ? "" : hashMap.get("2").get(i)).replace(".", ","),
                    String.valueOf(i > list3Length - 1 ? "" : hashMap.get("3").get(i)).replace(".", ",")}
            );
        }

        writer.close();
        EnrollDataBenchmark.clear();

        return "BENCHMARK ENROLL ESEGUITO";
    }
}
