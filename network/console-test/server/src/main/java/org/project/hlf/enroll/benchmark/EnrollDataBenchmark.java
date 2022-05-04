package org.project.hlf.enroll.benchmark;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class EnrollDataBenchmark {
    private static final HashMap<String, ArrayList<Double>> multiListTimesEnroll = new HashMap<>(3);

    public static synchronized HashMap<String, ArrayList<Double>> getMultiListTimes() {
        return multiListTimesEnroll;
    }

    public static synchronized void putTimes(String key, @NotNull ArrayList<Double> times) {
        multiListTimesEnroll.put(key, times);
    }

    public static synchronized void clear() {
        multiListTimesEnroll.clear();
    }
}
