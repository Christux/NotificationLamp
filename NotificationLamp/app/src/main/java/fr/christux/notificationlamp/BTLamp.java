package fr.christux.notificationlamp;

import android.util.Log;

public class BTLamp {

    private static BTLamp instance = null;

    private BTLamp()
    { }

    public static BTLamp getInstance()
    {
        if(instance == null)
            instance = new BTLamp();

        return instance;
    }

    private void sendToDevice(int animation, int R, int G, int B)
    {
        String message = "<"
                + String.format("%1d", animation) + ","
                + String.format("%03d", R) + ","
                + String.format("%03d", G) + ","
                + String.format("%03d", B) + ">";

        Log.d("BTsend",message);
        BTConnection.getInstance().writeOnDevice(message);
    }

    public void sendToDevice(int animation, RgbColor color, boolean saveState)
    {
        if(saveState) {
            Config.getInstance().animation = animation;
            Config.getInstance().color = color;
        }

        sendToDevice(animation, color.R, color.G, color.B);
    }

    public void sendToDevice(int animation)
    {
        sendToDevice(animation, Config.getInstance().color, true);
    }

    public void sendToDevice(RgbColor color)
    {
        sendToDevice(Config.getInstance().animation, color, true);
    }

    public void turnOffLed()
    {
        sendToDevice(BTLampAnimation.OFF, RgbColor.blank,false);
    }

    public void turnOnLed()
    {
        sendToDevice(Config.getInstance().animation, Config.getInstance().color, false);
    }

}
