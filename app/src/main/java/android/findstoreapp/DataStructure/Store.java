package android.findstoreapp.DataStructure;

/**
 * Created by ngockhiem27 on 23/02/2016.
 */
public class Store extends Base implements Comparable {
    private Brand brand;
    private float latitude;
    private float longitude;
    private String address;
    private String city;
    private String district;
    private double distance;

    public Store(String name, Brand brand, float latitude, float longtitude, String address, String city, String district) {
        super(name);
        this.brand = brand;
        this.latitude = latitude;
        this.longitude = longtitude;
        this.address = address;
        this.city = city;
        this.district = district;
        this.distance = 99999999;
    }

    public Store() {
        this.distance = 99999999;
    }

    @Override
    public int compareTo(Object store) {
        double compareDistance = ((Store) store).getDistance();
        /* For Ascending order*/
        return ((int) this.getDistance() - (int) compareDistance);

    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(float longtitude) {
        this.longitude = longtitude;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public Brand getBrand() {
        return brand;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getDistrict() {
        return district;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
