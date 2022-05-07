package org.project.hlf.supervisor.benchmark.invoke;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class SupervisorInvokeDataBenchmark {
    private static final Map<String, List<Double>> multiListTimesSupervisorInvoke = new HashMap<>(3);

    static synchronized Map<String, List<Double>> getMultiListTimes() {
        return multiListTimesSupervisorInvoke;
    }

    static synchronized void putTimes(String key, @NotNull List<Double> times) {
        multiListTimesSupervisorInvoke.put(key, times);
    }

    static synchronized void clear() {
        multiListTimesSupervisorInvoke.clear();
    }
}
