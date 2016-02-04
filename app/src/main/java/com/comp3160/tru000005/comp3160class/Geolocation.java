package com.comp3160.tru000005.comp3160class;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by alial on 2016-01-29.
 */
public class Geolocation extends Activity{

    LocationManager locationManager;
    LocationListener locationListener;
    TextView loctext01, latt, longtt;
    String bestProvider;
    double lattitude;
    double longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geolocation);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        loctext01 = (TextView) findViewById(R.id.textView13);
        latt = (TextView) findViewById(R.id.textView9);
        longtt = (TextView) findViewById(R.id.textView11);
        // 1. choose the best location provider
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(false);

        bestProvider =
                locationManager.getBestProvider(criteria, true);
        loctext01.setText("1- Recommended Location provider is " + bestProvider +"\n\n");
        Log.d("Location", "1- Recommended Location provider is " + bestProvider);

        // 4. getLastKnownLocation
        Location LastKnownLocation = locationManager.getLastKnownLocation(bestProvider);
        UpdateLocation(LastKnownLocation, "Last Known Location");

        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                Log.d("Location", "2- A new location is found by the location provider ");
                UpdateLocation(location, "Location Changed");
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

    }

    public void UpdateLocation(Location location, String state){

        lattitude = location.getLatitude();
        longitude = location.getLongitude();
        Log.d("Location", "** " + state + " ** - Lattitue = " + lattitude + ", and Longitude = " + longitude);
        latt.setText(String.valueOf(lattitude));
        longtt.setText(String.valueOf(longitude));

        Geocoder geocoder= new Geocoder(this, Locale.ENGLISH);
        try {
            List<Address> addresses = geocoder.getFromLocation(lattitude,longitude, 1);
            if(addresses != null) {

                Address fetchedAddress = addresses.get(0);
                StringBuilder strAddress = new StringBuilder();

                for(int i=0; i<fetchedAddress.getMaxAddressLineIndex(); i++) {
                    strAddress.append(fetchedAddress.getAddressLine(i)).append("\n");
                }

                Log.d("TAG","I am at: " +strAddress.toString());
                loctext01.append("I am at: " +strAddress.toString()+"\n\n");


            }

            else
                Log.d("TAG", "No location found..!");

        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Could not get address..!", Toast.LENGTH_LONG).show();
        }
    }



    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(locationListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(bestProvider, 0, 0, locationListener);
    }

}
