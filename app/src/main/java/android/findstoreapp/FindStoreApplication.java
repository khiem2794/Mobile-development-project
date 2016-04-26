package android.findstoreapp;

import android.app.Application;
import android.content.Context;
import android.findstoreapp.DataStructure.*;
import android.findstoreapp.DataStructure.Setting;
import android.findstoreapp.DatabaseHandler.DatabaseHandler;

import java.util.Set;

public class FindStoreApplication extends Application {
    private static FindStoreApplication findStoreApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        customizeRange(FindStoreApplication.this);
        findStoreApplication = this;
    }

    private void customizeRange(Context context) {
       DatabaseHandler db = new DatabaseHandler(context);
        int range = Integer.parseInt(db.getMinimumSetting());
        Setting.CURRENT_DISTANCE = range;
        if(range > Setting.MAXIMUM_DISTANCE) {
            db.setMinimumSetting(Setting.DEFAULT_DISTANCE);
            Setting.CURRENT_DISTANCE = Setting.DEFAULT_DISTANCE;
        }
        db.closeDB();
    }

    public static FindStoreApplication getInstance() {
        return findStoreApplication;
    }

    public static Context getAplicationContext() {
        return findStoreApplication.getApplicationContext();
    }
}