package org.project.hlf.sensor.benchmark.invoke;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

class SensorInvokeDataBenchmark {
    private static final HashMap<String, ArrayList<Double>> multiListTimesSensorInvoke = new HashMap<>(3);

    static synchronized HashMap<String, ArrayList<Double>> getMultiListTimes() {
        return multiListTimesSensorInvoke;
    }

    static synchronized void putTimes(String key, @NotNull ArrayList<Double> times) {
        multiListTimesSensorInvoke.put(key, times);
    }

    static synchronized void clear() {
        multiListTimesSensorInvoke.clear();
    }
}
