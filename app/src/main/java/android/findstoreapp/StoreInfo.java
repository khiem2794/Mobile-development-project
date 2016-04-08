package android.findstoreapp;

import android.content.Intent;
import android.findstoreapp.DataStructure.Brand;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class StoreInfo extends AppCompatActivity {
    public GridView kindOfItemsGV;
    public TextView brandNameTV, storeAddressTV;
    public ImageView brandLogoImV;
    public FloatingActionButton fab;
    private Intent intent;
    private ArrayList<String> purposes;
    private String brand, address, district, city, latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_info);

        intent = getIntent();
        purposes = intent.getStringArrayListExtra("purposes");
        brand = intent.getStringExtra("brand");
        address = intent.getStringExtra("address");
        district = intent.getStringExtra("district");
        city = intent.getStringExtra("city");
        latitude = intent.getStringExtra("latitude");
        longitude = intent.getStringExtra("longitude");

        brandNameTV = (TextView) findViewById(R.id.brand_name_tv);
        storeAddressTV = (TextView) findViewById(R.id.store_address_tv);
        kindOfItemsGV = (GridView) findViewById(R.id.items_list_gv);
        brandLogoImV = (ImageView) findViewById(R.id.brand_logo_imv);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        purposes = (purposes != null) ? purposes : initializeItems();
        brandNameTV.setText(brand.toString());
        storeAddressTV.setText(String.format("Địa chỉ: %s, Quận %s, %s", address, district, city));
        setBrandLogo(brand.toString());

        ArrayAdapter<String> adapter = new ArrayAdapter(StoreInfo.this, R.layout.items_list, R.id.item, purposes);
        kindOfItemsGV.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!latitude.isEmpty() && !longitude.isEmpty()) {
                    Intent i = new Intent(StoreInfo.this, MapDisplay.class);
                    ArrayList<String> latitudes, longitudes, stores;

                    latitudes = new ArrayList<>();
                    longitudes = new ArrayList<>();
                    stores = new ArrayList<>();

                    latitudes.add(latitude);
                    longitudes.add(longitude);
                    stores.add(brand);

                    i.putStringArrayListExtra("latitudes", latitudes);
                    i.putStringArrayListExtra("longitudes", longitudes);
                    i.putStringArrayListExtra("stores", stores);
                    i.putExtra("status", 1);

                    startActivity(i);
                } else {
                    Toast.makeText(StoreInfo.this, "The store position isn't correct", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_store_info, menu);
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

    private void setBrandLogo(String brandName) {
        switch (brandName) {
            case "CircleK":
                brandLogoImV.setImageResource(R.drawable.circlek);
                break;
            case "Satrafoods":
                brandLogoImV.setImageResource(R.drawable.satrafoods);
                break;
            case "ShopAndGo":
                brandLogoImV.setImageResource(R.drawable.shopgo);
                break;
            default:
                brandLogoImV.setImageResource(R.drawable.circlek);
        }
    }

    private ArrayList<String> initializeItems() {
        ArrayList<String> result = new ArrayList<>();
        for (String purpose : purposes) {
            result.add(purpose);
        }
        return result;
    }
}
