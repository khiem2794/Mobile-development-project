package android.findstoreapp.DataStructure;

import java.util.ArrayList;

/**
 * Created by ngockhiem27 on 23/02/2016.
 */
public class Brand extends Base {
    private ArrayList<Store> stores;
    private ArrayList<Purpose> purposes;

    public Brand(){
    }
    public Brand(String name, ArrayList<Store> stores, ArrayList<Purpose> purposes) {
        super(name);
        this.stores = stores;
        this.purposes = purposes;
    }
    public Brand(Integer id, String name, ArrayList<Store> stores, ArrayList<Purpose> purposes) {
        super(id, name);
        this.stores = stores;
        this.purposes = purposes;
    }

    public void setStores(ArrayList<Store> stores) {
        this.stores = stores;
    }

    public void setPurposes(ArrayList<Purpose> purposes) {
        this.purposes = purposes;
    }

    public ArrayList<Store> getStores() {
        return stores;
    }

    public ArrayList<Purpose> getPurposes() {
        return purposes;
    }
}
