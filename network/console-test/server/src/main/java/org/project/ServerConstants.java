package org.project;

public class ServerConstants {
    //Color string console
    public final static String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\033[0;35m";
    public final static String ANSI_RESET = "\u001B[0m";

    //Number
    public final static long ONE_THOUSAND = 1_000;
    public final static long ONE_BILION = 1_000_000_000;

    //Port
    public final static String LANDLORD_PORT = "8801";
    public final static String TENANT_PORT = "8802";
    public final static String GUEST_PORT = "8803";
    public final static String SENSOR_PORT = "8804";

    //Parameter for timer enroll
    public final static long MINUTE_TIMER_ENROLL = 60 * 9;

    //Parameter for sensor
    //Weather
    public final static long MIN_TEMPERATURE = 10;
    public final static long MAX_TEMPERATURE = 35;
    public final static long MIN_HUMIDITY = 5;
    public final static long MAX_HUMIDITY = 100;
    //Electricity
    public final static long MIN_ABSORPTION_KW = 0;
    public final static long MAX_ABSORPTION_KW = 4;
    //Sensor
    public final static long SECONDS_INSERT_DATA_SENSOR = 60 * 5;
    public final static long SECONDS_DELAY_START_SENSOR = 15;

    //Parameter for benchmark
    public final static int NUMBER_OF_PROCESS = 3; //This cannot be changed
    public final static int SIZE_THREAD_POOL = 5;
    public final static long SECONDS_DURATION_BENCHMARK = 60 * 2;
    public final static long MILLIS_RATE_REQUEST_BENCHMARK = 250; // 500 = 2 transaction per second
}
