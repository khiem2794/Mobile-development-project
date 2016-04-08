package android.findstoreapp.DataStructure;

/**
 * Created by ngockhiem27 on 23/02/2016.
 */
public abstract class Base {
    private long id;
    private String name;

    public Base(long id, String name) {
        this.id = id;
        this.name = name;
    }
    public Base(String name) {
        this.name = name;
    }
    public Base(){}
    public void setName(String name) {
        this.name = name;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
