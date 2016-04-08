package android.findstoreapp.CloudHandler;

import android.findstoreapp.FindStoreApplication;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {
    private static VolleySingleton volleyInstance = null;
    private RequestQueue requestQueue;

    private VolleySingleton() {
        requestQueue = Volley.newRequestQueue(FindStoreApplication.getAplicationContext());
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public static VolleySingleton getInstance() {
        if (volleyInstance == null) {
            volleyInstance = new VolleySingleton();
        }
        return volleyInstance;
    }
}
