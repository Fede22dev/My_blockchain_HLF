/*
 * SPDX-License-Identifier: Apache2.0
 */

package org.project;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

/**
 * In questa classe sono presente le informazioni necessarie alla creazione
 * della transazione e del blocco stesso all'interno del network. I parametri
 * vengono poi utilizzati in HouseSupervisor
 */
@DataType()
public class HouseSupervisor {

    @Property()
    private String paymentFrom;

    @Property()
    private String paymentTo;

    @Property()
    private double rent;

    @Property()
    private double deposit;

    @Property()
    private double bills;

    @Property()
    private double condominiumFees;

    @Property()
    private String timestamp;

    public HouseSupervisor() {
        rent = 0;
        deposit = 0;
        bills = 0;
        condominiumFees = 0;
    }

    public static @NotNull HouseSupervisor fromJSONString(String json) {
        JSONObject jsonObject = new JSONObject(json);
        String paymentFrom = jsonObject.getString("paymentFrom");
        String paymentTo = jsonObject.getString("paymentTo");
        double rent = jsonObject.getDouble("rent");
        double deposit = jsonObject.getDouble("deposit");
        double bills = jsonObject.getDouble("bills");
        double condominiumFees = jsonObject.getDouble("condominiumFees");
        String timestamp = jsonObject.getString("timestamp");
        HouseSupervisor asset = new HouseSupervisor();
        asset.setPaymentFrom(paymentFrom);
        asset.setPaymentTo(paymentTo);
        asset.setRent(rent);
        asset.setDeposit(deposit);
        asset.setBills(bills);
        asset.setCondominiumFees(condominiumFees);
        asset.setTimestamp(timestamp);
        return asset;
    }

    public String getPaymentFrom() {
        return paymentFrom;
    }

    public void setPaymentFrom(String paymentFrom) {
        this.paymentFrom = paymentFrom;
    }

    public String getPaymentTo() {
        return paymentTo;
    }

    public void setPaymentTo(String paymentTo) {
        this.paymentTo = paymentTo;
    }

    public double getRent() {
        return rent;
    }

    public void setRent(double rent) {
        this.rent = rent;
    }

    public double getDeposit() {
        return deposit;
    }

    public void setDeposit(double deposit) {
        this.deposit = deposit;
    }

    public double getBills() {
        return bills;
    }

    public void setBills(double bills) {
        this.bills = bills;
    }

    public double getCondominiumFees() {
        return condominiumFees;
    }

    public void setCondominiumFees(double condominiumFees) {
        this.condominiumFees = condominiumFees;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String toJSONString() {
        return new JSONObject(this).toString();
    }
}
