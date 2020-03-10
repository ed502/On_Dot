package kr.ac.kpu.ondot.DB;

public class ConnectDB {

    private static ConnectDB instance = new ConnectDB();

    public static ConnectDB getInstance() {
        return instance;
    }

    public ConnectDB() {}



}
