package org.project.supervisor;

import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.*;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;
import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

import static java.nio.charset.StandardCharsets.UTF_8;

@Contract(name = "HouseSupervisorContract",
        info = @Info(title = "House Supervisor Contract",
                description = "House Supervisor implementation",
                version = "0.0.1",
                license =
                @License(name = "Apache2.0"),
                contact = @Contact(email = "HouseSupervisor@example.com",
                        name = "House Supervisor",
                        url = "http://housesupervisor.org")))
@Default
public class HouseSupervisorContract implements ContractInterface {
    public HouseSupervisorContract() {
    }

    /**
     * Metodo utilizzato per effettuare i pagamenti degli affitti. Il prezzo
     * e' stato inserito pensando ad un precedente accordo fra landlord e tenant. Viene effettuato
     * un doppio controllo, prima per verificare se e' effettivamente un tenant o un guest, una seconda per
     * verificare se e' la prima volta che paga l'affitto quel mese, questo
     * onde evitare doppi pagamenti.
     *
     * @param ctx         context
     * @param paymentFrom nome del tenant
     * @param paymentTo   nome del landlord
     * @param rent        affito stabilito precendemente
     * @return stringa a console positiva in caso di succeso, negativa in caso di problemi
     */
    @Transaction()
    public String payRent(@NotNull Context ctx, String paymentFrom, String paymentTo, double rent) {
        if (ctx.getClientIdentity().getMSPID().contains("Tenant")) {
            String key = "RENT" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "-" + Calendar.getInstance().get(Calendar.YEAR);
            ChaincodeStub stub = ctx.getStub();
            byte[] buffer = stub.getState(key);
            if (!(buffer != null && buffer.length > 0)) {
                if (rent == 450) {

                    HouseSupervisor asset = new HouseSupervisor();
                    asset.setPaymentFrom(paymentFrom);
                    asset.setPaymentTo(paymentTo);
                    asset.setRent(rent);
                    asset.setTimestamp(new Timestamp(System.currentTimeMillis()).toString());

                    stub.putState(key, asset.toJSONString().getBytes(UTF_8));

                    return "Payment ok: " + key + " " + asset.toJSONString();
                } else {
                    return "Payment refused";
                }
            } else {
                return "Payment already exist";
            }
        } else {
            return "Access denied to this method by this type of account, only tenant can pay this";
        }
    }

    /**
     * Metodo utilizzato per effettuare i pagamenti delle caparra. Il prezzo
     * e' stato inserito pensando ad un precedente accordo fra landlord e tenant/guest. Viene effettuato
     * un doppio controllo, prima per verificare se e' effettivamente un tenant o un guest, una seconda per
     * verificare se e' la prima volta che paga la caparra, questo
     * onde evitare doppi pagamenti.
     *
     * @param ctx         context
     * @param paymentFrom nome del tenant/guest
     * @param paymentTo   nome del landlord
     * @param deposit     caparra stabilita precendemente
     * @return stringa a console positiva in caso di succeso, negativa in caso di problemi
     */
    @Transaction()
    public String payDeposit(@NotNull Context ctx, String paymentFrom, String paymentTo, double deposit) {
        if (ctx.getClientIdentity().getMSPID().contains("Tenant") || ctx.getClientIdentity().getMSPID().contains("Guest")) {
            String key = "DEPOSIT" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "-" + Calendar.getInstance().get(Calendar.YEAR);
            ChaincodeStub stub = ctx.getStub();
            byte[] buffer = stub.getState(key);
            if (!(buffer != null && buffer.length > 0)) {
                if (deposit == 100) {

                    HouseSupervisor asset = new HouseSupervisor();
                    asset.setPaymentFrom(paymentFrom);
                    asset.setPaymentTo(paymentTo);
                    asset.setDeposit(deposit);
                    asset.setTimestamp(new Timestamp(System.currentTimeMillis()).toString());

                    stub.putState(key, asset.toJSONString().getBytes(UTF_8));

                    return "Payment ok: " + key + " " + asset.toJSONString();
                } else {
                    return "Payment refused";
                }
            } else {
                return "Payment already exist";
            }
        } else {
            return "Access denied to this method by this type of account, only tenant can pay this";
        }
    }

    /**
     * Metodo utilizzato per effettuare i pagamenti delle bollette. Il prezzo
     * e' stato inserito pensando ad ipotetica cifra di una bolletta. Viene effettuato
     * un doppio controllo, prima per verificare se e' effettivamente un tenant, una seconda per
     * verificare se e' la prima volta che paga la bolletta (ipoteticamente una volta al mese), questo
     * onde evitare doppi pagamenti.
     *
     * @param ctx         context
     * @param paymentFrom nome del tenant
     * @param paymentTo   nome del landord
     * @param bills       bolletta stabilita ipoteticamente
     * @return stringa a console positiva in caso di succeso, negativa in caso di problemi
     */
    @Transaction()
    public String payBill(@NotNull Context ctx, String paymentFrom, String paymentTo, double bills) {
        if (ctx.getClientIdentity().getMSPID().contains("Tenant")) {
            String key = "BILL" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "-" + Calendar.getInstance().get(Calendar.YEAR);
            ChaincodeStub stub = ctx.getStub();
            byte[] buffer = stub.getState(key);
            if (!(buffer != null && buffer.length > 0)) {
                if (bills == 50) {

                    HouseSupervisor asset = new HouseSupervisor();
                    asset.setPaymentFrom(paymentFrom);
                    asset.setPaymentTo(paymentTo);
                    asset.setBills(bills);
                    asset.setTimestamp(new Timestamp(System.currentTimeMillis()).toString());

                    stub.putState(key, asset.toJSONString().getBytes(UTF_8));

                    return "Payment ok: " + key + " " + asset.toJSONString();
                } else {
                    return "Payment refused";
                }
            } else {
                return "Payment already exist";
            }
        } else {
            return "Access denied to this method by this type of account, only tenant can pay this";
        }
    }

    /**
     * Metodo utilizzato per effettuare i pagamenti delle spese condominiali. Il prezzo
     * è stato inserito pensando ad un precedente accordo fra landlord e tenant. Viene effettuato
     * un doppio controllo, prima per verificare se e' effettivamente un tenant, una seconda per
     * verificare se è la prima volta che paga la tassa (ipoteticamente una volta all'anno), questo
     * onde evitare doppi pagamenti.
     *
     * @param ctx             context
     * @param paymentFrom     tenant
     * @param paymentTo       landlord
     * @param condominiumFees quota precedentemente stabilita
     * @return stringa a console positiva in caso di succeso, negativa in caso di problemi
     */
    @Transaction()
    public String payCondominiumFees(@NotNull Context ctx, String paymentFrom, String paymentTo, double condominiumFees) {
        if (ctx.getClientIdentity().getMSPID().contains("Tenant")) {
            String key = "CONDUMINIUMFEES" + Calendar.getInstance().get(Calendar.YEAR);
            ChaincodeStub stub = ctx.getStub();
            byte[] buffer = stub.getState(key);
            if (!(buffer != null && buffer.length > 0)) {
                if (condominiumFees == 600) {

                    HouseSupervisor asset = new HouseSupervisor();
                    asset.setPaymentFrom(paymentFrom);
                    asset.setPaymentTo(paymentTo);
                    asset.setCondominiumFees(condominiumFees);
                    asset.setTimestamp(new Timestamp(System.currentTimeMillis()).toString());

                    stub.putState(key, asset.toJSONString().getBytes(UTF_8));

                    return "Payment ok: " + key + " " + asset.toJSONString();
                } else {
                    return "Payment refused";
                }
            } else {
                return "Payment already exist";
            }
        } else {
            return "Access denied to this method by this type of account, only tenant can pay this";
        }
    }

    /**
     * Metodo utilizzato per poter leggere all'interno del ledger i vari pagamenti presenti,
     * potendo filtrare in base ai tipi presenti (bolletta, affitto, caparra).
     *
     * @param ctx        context
     * @param type       tipo di pagamento (bolletta, affitto, caparra)
     * @param startMonth mese di inzio (come lasso di tempo)
     * @param startYear  anno di inizio (come lasso di tempo)
     * @param endMonth   mese di fine escluso (come lasso di tempo)
     * @param endYear    anno di fine escluso (come lasso di tempo)
     * @return json convertito a stringa con i pagamenti richiesti, stringa di errore in caso di errori.
     */
    @Transaction()
    public String readAllPaymentType(@NotNull Context ctx, @NotNull String type, String startMonth, String startYear, String endMonth, String endYear) {
        QueryResultsIterator<KeyValue> results;

        switch (type) {
            case "rents":
                results = ctx.getStub().getStateByRange("RENT" + startMonth + "-" + startYear, "RENT" + endMonth + "-" + endYear);
                break;
            case "bills":
                results = ctx.getStub().getStateByRange("BILL" + startMonth + "-" + startYear, "BILL" + endMonth + "-" + endYear);
                break;
            case "deposits":
                results = ctx.getStub().getStateByRange("DEPOSIT" + startMonth + "-" + startYear, "DEPOSIT" + endMonth + "-" + endYear);
                break;
            case "condominium fees":
                results = ctx.getStub().getStateByRange("CONDUMINIUMFEES" + startYear, "CONDUMINIUMFEES" + endYear);
                break;
            default:
                return "The inserted type is not accepted";
        }

        ArrayList<String> queryResults = new ArrayList<>();
        for (KeyValue result : results) {
            queryResults.add(result.getKey() + ":" + result.getStringValue());
        }

        return queryResults.toString();
    }

    @Transaction()
    public String invokeBenchmark(@NotNull Context ctx, int i) {
        String key = "BENCH" + i;

        HouseSupervisor asset = new HouseSupervisor();
        asset.setPaymentFrom("tenant");
        asset.setPaymentTo("landlord");
        asset.setRent(450);
        asset.setTimestamp(new Timestamp(System.currentTimeMillis()).toString());

        ctx.getStub().putState(key, asset.toJSONString().getBytes(UTF_8));

        return "Payment ok: " + key;
    }

    @Transaction
    public String queryBenchmark(@NotNull Context ctx, int i) {
        return ctx.getStub().getStringState("BENCH" + i);
    }
}
