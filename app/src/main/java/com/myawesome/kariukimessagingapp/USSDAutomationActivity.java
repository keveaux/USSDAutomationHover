package com.myawesome.kariukimessagingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.hover.sdk.api.Hover;
import com.hover.sdk.api.HoverParameters;

public class USSDAutomationActivity extends AppCompatActivity {

    String number,last_number;
    String amount_choice_one,amount_choice_two;
    String amount;
    String[] parts;
    Intent smsServiceIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ussdautomation);

        Hover.initialize(this);


        Intent intent = this.getIntent();


        /* Obtain String from Intent  */
        if (intent != null) {
            String strdata = intent.getExtras().getString("message");
            buy_bundles(strdata);

        }

        smsServiceIntent = new Intent(this, SmsProcessService.class);


    }

    private void buy_bundles(String data){

            amount=data.substring(data.indexOf("Ksh")+3,data.indexOf("from"));

            parts = amount.split("\\.");

            amount = parts[0].replaceAll("[^\\d.]", "");

            Toast.makeText(USSDAutomationActivity.this, ""+amount, Toast.LENGTH_SHORT).show();

            number=data.substring(data.indexOf("from"),data.indexOf(". New"));



            last_number=number.replaceAll("\\D+","");


            String name=data.substring(data.indexOf("from")+4,data.indexOf(last_number));
            name.replaceAll("\\s+","");



            Toast.makeText(USSDAutomationActivity.this, "name "+name+last_number, Toast.LENGTH_SHORT).show();

            switch (amount){
                    case "999":
                        amount_choice_one ="1";
                        amount_choice_two="2";
                        break;

                    case "1000":
                        amount_choice_one ="1";
                        amount_choice_two="3";

                        break;

                    case "1999":
                        amount_choice_one ="2";
                        amount_choice_two="2";

                        break;

                    case "2000":
                        amount_choice_one ="2";
                        amount_choice_two="3";
                        break;

                    case "2999":
                        amount_choice_one ="3";
                        amount_choice_two="2";

                        break;

                    case "3000":
                        amount_choice_one ="3";
                        amount_choice_two="3";

                        break;
            }

            if(Integer.parseInt(amount)==1000||Integer.parseInt(amount)==2000||Integer.parseInt(amount)==3000||Integer.parseInt(amount)==999||Integer.parseInt(amount)==1999||Integer.parseInt(amount)==2999){
                Intent i = new HoverParameters.Builder(USSDAutomationActivity.this)
                        .request("7348ec75")
                        .showUserStepDescriptions(false)
                        .extra("number", last_number) // Only if your action has variables
                        .extra("choiceOne", amount_choice_one)
                        .extra("choiceTwo", amount_choice_two)
                        .buildIntent();
                startActivityForResult(i, 0);
            }

    }

    @Override
    protected void onResume() {

        super.onResume();

        startService(smsServiceIntent);
    }
}

