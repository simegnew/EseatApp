package com.app.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.ActionBar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.app.myapplication.HelperUtils.HelperUtilities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class OneWayActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private Cursor cursor;
    private String origin;
    private String destination;
    private String departureDate;
    private String returnDate;
    private TextView txtMessage;
    private ListView onewayList;
    private Bundle bundle;
    private int currentTab;
    private int oneWayRouteID;
    private int outboundRouteID;
    private int returnRouteID;
    private Intent intent;
    private ActionBar actionBar;
    //custom dialog view
    private View dialogLayout;
    private TextView txtSortBy;
    private ListView sortList;
    private Button btnSort;
    private int sortByID = 100;
    private TextView routeNotFound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_way);

        actionBar = getSupportActionBar();

        sharedPreferences = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        currentTab = sharedPreferences.getInt("CURRENT_TAB", 0);
        origin = sharedPreferences.getString("ORIGIN", "");
        destination = sharedPreferences.getString("DESTINATION", "");
        departureDate = sharedPreferences.getString("DEPARTURE_DATE", "");
        returnDate = sharedPreferences.getString("RETURN_DATE", "");
        sortByID = sharedPreferences.getInt("ONEWAY_SORT_ID", 100);

        //Toast.makeText(getApplicationContext(), flightClass, Toast.LENGTH_SHORT).show();

        btnSort = (Button) findViewById(R.id.btnSort);

        routeNotFound = (TextView)findViewById(R.id.txtOneWayNotFound);

        routeNotFound.setVisibility(View.INVISIBLE);


        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortDialog().show();
            }
        });

        onewayList = (ListView) findViewById(R.id.onewayList);

        searchOneWay(sortByID);
    }

    private void searchOneWay(int sortByID) {


        try {
            Connection con = ConnectionHelper.CONN();

            if (sortByID == 0) {
//                cursor = DatabaseHelper.selectFlight(db, origin, destination,
//                        departureDate, flightClass, "FARE");
            } else if (sortByID == 1) {
//                cursor = DatabaseHelper.selectFlight(db, origin, destination,
//                        departureDate, flightClass, "FLIGHTDURATION");
            } else {
//                cursor = DatabaseHelper.selectFlight(db, origin, destination,
//                        departureDate, flightClass);
            }



            if (cursor != null && cursor.getCount() > 0) {

                actionBar.setTitle("Select one way flight");
                actionBar.setSubtitle(HelperUtilities.capitalize(origin) + " -> " + HelperUtilities.capitalize(destination));

                CursorAdapter listAdapter = new SimpleCursorAdapter(getApplicationContext(),
                        R.layout.custom_list_view,
                        cursor,
                        new String[]{"DEPARTURETIME", "ARRIVALTIME", "FARE", "AIRLINENAME",
                                "FLIGHTDURATION", "FLIGHTCLASSNAME"},
                        new int[]{R.id.txtDepartureTime, R.id.txtArrivalTime, R.id.txtFare,
                                R.id.txtAirline, R.id.txtTravelTime, R.id.txtClass},
                        0);

                onewayList.setAdapter(listAdapter);

            } else {

                routeNotFound.setVisibility(View.VISIBLE);
                btnSort.setVisibility(View.INVISIBLE);
            }

            onewayList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                    oneWayRouteID = (int) id;
                    intent = new Intent(getApplicationContext(), CheckOutActivity.class);

                    sharedPreferences = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove("ONEWAY_FLIGHT_ID");
                    editor.putInt("ONEWAY_FLIGHT_ID", oneWayRouteID);

                    editor.commit();

                    startActivity(intent);
                    finish();
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Database error", Toast.LENGTH_SHORT).show();
        }
    }



    //sort by dialog (one way)
    public Dialog sortDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sort by")
                .setItems(R.array.sort, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {

                        sharedPreferences = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove("ONEWAY_SORT_ID");
                        editor.putInt("ONEWAY_SORT_ID", id);
                        editor.commit();

                        intent = new Intent(getApplicationContext(), OneWayActivity.class);
                        startActivity(intent);


                    }
                });

        return builder.create();
    }

}
