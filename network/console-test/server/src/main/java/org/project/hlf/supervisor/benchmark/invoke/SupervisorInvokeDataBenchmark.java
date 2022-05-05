package org.project.hlf.supervisor.benchmark.invoke;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

class SupervisorInvokeDataBenchmark {
    private static final HashMap<String, ArrayList<Double>> multiListTimesSupervisorInvoke = new HashMap<>(3);

    static synchronized HashMap<String, ArrayList<Double>> getMultiListTimes() {
        return multiListTimesSupervisorInvoke;
    }

    static synchronized void putTimes(String key, @NotNull ArrayList<Double> times) {
        multiListTimesSupervisorInvoke.put(key, times);
    }

    static synchronized void clear() {
        multiListTimesSupervisorInvoke.clear();
    }
}
