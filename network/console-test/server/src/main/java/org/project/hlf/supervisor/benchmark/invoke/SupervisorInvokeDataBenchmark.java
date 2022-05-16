package org.project.hlf.supervisor.benchmark.invoke;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class SupervisorInvokeDataBenchmark {
    private static final Map<String, List<Double>> MULTI_LIST_TIMES_SUPERVISOR_INVOKE = new HashMap<>(3);

    static synchronized Map<String, List<Double>> getMultiListTimes() {
        return MULTI_LIST_TIMES_SUPERVISOR_INVOKE;
    }

    static synchronized void putTimes(final String key, @NotNull final List<Double> times) {
        MULTI_LIST_TIMES_SUPERVISOR_INVOKE.put(key, times);
    }

    static synchronized void clear() {
        MULTI_LIST_TIMES_SUPERVISOR_INVOKE.clear();
    }
}
