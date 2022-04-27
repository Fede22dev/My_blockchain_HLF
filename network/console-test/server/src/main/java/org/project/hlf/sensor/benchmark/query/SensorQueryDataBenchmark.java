package org.project.hlf.sensor.benchmark.query;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

class SensorQueryDataBenchmark {
    private static final HashMap<String, ArrayList<Double>> multiListTimesSensorQuery = new HashMap<>(3);

    static synchronized @NotNull ArrayList<Double> getMultiListTimes() {
        ArrayList<Double> totals = new ArrayList<>();
        multiListTimesSensorQuery.forEach((key, value) ->
                totals.addAll(value)
        );
        return totals;
    }

    static synchronized void putTimes(String key, @NotNull ArrayList<Double> times) {
        multiListTimesSensorQuery.put(key, times);
    }

    static synchronized void clear() {
        multiListTimesSensorQuery.clear();
    }
}
