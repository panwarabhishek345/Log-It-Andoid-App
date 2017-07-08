package com.geekyartists.newplotter;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.gson.Gson;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    boolean firsttime = true;
    public static RecyclerView recyclerView;
    public static singleRowAdapter adapter;
    DrawerLayout drawer;
    NavigationView navigationView;

    //ExpandableAdapter exlistAdapter;
    //ExpandableListView expListView;
    //List<String> listDataHeader;
    //HashMap<String, List<String>> listDataChild;
   // ArrayList<String> profilesTitles;


    public final int PICKFILE_REQUEST_CODE = 20;


    public static Boolean actionModeState = false;

    public static ArrayList<ArrayList<String>> outerArr;
    //public static ArrayList<ArrayList<ArrayList<String>>> outerArrProfiles;

    //int currentProfile =0;

    public static ArrayList<String> attrTitles;
   // public static ArrayList<ArrayList<String>> attrTitlesProfiles;


    public static String exportLocation;

    Calendar calendar;
    SharedPreferences mPrefs;
    SharedPreferences sharedPref;
    final int STORAGE_PERMISSION_CODE = 200;

    AdView mAdView;
    AdRequest adRequest;

    public static ActionMode actionMode;
    FloatingActionButton fab;


    @Override
    protected void onStop() {
        super.onStop();

        //outerArrProfiles.set(currentProfile,outerArr);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String outerArrjson = gson.toJson(outerArr);
        //String outerArrProfilesjson = gson.toJson(outerArrProfiles);
        String attrTitlesjson = gson.toJson(attrTitles);
        //String attrTitlesProfilesjson = gson.toJson(attrTitles);
        prefsEditor.putString("outerArr", outerArrjson);
        prefsEditor.commit();
        /*prefsEditor.putString("outerArrProfiles", outerArrProfilesjson);
        prefsEditor.commit();*/
        prefsEditor.putString("attrTitles", attrTitlesjson);
        prefsEditor.commit();

        /*prefsEditor.putString("attrTitlesProfiles", attrTitlesProfilesjson);
        prefsEditor.commit();*/

        SharedPreferences.Editor prefsEditor1 = sharedPref.edit();
        prefsEditor1.putBoolean("firsttime", false);
        prefsEditor1.commit();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPref = getPreferences(MODE_PRIVATE);
        firsttime = sharedPref.getBoolean("firsttime", true);

        if (firsttime == true) {

            Intent intent = new Intent(MainActivity.this,
                    SplashScreen.class);
            startActivity(intent);
        }


        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertBoxAttribute();
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.bringToFront();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //profilesTitles = new ArrayList<String>();

        // get the listview
        //expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // preparing list data
       // prepareListData();


        //exlistAdapter = new ExpandableAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        //expListView.setAdapter(exlistAdapter);

        // Listview Group click listener
        /*expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                if (groupPosition == 1) {
                    Intent intent = new Intent(MainActivity.this, settings.class);
                    startActivity(intent);

                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
                    expListView.collapseGroup(0);

                } else if (groupPosition == 2) {
                    Intent intent = new Intent(MainActivity.this, Help.class);
                    startActivity(intent);

                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
                    expListView.collapseGroup(0);

                } else if (groupPosition == 3) {
                    Intent intent = new Intent(MainActivity.this, About.class);
                    startActivity(intent);

                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
                    expListView.collapseGroup(0);

                } else if (groupPosition == 4) {
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    String shareBodyText = "Checkout the Log It! app to keep a log on all your data. Link: https://play.google.com/store/apps/details?id=com.geekyartists.newplotter";
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject here");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
                    startActivity(Intent.createChooser(sharingIntent, "Sharing Option"));

                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
                    expListView.collapseGroup(0);

                }


                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                ImageView dropdown = (ImageView)findViewById(R.id.arrowDropDown);
                Animation rotation = AnimationUtils.loadAnimation(expListView.getContext(), R.anim.rotationupwards);
                rotation.setFillAfter(true);
                dropdown.startAnimation(rotation);

            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                ImageView dropdown = (ImageView)findViewById(R.id.arrowDropDown);
                Animation rotation = AnimationUtils.loadAnimation(expListView.getContext(), R.anim.rotationdownwards);
                rotation.setFillAfter(true);
                dropdown.startAnimation(rotation);


            }
        });


        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                if(childPosition == 0)
                AlertBoxProfile();
                else {
                    outerArrProfiles.set(currentProfile,outerArr);
                    attrTitlesProfiles.set(currentProfile,attrTitles);
                    outerArr = outerArrProfiles.get(childPosition);
                    attrTitles = attrTitlesProfiles.get(childPosition);
                    updateadapter();
                }

                return false;
            }
        });*/




        MobileAds.initialize(getApplicationContext(), "ca-app-pub-7528515959312214~2453172683");
        mAdView = (AdView) findViewById(R.id.adViewmain);
        adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //outerArrProfiles = new ArrayList<ArrayList<ArrayList<String>>>();
        outerArr = new ArrayList<ArrayList<String>>();
        attrTitles = new ArrayList<String>();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        adapter = new singleRowAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);



        mPrefs = getPreferences(MODE_PRIVATE);

        Gson gson = new Gson();
        String outerArrjson = mPrefs.getString("outerArr", "");
        //String outerArrProfilesjson = mPrefs.getString("outerArrProfiles", "");
        String attrTitlesjson = mPrefs.getString("attrTitles", "");
        //String attrTitlesProfilesjson = mPrefs.getString("attrTitlesProfiles", "");
        outerArr = gson.fromJson(outerArrjson, new ArrayList<ArrayList<String>>().getClass());
        //outerArrProfiles = gson.fromJson(outerArrProfilesjson, new ArrayList<ArrayList<ArrayList<String>>>().getClass());
        attrTitles = gson.fromJson(attrTitlesjson, attrTitles.getClass());
        //attrTitlesProfiles = gson.fromJson(attrTitlesProfilesjson, new ArrayList<ArrayList<String>>().getClass());

        /*if (outerArrProfiles == null) {
            outerArrProfiles = new ArrayList<ArrayList<ArrayList<String>>>();
        }*/

        if (outerArr == null) {
            outerArr = new ArrayList<ArrayList<String>>();
        }

       /* if (attrTitlesProfiles == null) {
            attrTitlesProfiles = new ArrayList<ArrayList<String>>();
        }*/

        if (attrTitles == null) {
            attrTitles = new ArrayList<String>();
        }

        updateadapter();


    }

 /*   private void prepareListData() {



        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();


        // Adding child data
        listDataHeader.add("Profiles");
        listDataHeader.add("Settings");
        listDataHeader.add("Help");
        listDataHeader.add("About");
        listDataHeader.add("Share");


        // Adding child data
        List<String> profiles = new ArrayList<String>();
        List<String> settings = new ArrayList<String>();
        List<String> help = new ArrayList<String>();
        List<String> about = new ArrayList<String>();
        List<String> share = new ArrayList<String>();

        for(int i = 0; i< profilesTitles.size(); i++){
            profiles.add(profilesTitles.get(i));
        }

        profiles.add("ADD");
        int y = profiles.size();
        y = y+1;


        listDataChild.put(listDataHeader.get(0), profiles);
        listDataChild.put(listDataHeader.get(1), settings);
        listDataChild.put(listDataHeader.get(2), help);
        listDataChild.put(listDataHeader.get(3), about);
        listDataChild.put(listDataHeader.get(4), share);// Header, Child data

        exlistAdapter = new ExpandableAdapter(this, listDataHeader, listDataChild);
        expListView.setAdapter(exlistAdapter);

    }
*/
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            //expListView.collapseGroup(0);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_item_add) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                Toast.makeText(this,"Press the import button again!",Toast.LENGTH_LONG).show();
            }
            else{
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("file/*");
                startActivityForResult(intent, PICKFILE_REQUEST_CODE);

            }

            return true;
        } else if (id == R.id.menu_item_export) {
            AlertBoxExport();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_settings) {
            Intent intent = new Intent(MainActivity.this, settings.class);
            startActivity(intent);

        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(MainActivity.this, About.class);
            startActivity(intent);

        } else if (id == R.id.nav_help) {
            Intent intent = new Intent(MainActivity.this, Help.class);
            startActivity(intent);

        } else if (id == R.id.nav_share) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBodyText = "Checkout the Log It! app to keep a log on all your data. Link: https://play.google.com/store/apps/details?id=com.geekyartists.newplotter";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject here");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
            startActivity(Intent.createChooser(sharingIntent, "Sharing Option"));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

  /*  public void AlertBoxProfile() {

        float dpi = this.getResources().getDisplayMetrics().density;

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this, R.style.MyDialog);
        alertDialog.setTitle("Profile Name");

        final EditText input = new EditText(MainActivity.this);
        input.setTextColor(Color.parseColor("#ffffff"));
        alertDialog.setView(input, (int) (19 * dpi), (int) (5 * dpi), (int) (14 * dpi), (int) (5 * dpi));

        alertDialog.setPositiveButton("Add",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        Boolean sameexists = false;
                        String tmpProfile = input.getText().toString();

                        for (int x = 0; x < profilesTitles.size(); x++) {
                            if (profilesTitles.get(x).equals(tmpProfile)) {
                                Toast.makeText(MainActivity.this, "This Profile already exists", Toast.LENGTH_SHORT).show();
                                sameexists = true;
                            }
                        }

                        if (sameexists == false && tmpProfile.length() <= 10) {
                            if (tmpProfile == null || tmpProfile.length() == 0) {
                                Toast.makeText(MainActivity.this, "No Profile added as the text field was empty.", Toast.LENGTH_SHORT).show();

                            } else {
                                attrTitlesProfiles.add(new ArrayList<String>());
                                outerArrProfiles.add(new ArrayList<ArrayList<String>>());
                                profilesTitles.add(tmpProfile);
                                prepareListData();


                            }

                        } else
                            Toast.makeText(MainActivity.this, "Profile name is too long. No attribute added.", Toast.LENGTH_SHORT).show();


                    }
                });


        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();


    }*/


    public void AlertBoxAttribute() {

        float dpi = this.getResources().getDisplayMetrics().density;

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this, R.style.MyDialog);
        alertDialog.setTitle("Attribute Name");

        final EditText input = new EditText(MainActivity.this);
        input.setTextColor(Color.parseColor("#ffffff"));
        alertDialog.setView(input, (int) (19 * dpi), (int) (5 * dpi), (int) (14 * dpi), (int) (5 * dpi));

        alertDialog.setPositiveButton("Set",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        Boolean sameexists = false;
                        String tmpTitle = input.getText().toString();

                        for (int x = 0; x < attrTitles.size(); x++) {
                            if (attrTitles.get(x).equals(tmpTitle)) {
                                Toast.makeText(MainActivity.this, "This Attribute already exists", Toast.LENGTH_SHORT).show();
                                sameexists = true;
                            }
                        }

                        if (sameexists == false && tmpTitle.length() <= 15) {
                            if (tmpTitle == null || tmpTitle.length() == 0) {
                                Toast.makeText(MainActivity.this, "No Attribute added as the text field was empty.", Toast.LENGTH_SHORT).show();

                            } else {
                                attrTitles.add(tmpTitle);
                                adapter.list.add(new singleRow(tmpTitle, "0", "0", "0"));
                                adapter.notifyDataSetChanged();
                                outerArr.add(new ArrayList<String>());

                            }

                        } else
                            Toast.makeText(MainActivity.this, "Attribute name is too long. No attribute added.", Toast.LENGTH_SHORT).show();


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


    public void AlertBoxModifyAttribute(final int pos) {

        float dpi = this.getResources().getDisplayMetrics().density;

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this, R.style.MyDialog);
        alertDialog.setTitle("Insert New Name");

        final EditText input = new EditText(MainActivity.this);
        input.setTextColor(Color.parseColor("#ffffff"));
        alertDialog.setView(input, (int) (19 * dpi), (int) (5 * dpi), (int) (14 * dpi), (int) (5 * dpi));

        alertDialog.setPositiveButton("Rename",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        String uvalue = input.getText().toString();
                        if(uvalue == null || uvalue.length() == 0){
                            Toast.makeText(MainActivity.this,"Attribute was not modified as the text field was empty.",Toast.LENGTH_SHORT).show();

                        }
                        else {

                            attrTitles.set(pos,uvalue);
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

    public void AlertBoxAddValue(final singleRow tmp1, final int pos) {

        float dpi = this.getResources().getDisplayMetrics().density;

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this, R.style.MyDialog);
        alertDialog.setTitle("Insert Entry");

        final EditText input = new EditText(MainActivity.this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        input.setTextColor(Color.parseColor("#ffffff"));
        alertDialog.setView(input, (int) (19 * dpi), (int) (5 * dpi), (int) (14 * dpi), (int) (5 * dpi));

        alertDialog.setPositiveButton("Add",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        String uvalue = input.getText().toString();
                        if (uvalue == null || uvalue.length() == 0) {
                            Toast.makeText(MainActivity.this, "No Entry added as the text field was empty.", Toast.LENGTH_SHORT).show();

                        } else {
                            ArrayList<String> stringarray = outerArr.get(pos);
                            calendar = Calendar.getInstance();
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String formattedDate = df.format(calendar.getTime());
                            String currentDate = formattedDate.substring(0, 10);
                            String currentTime = formattedDate.substring(11, 19);
                            String newPoint = currentTime + currentDate + uvalue;
                            stringarray.add(newPoint);
                            outerArr.set(pos, stringarray);
                            tmp1.updateValues(uvalue);
                            updateadapter();

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

    public void AlertBoxDelete(final int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialog);

        builder.setMessage("Are you sure you want to delete this attribute?")
                .setTitle("Delete Attribute");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                adapter.list.remove(pos);
                adapter.notifyDataSetChanged();
                outerArr.remove(pos);
                attrTitles.remove(pos);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void AlertBoxExport() {


        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }

        float dpi = this.getResources().getDisplayMetrics().density;

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this, R.style.MyDialog);
        alertDialog.setTitle("File Name");
        alertDialog.setMessage("Only type the name, no need to add .CSV Extension.");

        final EditText input = new EditText(MainActivity.this);
        input.setTextColor(Color.parseColor("#ffffff"));
        alertDialog.setView(input, (int) (19 * dpi), (int) (5 * dpi), (int) (14 * dpi), (int) (5 * dpi));

        alertDialog.setPositiveButton("Export",
                new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    public void onClick(DialogInterface dialog, int which) {

                        if (input.getText().toString().length() > 0) {
                            try {
                              exportCSV(input.getText().toString());
                            } catch (IOException e) {
                                e.printStackTrace();
                                Toast.makeText(MainActivity.this, "Snap! File Couldn't be saved.", Toast.LENGTH_SHORT).show();

                            }
                        } else
                            Toast.makeText(MainActivity.this, "No file Exported as the file name field was empty.", Toast.LENGTH_SHORT).show();


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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setMessage("We need Read and Write Storage permissions for importing and exporting the CSV files.");
                builder.show();
            }
        }

    }


    public void exportCSV(String name) throws IOException {


        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Logit");
        if (dir.exists() && dir.isDirectory()) {
            String csv = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Logit/" + name + ".csv";
            CSVWriter writer = new CSVWriter(new FileWriter(csv));

            String[] values = new String[3 * attrTitles.size()];

            for (int x = 0, y = 0; x < 3 * attrTitles.size(); x = x + 3, y++) {

                values[x] = attrTitles.get(y) + " (Time)";
                values[x + 1] = attrTitles.get(y) + " (Date)";
                values[x + 2] = attrTitles.get(y) + " (Value)";

            }
            writer.writeNext(values);

            int[] sizes = new int[attrTitles.size()];

            int maxSize = outerArr.get(0).size();
            for (int k = 0; k < outerArr.size(); k++) {
                sizes[k] = outerArr.get(k).size();
                if (maxSize < outerArr.get(k).size())
                    maxSize = outerArr.get(k).size();
            }

            for (int z = 0; z < maxSize; z++) {


                for (int x = 0, y = 0; x < 3 * attrTitles.size(); x = x + 3, y++) {

                    if (z + 1 > outerArr.get(y).size()) {
                        values[x] = "";
                        values[x + 1] = "";
                        values[x + 2] = "";
                    } else {
                        values[x] = outerArr.get(y).get(z).substring(0, 8);
                        values[x + 1] = outerArr.get(y).get(z).substring(8, 18);
                        values[x + 2] = outerArr.get(y).get(z).substring(18, outerArr.get(y).get(z).length());
                    }

                }
                writer.writeNext(values);

            }

            writer.close();
            Toast.makeText(MainActivity.this, "File Saved successfully.", Toast.LENGTH_SHORT).show();
        } else {
            try {
                if (dir.mkdir()) {
                    System.out.println("Directory created");
                    exportCSV(name);
                } else {
                    System.out.println("Directory is not created");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

    }

    public void importCSV(String path) throws IOException {


        outerArr.clear();
        attrTitles.clear();

        CSVReader reader = new CSVReader(new FileReader(path));
        String [] nextLine;
        int size = 0;
        if((nextLine = reader.readNext()) != null) {
            size = nextLine.length;

            for(int i = 0 ; i < size ; i = i+3){
                int sizeName = nextLine[i].length();
                sizeName = sizeName - 7;
                String titleCurrent = nextLine[i].substring(0,sizeName);
                attrTitles.add(titleCurrent);
            }

        }

        int attrNo = size/3;

        for(int j = 0; j < attrNo; j++){
            CSVReader reader1 = new CSVReader(new FileReader(path));
            String [] nextLine1;
            reader1.readNext();
            ArrayList<String> tmp = new ArrayList<>();
            while ((nextLine1 = reader1.readNext()) != null){
                String currentString = nextLine1[3*j] + nextLine1[3*j+1] + nextLine1[3*j+2];
                if(currentString.length()!=0)
                tmp.add(currentString);
            }
            outerArr.add(tmp);
        }


        updateadapter();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == RESULT_CANCELED) {
            // code to handle cancelled state
        }
        else if (requestCode == PICKFILE_REQUEST_CODE) {

            Uri uri = data.getData();
            File myFile = new File(uri.toString());
            String FilePath = myFile.getAbsolutePath();
            FilePath = FilePath.substring(7,FilePath.length());
            String extention = FilePath.substring(FilePath.length()-4,FilePath.length());
            if( extention.equals(".csv") || extention.equals(".CSV")){
                try {
                    importCSV(FilePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else
                Toast.makeText(this,"Wrong type of file chosen!",Toast.LENGTH_SHORT).show();

        }

    }

    public void ContextualToolbar(int pos) {
        actionMode = startActionMode(new ContextualCallback(pos));
        fab.setEnabled(false);

    }


    class ContextualCallback implements ActionMode.Callback {

        int positon;

        ContextualCallback(int pos) {
            positon = pos;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.contextual_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int id = item.getItemId();

            if (id == R.id.menu_item_delete) {
                AlertBoxDelete(positon);
                mode.finish();
                actionModeState = false;
                return true;
            } else if (id == R.id.menu_item_edit) {
                AlertBoxModifyAttribute(positon);
                mode.finish();
                actionModeState = false;
                return true;
            }

            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            actionMode = null;
            actionModeState = false;
            recyclerView.getLayoutManager().findViewByPosition(positon).setSelected(false);
            fab.setEnabled(true);


        }
    }

    public static void updateadapter() {
        if (outerArr != null) {

            adapter.list.clear();

            for (int z = 0; z < attrTitles.size(); z++) {
                int size = outerArr.get(z).size();
                if (size >= 3) {
                    adapter.list.add(new singleRow(attrTitles.get(z), outerArr.get(z).get(size - 1).substring(18, outerArr.get(z).get(size - 1).length()),
                            outerArr.get(z).get(size - 2).substring(18, outerArr.get(z).get(size - 2).length()),
                            outerArr.get(z).get(size - 3).substring(18, outerArr.get(z).get(size - 3).length())));
                } else if (size == 2) {

                    adapter.list.add(new singleRow(attrTitles.get(z), outerArr.get(z).get(size - 1).substring(18, outerArr.get(z).get(size - 1).length()),
                            outerArr.get(z).get(size - 2).substring(18, outerArr.get(z).get(size - 2).length()), "0"));

                } else if (size == 1) {

                    adapter.list.add(new singleRow(attrTitles.get(z), outerArr.get(z).get(size - 1).substring(18, outerArr.get(z).get(size - 1).length()), "0", "0"));

                } else if (size == 0) {

                    adapter.list.add(new singleRow(attrTitles.get(z), "0", "0", "0"));

                }
                adapter.notifyDataSetChanged();
            }

        }
    }


}

class singleRow {
    String title;
    String value1;
    String value2;
    String value3;

    singleRow(String title, String value1, String value2, String value3) {
        this.title = title;
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
    }

    public void updateValues(String uValue) {
        this.value3 = this.value2;
        this.value2 = this.value1;
        this.value1 = uValue;

    }


}


