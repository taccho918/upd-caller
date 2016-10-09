package com.example.takahashimomoka.currentlocation;


import android.os.Bundle;
import android.telephony.gsm.SmsManager;

public class AnotherActivity extends MainActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        send();
    }


    private void sendSMS(String phoneNumber, String message) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }
    String number = MainActivity.number;
    String msg = MainActivity.msg + MainActivity.latitude + MainActivity.longitude;

    

    private void send() {
        sendSMS(number, msg);
        setContentView(R.layout.activity_sent);
    }







}
