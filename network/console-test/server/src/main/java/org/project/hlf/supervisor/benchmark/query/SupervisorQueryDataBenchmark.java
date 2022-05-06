package org.project.hlf.supervisor.benchmark.query;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

class SupervisorQueryDataBenchmark {
    private static final HashMap<String, List<Double>> multiListTimesSupervisorQuery = new HashMap<>(3);

    static synchronized HashMap<String, List<Double>> getMultiListTimes() {
        return multiListTimesSupervisorQuery;
    }

    static synchronized void putTimes(String key, @NotNull List<Double> times) {
        multiListTimesSupervisorQuery.put(key, times);
    }

    static synchronized void clear() {
        multiListTimesSupervisorQuery.clear();
    }
}
