package org.project.weather;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

@DataType()
public class HouseWeather {

    @Property()
    private double temperature;

    @Property()
    private double humidity;

    @Property()
    private double electricityUse;

    @Property()
    private String timeStamp;

    public HouseWeather() {
    }

    public static @NotNull HouseWeather fromJSONString(String json) {
        JSONObject jsonObject = new JSONObject(json);
        double temperature = jsonObject.getDouble("temperature");
        double humidity = jsonObject.getDouble("humidity");
        double electricityUse = jsonObject.getDouble("electricityUse");
        String timeStamp = jsonObject.getString("timeStamp");
        HouseWeather asset = new HouseWeather();
        asset.setTemperature(temperature);
        asset.setHumidity(humidity);
        asset.setElectricityUse(electricityUse);
        asset.setTimeStamp(timeStamp);
        return asset;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getElectricityUse() {
        return electricityUse;
    }

    public void setElectricityUse(double electricityUse) {
        this.electricityUse = electricityUse;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String toJSONString() {
        return new JSONObject(this).toString();
    }
}
