package org.project.supervisor;

import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;
import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

@Contract(name = "HouseSupervisorContract")
public class HouseSupervisorContract implements ContractInterface {
    private final static int VALUE_RENT = 450;
    private final static int VALUE_DEPOSIT = 100;
    private final static int VALUE_BILLS = 50;
    private final static int VALUE_CONDOMINIUM_FEES = 600;

    @Transaction()
    public String payRent(@NotNull final Context ctx, final String paymentFrom, final String paymentTo, final double rent) {
        if (!ctx.getClientIdentity().getMSPID().contains("Tenant")) {
            return "Access denied to this method by this type of account, only tenant can pay this";
        }

        String key = "RENT" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "-" + Calendar.getInstance().get(Calendar.YEAR);
        ChaincodeStub stub = ctx.getStub();
        byte[] buffer = stub.getState(key);
        if (buffer != null && buffer.length > 0) {
            return "Payment already exist";
        }

        if (rent != VALUE_RENT) {
            return "Payment refused, the amount is incorrect";
        }

        HouseSupervisor asset = new HouseSupervisor();
        asset.setPaymentFrom(paymentFrom);
        asset.setPaymentTo(paymentTo);
        asset.setRent(rent);
        asset.setTimestamp(new Timestamp(System.currentTimeMillis()).toString());

        stub.putState(key, asset.toJSONString().getBytes(UTF_8));
        return "Payment ok: " + key + " " + asset.toJSONString();
    }

    @Transaction()
    public String payDeposit(@NotNull final Context ctx, final String paymentFrom, final String paymentTo, final double deposit) {
        if (!(ctx.getClientIdentity().getMSPID().contains("Tenant") || ctx.getClientIdentity().getMSPID().contains("Guest"))) {
            return "Access denied to this method by this type of account, only tenant or guest can pay this";
        }

        String key = "DEPOSIT" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "-" + Calendar.getInstance().get(Calendar.YEAR);
        ChaincodeStub stub = ctx.getStub();
        byte[] buffer = stub.getState(key);
        if (buffer != null && buffer.length > 0) {
            return "Payment already exist";
        }

        if (deposit != VALUE_DEPOSIT) {
            return "Payment refused, the amount is incorrect";
        }

        HouseSupervisor asset = new HouseSupervisor();
        asset.setPaymentFrom(paymentFrom);
        asset.setPaymentTo(paymentTo);
        asset.setDeposit(deposit);
        asset.setTimestamp(new Timestamp(System.currentTimeMillis()).toString());

        stub.putState(key, asset.toJSONString().getBytes(UTF_8));
        return "Payment ok: " + key + " " + asset.toJSONString();
    }

    @Transaction()
    public String payBills(@NotNull final Context ctx, final String paymentFrom, final String paymentTo, final double bills) {
        if (!ctx.getClientIdentity().getMSPID().contains("Tenant")) {
            return "Access denied to this method by this type of account, only tenant can pay this";
        }

        String key = "BILL" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "-" + Calendar.getInstance().get(Calendar.YEAR);
        ChaincodeStub stub = ctx.getStub();
        byte[] buffer = stub.getState(key);
        if (buffer != null && buffer.length > 0) {
            return "Payment already exist";
        }

        if (bills != VALUE_BILLS) {
            return "Payment refused, the amount is incorrect";
        }

        HouseSupervisor asset = new HouseSupervisor();
        asset.setPaymentFrom(paymentFrom);
        asset.setPaymentTo(paymentTo);
        asset.setBills(bills);
        asset.setTimestamp(new Timestamp(System.currentTimeMillis()).toString());

        stub.putState(key, asset.toJSONString().getBytes(UTF_8));
        return "Payment ok: " + key + " " + asset.toJSONString();
    }

    @Transaction()
    public String payCondominiumFees(@NotNull final Context ctx, final String paymentFrom, final String paymentTo, final double condominiumFees) {
        if (!ctx.getClientIdentity().getMSPID().contains("Tenant")) {
            return "Access denied to this method by this type of account, only tenant can pay this";
        }

        String key = "CONDOMINIUMFEES" + Calendar.getInstance().get(Calendar.YEAR);
        ChaincodeStub stub = ctx.getStub();
        byte[] buffer = stub.getState(key);
        if (buffer != null && buffer.length > 0) {
            return "Payment already exist";
        }

        if (condominiumFees != VALUE_CONDOMINIUM_FEES) {
            return "Payment refused, the amount is incorrect";
        }

        HouseSupervisor asset = new HouseSupervisor();
        asset.setPaymentFrom(paymentFrom);
        asset.setPaymentTo(paymentTo);
        asset.setCondominiumFees(condominiumFees);
        asset.setTimestamp(new Timestamp(System.currentTimeMillis()).toString());

        stub.putState(key, asset.toJSONString().getBytes(UTF_8));
        return "Payment ok: " + key + " " + asset.toJSONString();
    }

    @Transaction()
    public String readAllPaymentType(@NotNull final Context ctx, @NotNull final String type, final String startMonth, final String startYear, final String endMonth, final String endYear) {
        QueryResultsIterator<KeyValue> results;
        switch (type) {
            case "rents":
                if (!(ctx.getClientIdentity().getMSPID().contains("Landlord") || ctx.getClientIdentity().getMSPID().contains("Tenant"))) {
                    return "Access denied to this method by this type of account, only landlord or tenant can read this";
                }

                results = ctx.getStub().getStateByRange("RENT" + startMonth + "-" + startYear, "RENT" + endMonth + "-" + endYear);
                break;
            case "bills":
                if (!ctx.getClientIdentity().getMSPID().contains("Tenant")) {
                    return "Access denied to this method by this type of account, only tenant can read this";
                }

                results = ctx.getStub().getStateByRange("BILL" + startMonth + "-" + startYear, "BILL" + endMonth + "-" + endYear);
                break;
            case "deposits":
                if (ctx.getClientIdentity().getMSPID().contains("Sensors")) {
                    return "Access denied to this method by this type of account, only landlord, tenant or guest can read this";
                }

                results = ctx.getStub().getStateByRange("DEPOSIT" + startMonth + "-" + startYear, "DEPOSIT" + endMonth + "-" + endYear);
                break;
            case "condominium_fees":
                if (!ctx.getClientIdentity().getMSPID().contains("Tenant")) {
                    return "Access denied to this method by this type of account, only tenant can read this";
                }

                results = ctx.getStub().getStateByRange("CONDOMINIUMFEES" + startYear, "CONDOMINIUMFEES" + endYear);
                break;
            default:
                return "The type entered does not exist";
        }

        List<String> queryResults = new ArrayList<>();
        for (KeyValue result : results) {
            queryResults.add(result.getKey() + ":" + result.getStringValue());
        }

        return queryResults.toString();
    }

    @Transaction()
    public String invokeBenchmark(@NotNull final Context ctx) {
        HouseSupervisor asset = new HouseSupervisor();
        asset.setPaymentFrom("from_bench");
        asset.setPaymentTo("to_bench");
        asset.setRent(111);
        asset.setTimestamp(new Timestamp(System.currentTimeMillis()).toString());

        String key = "BENCH" + System.currentTimeMillis();
        ctx.getStub().putState(key, asset.toJSONString().getBytes(UTF_8));
        return "Payment ok: " + key;
    }

    @Transaction
    public String queryBenchmark(@NotNull final Context ctx, final long startMillis, final long endMillis) {
        QueryResultsIterator<KeyValue> results = ctx.getStub().getStateByRange("BENCH" + startMillis, "BENCH" + endMillis);
        List<String> queryResults = new ArrayList<>();
        for (KeyValue result : results) {
            queryResults.add(result.getKey() + ":" + result.getStringValue());
        }

        return queryResults.toString();
    }
}
