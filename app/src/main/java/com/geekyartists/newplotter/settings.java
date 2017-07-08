package com.geekyartists.newplotter;

import android.content.DialogInterface;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.NativeExpressAdView;

public class settings extends AppCompatActivity {

    Button buttonReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        this.setTitle(Html.fromHtml("<font color='#bce27f'>SETTINGS </font>"));

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-7528515959312214~2453172683" );
        NativeExpressAdView mAdView = (NativeExpressAdView)findViewById(R.id.adViewsettings);

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        buttonReset = (Button)findViewById(R.id.buttonReset);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertBoxReset();
            }
        });
    }

    public void AlertBoxReset() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Are you sure you want to delete all the Data?")
                .setTitle("Erase all Data");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                MainActivity.outerArr.clear();
                MainActivity.attrTitles.clear();
                MainActivity.adapter.list.clear();
                MainActivity.adapter.notifyDataSetChanged();
                MainActivity.exportLocation = Environment.getExternalStorageDirectory().getAbsolutePath();
                finish();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

// Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
