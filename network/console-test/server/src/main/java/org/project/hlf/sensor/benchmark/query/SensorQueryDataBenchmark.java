package org.project.hlf.sensor.benchmark.query;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

class SensorQueryDataBenchmark {
    private static final HashMap<String, ArrayList<Double>> multiListTimesSensorQuery = new HashMap<>(3);

    static synchronized HashMap<String, ArrayList<Double>> getMultiListTimes() {
        return multiListTimesSensorQuery;
    }

    static synchronized void putTimes(String key, @NotNull ArrayList<Double> times) {
        multiListTimesSensorQuery.put(key, times);
    }

    static synchronized void clear() {
        multiListTimesSensorQuery.clear();
    }
}
