package android.findstoreapp.ActionHandler;


import android.content.Context;
import android.findstoreapp.DataStructure.Store;
import android.findstoreapp.DatabaseHandler.DatabaseHandler;
import android.findstoreapp.Utils.Utils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

public class Search {
    public static ArrayList<Store> searchNearest(float latitude, float longitude, Context context) {
        DatabaseHandler dbHandler = new DatabaseHandler(context);
        ArrayList<Store> stores = dbHandler.getAllStore();
        ArrayList<Store> nearStores = new ArrayList<>();
        int minimum = Integer.parseInt(dbHandler.getMinimumSetting());
        Log.i("MINIMUM DIST", String.valueOf(minimum));
        for (Store store : stores) {
            double distance = Utils.distanceCalculate(latitude, longitude, store.getLatitude(), store.getLongitude());
            if (distance <= minimum) {
                store.setDistance(distance);
                nearStores.add(store);
            }
        }
        Collections.sort(nearStores);
        return nearStores;
    }
}
