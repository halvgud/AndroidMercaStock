package mx.mercatto.mercastock;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class SucursalAdapter extends ArrayAdapter<ListaSucursal> {
    private Activity context;
ArrayList<ListaSucursal> data = null;

    public SucursalAdapter(Activity context, int resource,
                           ArrayList<ListaSucursal> data) {
        super(context, resource, data);
        this.context = context;
        this.data = data;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.simple_spinner_item, parent, false);
        }

        ListaSucursal item = data.get(position);

        if (item != null) { // Parse the data from each object and set it.
            TextView CountryId = (TextView) row.findViewById(R.id.item_id);
            TextView CountryName = (TextView) row.findViewById(R.id.item_value);
            if (CountryId != null) {
                CountryId.setText(item.getId());
            }
            if (CountryName != null) {
                CountryName.setText(item.getName());
            }

        }

        return row;
    }
}
