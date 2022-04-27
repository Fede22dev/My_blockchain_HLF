package org.project.hlf.supervisor.benchmark.query;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

class SupervisorQueryDataBenchmark {
    private static final HashMap<String, ArrayList<Double>> multiListTimesSupervisorQuery = new HashMap<>(3);

    static synchronized @NotNull ArrayList<Double> getMultiListTimes() {
        ArrayList<Double> totals = new ArrayList<>();
        multiListTimesSupervisorQuery.forEach((key, value) ->
                totals.addAll(value)
        );
        return totals;
    }

    static synchronized void putTimes(String key, @NotNull ArrayList<Double> times) {
        multiListTimesSupervisorQuery.put(key, times);
    }

    static synchronized void clear() {
        multiListTimesSupervisorQuery.clear();
    }
}
