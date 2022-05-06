package org.project.hlf.enroll.benchmark;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

class EnrollDataBenchmark {
    private static final HashMap<String, List<Double>> multiListTimesEnroll = new HashMap<>(3);

    static synchronized HashMap<String, List<Double>> getMultiListTimes() {
        return multiListTimesEnroll;
    }

    static synchronized void putTimes(String key, @NotNull List<Double> times) {
        multiListTimesEnroll.put(key, times);
    }

    static synchronized void clear() {
        multiListTimesEnroll.clear();
    }
}
