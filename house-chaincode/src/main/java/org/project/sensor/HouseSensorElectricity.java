package org.project.sensor;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import org.json.JSONObject;

@DataType()
public class HouseSensorElectricity {
    @Property()
    private double useElectricity;

    @Property()
    private double washingMachineKw;

    @Property()
    private double fridgeKw;

    @Property()
    private double dishwasherKw;

    @Property()
    private String timeStamp;

    public HouseSensorElectricity() {
    }

    public double getUseElectricity() {
        return useElectricity;
    }

    public void setUseElectricity(double useElectricity) {
        this.useElectricity = useElectricity;
    }

    public double getWashingMachineKw() {
        return washingMachineKw;
    }

    public void setWashingMachineKw(double washingMachineKw) {
        this.washingMachineKw = washingMachineKw;
    }

    public double getFridgeKw() {
        return fridgeKw;
    }

    public void setFridgeKw(double fridgeKw) {
        this.fridgeKw = fridgeKw;
    }

    public double getDishwasherKw() {
        return dishwasherKw;
    }

    public void setDishwasherKw(double dishwasherKw) {
        this.dishwasherKw = dishwasherKw;
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
