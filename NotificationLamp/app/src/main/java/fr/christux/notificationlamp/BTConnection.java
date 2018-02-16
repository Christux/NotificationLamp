package fr.christux.notificationlamp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;


public class BTConnection
{
    private boolean ConnectSuccess = false; //if it's here, it's almost connected
    private boolean isBtConnected = false;

    private BluetoothAdapter myBluetooth = null;
    private BluetoothSocket btSocket = null;

    private String address = null;
    //SPP UUID. Look for it
    private final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private List<IBTActivity> observers = new LinkedList<IBTActivity>();

    // Singleton
    private BTConnection()
    { }

    private static BTConnection instance = null;

    public static BTConnection getInstance()
    {
        if(instance == null) {
            instance = new BTConnection();
        }
        return instance;
    }


    public void setAddress(String a) {
        address = a;
    }

    public boolean isConnected() {
        return isBtConnected;
    }

    public void Connect() {

        ConnectSuccess = true;

        try {
            if (btSocket == null || !isBtConnected) {
                myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                BluetoothDevice device = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                btSocket = device.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                btSocket.connect();//start connection
            }
        } catch (IOException e) {
            ConnectSuccess = false;//if the try failed, you can check the exception here
        }

        if (ConnectSuccess) {
            isBtConnected = true;
        }
    }

    public void Reconnect() {

    }

    // Observer methods

    public void register(IBTActivity observer) {
        observers.add(observer);
    }

    public void unregister(IBTActivity observer) {
        observers.remove(observer);
    }

    private void msg(String mes) {
        Iterator<IBTActivity> it = observers.iterator();

        while(it.hasNext()){
            IBTActivity obs = it.next();
            obs.msg(mes);
        }
    }

    private void finish() {
        Iterator<IBTActivity> it = observers.iterator();

        while(it.hasNext()){
            IBTActivity obs = it.next();
            obs.finish();
        }
    }

    private void stop() {

        if (btSocket!=null) //If the btSocket is busy
        {
            try
            {
                btSocket.close(); //close connection
            }
            catch (IOException e)
            {
                msg("Error: "+e.getMessage());
            }
            finally {
                isBtConnected = false;
                myBluetooth = null;
                btSocket = null;
            }
        }
    }

    public void Disconnect()
    {
        stop();
        finish(); //return to the first layout
    }

    public void writeOnDevice(String msg) {

        if(btSocket == null)
        {
            Reconnect();
        }

        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write(msg.getBytes());
            }
            catch (IOException e)
            {
                msg("Error "+e.getMessage());
            }
        }
    }
}