package android.findstoreapp.DataStructure;

import java.util.ArrayList;


public class Purpose extends Base {
    private ArrayList<Brand> brands;

    public Purpose(long id, String name, ArrayList<Brand> brands) {
        super(id, name);
        this.brands = brands;
    }

    public Purpose(String name, ArrayList<Brand> brands) {
        super(name);
        this.brands = brands;
    }

    public ArrayList<Brand> getBrands() {
        return brands;
    }

    public void setBrands(ArrayList<Brand> brands) {
        this.brands = brands;
    }
}
