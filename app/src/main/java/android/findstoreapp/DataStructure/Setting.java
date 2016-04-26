package android.findstoreapp.DataStructure;


public class Setting extends Base{
    private long value;
    public static String MINIMUM_DISTANCE = "minimum_distance";
    public static int DEFAULT_DISTANCE = 5;
    public static int MAXIMUM_DISTANCE = 20;
    public static int CURRENT_DISTANCE = 0; // only available when app start
    public Setting(String name, long value) {
        super(name);
        this.value = value;
    }


    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }
}
