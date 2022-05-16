package org.project;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ClientConstants {
    //Color string console
    public final static String ANSI_RESET = "\u001B[0m";
    public final static String ANSI_BLUE = "\u001B[34m";

    //Console input
    public final static BufferedReader CONSOLE = new BufferedReader(new InputStreamReader(System.in));

    //Typology of read supervisor
    public final static String[] TYPOLOGY = {"rents", "bills", "deposits", "condominium_fees"};

    //Port
    public final static String LANDLORD_PORT = "8801";
    public final static String TENANT_PORT = "8802";
    public final static String GUEST_PORT = "8803";
    public final static String SENSOR_PORT = "8804";
}
