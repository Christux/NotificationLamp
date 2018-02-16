package fr.christux.notificationlamp;

public class Config
{
    private final static Config instance = new Config();;

    String deviceAddress;
    boolean autoConnect = false;
    boolean manualDisconnection = false;
    public int animation = 1;
    public RgbColor color = RgbColor.red;

    public static Config getInstance() { return instance; }

    private Config()
    {}
}
