package org.project.weather;

import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.*;
import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;

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

    @Transaction
    public String insertHouseData(@NotNull Context ctx, double temperature, double humidity, double electricityUse) {
        long time = System.currentTimeMillis();
        String key = "DATA" + time;

        HouseWeather asset = new HouseWeather();
        asset.setTemperature(temperature);
        asset.setHumidity(humidity);
        asset.setElectricityUse(electricityUse);
        asset.setTimeStamp(new Timestamp(time).toString());

        ctx.getStub().putState(key, asset.toJSONString().getBytes(UTF_8));

        return "Insert data ok: " + key + " " + asset.toJSONString();
    }
}
