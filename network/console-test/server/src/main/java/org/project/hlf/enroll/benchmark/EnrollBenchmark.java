package org.project.hlf.enroll.benchmark;

import com.opencsv.CSVWriter;
import org.jetbrains.annotations.NotNull;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Stream;

import static org.project.server.ServerImpl.MINTESTBENCHMARK;
import static org.project.server.ServerImpl.RATETESTMILLIS;

public class EnrollBenchmark {
    private static final ThreadTestEnrollBenchmark[] threads = new ThreadTestEnrollBenchmark[3];

    public static @NotNull String benchmarkEnroll() throws InterruptedException, IOException {
        for (int i = 0; i < 3; i++) {
            threads[i] = new ThreadTestEnrollBenchmark(String.valueOf(i));
        }

        for (int i = 0; i < 3; i++) {
            threads[i].join();
        }

        CSVWriter writer = new CSVWriter(new FileWriter("/media/sf_Passaggio_File/bench_enroll_" + "rate" + RATETESTMILLIS + "_dur" + MINTESTBENCHMARK + ".csv"));
        writer.writeNext(new String[]{"enroll time 1", "enroll time 2", "enroll time 3"});

        HashMap<String, ArrayList<Double>> hashMap = EnrollDataBenchmark.getMultiListTimes();
        int list0Length = hashMap.get("0").size();
        int list1Length = hashMap.get("1").size();
        int list2Length = hashMap.get("2").size();

        int max = Stream.of(list0Length, list1Length, list2Length).max(Integer::compareTo).get();
        for (int i = 0; i < max; i++) {
            writer.writeNext(new String[]{String.valueOf(i >= list0Length ? "" : hashMap.get("0").get(i)).replace(".", ","),
                    String.valueOf(i >= list1Length ? "" : hashMap.get("1").get(i)).replace(".", ","),
                    String.valueOf(i >= list2Length ? "" : hashMap.get("2").get(i)).replace(".", ",")}
            );
        }

        writer.close();
        EnrollDataBenchmark.clear();

        return "BENCHMARK ENROLL ESEGUITO";
    }
}
