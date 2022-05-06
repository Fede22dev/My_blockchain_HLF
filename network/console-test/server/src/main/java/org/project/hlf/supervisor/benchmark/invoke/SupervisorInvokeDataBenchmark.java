package org.project.hlf.supervisor.benchmark.invoke;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

class SupervisorInvokeDataBenchmark {
    private static final HashMap<String, List<Double>> multiListTimesSupervisorInvoke = new HashMap<>(3);

    static synchronized HashMap<String, List<Double>> getMultiListTimes() {
        return multiListTimesSupervisorInvoke;
    }

    static synchronized void putTimes(String key, @NotNull List<Double> times) {
        multiListTimesSupervisorInvoke.put(key, times);
    }

    static synchronized void clear() {
        multiListTimesSupervisorInvoke.clear();
    }
}
