package com.myawesome.kariukimessagingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hover.sdk.api.Hover;
import com.hover.sdk.api.HoverParameters;

public class MainActivity extends AppCompatActivity {

    Intent smsServiceIntent;
    String number,last_number;
    String amount_choice;
    String amount;
    String[] parts;

    NotificationCompat.Builder b;
    NotificationManager nm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Hover.initialize(this);

        Button btn=findViewById(R.id.btn);
        final EditText et=findViewById(R.id.et);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String data=et.getText().toString();

                if(data.contains("Confirmed.on")&&data.contains("received")){

                     amount=data.substring(data.indexOf("Ksh")+3,data.indexOf("from"));

                     parts = amount.split("\\.");

                    amount = parts[0].replaceAll("[^\\d.]", "");

                    Toast.makeText(MainActivity.this, ""+amount, Toast.LENGTH_SHORT).show();

                     number=data.substring(data.indexOf("from"),data.indexOf(". New"));



                   last_number=number.replaceAll("\\D+","");


                    String name=data.substring(data.indexOf("from")+4,data.indexOf(last_number));
                    name.replaceAll("\\s+","");



                    Toast.makeText(MainActivity.this, "name "+name+last_number, Toast.LENGTH_SHORT).show();

                    switch (amount){
                        case "1000":
                            amount_choice="1";
                            break;

                        case "2000":
                            amount_choice="2";

                            break;

                        case "3000":
                            amount_choice="3";

                            break;
                    }

                    if(Integer.parseInt(amount)==1000||Integer.parseInt(amount)==2000||Integer.parseInt(amount)==3000){
                        Intent i = new HoverParameters.Builder(MainActivity.this)
                                .request("1bd0ad14")
                                .showUserStepDescriptions(false)
                                .extra("number", last_number) // Only if your action has variables
                                .extra("amount",amount_choice)
                                .buildIntent();
                        startActivityForResult(i, 0);
                    }
                }





            }
        });


        if(!checkIfAlreadyhavePermission()){

            requestForSpecificPermission();

        }

        smsServiceIntent = new Intent(this, SmsProcessService.class);

         b = new NotificationCompat.Builder(getBaseContext());
        b.setOngoing(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_keyboard_key_k)
                .setContentTitle("We are on!");
         nm = (NotificationManager)getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(1, b.build());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        nm.cancel(1);

    }

    private boolean checkIfAlreadyhavePermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);
        if (result == PackageManager.PERMISSION_GRANTED) {

            return true;
        } else {
            return false;
        }
    }

    private void requestForSpecificPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.GET_ACCOUNTS, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 101:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //granted
//                    SmsReceiver.bindListener(this);

                } else {
                    //not granted
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }



    @Override
    protected void onResume() {

        super.onResume();

        startService(smsServiceIntent);
    }
}
