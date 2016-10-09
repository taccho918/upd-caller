package com.example.takahashimomoka.currentlocation;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private LocationManager locationManager;
    public static String latitude;
    public static String longitude;
    private Button returnBtn, moveBtn;
    public static String number, msg;
    private Button editInfoBtn, msgBtn, cancelBtn;
    private TextView number_txt, msg_txt;
    private EditText editTxt_number, editTxt_msg;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setScreenMain();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        }
        else{
            locationStart();
        }
    }

    private void locationStart(){
        Log.d("debug","locationStart()");

        // create LocationManager instance
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        final boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            // GPS setting
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
            Log.d("debug", "gpsEnable, startActivity");
        } else {
            Log.d("debug", "gpsEnabled");
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);

            Log.d("debug", "checkSelfPermission false");
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 50, this);
    }

    // get the result
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1000) {
            // allowed to use
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("debug","checkSelfPermission true");

                locationStart();
                return;

            } else {
                // not allowed to use
                Toast toast = Toast.makeText(this, "can't use current location", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        switch (status) {
            case LocationProvider.AVAILABLE:
                Log.d("debug", "LocationProvider.AVAILABLE");
                break;
            case LocationProvider.OUT_OF_SERVICE:
                Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                break;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        /*// show the latitude
        TextView textView1 = (TextView) findViewById(R.id.text_view1);
        textView1.setText("Latitude:"+location.getLatitude());

        // show the longitude
        TextView textView2 = (TextView) findViewById(R.id.text_view2);
        textView2.setText("Longitude:"+location.getLongitude());
*/
        latitude = "Latitude: " + String.valueOf(location.getLatitude());
        longitude = "Longitude: " + String.valueOf(location.getLongitude());;

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    //Sends an SMS message to another device

    private void sendSMS(String phoneNumber, String message) {

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message+"\n"+latitude + "\n" + longitude, null, null);
        ToneGenerator toneGenerator=new ToneGenerator(AudioManager.STREAM_SYSTEM, ToneGenerator.MAX_VOLUME);
        toneGenerator.startTone(ToneGenerator.TONE_SUP_ERROR);
    }

   private void setScreenMain(){
        setContentView(R.layout.activity_main);
        number_txt = (TextView)findViewById(R.id.numberTxt);
        number_txt.setText(number);
        msg_txt = (TextView)findViewById(R.id.messageTxt);
        msg_txt.setText(msg);
        moveBtn = (Button)findViewById(R.id.move_btn);
        moveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setScreenSub();
            }
        });
        Button btnSendSMS = (Button) findViewById(R.id.send_btn);
        btnSendSMS.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                sendSMS(number, msg);
            }
        });

       cancelBtn = (Button)findViewById(R.id.cancel_btn);
           cancelBtn.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   sendSMS(number, "This was a mistake. Ignore previous message.");
               }
           });



       }

   private void setScreenSub() {
        setContentView(R.layout.activity_sub);
        returnBtn = (Button) findViewById(R.id.return_btn);
        editTxt_number = (EditText)findViewById(R.id.editTextNumber);
        editTxt_number.setText(number);
        editTxt_msg = (EditText)findViewById(R.id.editTextMessage);
        editTxt_msg.setText(msg);
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number = editTxt_number.getText().toString();
                msg = editTxt_msg.getText().toString();
                setScreenMain();
            }
        });
    }

    public String getNumber(){
        return number;
    }



}
