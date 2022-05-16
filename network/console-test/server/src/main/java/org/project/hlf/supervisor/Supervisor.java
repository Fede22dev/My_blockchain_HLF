package org.project.hlf.supervisor;

import org.jetbrains.annotations.NotNull;
import org.project.models.MyRequest;
import org.project.models.MyResponse;

import java.io.IOException;

import static org.project.hlf.Utils.executeRequest;

public class Supervisor {
    public static @NotNull MyResponse payRent(@NotNull final MyRequest myRequest) throws IOException {
        return executeRequest(myRequest);
    }

    public static @NotNull MyResponse payDeposit(@NotNull final MyRequest myRequest) throws IOException {
        return executeRequest(myRequest);
    }

    public static @NotNull MyResponse payBill(@NotNull final MyRequest myRequest) throws IOException {
        return executeRequest(myRequest);
    }

    public static @NotNull MyResponse payCondominiumFees(@NotNull final MyRequest myRequest) throws IOException {
        return executeRequest(myRequest);
    }

    public static @NotNull MyResponse readAllPaymentType(@NotNull final MyRequest myRequest) throws IOException {
        return executeRequest(myRequest);
    }
}

