package org.project.hlf.supervisor.benchmark.query;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class SupervisorQueryDataBenchmark {
    private static final Map<String, List<Double>> multiListTimesSupervisorQuery = new HashMap<>(3);

    static synchronized Map<String, List<Double>> getMultiListTimes() {
        return multiListTimesSupervisorQuery;
    }

    static synchronized void putTimes(String key, @NotNull List<Double> times) {
        multiListTimesSupervisorQuery.put(key, times);
    }

    static synchronized void clear() {
        multiListTimesSupervisorQuery.clear();
    }
}
