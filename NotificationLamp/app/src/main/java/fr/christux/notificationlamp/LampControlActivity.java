package fr.christux.notificationlamp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class LampControlActivity extends AppCompatActivity implements IBTActivity {

    Button btnOn, btnOff, btnDis;
    RadioGroup gAnimation, gColor;
    CheckBox cbAutoConnect;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lamp_control);

        //Read prefs
        readPrefs();

        BTConnection.getInstance().setAddress(Config.getInstance().deviceAddress);

        //Call the class to connect
        new ConnectBTAsync().execute();

        //call the widgtes
        btnOn = findViewById(R.id.buttonOn);
        btnOff = findViewById(R.id.buttonOff);
        btnDis = findViewById(R.id.buttonDisconnect);
        gAnimation = findViewById(R.id.groupAnimation);
        gColor = findViewById(R.id.groupColor);
        cbAutoConnect = findViewById(R.id.checkBoxAutoConnect);

        // Register observer
        BTConnection.getInstance().register(this);


        /**
         * Controls
         */
        btnOn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                BTLamp.getInstance().turnOnLed();      //method to turn on
            }
        });

        btnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                BTLamp.getInstance().turnOffLed();   //method to turn off
            }
        });

        btnDis.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Config.getInstance().manualDisconnection = true;
                BTConnection.getInstance().Disconnect(); //close connection
            }
        });

        /**
         * Animations
         */

        switch(Config.getInstance().animation) {

            case BTLampAnimation.STATIC:
                gAnimation.check(R.id.radioButtonStatic);
                break;

            case BTLampAnimation.BREATHING:
                gAnimation.check(R.id.radioButtonBreathing);
                break;

            case BTLampAnimation.RAINBOW:
                gAnimation.check(R.id.radioButtonRainbow);
                break;

            case BTLampAnimation.BLINK:
                gAnimation.check(R.id.radioButtonBlink);
        }

        gAnimation.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {

                    case R.id.radioButtonStatic:
                        BTLamp.getInstance().sendToDevice(BTLampAnimation.STATIC);
                        break;

                    case R.id.radioButtonBreathing:
                        BTLamp.getInstance().sendToDevice(BTLampAnimation.BREATHING);
                        break;

                    case R.id.radioButtonRainbow:
                        BTLamp.getInstance().sendToDevice(BTLampAnimation.RAINBOW);
                        break;

                    case R.id.radioButtonBlink:
                        BTLamp.getInstance().sendToDevice(BTLampAnimation.BLINK);
                        break;
                }
            }
        });

        /**
         * Colors
         */

        if(Config.getInstance().color.isEqual(RgbColor.white)) {
            gColor.check(R.id.radioButtonWhite);
        }
        if(Config.getInstance().color.isEqual(RgbColor.red)) {
            gColor.check(R.id.radioButtonRed);
        }
        if(Config.getInstance().color.isEqual(RgbColor.green)) {
            gColor.check(R.id.radioButtonGreen);
        }
        if(Config.getInstance().color.isEqual(RgbColor.orange)) {
            gColor.check(R.id.radioButtonOrange);
        }
        if(Config.getInstance().color.isEqual(RgbColor.blue)) {
            gColor.check(R.id.radioButtonBlue);
        }
        if(Config.getInstance().color.isEqual(RgbColor.purple)) {
            gColor.check(R.id.radioButtonPurple);
        }

        gColor.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {

                    case R.id.radioButtonWhite:
                        BTLamp.getInstance().sendToDevice(RgbColor.white);
                        break;

                    case R.id.radioButtonGreen:
                        BTLamp.getInstance().sendToDevice(RgbColor.green);
                        break;

                    case R.id.radioButtonRed:
                        BTLamp.getInstance().sendToDevice(RgbColor.red);
                        break;

                    case R.id.radioButtonBlue:
                        BTLamp.getInstance().sendToDevice(RgbColor.blue);
                        break;

                    case R.id.radioButtonOrange:
                        BTLamp.getInstance().sendToDevice(RgbColor.orange);
                        break;

                    case R.id.radioButtonPurple:
                        BTLamp.getInstance().sendToDevice(RgbColor.purple);
                        break;
                }
            }
        });

        /**
         * Auto connect checkbox
         */
        cbAutoConnect.setChecked(Config.getInstance().autoConnect);

        cbAutoConnect.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener()
         {

             @Override
             public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                 Config.getInstance().autoConnect = b;
                 finish();
             }
         });
    }

    @Override
    public void onDestroy() {
        // Register observer
        BTConnection.getInstance().unregister(this);

        //Call the class to disconnect
        BTConnection.getInstance().Disconnect();

        super.onDestroy();
    }

    @Override
    public void onStop() {

        // Save prefs
        savePrefs();

        super.onStop();
    }

    private void readPrefs() {
        int animation, red, green, blue;
        SharedPreferences sharedPref = LampControlActivity.this.getPreferences(Context.MODE_PRIVATE);

        // Static red by default
        animation = sharedPref.getInt("animation", BTLampAnimation.STATIC);
        red = sharedPref.getInt("red", 255);
        green = sharedPref.getInt("green", 0);
        blue = sharedPref.getInt("blue", 0);

        Config.getInstance().animation = animation;
        Config.getInstance().color = new RgbColor(red,green,blue);
    }

    private void savePrefs() {
        int animation, red, green, blue;
        SharedPreferences sharedPref = LampControlActivity.this.getPreferences(Context.MODE_PRIVATE);

        animation = Config.getInstance().animation;
        red = Config.getInstance().color.R;
        green = Config.getInstance().color.G;
        blue = Config.getInstance().color.B;

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("animation", animation);
        editor.putInt("red", red);
        editor.putInt("green", green);
        editor.putInt("blue",blue);
        editor.commit();
    }

    @Override
    public void finish() {
        Config.getInstance().manualDisconnection = true;
        super.finish();
    }

    // fast way to call Toast
    public void msg(String s)
    {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }

    // Waiting BT connection
    private class ConnectBTAsync extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(LampControlActivity.this, "Connecting...", "Please wait!!!");  //show a progress dialog
        }

        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!BTConnection.getInstance().isConnected()) {
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                finish();
            } else {
                msg("Connected.");
            }
            progress.dismiss();
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            BTConnection.getInstance().Connect();
            return null;
        }
    }
}
