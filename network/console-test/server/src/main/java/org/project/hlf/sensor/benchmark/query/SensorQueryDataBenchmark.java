package org.project.hlf.sensor.benchmark.query;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class SensorQueryDataBenchmark {
    private static final Map<String, List<Double>> MULTI_LIST_TIMES_SENSOR_QUERY = new HashMap<>(3);

    static synchronized Map<String, List<Double>> getMultiListTimes() {
        return MULTI_LIST_TIMES_SENSOR_QUERY;
    }

    static synchronized void putTimes(final String key, @NotNull final List<Double> times) {
        MULTI_LIST_TIMES_SENSOR_QUERY.put(key, times);
    }

    static synchronized void clear() {
        MULTI_LIST_TIMES_SENSOR_QUERY.clear();
    }
}
