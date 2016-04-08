package android.findstoreapp;

import android.content.Context;
import android.findstoreapp.ActionHandler.Update;
import android.findstoreapp.CloudHandler.CloudHandler;
import android.findstoreapp.CloudHandler.VolleySingleton;
import android.findstoreapp.DatabaseHandler.DatabaseHandler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.appyvet.rangebar.RangeBar;

public class Setting extends AppCompatActivity {

    private static final int INITIALIZE_RANGE = 5;

    public Toolbar toolbar;
    public TextView rangeTV;
    public RangeBar radiusRB;
    public int range;

    public Button updateBtn;
    private final DatabaseHandler db = new DatabaseHandler(FindStoreApplication.getAplicationContext());
    private final Context context = this;
    private final CloudHandler cloudHandler = new CloudHandler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        range = Integer.parseInt(db.getMinimumSetting());
        radiusRB = (RangeBar) findViewById(R.id.rangebar);
        radiusRB.setSeekPinByIndex(range - INITIALIZE_RANGE);

        rangeTV = (TextView) findViewById(R.id.radiusTv);
        rangeTV.setText(String.format("%s km", range));

        radiusRB.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                range = Integer.parseInt(rightPinValue);
                rangeTV.setText(String.format("%s km", range));
            }
        });

        updateBtn = (Button) findViewById(R.id.updateBtn);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://storefinder.firebaseio.com/Brands.json", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("RESPONSE", response);
                        db.clearDB();
                        Update.UpdateAll(FindStoreApplication.getAplicationContext(), response);
                        Toast.makeText(context, "Success update", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Error update. Please check your network connection and try again.", Toast.LENGTH_SHORT).show();
                    }
                });
                VolleySingleton.getInstance().getRequestQueue().add(stringRequest);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        db.setMinimumSetting(range);
        super.onBackPressed();
    }
}
