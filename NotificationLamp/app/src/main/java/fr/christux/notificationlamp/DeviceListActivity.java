package fr.christux.notificationlamp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class DeviceListActivity extends AppCompatActivity {

    private ListView deviceList;

    //Bluetooth
    private BluetoothAdapter myBluetooth = null;
    private Set<BluetoothDevice> pairedDevices;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);

        deviceList = findViewById(R.id.listView);
        myBluetooth = BluetoothAdapter.getDefaultAdapter();

        if(myBluetooth == null)
        {
            Toast.makeText(getApplicationContext(), "Bluetooth Device Not Available", Toast.LENGTH_LONG).show();

            finish();
        }
        else if(!myBluetooth.isEnabled())
        {
            Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnBTon,1);
        }

        SharedPreferences sharedPref = DeviceListActivity.this.getPreferences(Context.MODE_PRIVATE);
        Config.getInstance().deviceAddress = sharedPref.getString("device_address",null);
        Config.getInstance().autoConnect = sharedPref.getBoolean("auto_connect",false);
    }

    @Override
    protected void onStart() {
        super.onStart();

        pairedDevicesList();
    }

    @Override
    protected void onStop() {

        SharedPreferences sharedPref = DeviceListActivity.this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("device_address", Config.getInstance().deviceAddress);
        editor.putBoolean("auto_connect", Config.getInstance().autoConnect);
        editor.commit();

        super.onStop();
    }

    private void pairedDevicesList()
    {
        pairedDevices = myBluetooth.getBondedDevices();
        ArrayList<String> list = new ArrayList<>();

        if (pairedDevices.size()>0)
        {
            for(BluetoothDevice bt : pairedDevices)
            {
                list.add(bt.getName() + "\n" + bt.getAddress());

                if(Config.getInstance().autoConnect &&
                        !Config.getInstance().manualDisconnection &&
                        bt.getAddress().compareTo(Config.getInstance().deviceAddress)==0)
                {
                    goToNextActivity();
                }
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "No Paired Bluetooth Devices Found.", Toast.LENGTH_LONG).show();
        }

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, list);
        deviceList.setAdapter(adapter);
        deviceList.setOnItemClickListener(myListClickListener);
    }

    private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener()
    {
        public void onItemClick (AdapterView<?> av, View v, int arg2, long arg3)
        {

            String info = ((TextView) v).getText().toString();
            Config.getInstance().deviceAddress = info.substring(info.length() - 17);

            goToNextActivity();
        }
    };

    private void goToNextActivity() {

        Intent i = new Intent(DeviceListActivity.this, LampControlActivity.class);
        startActivity(i);
    }


}
