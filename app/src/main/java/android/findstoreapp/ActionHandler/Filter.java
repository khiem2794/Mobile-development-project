package android.findstoreapp.ActionHandler;

import android.findstoreapp.DataStructure.Purpose;
import android.findstoreapp.DataStructure.Store;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

public class Filter {
    public static final String SHOW_ALL = "---";

    public static ArrayList<Store> filterByCity(ArrayList<Store> stores, String cityCon){
        ArrayList<Store> result = new ArrayList<>();
        for (Store store : stores) {
            if (store.getCity().equals(cityCon)){
                result.add(store);
            }
        }
//        Log.i("CITY RESULT SIZE", String.valueOf(result.size()));
        return result;
    }
    public static ArrayList<Store> filterBydistrict(ArrayList<Store> stores, String disCon){
        ArrayList<Store> result = new ArrayList<>();
        for (Store store : stores) {
            if (store.getDistrict().equals(disCon)){
                result.add(store);
            }
        }
//        Log.i("DISTRICT RESULT SIZE", String.valueOf(result.size()));
        return result;
    }
    public static ArrayList<Store> filterByBrand(ArrayList<Store> stores, String brandCon){
        ArrayList<Store> result = new ArrayList<>();
        Log.i("BEGIN FILTER BRAND", brandCon);
        for (Store store : stores) {
            if (store.getName().equals(brandCon)){
                result.add(store);
            }
        }
        Log.i("BRAND RESULT SIZE", String.valueOf(result.size()));
        return result;
    }
    public static ArrayList<Store> filterByPurposes(ArrayList<Store> stores, Purpose Pur){
        ArrayList<Store> result = new ArrayList<>();
        for (Store store : stores) {
            if (store.getBrand().getPurposes().contains(Pur)){
                result.add(store);
            }
        }
        return result;
    }



    public static ArrayList<String> filterDistinctDistrict(ArrayList<Store> stores){
        ArrayList<String> districts = new ArrayList<>();
        for (Store store : stores) {
            if (!districts.contains(store.getDistrict())){
                districts.add(store.getDistrict());
            }
        }
        Collections.sort(districts);
        return districts;
    }

    public static ArrayList<String> filterDistinctCity(ArrayList<Store> stores){
        ArrayList<String> citys = new ArrayList<>();
        for (Store store : stores) {
            if (!citys.contains(store.getCity())){
                citys.add(store.getCity());
            }
        }
        Collections.sort(citys);
        return citys;
    }

    public static ArrayList<String> filterDistinctBrand(ArrayList<Store> stores){
        ArrayList<String> brands = new ArrayList<>();
        for (Store store : stores) {
            if (!brands.contains(store.getName())){
                brands.add(store.getName());
            }
        }
        Collections.sort(brands);
        return brands;
    }
}
