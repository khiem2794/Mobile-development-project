package android.findstoreapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.findstoreapp.DataStructure.Store;
import android.findstoreapp.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by kiddy_000 on 04-Mar-16.
 */
public class StoresListAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Activity activity;
    private List<Store> stores;
    private ArrayList<String> distances;

    public TextView brandNameTV, storeAddessTV, distanceFromMeTv;
    public CircleImageView brandLogoImV;

    public StoresListAdapter(Activity activity, List<Store> stores) {
        this.activity = activity;
        this.stores = stores;
        this.distances = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return stores.size();
    }

    @Override
    public Object getItem(int position) {
        return stores.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null)
            convertView = inflater.inflate(R.layout.stores_list, null);

        brandNameTV = (TextView) convertView.findViewById(R.id.brand_name_tv);
        storeAddessTV = (TextView) convertView.findViewById(R.id.store_address_tv);
        distanceFromMeTv = (TextView) convertView.findViewById(R.id.distance_from_me_tv);
        brandLogoImV = (CircleImageView) convertView.findViewById(R.id.brand_logo_imv);
        Store s = stores.get(position);

        updateListGUI(s);

        return convertView;
    }

    private void updateListGUI(Store s) {
        brandNameTV.setText(s.getName());
        storeAddessTV.setText(s.getAddress());
        switch (s.getName()) {
            case "CircleK":
                brandLogoImV.setImageResource(R.drawable.circlek);
                break;
            case "ShopAndGo":
                brandLogoImV.setImageResource(R.drawable.shopgo);
                break;
            case "Satrafoods":
                brandLogoImV.setImageResource(R.drawable.satrafoods);
                break;
            default:
                brandLogoImV.setImageResource(R.drawable.circlek);
        }
        DecimalFormat format = new DecimalFormat("#.#");
        distanceFromMeTv.setText(String.valueOf(format.format(s.getDistance())));
    }
}
