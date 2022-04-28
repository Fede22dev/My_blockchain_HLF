package org.project.hlf.supervisor;

import org.jetbrains.annotations.NotNull;
import org.project.models.MyRequest;
import org.project.models.MyResponse;

import java.io.IOException;

import static org.project.hlf.Utils.execRequest;

public class Supervisor {
    public static @NotNull MyResponse payRent(@NotNull MyRequest myRequest) throws IOException {
        return execRequest(myRequest);
    }

    public static @NotNull MyResponse payDeposit(@NotNull MyRequest myRequest) throws IOException {
        return execRequest(myRequest);
    }

    public static @NotNull MyResponse payBill(@NotNull MyRequest myRequest) throws IOException {
        return execRequest(myRequest);
    }

    public static @NotNull MyResponse payCondominiumFees(@NotNull MyRequest myRequest) throws IOException {
        return execRequest(myRequest);
    }

    public static @NotNull MyResponse readAllPaymentType(@NotNull MyRequest myRequest) throws IOException {
        return execRequest(myRequest);
    }
}

