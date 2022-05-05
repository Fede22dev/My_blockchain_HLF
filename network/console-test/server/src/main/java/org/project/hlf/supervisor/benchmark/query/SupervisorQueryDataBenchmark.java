package org.project.hlf.supervisor.benchmark.query;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

class SupervisorQueryDataBenchmark {
    private static final HashMap<String, ArrayList<Double>> multiListTimesSupervisorQuery = new HashMap<>(3);

    static synchronized HashMap<String, ArrayList<Double>> getMultiListTimes() {
        return multiListTimesSupervisorQuery;
    }

    static synchronized void putTimes(String key, @NotNull ArrayList<Double> times) {
        multiListTimesSupervisorQuery.put(key, times);
    }

    static synchronized void clear() {
        multiListTimesSupervisorQuery.clear();
    }
}
