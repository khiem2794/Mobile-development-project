package android.findstoreapp.DatabaseHandler;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.findstoreapp.DataStructure.Brand;
import android.findstoreapp.DataStructure.Purpose;
import android.findstoreapp.DataStructure.Setting;
import android.findstoreapp.DataStructure.Store;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {
    //define database constants
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "findstore.db";
    public static final String _ID = "id"; // we need to use _ID because ID is already used by the system.
    public static final String NAME = "name";

    /**
     * BRAND TABLE
     */
    public static final String BRAND_TABLE = "brand";
    /**
     * STORE TABLE
     */
    public static final String STORE_TABLE = "store";
    public static final String STORE_LAT = "latitude";
    public static final String STORE_LONG = "longitude";
    public static final String STORE_ADDRESS = "address";
    public static final String STORE_DISTRICT = "district";
    public static final String STORE_CITY = "city";
    public static final String BRAND_ID = "brand_id";
    /**
     * PURPOSE TABLE
     */
    public static final String PURPOSE_TABLE = "purpose";
    /**
     * SETTING TABLE
     */
    public static final String SETTING_TABLE = "setting";
    public static final String SETTING_VALUE = "value";

    /**
     * BRAND_PURPOSE TABLE
     */
    public static final String BRAND_PURPOSE_TABLE = "brand_purpose";
    public static final String BRAND_PURPOSE_BRAND_ID = "brand_id";
    public static final String BRAND_PURPOSE_PURPOSE_ID = "purpose_id";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Will only Run if the Database does not exist
        final String SQL_CREATE_BRAND_TABLE = "CREATE TABLE " + BRAND_TABLE + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                NAME + " TEXT NOT NULL UNIQUE" +
                ");";

        final String SQL_CREATE_STORE_TABLE = "CREATE TABLE " + STORE_TABLE + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NAME + " TEXT NOT NULL, " +
                STORE_LAT + " FLOAT NOT NULL, " +
                STORE_LONG + " FLOAT NOT NULL, " +
                STORE_ADDRESS + " TEXT UNIQUE NOT NULL, " +
                STORE_DISTRICT + " TEXT NOT NULL, " +
                STORE_CITY + " TEXT NOT NULL, " +
                BRAND_ID + " INTEGER NOT NULL" +
                ");";
        final String SQL_CREATE_PURPOSE_TABLE = "CREATE TABLE " + PURPOSE_TABLE + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NAME + " TEXT NOT NULL " +
                ");";
        final String SQL_CREATE_SETTING_TABLE = "CREATE TABLE " + SETTING_TABLE + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NAME + " TEXT NOT NULL, " +
                SETTING_VALUE + " TEXT NOT NULL " +
                ");";
        final String SQL_CREATE_BRAND_PURPOSE_TABLE = "CREATE TABLE " + BRAND_PURPOSE_TABLE + " (" +
                BRAND_PURPOSE_BRAND_ID + " INTEGER NOT NULL, " +
                BRAND_PURPOSE_PURPOSE_ID + " INTEGER NOT NULL, " +
                "PRIMARY KEY (" + BRAND_PURPOSE_BRAND_ID + "," + BRAND_PURPOSE_PURPOSE_ID + ")" +
                ");";
        // Create tables if not exist
        db.execSQL(SQL_CREATE_BRAND_TABLE);
        db.execSQL(SQL_CREATE_STORE_TABLE);
        db.execSQL(SQL_CREATE_PURPOSE_TABLE);
        db.execSQL(SQL_CREATE_SETTING_TABLE);
        db.execSQL(SQL_CREATE_BRAND_PURPOSE_TABLE);
        setDefaultMinimumSetting(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + BRAND_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + STORE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + PURPOSE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SETTING_TABLE);
        // Create tables again
        onCreate(db);
    }

    /**
     * Closes the Database Connection.
     */
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    /**
     * Clear DB
     */
    public void clearDB() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + BRAND_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + STORE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + PURPOSE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SETTING_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + BRAND_PURPOSE_TABLE);
        onCreate(db);
    }

    /**
     * DELETE DB
     */
    public void deleteDB() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + BRAND_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + STORE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + PURPOSE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SETTING_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + BRAND_PURPOSE_TABLE);
    }

    /**
     * Get all store in DB
     */
    public ArrayList<Store> getAllStore() {
        ArrayList<Store> stores = new ArrayList<Store>();
        ArrayList<Brand> brands = getAllBrands();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + STORE_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Store store = new Store();
                store.setId(Integer.parseInt(cursor.getString(0)));
                store.setName(cursor.getString(1));
                store.setLatitude(Float.parseFloat(cursor.getString(2)));
                store.setLongitude(Float.parseFloat(cursor.getString(3)));
                store.setAddress(cursor.getString(4));
                store.setDistrict(cursor.getString(5));
                store.setCity(cursor.getString(6));
                for (Brand brand : brands) {
                    if (brand.getName().equals(store.getName())) {
                        store.setBrand(brand);
                    }
                }
                // Adding store to list
                stores.add(store);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return stores;
    }

    /**
     * GET ALL BRANDS
     */
    public ArrayList<Brand> getAllBrands() {
        ArrayList<Brand> brands = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + BRAND_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Brand brand = new Brand();
                brand.setId(Integer.parseInt(cursor.getString(0)));
                brand.setName(cursor.getString(1));
                brand.setStores(getStoresByBrand(brand));
                brand.setPurposes(getBrandPurposes(brand));
                brands.add(brand);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return brands;
    }

    /**
     * Get all store in a brand
     */
    public ArrayList<Store> getStoresByBrand(Brand brand) {
        ArrayList<Store> stores = new ArrayList<Store>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + STORE_TABLE + " WHERE " + NAME + " =?";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{brand.getName()});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Store store = new Store();
                store.setId(Integer.parseInt(cursor.getString(0)));
                store.setName(cursor.getString(1));
                store.setLatitude(Float.parseFloat(cursor.getString(2)));
                store.setLongitude(Float.parseFloat(cursor.getString(3)));
                store.setAddress(cursor.getString(4));
                store.setCity(cursor.getString(5));
                store.setDistrict(cursor.getString(6));
                store.setBrand(brand);
                // Adding store to list
                stores.add(store);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return stores;
    }

    /**
     * GET MINIMUM DISTANCE SETTINGS
     */
    public String getMinimumSetting() {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT  * FROM " + SETTING_TABLE + " WHERE " + NAME + " =?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{Setting.MINIMUM_DISTANCE});
        if (cursor.moveToLast()) {
            String distance = cursor.getString(2);
            cursor.close();
            db.close();
            return distance;
        } else {
            cursor.close();
            db.close();
            throw new Resources.NotFoundException();
        }
    }

    public void setMinimumSetting(int distance) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(NAME, Setting.MINIMUM_DISTANCE);
        value.put(SETTING_VALUE, distance);
        String updateQuery = "UPDATE " + SETTING_TABLE + " SET VALUE = " + "'" + distance + "'";
        db.execSQL(updateQuery);
        //db.update(SETTING_TABLE, value, _ID + " =?", null);
        db.close();
    }

    public void setDefaultMinimumSetting(SQLiteDatabase db) {
        ContentValues value = new ContentValues();
        value.put(NAME, Setting.MINIMUM_DISTANCE);
        value.put(SETTING_VALUE, String.valueOf(Setting.DEFAULT_DISTANCE));
        db.insert(SETTING_TABLE, null, value);
    }


    /**
     * GET ALL DISTINCT DISTRICT
     */
    public ArrayList<String> getDistricts() {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT DISTINCT " + STORE_DISTRICT + " FROM " + STORE_TABLE;
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<String> result = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                result.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return result;
    }

    /**
     * GET ALL DISTINCT CITY
     */
    public ArrayList<String> getCitys() {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT DISTINCT " + STORE_CITY + " FROM " + STORE_TABLE;
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<String> result = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                result.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return result;
    }

    /**
     * Get Brand by Name
     */
    public Brand getBrandByName(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT  * FROM " + BRAND_TABLE + " WHERE " + NAME + " =?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{name});
        if (cursor == null) {
            db.close();
            return null;
        }
        if (cursor.moveToFirst()) {
            Brand brand = new Brand(Integer.parseInt(cursor.getString(0)), cursor.getString(1), null, null);
            cursor.close();
            db.close();
            return brand;
        }
        return null;
    }

    /**
     * Insert new brand into DB, if the brand is already existed then retrieve it
     */
    public Brand createBrand(Brand brand) {
        Brand existCheck = getBrandByName(brand.getName());
        if (existCheck != null) {
            existCheck.setStores(brand.getStores());
            existCheck.setPurposes(brand.getPurposes());
            return existCheck;
        } else {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues brandIns = new ContentValues();
            brandIns.put(NAME, brand.getName());
            int id = (int) db.insert(BRAND_TABLE, null, brandIns);
            if (id == -1) {
                db.close();
                return null;
            }
            db.close();
            return new Brand(id, brand.getName(), brand.getStores(), brand.getPurposes());
        }

    }

    /**
     * Get Store by address
     */
    public Store getStoreByAddress(String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT  * FROM " + STORE_TABLE + " WHERE " + STORE_ADDRESS + " =?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{address});
        if (cursor == null) {
            db.close();
            return null;
        }
        if (cursor.moveToFirst()) {
            Store store = new Store();
            store.setId(Integer.parseInt(cursor.getString(0)));
            store.setName(cursor.getString(1));
            store.setLatitude(Float.parseFloat(cursor.getString(2)));
            store.setLongitude(Float.parseFloat(cursor.getString(3)));
            store.setAddress(cursor.getString(4));
            store.setCity(cursor.getString(5));
            store.setDistrict(cursor.getString(6));
            store.setBrand(new Brand(Integer.parseInt(cursor.getString(7)), store.getName(), null, null));
            cursor.close();
            db.close();
            return store;
        }
        return null;
    }

    /**
     * Inset new Store into DB
     */
    public int insertStore(Brand brand, Store newStore) {
        Store existCheck = getStoreByAddress(newStore.getAddress());
        if (existCheck != null) {
            return (int) existCheck.getId();
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues storeIns = new ContentValues();
        storeIns.put(NAME, brand.getName());
        storeIns.put(STORE_LAT, newStore.getLatitude());
        storeIns.put(STORE_LONG, newStore.getLongitude());
        storeIns.put(STORE_ADDRESS, newStore.getAddress());
        storeIns.put(STORE_DISTRICT, newStore.getDistrict());
        storeIns.put(STORE_CITY, newStore.getCity());
        storeIns.put(BRAND_ID, brand.getId());
        return (int) db.insert(STORE_TABLE, null, storeIns);
    }

    /**
     * Insert a brand and all its purpose and store into db
     */
    public void insertBrand(Brand newBrand) {
        SQLiteDatabase db = this.getWritableDatabase();
        Brand brand = createBrand(new Brand(newBrand.getName(), newBrand.getStores(), newBrand.getPurposes()));
        for (Store store : brand.getStores()) {
            insertStore(brand, store);
        }
        for (Purpose purpose : brand.getPurposes()) {
            Purpose p = insertPurpose(purpose);
            setBrandPurpose(brand, p);
        }
        db.close(); // Closing database connection
    }

    /**
     * GET BRAND's PURPOSES
     */
    public ArrayList<Purpose> getBrandPurposes(Brand brand) {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Purpose> result = new ArrayList<>();
        String getSQL = "SELECT * " +
                "FROM " + PURPOSE_TABLE + " p " +
                "INNER JOIN " + BRAND_PURPOSE_TABLE + " b " +
                "ON p." + _ID + "=b." + BRAND_PURPOSE_PURPOSE_ID + " " +
                "WHERE b." + BRAND_PURPOSE_BRAND_ID + "=?";
        Cursor cursor = db.rawQuery(getSQL, new String[]{String.valueOf(brand.getId())});
        if (cursor == null) {
            db.close();
            return result;
        }
        ArrayList<Brand> brands = new ArrayList<>();
        brands.add(brand);
        if (cursor.moveToFirst()) {
            do {
                Purpose purpose = new Purpose(Integer.parseInt(cursor.getString(0)), cursor.getString(1), brands);
                result.add(purpose);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return result;
    }

    /**
     * Get purpose by name, null if not exist
     */
    public Purpose getPurposeByName(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT  * FROM " + PURPOSE_TABLE + " WHERE " + NAME + " =?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{name});
        if (cursor == null) {
            db.close();
            return null;
        }
        if (cursor.moveToFirst()) {
            Purpose purpose = new Purpose(Integer.parseInt(cursor.getString(0)), cursor.getString(1), null);
            cursor.close();
            db.close();
            return purpose;
        }
        return null;
    }

    /**
     * Insert purpose
     */
    public Purpose insertPurpose(Purpose purpose) {
        Purpose existCheck = getPurposeByName(purpose.getName());
        if (existCheck != null) {
            existCheck.setBrands(purpose.getBrands());
            return existCheck;
        } else {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues purposeIns = new ContentValues();
            purposeIns.put(NAME, purpose.getName());
            int id = (int) db.insert(PURPOSE_TABLE, null, purposeIns);
            if (id == -1) {
                db.close();
                return null;
            }
            db.close();
            return new Purpose(id, purpose.getName(), purpose.getBrands());
        }
    }

    /**
     * Set Brand and Purpose
     */
    public void setBrandPurpose(Brand brand, Purpose purpose) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM " + BRAND_PURPOSE_TABLE + " WHERE " + BRAND_PURPOSE_BRAND_ID + " =" + String.valueOf(brand.getId()) + " AND " + BRAND_PURPOSE_PURPOSE_ID + " =" + String.valueOf(purpose.getId());
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() <= 0) {
            ContentValues setIns = new ContentValues();
            setIns.put(BRAND_PURPOSE_BRAND_ID, brand.getId());
            setIns.put(BRAND_PURPOSE_PURPOSE_ID, purpose.getId());
            db.insert(BRAND_PURPOSE_TABLE, null, setIns);
            db.close();
        } else {
            db.close();
            cursor.close();
        }
    }
}
