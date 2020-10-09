package com.example.willy.newapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

public class MapActivity extends FragmentActivity implements NavigationView.OnNavigationItemSelectedListener,
        LocationListener, OnMapReadyCallback, GoogleMap.OnMarkerClickListener, View.OnClickListener {

    private GoogleMap mMap;
    private LocationManager locationManager;

    private TextView btnEmergencia;
    private Button rg;
    SlidingUpPanelLayout sl;

    boolean clicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapa);
        mapFragment.getMapAsync(this);


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                2000,
                3, this);

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                2000,
                3, this);

        btnEmergencia = (TextView) findViewById(R.id.btnEmergencia);
        addClickEffect(btnEmergencia);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        rg = (Button) findViewById(R.id.registrarBus);

        rg.setOnClickListener(this);

        sl = (SlidingUpPanelLayout) findViewById(R.id.sliding);


        sl.setDragView(btnEmergencia);
        btnEmergencia.setOnClickListener(this);

        /*if(sl.isClipPanel()) {
            Toast.makeText(getApplicationContext(), "YES1", Toast.LENGTH_LONG).show();
        }*/

    }

    private int count = 0;
    @Override
    public void onBackPressed() {
        //Toast.makeText(getApplicationContext(), "xDD", Toast.LENGTH_LONG).show();
       if(clicked) {
           sl.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
           clicked = false;
       } else {
           super.onBackPressed();
       }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_incomes) {

            startActivity(new Intent(this, com.example.willy.newapp.EmpresaActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.act_main);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    void addClickEffect(View view)
    {
        Drawable drawableNormal = view.getBackground();

        Drawable drawablePressed = view.getBackground().getConstantState().newDrawable();
        drawablePressed.mutate();
        drawablePressed.setColorFilter(Color.argb(50, 0, 0, 0), PorterDuff.Mode.SRC_ATOP);

        StateListDrawable listDrawable = new StateListDrawable();
        listDrawable.addState(new int[] {android.R.attr.state_pressed}, drawablePressed);
        listDrawable.addState(new int[] {}, drawableNormal);
        view.setBackground(listDrawable);
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setOnMarkerClickListener(this);
        mMap.setMyLocationEnabled(true);
    }


    Marker markerd;
    boolean first = true;
    @Override
    public void onLocationChanged(Location location) {
        LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, 18);
        mMap.animateCamera(update);

        //mMap.addMarker(new MarkerOptions().title("xd").position(ll));


        markerd = mMap.addMarker(new MarkerOptions()
                .position(ll)
                .flat(true).anchor(.5f, 0.5f));

        LatLng correctLocation = null;

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onClick(View view) {
        if(view == rg) {
            //clicked = true;
            sl.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else if(view == btnEmergencia) {
            sl.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
            clicked = true;
        }
    }


}
