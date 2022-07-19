package org.project.sensor;

import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;
import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

@Contract(name = "HouseSensorContract")
public class HouseSensorContract implements ContractInterface {
    @Transaction
    public String insertHouseWeather(@NotNull final Context ctx, final double temperature, final double humidity) {
        if (ctx.getClientIdentity().getMSPID().contains("Sensors")) {
            long time = System.currentTimeMillis();

            HouseSensorWeather asset = new HouseSensorWeather();
            asset.setTemperature(temperature);
            asset.setHumidity(humidity);
            asset.setTimeStamp(new Timestamp(time).toString());

            String key = "WEATHER" + time;
            ctx.getStub().putState(key, asset.toJSONString().getBytes(UTF_8));
            return "Insert data ok: " + key + " " + asset.toJSONString();
        } else {
            return "Access denied to this method by this type of account";
        }
    }

    @Transaction
    public String insertHouseElectricity(@NotNull final Context ctx, final double useElectricity, final double washingMachineKw, final double fridgeKw, final double dishwasherKw) {
        if (ctx.getClientIdentity().getMSPID().contains("Sensors")) {
            long time = System.currentTimeMillis();

            HouseSensorElectricity asset = new HouseSensorElectricity();
            asset.setUseElectricity(useElectricity);
            asset.setWashingMachineKw(washingMachineKw);
            asset.setFridgeKw(fridgeKw);
            asset.setDishwasherKw(dishwasherKw);
            asset.setTimeStamp(new Timestamp(time).toString());

            String key = "ELECTRICITY" + time;
            ctx.getStub().putState(key, asset.toJSONString().getBytes(UTF_8));
            return "Insert data ok: " + key + " " + asset.toJSONString();
        } else {
            return "Access denied to this method by this type of account";
        }
    }

    @Transaction
    public String readAllHouseWeather(@NotNull final Context ctx, final long startMillis, final long endMillis) {
        if (ctx.getClientIdentity().getMSPID().contains("Tenant")) {
            QueryResultsIterator<KeyValue> results = ctx.getStub().getStateByRange("WEATHER" + startMillis, "WEATHER" + endMillis);
            List<String> queryResults = new ArrayList<>();
            for (KeyValue result : results) {
                queryResults.add(result.getKey() + ":" + result.getStringValue());
            }

            return queryResults.toString();
        } else {
            return "Access denied to this method by this type of account";
        }
    }

    @Transaction
    public String readAllHouseElectricity(@NotNull final Context ctx, final long startMillis, final long endMillis) {
        if (ctx.getClientIdentity().getMSPID().contains("Tenant")) {
            QueryResultsIterator<KeyValue> results = ctx.getStub().getStateByRange("ELECTRICITY" + startMillis, "ELECTRICITY" + endMillis);
            List<String> queryResults = new ArrayList<>();
            for (KeyValue result : results) {
                queryResults.add(result.getKey() + ":" + result.getStringValue());
            }

            return queryResults.toString();
        } else {
            return "Access denied to this method by this type of account";
        }
    }
}
