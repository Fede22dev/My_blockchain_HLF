package org.project.hlf.sensor.benchmark.query;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

class SensorQueryDataBenchmark {
    private static final HashMap<String, List<Double>> multiListTimesSensorQuery = new HashMap<>(3);

    static synchronized HashMap<String, List<Double>> getMultiListTimes() {
        return multiListTimesSensorQuery;
    }

    static synchronized void putTimes(String key, @NotNull List<Double> times) {
        multiListTimesSensorQuery.put(key, times);
    }

    static synchronized void clear() {
        multiListTimesSensorQuery.clear();
    }
}
