package android.findstoreapp.Utils;


import android.findstoreapp.DataStructure.Store;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static double distanceCalculate(float userLat, float userLong, float storeLat, float storeLong) {
        double dlon = Math.toRadians(storeLong - userLong);
        double dlat = Math.toRadians(storeLat - userLat);
        double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(Math.toRadians(userLat)) * Math.cos(Math.toRadians(storeLat)) * Math.pow(Math.sin(dlon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = 6371 * c;
        return d;
    }

    public static ArrayList<String> getDistanceFromMe(float userLat, float userLong, List<Store> stores, double range) {
        ArrayList<String> distances = new ArrayList<>();
        for (Store store : stores) {
            double distance = Utils.distanceCalculate(userLat, userLong, store.getLatitude(), store.getLongitude());
            if (distance <= range) {
                distances.add(String.format("%.1f", distance));
            }
        }
        return distances;
    }
}
