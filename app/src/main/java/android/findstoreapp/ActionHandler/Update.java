package android.findstoreapp.ActionHandler;


import android.content.Context;
import android.findstoreapp.CloudHandler.CloudHandler;
import android.findstoreapp.DataStructure.Brand;
import android.findstoreapp.DatabaseHandler.DatabaseHandler;

import java.util.ArrayList;

public class Update {
    public static void UpdateAll(Context context, String data){
        CloudHandler cloudHandler = new CloudHandler();
        ArrayList<Brand> brands =  cloudHandler.retrieveAllBrands(data);
        DatabaseHandler dbHandler = new DatabaseHandler(context);
        for (Brand brand : brands) {
            dbHandler.insertBrand(brand);
        }
    }
}
