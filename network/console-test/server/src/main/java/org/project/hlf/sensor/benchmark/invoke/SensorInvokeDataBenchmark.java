package org.project.hlf.sensor.benchmark.invoke;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

class SensorInvokeDataBenchmark {
    private static final HashMap<String, ArrayList<Double>> multiListTimesSensorInvoke = new HashMap<>(3);

    static synchronized @NotNull ArrayList<Double> getMultiListTimes() {
        ArrayList<Double> totals = new ArrayList<>();
        multiListTimesSensorInvoke.forEach((key, value) ->
                totals.addAll(value)
        );
        return totals;
    }

    static synchronized void putTimes(String key, @NotNull ArrayList<Double> times) {
        multiListTimesSensorInvoke.put(key, times);
    }

    static synchronized void clear() {
        multiListTimesSensorInvoke.clear();
    }
}
