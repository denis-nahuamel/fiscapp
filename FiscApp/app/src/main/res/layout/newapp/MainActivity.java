package com.example.willy.newapp;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class MainActivity extends AppCompatActivity
        implements
        NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        LocationListener, View.OnClickListener,
        GoogleMap.OnCameraMoveStartedListener,
        GoogleMap.OnCameraMoveListener,
        GoogleMap.OnCameraMoveCanceledListener,
        GoogleMap.OnCameraIdleListener, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private Button registrarBus;
    private static final long INTERVAL = 1000 * 5;
    private long FASTEST_INTERVAL = 1000; /* 1 sec */
    PendingResult<LocationSettingsResult> result;
    final static int REQUEST_LOCATION = 199;
    Location mCurrentLocation;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapa);
        mapFragment.getMapAsync(this);

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
           Toast.makeText(this, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();
        } else {
            showGPSDisabledAlertToUser();
        }
    }


    protected void createLocationRequest() {
        Toast.makeText(this, "CREATE LOCATION REQUEST", Toast.LENGTH_SHORT).show();
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(FASTEST_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("El GPS está desactivado en su dispositivo. ¿Te gustaría habilitarlo?")
                .setCancelable(false)
                .setPositiveButton("Aceptar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.cancel();
                        Intent callGPSSettingIntent = new Intent(
                                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(callGPSSettingIntent);
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onClick(View view) {
        if (view == registrarBus) {
            startActivity(new Intent(this, EmpresaActivity.class));
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        Toast.makeText(this, "ON CONNECTED", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        //LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
        //mMap.addMarker(new MarkerOptions().title("Marker").position(ll));
        Toast.makeText(this, "HELLO", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCameraIdle() {

    }

    @Override
    public void onCameraMoveCanceled() {

    }

    @Override
    public void onCameraMove() {

    }

    @Override
    public void onCameraMoveStarted(int i) {

    }


    Marker m1, m2;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        /*try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));

            if (!success) {
                Log.e("Error Style", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("Error Style Json", "Can't find style. Error: ", e);
        }*/
        //Initialize Google Play Services
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
//                mMap.setMyLocationEnabled(true);
                // mMap.setMinZoomPreference(15.0f);
                // mMap.setMaxZoomPreference(18.0f);
                mMap.setPadding(0,520,0,0);
//                mMap.setTrafficEnabled(true);

            }
        } else {
            buildGoogleApiClient();
//            mMap.setMyLocationEnabled(true);
            mMap.setMinZoomPreference(16.0f);
            mMap.setMaxZoomPreference(19.0f);
            mMap.setPadding(0,520,0,0);



//            mMap.setTrafficEnabled(true);
        }


        mMap.setOnCameraIdleListener(this);
        mMap.setOnCameraMoveStartedListener(this);
        mMap.setOnCameraMoveListener(this);
        mMap.setOnCameraMoveCanceledListener(this);





        //drawTest();
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener(){

            @Override
            public void onMapClick(LatLng point) {


                /*int fromIdx = getMinDistanceIndex(point);

                if(m1 != null & m2 != null) {
                    m1.remove();
                    m2.remove();
                    m1 = null;
                    m2 = null;
                }
                if(m1 == null) {
                    m1 = mMap.addMarker(new MarkerOptions().title("A").position(point));
                } else {
                    m2 = mMap.addMarker(new MarkerOptions().title("B").position(point));
                    int from = getMinDistanceIndex(m1.getPosition());
                    int to = getMinDistanceIndex(m2.getPosition());
                    Toast.makeText(getApplicationContext(), from + " -> " + to + " dis = " + calcDistance(from, to), Toast.LENGTH_SHORT).show();
                }*/
                //mMap.addMarker(new MarkerOptions().position(point));
                /*if(c == 0) {
                    drawCircle(point);
                    mycenter = point;
                }
                else {

                    Location location = new Location("0,0");
                    location.setLatitude(point.latitude);
                    location.setLongitude(point.longitude);
                    float[] distance = new float[2];
                    Location.distanceBetween( location.getLatitude(), location.getLongitude(),
                            mapCircle.getCenter().latitude, mapCircle.getCenter().longitude, distance);


                    if(distance[0] <= mapCircle.getRadius()) {
                        Toast.makeText(MainActivity.this, "Dentro", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Fuera", Toast.LENGTH_SHORT).show();
                    }


                }

                c++;*/

            }

        });
    }
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Log.d("onActivityResult()", Integer.toString(resultCode));

        //final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode)
        {
            case REQUEST_LOCATION:
                switch (resultCode)
                {
                    case Activity.RESULT_OK:
                    {
                        // All required changes were successfully made
                        Toast.makeText(com.example.willy.newapp.MainActivity.this, "Location enabled by user!", Toast.LENGTH_LONG).show();
                        break;
                    }
                    case Activity.RESULT_CANCELED:
                    {
                        // The user was asked to change settings, but chose not to
                        Toast.makeText(com.example.willy.newapp.MainActivity.this, "Location not enabled, user cancelled.", Toast.LENGTH_LONG).show();
                        break;
                    }
                    default:
                    {
                        break;
                    }
                }
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_incomes) {

            startActivity(new Intent(this, com.example.willy.newapp.ActaActivity.class));
        }

        return true;
    }
}
