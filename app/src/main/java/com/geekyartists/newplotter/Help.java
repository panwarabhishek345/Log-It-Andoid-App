package com.geekyartists.newplotter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class Help extends AppCompatActivity {

    TextView helpContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        this.setTitle(Html.fromHtml("<font color='#bce27f'>HELP </font>"));

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-7528515959312214~2453172683" );
         AdView mAdView = (AdView) findViewById(R.id.adViewhelp);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        String contentString = "1. Tap on the PLUS ICON on the top right corner of your screen to add a new attribute.\n" +
                               "2. Tap on the GREEN PLUS ICON in each attribute to add a new entry.\n" +
                               "3. Tap on the name of the attribute to open the list of previous entries.\n" +
                               "4. You can also see the graph (previous entries vs. time) in the entries tab.\n" +
                               "5. You can delete an attribute by long pressing on it.\n" +
                               "6. You can delete a value by long pressing on it.\n" +
                               "7. You can Export all the data to a CSV file by tapping on the Export icon on the top right corner.\n" +
                               "8. You can modify individual entries by clicking on them.\n" +
                               "9. To import a CSV file it is necessary that the data is ordered just in the way the exported CSV files have\n";

        helpContent = (TextView)findViewById(R.id.helpcontent);

        helpContent.setText(contentString);
    }
}
