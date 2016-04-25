package android.findstoreapp;

import android.content.Intent;
import android.findstoreapp.ActionHandler.Filter;
import android.findstoreapp.ActionHandler.Search;
import android.findstoreapp.DataStructure.Brand;
import android.findstoreapp.DataStructure.Purpose;
import android.findstoreapp.DataStructure.Store;
import android.findstoreapp.GPSHandler.GPSHandler;
import android.findstoreapp.adapter.StoresListAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.flipboard.bottomsheet.BottomSheetLayout;

import java.util.ArrayList;

public class ListDisplay extends AppCompatActivity {
    public ImageView switchToMapBtn, searchNearestBtn, storeFilterBtn, settingBtn;
    public RelativeLayout startLayout;
    public BottomSheetLayout bottomSheet;
    public ListView storesLV;
    public View filerView;
    public Spinner districtSpn, citySpn, brandSpn;
    public Button filterBtn;
    private ArrayList<Store> storeList = new ArrayList<>();
    private ArrayList<Store> searchResult = new ArrayList<>();
    private StoresListAdapter adapter;
    private float latitude, longitude;
    private GPSHandler gpsHandler;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_display);

        startLayout = (RelativeLayout) findViewById(R.id.start_layout);

        bottomSheet = (BottomSheetLayout) findViewById(R.id.bottomsheet);

        switchToMapBtn = (ImageView) findViewById(R.id.switch_to_map_btn);
        searchNearestBtn = (ImageView) findViewById(R.id.search_nearest_btn);
        storeFilterBtn = (ImageView) findViewById(R.id.store_filter_btn);
        settingBtn = (ImageView) findViewById(R.id.setting_btn);

        storesLV = (ListView) findViewById(R.id.stores_lv);
        adapter = new StoresListAdapter(ListDisplay.this, storeList);
        storesLV.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        filerView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.store_filter, bottomSheet, false);
        districtSpn = (Spinner) filerView.findViewById(R.id.district_spn);
        citySpn = (Spinner) filerView.findViewById(R.id.city_spn);
        brandSpn = (Spinner) filerView.findViewById(R.id.brand_spn);

        initializeSpinner1();
        initializeSpinner2();
        initializeSpinner3();

        filterBtn = (Button) filerView.findViewById(R.id.filter_btn);

        startLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLayout.setVisibility(View.INVISIBLE);
                searchNearestBtn.performClick();
            }
        });

        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String districtCon = String.valueOf(districtSpn.getSelectedItem());
                String cityCon = String.valueOf(citySpn.getSelectedItem());
                String brandCon = String.valueOf(brandSpn.getSelectedItem());
                ArrayList<Store> filterResult = new ArrayList<>();
                filterResult.addAll(searchResult);
                if (!districtCon.equals(Filter.SHOW_ALL)) {
                    ArrayList<Store> disResult = Filter.filterBydistrict(filterResult, districtCon);
                    filterResult.clear();
                    filterResult.addAll(disResult);
                }

                if (!cityCon.equals(Filter.SHOW_ALL)) {
                    ArrayList<Store> cityResult = Filter.filterByCity(filterResult, cityCon);
                    filterResult.clear();
                    filterResult.addAll(cityResult);
                }
                if (!brandCon.equals(Filter.SHOW_ALL)) {
                    Log.i("BEFORE BRAND SIZE", String.valueOf(filterResult.size()));
                    ArrayList<Store> brandResult = Filter.filterByBrand(filterResult, brandCon);
                    Log.i("BRAND SIZE", String.valueOf(brandResult.size()));
                    filterResult.clear();
                    filterResult.addAll(brandResult);
                }
                storeList.clear();
                storeList.addAll(filterResult);
                adapter.notifyDataSetChanged();
            }
        });

        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ListDisplay.this, android.findstoreapp.Setting.class);
                startActivity(i);
            }
        });

        storesLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(ListDisplay.this, StoreInfo.class);
                Store s = storeList.get(position);
                Brand b = s.getBrand();
                i.putExtra("address", s.getAddress());
                i.putExtra("district", s.getDistrict());
                i.putExtra("city", s.getCity());
                i.putExtra("brand", b.getName());
                i.putExtra("purposes", copyPurposesToArray(b.getPurposes()));
                i.putExtra("latitude", String.valueOf(s.getLatitude()));
                i.putExtra("longitude", String.valueOf(s.getLongitude()));
                startActivity(i);
            }
        });

        storeFilterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheet.showWithSheetView(filerView);
            }
        });

        switchToMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ListDisplay.this, MapDisplay.class);
                ArrayList<String> latitudes, longitudes, stores;
                if (storeList.size() > 0) {
                    latitudes = new ArrayList<>();
                    longitudes = new ArrayList<>();
                    stores = new ArrayList<>();

                    for (Store s : storeList) {
                        latitudes.add(String.valueOf(s.getLatitude()));
                        longitudes.add(String.valueOf(s.getLongitude()));
                        stores.add(String.valueOf(s.getName()));
                    }
                    i.putStringArrayListExtra("latitudes", latitudes);
                    i.putStringArrayListExtra("longitudes", longitudes);
                    i.putStringArrayListExtra("stores", stores);
                    i.putExtra("status", 1);
                }
                startActivity(i);
            }
        });


        searchNearestBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                gpsHandler = new GPSHandler(ListDisplay.this);
                if (gpsHandler.canGetLocation()) {
                    latitude = (float) gpsHandler.getLatitude();
                    longitude = (float) gpsHandler.getLongitude();
                    if ((int) latitude == (int) longitude && (int) latitude == 0) {
                        handler.postDelayed(keepGetGPS, 1200);
                        Toast.makeText(ListDisplay.this, "Finding stores...", Toast.LENGTH_SHORT).show();
                        //Toast.makeText(ListDisplay.this, "Lat: " + latitude + "  Long: " + longitude, Toast.LENGTH_SHORT).show();
                    } else {
                        ArrayList<Store> stores = Search.searchNearest(latitude, longitude, getApplicationContext());
                        if (stores.size() == 0) {
                            Toast.makeText(FindStoreApplication.getAplicationContext(), "No founded store in this area. You should want to update your data and try again.", Toast.LENGTH_SHORT).show();
                        } else {
                            handler.removeCallbacks(keepGetGPS);
                            gpsHandler.stopUsingGPS();
                            updateStoresList(stores);
                            initializeSpinner1();
                            initializeSpinner2();
                            initializeSpinner3();
                        }
                    }

                } else {
                    gpsHandler.showSettingsAlert();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    private void updateStoresList(ArrayList<Store> list) {
        searchResult.clear();
        for (Store s : list) {
            searchResult.add(s);
        }
        storeList.clear();
        storeList.addAll(searchResult);
        adapter.notifyDataSetChanged();
    }

    public ArrayList<String> copyPurposesToArray(ArrayList<Purpose> purposes) {
        ArrayList<String> demands = new ArrayList<>();
        if (purposes == null) return null;
        for (Purpose p : purposes) {
            demands.add(p.getName());
        }
        return demands;
    }

    private void initializeSpinner1() {
        ArrayList<String> districts = Filter.filterDistinctDistrict(storeList);
        districts.add(0, Filter.SHOW_ALL);
        ArrayAdapter arrayAdapter = new ArrayAdapter(filerView.getContext(), R.layout.spinner_item, districts);
        arrayAdapter.setDropDownViewResource(R.layout.spiner_dropdown_item);
        districtSpn.setAdapter(arrayAdapter);
    }

    private void initializeSpinner2() {
        ArrayList<String> citys = Filter.filterDistinctCity(storeList);
        citys.add(0, Filter.SHOW_ALL);
        ArrayAdapter arrayAdapter = new ArrayAdapter(filerView.getContext(), R.layout.spinner_item, citys);
        arrayAdapter.setDropDownViewResource(R.layout.spiner_dropdown_item);
        citySpn.setAdapter(arrayAdapter);
    }

    private void initializeSpinner3() {
        ArrayList<String> brands = Filter.filterDistinctBrand(storeList);
        brands.add(0, Filter.SHOW_ALL);
        ArrayAdapter arrayAdapter = new ArrayAdapter(filerView.getContext(), R.layout.spinner_item, brands);
        arrayAdapter.setDropDownViewResource(R.layout.spiner_dropdown_item);
        brandSpn.setAdapter(arrayAdapter);
    }

    private final Runnable keepGetGPS = new Runnable() {
        @Override
        public void run() {
            searchNearestBtn.performClick();
        }
    };
}
