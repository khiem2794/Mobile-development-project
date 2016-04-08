package android.findstoreapp.CloudHandler;

import android.findstoreapp.DataStructure.Brand;
import android.findstoreapp.DataStructure.Purpose;
import android.findstoreapp.DataStructure.Store;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

public class CloudHandler {

    public final String fakeDataAllBrands = "{\"CircleK\":{\"purposes\":{\"-KAzI8SIBlzTukqzLvu2\":{\"name\":\"Eat\"},\"-KAzITgybIB1ufok19KX\":{\"name\":\"Drink\"}},\"stores\":{\"-KArwjJIkNCVBqUscknX\":{\"address\":\"36 Hai Bà Trưng\",\"city\":\"Ho Chi Minh\",\"district\":1,\"lat\":\"10.777075\",\"long\":\"106.704793\"},\"-KAs7Yrc_ZDtfcYxg2oM\":{\"address\":\"44 Lê Lai\",\"city\":\"Ho Chi Minh\",\"district\":1,\"lat\":\"10.771442\",\"long\":\"106.696884\"}}},\"Satrafoods\":{\"purposes\":{\"-KAzIDf61iEQWGqEfN7j\":{\"name\":\"Eat\"}},\"stores\":{\"-KAsBF4LRR6uJCJtAGWl\":{\"address\":\"74 Lê Lợi\",\"city\":\"Ho Chi Minh\",\"district\":1,\"lat\":\"10.773093\",\"long\":\"106.699594\"},\"-KAsBO5-mrRTYVw4XeIx\":{\"address\":\"103 Nguyễn Huệ\",\"city\":\"Ho Chi Minh\",\"district\":1,\"lat\":\"10.773489\",\"long\":\"106.703785\"}}},\"ShopAndGo\":{\"purposes\":{\"-KAzIF3TvhCzEcmtTyia\":{\"name\":\"Eat\"}},\"stores\":{\"-KAsB_gC8BUmj1iCGDY-\":{\"address\":\"2A4 Nguyễn Thị Minh Khai\",\"city\":\"Ho Chi Minh\",\"district\":1,\"lat\":\"10.777902\",\"long\":\"106.693421\"},\"-KAsBh978L5gCJMswwjr\":{\"address\":\"54 Nguyễn Du\",\"city\":\"Ho Chi Minh\",\"district\":1,\"lat\":\"10.779367\",\"long\":\"106.700338\"},\"-KAsCfYzY8Zi6-ISeaTp\":{\"address\":\"8a/g6 Thái Văn Lung\",\"city\":\"Ho Chi Minh\",\"district\":1,\"lat\":\"10.779270\",\"long\":\"106.705449\"}}}}";
    public final String fakeDataCircleK = "{\"purposes\":{\"-KAzI8SIBlzTukqzLvu2\":{\"name\":\"Eat\"},\"-KAzITgybIB1ufok19KX\":{\"name\":\"Drink\"}},\"stores\":{\"-KArwjJIkNCVBqUscknX\":{\"address\":\"36 Hai Bà Trưng\",\"city\":\"Ho Chi Minh\",\"district\":1,\"lat\":\"10.777075\",\"long\":\"106.704793\"},\"-KAs7Yrc_ZDtfcYxg2oM\":{\"address\":\"44 Lê Lai\",\"city\":\"Ho Chi Minh\",\"district\":1,\"lat\":\"10.771442\",\"long\":\"106.696884\"}}}";
    public final String fakeDataShopAndGo = "{\"purposes\":{\"-KAzIF3TvhCzEcmtTyia\":{\"name\":\"Eat\"}},\"stores\":{\"-KAsB_gC8BUmj1iCGDY-\":{\"address\":\"2A4 Nguyễn Thị Minh Khai\",\"city\":\"Ho Chi Minh\",\"district\":1,\"lat\":\"10.777902\",\"long\":\"106.693421\"},\"-KAsBh978L5gCJMswwjr\":{\"address\":\"54 Nguyễn Du\",\"city\":\"Ho Chi Minh\",\"district\":1,\"lat\":\"10.779367\",\"long\":\"106.700338\"},\"-KAsCfYzY8Zi6-ISeaTp\":{\"address\":\"8a/g6 Thái Văn Lung\",\"city\":\"Ho Chi Minh\",\"district\":1,\"lat\":\"10.779270\",\"long\":\"106.705449\"}}}";
    public final String fakeDataSatrafoods = "{\"purposes\":{\"-KAzIDf61iEQWGqEfN7j\":{\"name\":\"Eat\"}},\"stores\":{\"-KAsBF4LRR6uJCJtAGWl\":{\"address\":\"74 Lê Lợi\",\"city\":\"Ho Chi Minh\",\"district\":1,\"lat\":\"10.773093\",\"long\":\"106.699594\"},\"-KAsBO5-mrRTYVw4XeIx\":{\"address\":\"103 Nguyễn Huệ\",\"city\":\"Ho Chi Minh\",\"district\":1,\"lat\":\"10.773489\",\"long\":\"106.703785\"}}}";

    public ArrayList<Store> retrieveStoreByBrand(Brand brand) {
        ArrayList<Store> storeList = new ArrayList<>();
        String data = fetchBrandData(brand);
        try {
            JSONObject obj = new JSONObject(data);
            JSONObject brandJSON = obj.getJSONObject(brand.getName());
            final JSONObject stores = brandJSON.getJSONObject("stores");
            Iterator<String> keys = stores.keys();

            while (keys.hasNext()) {
                String key = keys.next();
                JSONObject storeJSON = stores.getJSONObject(key);
                Store store = new Store();
                store.setBrand(brand);
                store.setLongitude(Float.parseFloat(storeJSON.get("long").toString()));
                store.setLatitude(Float.parseFloat(storeJSON.get("lat").toString()));
                store.setCity(storeJSON.get("city").toString());
                store.setDistrict(storeJSON.get("district").toString());
                store.setAddress(storeJSON.get("address").toString());
                storeList.add(store);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (Store store : storeList) {
            Log.i("STORE", store.getAddress() + " " + store.getDistrict() + " " + store.getCity());
        }
        return storeList;
    }

    public ArrayList<Brand> retrieveAllBrands(String data) {
        ArrayList<Brand> brands = new ArrayList<>();
        if (data.isEmpty()){
            data = fetchAll();
        }
        try {
            JSONObject dataObj = new JSONObject(data);
            Iterator<String> brandsName = dataObj.keys();
            while (brandsName.hasNext()) {
                Brand brand = new Brand(brandsName.next(), new ArrayList<Store>(), new ArrayList<Purpose>());
                final JSONObject brandJSON = dataObj.getJSONObject(brand.getName());
                // POPULATE STORES
                final JSONObject storesJSON = brandJSON.getJSONObject("stores");
                Iterator<String> storekeys = storesJSON.keys();
                while (storekeys.hasNext()) {
                    String key = storekeys.next();
                    JSONObject storeJSON = storesJSON.getJSONObject(key);
                    Store store = new Store();
                    store.setBrand(brand);
                    store.setLatitude(Float.parseFloat(storeJSON.get("lat").toString()));
                    store.setLongitude(Float.parseFloat(storeJSON.get("long").toString()));
                    store.setCity(storeJSON.get("city").toString());
                    store.setDistrict(storeJSON.get("district").toString());
                    store.setAddress(storeJSON.get("address").toString());
                    brand.getStores().add(store);
                }
                // POPULATE PURPOSE
                final JSONObject purposesJSON = brandJSON.getJSONObject("purposes");
                Iterator<String> purposekeys = purposesJSON.keys();
                while (purposekeys.hasNext()) {
                    String key = purposekeys.next();
                    JSONObject purposeJSON = purposesJSON.getJSONObject(key);
                    ArrayList<Brand> purposeBrands = new ArrayList<>();
                    purposeBrands.add(brand);
                    Purpose purpose = new Purpose(purposeJSON.get("name").toString(), purposeBrands);
                    brand.getPurposes().add(purpose);
                }
                brands.add(brand);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return brands;
    }

    private String fetchBrandData(Brand brand) {
        try {
            URL url = new URL("https://storefinder.firebaseio.com/Brands/" + brand.getName() + ".json");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        switch (brand.getName()) {
            case "CircleK":
                return fakeDataCircleK;
            case "Satrafoods":
                return fakeDataSatrafoods;
            case "ShopAndGo":
                return fakeDataShopAndGo;
            default:
                return "";
        }
    }

    private String fetchAll() {
        try {
            URL url = new URL("https://storefinder.firebaseio.com/Brands.json");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return fakeDataAllBrands;
    }

}
