package org.project.hlf.sensor.benchmark.invoke;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class SensorInvokeDataBenchmark {
    private static final Map<String, List<Double>> multiListTimesSensorInvoke = new HashMap<>(3);

    static synchronized Map<String, List<Double>> getMultiListTimes() {
        return multiListTimesSensorInvoke;
    }

    static synchronized void putTimes(String key, @NotNull List<Double> times) {
        multiListTimesSensorInvoke.put(key, times);
    }

    static synchronized void clear() {
        multiListTimesSensorInvoke.clear();
    }
}
