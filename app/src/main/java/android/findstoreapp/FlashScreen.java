package android.findstoreapp;

import android.content.Intent;
import android.findstoreapp.GPSHandler.GPSHandler;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class FlashScreen extends AppCompatActivity {

    private final static int SPLASH_TIME = 3000;
    public static ImageView backgroundImV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flash_screen);
        backgroundImV = (ImageView) findViewById(R.id.flash_screen_background);
        new BackgroundTask().execute();
    }

    private class BackgroundTask extends AsyncTask {

        public Intent intent;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            intent = new Intent(FlashScreen.this, ListDisplay.class);
        }

        @Override
        protected Object doInBackground(Object[] params) {
            try {
                Thread.sleep(SPLASH_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            /*
            *  Pass your loaded data here using Intent
            *
            *  intent.putExtra("data_key", data_value);
            * */
            backgroundImV.setImageResource(R.drawable.flash_screen_background2);
            startActivity(intent);
            finish();
        }
    }

}
