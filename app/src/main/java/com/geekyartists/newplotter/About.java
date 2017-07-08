package com.geekyartists.newplotter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.NativeExpressAdView;

public class About extends AppCompatActivity {

    TextView textViewAbout;
    TextView disclaimerContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-7528515959312214~2453172683" );
        NativeExpressAdView mAdView = (NativeExpressAdView)findViewById(R.id.adViewabout);

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        this.setTitle(Html.fromHtml("<font color='#bce27f'>ABOUT </font>"));

        String contentString = "DRS Chandel & A Panwar 2017, All rights Reserved.\n";
        String disclaimer = "Please don’t use this app for important data. " +
                            "The app is provided ‘as-is’ with no guarantee. " +
                            "We will accept no liability for loss of data if the app stops working. " +
                            "Please enable the auto-backup and export your data regularly to keep a record.\n";
        textViewAbout = (TextView)findViewById(R.id.textViewAbout);
        disclaimerContent = (TextView)findViewById(R.id.disclaimercontent);

        textViewAbout.setText(contentString);
        disclaimerContent.setText(disclaimer);
    }
}
