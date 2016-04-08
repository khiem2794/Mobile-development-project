package android.findstoreapp;

import android.content.Intent;
import android.findstoreapp.DataStructure.StaticValue;
import android.findstoreapp.GPSHandler.GPSHandler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapDisplay extends FragmentActivity implements OnMapReadyCallback {

    public MapFragment mapFragment;
    public FloatingActionButton fab;

    public Intent i;
    public ArrayList<String> latitudes, longitudes, stores;
    public boolean status = false;
    public GPSHandler gpsHandler;
    public double myLatitude, myLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_display);
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        i = getIntent();
        if (i.getIntExtra("status", 0) == 1) {
            latitudes = i.getStringArrayListExtra("latitudes");
            longitudes = i.getStringArrayListExtra("longitudes");
            stores = i.getStringArrayListExtra("stores");
            status = true;
        }

        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        gpsHandler = new GPSHandler(MapDisplay.this);
        if (gpsHandler.canGetLocation()) {
            myLatitude = gpsHandler.getLatitude();
            myLongitude = gpsHandler.getLongitude();
        } else {
            myLatitude = 10.879875;
            myLongitude = 106.77511;
            gpsHandler.showSettingsAlert();
        }
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map_display, menu);
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

    @Override
    public void onMapReady(GoogleMap googleMap) {


        if (status) {

            if (stores.size() == 1) {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(Double.valueOf(latitudes.get(0)), Double.valueOf(longitudes.get(0))), 16));
            } else {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(myLatitude, myLongitude), 15));
            }
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(myLatitude, myLongitude))
                    .title("Me")).showInfoWindow();
            for (int i = 0; i < stores.size(); i++) {
                int icon;
                switch (stores.get(i)) {
                    case StaticValue.SATRAFOOD_NAME:
                        icon = R.drawable.satrafoods_marker;
                        break;
                    case StaticValue.SHOPANDGO_NAME:
                        icon = R.drawable.shopgo_marker;
                        break;
                    default:
                        icon = R.drawable.circlek_marker;
                }
                googleMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(icon))
                        .position(new LatLng(Double.valueOf(latitudes.get(i)), Double.valueOf(longitudes.get(i))))
                        .title(stores.get(i)));
            }
        }
    }
}
