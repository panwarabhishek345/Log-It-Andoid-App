package com.geekyartists.newplotter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class GraphActivity extends AppCompatActivity {
    GraphView graphView;
    DataPoint[] pointGroup;
    Date strDateMaxX = null,strDateMinX = null;
    ArrayList<String> listPoint;
    String point;
    ListView valueList;
    RowadaptorValue rowadaptorValue;
    float minY,maxY;
    public static int posAttr;
    AdView mAdView;
    AdRequest adRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(Html.fromHtml("<font color='#bce27f'>Insights</font>"));
        setContentView(R.layout.activity_graph);
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-7528515959312214~2453172683" );
        mAdView = (AdView) findViewById(R.id.adViewgraph);
        adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        valueList = (ListView)findViewById(R.id.valuelist);
        rowadaptorValue = new RowadaptorValue(this);
        valueList.setAdapter(rowadaptorValue);

        graphView = (GraphView)findViewById(R.id.graphview);
        graphView.setTitle("Data Analytics");

        posAttr = getIntent().getExtras().getInt("position");
        listPoint = MainActivity.outerArr.get(posAttr);
        pointGroup = new DataPoint[listPoint.size()];


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss.SSS");
        try {


            int year = Integer.parseInt(listPoint.get(0).substring(8,12));
            int month = Integer.parseInt(listPoint.get(0).substring(13,15));
            int day = Integer.parseInt(listPoint.get(0).substring(16,18));
            int hour = Integer.parseInt(listPoint.get(0).substring(0,2));
            int minute = Integer.parseInt(listPoint.get(0).substring(3,5));
            int second = Integer.parseInt(listPoint.get(0).substring(6,8));

            String date = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second + ".000";

            strDateMaxX = sdf.parse(date);
            strDateMinX = sdf.parse(date);


        } catch (ParseException e) {
            e.printStackTrace();
        }


        minY = Float.parseFloat(listPoint.get(0).substring(18,listPoint.get(0).length()));
        maxY = Float.parseFloat(listPoint.get(0).substring(18,listPoint.get(0).length()));


        int i = 0;
        while ( i != listPoint.size()){
            point = listPoint.get(i);

            int year = Integer.parseInt(point.substring(8,12));
            int month = Integer.parseInt(point.substring(13,15));
            int day = Integer.parseInt(point.substring(16,18));
            int hour = Integer.parseInt(point.substring(0,2));
            int minute = Integer.parseInt(point.substring(3,5));
            int second = Integer.parseInt(point.substring(6,8));

            Calendar cal=new GregorianCalendar(year,month,day,hour,minute,second);

            pointGroup[i] = new DataPoint(cal.getTimeInMillis() ,Float.parseFloat(point.substring(18,point.length())));


            try {
                if(strDateMaxX.before(sdf.parse(point.substring(8,18))))
                    strDateMaxX = sdf.parse(point.substring(8,18));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            try {
                if(strDateMinX.after(sdf.parse(point.substring(8,18))))
                    strDateMinX = sdf.parse(point.substring(8,18));
            } catch (ParseException e) {
                e.printStackTrace();
            }



            if(minY > Float.parseFloat(point.substring(18,point.length())))
                minY = Float.parseFloat(point.substring(18,point.length()));

            if(maxY < Float.parseFloat(point.substring(18,point.length())))
                maxY = Float.parseFloat(point.substring(18,point.length()));

            i++;

        }


        GridLabelRenderer gridLabelRenderer= graphView.getGridLabelRenderer();
        gridLabelRenderer.setHorizontalAxisTitle("Time");
        gridLabelRenderer.setVerticalAxisTitle(MainActivity.adapter.list.get(posAttr).title);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(pointGroup);

        series.setColor(Color.parseColor("#bce27f"));
        series.setDrawBackground(true);
        series.setBackgroundColor(Color.parseColor("#67BCE27F"));


        graphView.addSeries(series);

        // set manual X bounds
        graphView.getViewport().setYAxisBoundsManual(true);
        graphView.getViewport().setMinY(minY);
        graphView.getViewport().setMaxY(maxY);

        graphView.getViewport().setXAxisBoundsManual(true);


        graphView.setTitleColor(Color.parseColor("#ffffff"));
        gridLabelRenderer.setVerticalLabelsColor(Color.parseColor("#ffffff"));
        gridLabelRenderer.setHorizontalLabelsColor(Color.parseColor("#ffffff"));

        gridLabelRenderer.setHorizontalLabelsVisible(false);


        gridLabelRenderer.setHorizontalAxisTitleColor(Color.parseColor("#ffffff"));
        gridLabelRenderer.setVerticalAxisTitleColor(Color.parseColor("#ffffff"));
        gridLabelRenderer.setGridColor(Color.parseColor("#ffffff"));

        Viewport viewport = graphView.getViewport();
        viewport.setBackgroundColor(Color.parseColor("#303030"));

        rowadaptorValue.list.add(new singleValueRow("Value", "Time", "Date"));
        rowadaptorValue.notifyDataSetChanged();

        for (int j = 0;j < listPoint.size();j++) {

            int size = listPoint.get(j).length();
           /* rowadaptorValue.list.add(new singleValueRow(listPoint.get(j).value, listPoint.get(j).time,listPoint.get(j).date));*/
            rowadaptorValue.list.add(new singleValueRow(listPoint.get(j).substring(18,size), listPoint.get(j).substring(0,8),listPoint.get(j).substring(8,18)));
           rowadaptorValue.notifyDataSetChanged();
        }

        valueList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           final int pos, long id) {
                // TODO Auto-generated method stub

                final CharSequence colors[] = new CharSequence[] {"Edit", "Delete"};

                AlertDialog.Builder builder = new AlertDialog.Builder(GraphActivity.this);
                builder.setTitle("Options");
                builder.setItems(colors, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(colors[which] == "Edit")
                            AlertBoxModifyValue(pos);
                        else if(colors[which] == "Delete")
                            AlertBoxDeleteEntry(pos);

                    }
                });
                builder.show();
                return true;
            }
        });

       /* valueList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertBoxModifyValue(position);
            }
        });  */


    }

    public void AlertBoxModifyValue(final int pos) {

        float dpi = this.getResources().getDisplayMetrics().density;

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(GraphActivity.this, R.style.MyDialog);
        alertDialog.setTitle("Insert New Value");

        final EditText input = new EditText(GraphActivity.this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        input.setTextColor(Color.parseColor("#ffffff"));
        alertDialog.setView(input, (int) (19 * dpi), (int) (5 * dpi), (int) (14 * dpi), (int) (5 * dpi));

        alertDialog.setPositiveButton("Modify",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        String uvalue = input.getText().toString();
                        if(uvalue == null || uvalue.length() == 0){
                            Toast.makeText(GraphActivity.this,"Entry was not modified as the text field was empty.",Toast.LENGTH_SHORT).show();

                        }
                        else {

                            String value = MainActivity.outerArr.get(posAttr).get(pos-1).substring(0,18);
                            value = value + uvalue;
                            MainActivity.outerArr.get(posAttr).set(pos-1,value);
                            Intent intent = getIntent();
                            finish();
                            int size = rowadaptorValue.list.size();
                            if(size-1 != 0)
                                startActivity(intent);
                            MainActivity.updateadapter();


                        }

                    }
                });


        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();


    }

    public void AlertBoxDeleteEntry(final int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialog);

        builder.setMessage("Are you sure you want to delete this Entry?")
                .setTitle("Delete Entry");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                rowadaptorValue.list.remove(pos);
                rowadaptorValue.notifyDataSetChanged();
                MainActivity.outerArr.get(GraphActivity.posAttr).remove(pos-1);
                Intent intent = getIntent();
                finish();
                int size = rowadaptorValue.list.size();
                if(size-1 != 0)
                startActivity(intent);
                MainActivity.updateadapter();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            finish();
            MainActivity.updateadapter();

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }


    class singleValueRow {
        String value;
        String time;
        String date;

        singleValueRow(String value, String time, String date) {
            this.value = value;
            this.time = time;
            this.date = date;
        }
    }

    class RowadaptorValue extends BaseAdapter{

        ArrayList<singleValueRow> list;
        Context context;

        RowadaptorValue(Context c){
            context =c;
            list = new ArrayList<singleValueRow>();

        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.singlevaluerow,parent,false);

            TextView value = (TextView)row.findViewById(R.id.valueText);
            TextView time = (TextView)row.findViewById(R.id.timeText);
            TextView date = (TextView)row.findViewById(R.id.dateText);

            final singleValueRow tmp = list.get(position);

            value.setText(String.valueOf(tmp.value));
            time.setText(String.valueOf(tmp.time));
            date.setText(String.valueOf(tmp.date));



            return row;
        }
    }
}
