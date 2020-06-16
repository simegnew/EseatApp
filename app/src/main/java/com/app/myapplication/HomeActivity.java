package com.app.myapplication;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.app.myapplication.HelperUtils.HelperUtilities;
import com.google.android.material.navigation.NavigationView;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private SharedPreferences sharedPreferences;
    private Cursor cursor;
    private Intent intent;
    private ProgressBar progressBar;
    private int currentTab;

    //date picker dialog
    private DatePickerDialog datePickerDialog1;
    private DatePickerDialog datePickerDialog2;
    private DatePickerDialog datePickerDialog3;

    //current date
    private int year;
    private int month;
    private int day;

    //id of date picker controls
    private final int ONE_WAY_DEPARTURE_DATE_PICKER = R.id.btnOneWayDepartureDatePicker;
    private final int ROUND_DEPARTURE_DATE_PICKER = R.id.btnRoundDepartureDatePicker;
    private final int ROUND_RETURN_DATE_PICKER = R.id.btnRoundReturnDatePicker;

    //add and remove image button controls in the dialog
    private ImageButton imgBtnAdd;
    private ImageButton imgBtnRemove;

    //custom dialog view
    private View dialogLayout;

    //one way UI controls
    private AutoCompleteTextView txtOneWayFrom;
    private AutoCompleteTextView txtOneWayTo;
    private Button btnOneWayDepartureDatePicker;

    //round trip UI controls
    private AutoCompleteTextView txtRoundFrom;
    private AutoCompleteTextView txtRoundTo;
    private Button btnRoundDepartureDatePicker;
    private Button btnRoundReturnDatePicker;

    //search button
    private Button btnSearch;

    private String oneWayDepartureDate, roundDepartureDate, roundReturnDate;
    private View header;
    private ImageView imgProfile;
    private int clientID;
    private int tempYear;
    private int tempMonth;
    private int tempDay;
    SimpleAdapter ADAhere;
    ResultSet rs;

    private boolean isValidOneWayDate = true;
    private boolean isValidRoundDate = true;
    private Spinner spinnercountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);

      //  ConnectionHelper con = new ConnectionHelper();

        spinnercountry = (Spinner) findViewById(R.id.spinnercountry);



       /* try {
                    Connection con = ConnectionHelper.CONN();
                  String query = "select locationName from Location";
                    PreparedStatement statement = con.prepareStatement(query);
                    final ArrayList list = new ArrayList();
                    rs = statement.executeQuery();
            ArrayList<String> data = new ArrayList<String>();
                    while (rs.next()) {
                        String id = rs.getString("locationName");
                        data.add(id);

                    }
            String[] array = data.toArray(new String[0]);
            ArrayAdapter NoCoreAdapter = new ArrayAdapter(this,
                    android.R.layout.simple_list_item_1, data);
            spinnercountry.setAdapter(NoCoreAdapter);

               } catch (SQLException | java.sql.SQLException e) {
                  e.printStackTrace();
                    Toast.makeText(HomeActivity.this, e.getMessage().toString(),Toast.LENGTH_LONG).show();
                }

        spinnercountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                String name = spinnercountry.getSelectedItem().toString();
                Toast.makeText(HomeActivity.this, name, Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

*/
        setSupportActionBar(toolbar);
        //navigation drawer manager
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        header = navigationView.getHeaderView(0);

       // clientID = clientID();

        //tab host manager
        final TabHost tabHost = (TabHost) findViewById(R.id.tabhost);
        tabHost.setup();

        //Tab 1
        TabHost.TabSpec spec = tabHost.newTabSpec("Tab One");
        spec.setContent(R.id.tab1);
        spec.setIndicator("One way");
        tabHost.addTab(spec);

        //Tab 2
        spec = tabHost.newTabSpec("Tab Two");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Round Trip");
        tabHost.addTab(spec);


        //tab text color
        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
            TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(getResources().getColor(R.color.colorInverted));
        }


        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                currentTab = tabHost.getCurrentTab();
            }
        });

//       ArrayAdapter<String> ada = new ArrayAdapter<String>(this,
//               android.R.layout.simple_dropdown_item_1line, CITIES);

        //one way form
/*        txtOneWayFrom.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                CITIES city = new CITIES();
                city.execute("");

            }
        });*/

        txtOneWayFrom = (AutoCompleteTextView) findViewById(R.id.txtOneWayFrom);
        //txtOneWayFrom.setAdapter(ada);
        try {
            Connection con = ConnectionHelper.CONN();
            String query = "select locationName from Location";
            PreparedStatement statement = con.prepareStatement(query);
            final ArrayList list = new ArrayList();
            rs = statement.executeQuery();
            ArrayList<String> data = new ArrayList<String>();
            while (rs.next()) {
                String id = rs.getString("locationName");
                data.add(id);

            }
            String[] array = data.toArray(new String[0]);
            ArrayAdapter NoCoreAdapter = new ArrayAdapter(this,
                    android.R.layout.simple_dropdown_item_1line, data);
            txtOneWayFrom.setAdapter(NoCoreAdapter);

        } catch (SQLException | java.sql.SQLException e) {
            e.printStackTrace();
            Toast.makeText(HomeActivity.this, e.getMessage().toString(),Toast.LENGTH_LONG).show();
        }

        txtOneWayFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                String name = txtOneWayFrom.getText().toString();
                Toast.makeText(HomeActivity.this, name, Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        txtOneWayTo = (AutoCompleteTextView) findViewById(R.id.txtOneWayTo);
        //txtOneWayTo.setAdapter(ada);

        try {
            Connection con = ConnectionHelper.CONN();
            String query = "select locationName from Location";
            PreparedStatement statement = con.prepareStatement(query);
            final ArrayList list = new ArrayList();
            rs = statement.executeQuery();
            ArrayList<String> data = new ArrayList<String>();
            while (rs.next()) {
                String id = rs.getString("locationName");
                data.add(id);

            }
            String[] array = data.toArray(new String[0]);
            ArrayAdapter NoCoreAdapter = new ArrayAdapter(this,
                    android.R.layout.simple_dropdown_item_1line, data);
            txtOneWayTo.setAdapter(NoCoreAdapter);

        } catch (SQLException | java.sql.SQLException e) {
            e.printStackTrace();
            Toast.makeText(HomeActivity.this, e.getMessage().toString(),Toast.LENGTH_LONG).show();
        }

        txtOneWayTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                String name = txtOneWayTo.getText().toString();
                Toast.makeText(HomeActivity.this, name, Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnOneWayDepartureDatePicker = (Button) findViewById(R.id.btnOneWayDepartureDatePicker);



        //round trip form
        txtRoundFrom = (AutoCompleteTextView) findViewById(R.id.txtRoundFrom);
       // txtRoundFrom.setAdapter(ada);
        try {
            Connection con = ConnectionHelper.CONN();
            String query = "select locationName from Location";
            PreparedStatement statement = con.prepareStatement(query);
            final ArrayList list = new ArrayList();
            rs = statement.executeQuery();
            ArrayList<String> data = new ArrayList<String>();
            while (rs.next()) {
                String id = rs.getString("locationName");
                data.add(id);

            }
            String[] array = data.toArray(new String[0]);
            ArrayAdapter NoCoreAdapter = new ArrayAdapter(this,
                    android.R.layout.simple_dropdown_item_1line, data);
            txtRoundFrom.setAdapter(NoCoreAdapter);

        } catch (SQLException | java.sql.SQLException e) {
            e.printStackTrace();
            Toast.makeText(HomeActivity.this, e.getMessage().toString(),Toast.LENGTH_LONG).show();
        }

        txtRoundFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                String name = txtRoundFrom.getText().toString();
                Toast.makeText(HomeActivity.this, name, Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        txtRoundTo = (AutoCompleteTextView) findViewById(R.id.txtRoundTo);
        //txtRoundTo.setAdapter(ada);
        try {
            Connection con = ConnectionHelper.CONN();
            String query = "select locationName from Location";
            PreparedStatement statement = con.prepareStatement(query);
            final ArrayList list = new ArrayList();
            rs = statement.executeQuery();
            ArrayList<String> data = new ArrayList<String>();
            while (rs.next()) {
                String id = rs.getString("locationName");
                data.add(id);

            }
            String[] array = data.toArray(new String[0]);
            ArrayAdapter NoCoreAdapter = new ArrayAdapter(this,
                    android.R.layout.simple_dropdown_item_1line, data);
            txtRoundTo.setAdapter(NoCoreAdapter);

        } catch (SQLException | java.sql.SQLException e) {
            e.printStackTrace();
            Toast.makeText(HomeActivity.this, e.getMessage().toString(),Toast.LENGTH_LONG).show();
        }

        txtRoundTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                String name = txtRoundTo.getText().toString();
                Toast.makeText(HomeActivity.this, name, Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnRoundDepartureDatePicker = (Button) findViewById(R.id.btnRoundDepartureDatePicker);
        btnRoundReturnDatePicker = (Button) findViewById(R.id.btnRoundReturnDatePicker);


        btnSearch = (Button) findViewById(R.id.btnSearch);
        imgProfile = (ImageView) header.findViewById(R.id.imgProfile);


        year = HelperUtilities.currentYear();
        month = HelperUtilities.currentMonth();
        day = HelperUtilities.currentDay();

//        drawerProfileInfo();
//        loadImage(clientID);


        //one way departure date picker on click listener
        btnOneWayDepartureDatePicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                datePickerDialog(ONE_WAY_DEPARTURE_DATE_PICKER).show();

            }
        });

        //round trip departure date picker on click listener
        btnRoundDepartureDatePicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                datePickerDialog(ROUND_DEPARTURE_DATE_PICKER).show();
            }
        });

        //round trip return date picker on click listener
        btnRoundReturnDatePicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                datePickerDialog(ROUND_RETURN_DATE_PICKER).show();
            }
        });


        //searches available flights on click
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //call search method here

                if (currentTab == 0) {

                    if (isValidOneWayInput() && isValidOneWayDate) {
                        searchOneWayFlight();

                    }

                } else if (currentTab == 1) {

                    if (isValidRoundInput() && isValidRoundDate) {
                        searchRoundFlight();
                    }
                }

            }
        });

   }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handles navigation view item on clicks
        int id = item.getItemId();

        if (id == R.id.nav_home) {
//            Intent intent = new Intent(getApplicationContext(), ItineraryActivity.class);
//            startActivity(intent);
        } else if (id == R.id.nav_profile) {
//            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
//            startActivity(intent);
        } else if (id == R.id.nav_security) {
//            Intent intent = new Intent(getApplicationContext(), SecurityActivity.class);
//            startActivity(intent);

        } else if (id == R.id.nav_about) {
//            Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
//            startActivity(intent);

        } else if (id == R.id.nav_logout) {

//            getApplicationContext().getSharedPreferences(LoginActivity.MY_PREFERENCES, 0).edit().clear().commit();
//            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//            startActivity(intent);
//            finish();

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public DatePickerDialog datePickerDialog(int datePickerId) {

        switch (datePickerId) {
            case ONE_WAY_DEPARTURE_DATE_PICKER:

                if (datePickerDialog1 == null) {
                    datePickerDialog1 = new DatePickerDialog(this, getOneWayDepartureDatePickerListener(), year, month, day);
                }
                datePickerDialog1.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                return datePickerDialog1;

            case ROUND_DEPARTURE_DATE_PICKER:

                if (datePickerDialog2 == null) {
                    datePickerDialog2 = new DatePickerDialog(this, getRoundDepartureDatePickerListener(), year, month, day);
                }
                datePickerDialog2.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                return datePickerDialog2;

            case ROUND_RETURN_DATE_PICKER:

                if (datePickerDialog3 == null) {
                    datePickerDialog3 = new DatePickerDialog(this, getRoundReturnDatePickerListener(), year, month, day);
                }
                datePickerDialog3.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                return datePickerDialog3;
        }
        return null;
    }

    public DatePickerDialog.OnDateSetListener getOneWayDepartureDatePickerListener() {
        return new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int startYear, int startMonth, int startDay) {

                //get one way departure date here

                oneWayDepartureDate = startYear + "-" + (startMonth + 1) + "-" + startDay;
                btnOneWayDepartureDatePicker.setText(HelperUtilities.formatDate(startYear, startMonth, startDay));

            }
        };
    }

    public DatePickerDialog.OnDateSetListener getRoundDepartureDatePickerListener() {
        return new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int startYear, int startMonth, int startDay) {

                tempYear = startYear;
                tempMonth = startMonth;
                tempDay = startDay;

                //get round trip departure date here
                roundDepartureDate = startYear + "-" + (startMonth + 1) + "-" + startDay;
                btnRoundDepartureDatePicker.setText(HelperUtilities.formatDate(startYear, startMonth, startDay));
            }
        };
    }

    public DatePickerDialog.OnDateSetListener getRoundReturnDatePickerListener() {
        return new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int startYear, int startMonth, int startDay) {

                String departureDate = tempYear + "-" + (tempMonth + 1) + "-" + tempDay;
                String returnDate = startYear + "-" + (startMonth + 1) + "-" + startDay;

                if (HelperUtilities.compareDate(departureDate, returnDate)) {
                    datePickerAlert().show();
                    isValidRoundDate = false;
                } else {
                    isValidRoundDate = true;
                    //get round trip return date here
                    roundReturnDate = startYear + "-" + (startMonth + 1) + "-" + startDay;
                    btnRoundReturnDatePicker.setText(HelperUtilities.formatDate(startYear, startMonth, startDay));
                }
            }
        };
    }

    public Dialog datePickerAlert() {
        return new AlertDialog.Builder(this)
                .setMessage("Please select a valid return date. The return date cannot be before the departure date.")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                }).create();
    }

    public Dialog datePickerOneAlert() {
        return new AlertDialog.Builder(this)
                .setMessage("Please select a departure date.")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                }).create();
    }

    public Dialog datePickerTwoAlert() {
        return new AlertDialog.Builder(this)
                .setMessage("Please select a return date.")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                }).create();
    }


    public void searchOneWayFlight() {

        intent = new Intent(getApplicationContext(), OneWayActivity.class);

        sharedPreferences = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        getApplicationContext().getSharedPreferences("PREFS", 0).edit().clear().commit();

        SharedPreferences.Editor editor = sharedPreferences.edit();


        editor.putInt("CURRENT_TAB", currentTab);
        editor.putString("ORIGIN", HelperUtilities.filter(txtOneWayFrom.getText().toString().trim()));
        editor.putString("DESTINATION", HelperUtilities.filter(txtOneWayTo.getText().toString().trim()));
        editor.putString("DEPARTURE_DATE", oneWayDepartureDate);


        editor.commit();

        startActivity(intent);


    }

    public void searchRoundFlight() {
       // intent = new Intent(getApplicationContext(), OutboundFlightListActivity.class);

        sharedPreferences = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        getApplicationContext().getSharedPreferences("PREFS", 0).edit().clear().commit();
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("CURRENT_TAB", currentTab);
        editor.putString("ORIGIN", HelperUtilities.filter(txtRoundFrom.getText().toString().trim()));
        editor.putString("DESTINATION", HelperUtilities.filter(txtRoundTo.getText().toString().trim()));
        editor.putString("DEPARTURE_DATE", roundDepartureDate);
        editor.putString("RETURN_DATE", roundReturnDate);



        editor.commit();

        startActivity(intent);
    }

    public void drawerProfileInfo() {
        try {

            //TextView profileName = (TextView) header.findViewById(R.id.profileName);
           // TextView profileEmail = (TextView) header.findViewById(R.id.profileEmail);


           /* databaseHelper = new DatabaseHelper(getApplicationContext());
            db = databaseHelper.getReadableDatabase();

            cursor = DatabaseHelper.selectClientJoinAccount(db, clientID);*/


            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                String fName = cursor.getString(0);
                String lName = cursor.getString(1);
                String email = cursor.getString(4);

                String fullName = fName + " " + lName;

//                profileName.setText(fullName);
//                profileEmail.setText(email);

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //loads image on create
    public void loadImage(int clientID) {
        try {
           /* databaseHelper = new DatabaseHelper(getApplicationContext());
            db = databaseHelper.getReadableDatabase();*/


//            cursor = DatabaseHelper.selectImage(db, clientID);

            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();

                // Create a bitmap from the byte array
                if (cursor.getBlob(0) != null) {
                    byte[] image = cursor.getBlob(0);

                    imgProfile.setImageBitmap(HelperUtilities.decodeSampledBitmapFromByteArray(image, 300, 300));
                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //validates user input
    public boolean isValidOneWayInput() {
        if (HelperUtilities.isEmptyOrNull(txtOneWayFrom.getText().toString())) {
            txtOneWayFrom.setError("Please enter the origin");
            return false;
        } else if (!HelperUtilities.isString(txtOneWayFrom.getText().toString())) {
            txtOneWayFrom.setError("Please enter a valid origin");
            return false;
        }

        if (HelperUtilities.isEmptyOrNull(txtOneWayTo.getText().toString())) {
            txtOneWayTo.setError("Please enter the destination");
            return false;
        } else if (!HelperUtilities.isString(txtOneWayTo.getText().toString())) {
            txtOneWayTo.setError("Please enter a valid destination");
            return false;
        }

        if (btnOneWayDepartureDatePicker.getText().toString().equalsIgnoreCase("departure date")) {
            datePickerOneAlert().show();
            return false;
        }
        return true;

    }


    //validates user input
    public boolean isValidRoundInput() {
        if (HelperUtilities.isEmptyOrNull(txtRoundFrom.getText().toString())) {
            txtRoundFrom.setError("Please enter the origin");
            return false;
        } else if (!HelperUtilities.isString(txtRoundFrom.getText().toString())) {
            txtRoundFrom.setError("Please enter a valid origin");
            return false;
        }

        if (HelperUtilities.isEmptyOrNull(txtRoundTo.getText().toString())) {
            txtRoundTo.setError("Please enter the destination");
            return false;
        } else if (!HelperUtilities.isString(txtRoundTo.getText().toString())) {
            txtRoundTo.setError("Please enter a valid destination");
            return false;
        }

        if (btnRoundDepartureDatePicker.getText().toString().equalsIgnoreCase("departure date")) {
            datePickerOneAlert().show();
            return false;
        }

        if (btnRoundReturnDatePicker.getText().toString().equalsIgnoreCase("return date")) {
            datePickerTwoAlert().show();
            return false;
        }
        return true;

    }


    //    public int clientID() {
//        LoginActivity.sharedPreferences = getSharedPreferences(LoginActivity.MY_PREFERENCES, Context.MODE_PRIVATE);
//        clientID = LoginActivity.sharedPreferences.getInt(LoginActivity.CLIENT_ID, 0);
//        return clientID;
//    }
//    public static final String[] CITIES = new String[] {
//            "Addis Ababa", "Bahir Dar", "Debre markos"
//
//    };


}
