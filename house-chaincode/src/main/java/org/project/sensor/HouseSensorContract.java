package org.project.sensor;

import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.*;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;
import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Smart contract che gestisce i sensori presenti in una casa
 */
@Contract(name = "HouseSensorContract",
        info = @Info(title = "House Sensor Contract",
                description = "House Sensor implementation",
                version = "0.0.1",
                license =
                @License(name = "Apache2.0"),
                contact = @Contact(email = "HouseSensor@example.com",
                        name = "House Sensor",
                        url = "https://housesensor.org")))
@Default
public class HouseSensorContract implements ContractInterface {
    /**
     * Metodo utilizzato per inserire i dati rilevati ipoteticamente da un sensore
     * interno (rileva dati metereologici) collegato al network. Viene inizialmente
     * fatto un controllo per verificare che la chiamata venga fatta da un organizzazione
     * che gestisce sensori.
     *
     * @param ctx         context
     * @param temperature temperature
     * @param humidity    humidity
     * @return stringa a console positiva in caso di succeso, negativa in caso di problemi
     */
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

    /**
     * Metodo utilizzato per inserire i dati rilevati ippoteticamente da un sensore
     * interno (rileva dati sui consumi energetici) collegato al network. Viene inizialmente
     * fatto un controllo per verificare che la chiamata venga fatta da un organizzazione
     * che gestisce sensori.
     *
     * @param ctx              context
     * @param useElectricity   elettricità usata
     * @param washingMachineKw kw usati dalla lavatrice
     * @param fridgeKw         kw usati dal frigorifero
     * @param dishwasherKw     kw usati dalla lavastoviglie
     * @return stringa a console positiva in caso di succeso, negativa in caso di problemi
     */
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

    /**
     * Metodo utilizzato per leggere i dati inseriti dai sensori interni metereologici
     * in un lasso di tempo specificato dall'utente. Solo l'affittuario può vedere i consumi.
     *
     * @param ctx         context
     * @param startMillis data di inizio
     * @param endMillis   data di fine
     * @return dati richiesti in caso di riuscita, stringa negativa in caso di problemi
     */
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

    /**
     * Metodo utilizzato per leggere i dati inseriti dai sensori che rilevano i consumi energetici
     * in un lasso di tempo specificato dall'utente. Solo l'affittuario può vedere i consumi.
     *
     * @param ctx         context
     * @param startMillis data di inizio
     * @param endMillis   data di fine
     * @return dati richiesti in caso di riuscita, stringa negativa in caso di problemi
     */
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
