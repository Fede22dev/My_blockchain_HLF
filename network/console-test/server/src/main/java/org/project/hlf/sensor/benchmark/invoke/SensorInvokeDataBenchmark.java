package org.project.hlf.sensor.benchmark.invoke;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

class SensorInvokeDataBenchmark {
    private static final HashMap<String, List<Double>> multiListTimesSensorInvoke = new HashMap<>(3);

    static synchronized HashMap<String, List<Double>> getMultiListTimes() {
        return multiListTimesSensorInvoke;
    }

    static synchronized void putTimes(String key, @NotNull List<Double> times) {
        multiListTimesSensorInvoke.put(key, times);
    }

    static synchronized void clear() {
        multiListTimesSensorInvoke.clear();
    }
}
