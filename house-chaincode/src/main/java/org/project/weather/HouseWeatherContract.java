package org.project.weather;

import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.*;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;
import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Objects;

import static java.nio.charset.StandardCharsets.UTF_8;

@Contract(name = "HouseWeatherContract",
        info = @Info(title = "House Weather Contract",
                description = "House Weather implementation",
                version = "0.0.1",
                license =
                @License(name = "Apache2.0"),
                contact = @Contact(email = "HouseWeather@example.com",
                        name = "House Weather",
                        url = "http://houseweather.org")))
@Default
public class HouseWeatherContract implements ContractInterface {
    public HouseWeatherContract() {
    }

    /**
     * Metodo utilizzato per inserire i dati rilevati ippoteticamente da un sensore
     * collegato al network. Viene inizialmente fatto un controllo per verificare che
     * la chiamata venga fatta da un organizzazione che gestisce sensori.
     *
     * @param ctx            context
     * @param temperature    temperature
     * @param humidity       humidity
     * @param electricityUse electricityUse
     * @return stringa a console positiva in caso di succeso, negativa in caso di problemi
     */
    @Transaction
    public String insertHouseData(@NotNull Context ctx, double temperature, double humidity, double electricityUse) {
        if (Objects.equals(ctx.getClientIdentity().getMSPID(), "Sensors1MSP")) {
            long time = System.currentTimeMillis();
            String key = "DATA" + time;

            HouseWeather asset = new HouseWeather();
            asset.setTemperature(temperature);
            asset.setHumidity(humidity);
            asset.setElectricityUse(electricityUse);
            asset.setTimeStamp(new Timestamp(time).toString());

            ctx.getStub().putState(key, asset.toJSONString().getBytes(UTF_8));

            return "Insert data ok: " + key + " " + asset.toJSONString();
        } else {
            return "Access denied to this method by this type of account";
        }
    }

    /**
     * Metodo utilizzato per leggere i dati inseriti dal sensore in un lasso di tempo
     * specificato dall'utente. Solo l'affittuario può vedere i consumi.
     *
     * @param ctx         context
     * @param startMillis data di inizio
     * @param endMillis   data di fine
     * @return dati richiesti in caso di riuscita, stringa negativa in caso di problemi
     */
    @Transaction
    public String readAllHouseData(@NotNull Context ctx, long startMillis, long endMillis) {
        if (Objects.equals(ctx.getClientIdentity().getMSPID(), "Tenant1MSP")) {
            QueryResultsIterator<KeyValue> results = ctx.getStub().getStateByRange("DATA" + startMillis, "DATA" + endMillis);
            ArrayList<String> queryResults = new ArrayList<>();
            for (KeyValue result : results) {
                queryResults.add(result.getKey() + ":" + result.getStringValue());
            }

            return queryResults.toString();
        } else {
            return "Access denied to this method by this type of account";
        }
    }
}