package org.project.hlf.supervisor;

import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.jetbrains.annotations.NotNull;
import org.project.models.MyResponse;
import org.project.server.ServerImpl;

import java.io.IOException;

import static org.project.server.ServerImpl.OBL;

public class Supervisor {
    public static @NotNull MyResponse payRent(@NotNull Request request, String port) throws IOException {
        request.setHeader("Authorization", "Bearer " + ServerImpl.getToken(port));
        long startTime = System.nanoTime();
        Response response = request.execute();
        long endTime = System.nanoTime();
        return new MyResponse(response, ((double) (endTime - startTime) / OBL));
    }

    public static @NotNull MyResponse payDeposit(@NotNull Request request, String port) throws IOException {
        request.setHeader("Authorization", "Bearer " + ServerImpl.getToken(port));
        long startTime = System.nanoTime();
        Response response = request.execute();
        long endTime = System.nanoTime();
        return new MyResponse(response, ((double) (endTime - startTime) / OBL));
    }

    public static @NotNull MyResponse payBill(@NotNull Request request, String port) throws IOException {
        request.setHeader("Authorization", "Bearer " + ServerImpl.getToken(port));
        long startTime = System.nanoTime();
        Response response = request.execute();
        long endTime = System.nanoTime();
        return new MyResponse(response, ((double) (endTime - startTime) / OBL));
    }

    public static @NotNull MyResponse payCondominiumFees(@NotNull Request request, String port) throws IOException {
        request.setHeader("Authorization", "Bearer " + ServerImpl.getToken(port));
        long startTime = System.nanoTime();
        Response response = request.execute();
        long endTime = System.nanoTime();
        return new MyResponse(response, ((double) (endTime - startTime) / OBL));
    }

    public static @NotNull MyResponse readAllPaymentType(@NotNull Request request, String port) throws IOException {
        request.setHeader("Authorization", "Bearer " + ServerImpl.getToken(port));
        long startTime = System.nanoTime();
        Response response = request.execute();
        long endTime = System.nanoTime();
        return new MyResponse(response, ((double) (endTime - startTime) / OBL));
    }
}

