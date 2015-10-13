package it.aeg2000srl.aeron.views.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import it.aeg2000srl.aeron.R;
import it.aeg2000srl.aeron.core.FavoriteProduct;

/**
 * Created by tiziano.michelessi on 08/10/2015.
 */


public class FavoriteProductsArrayAdapter extends ArrayAdapter<FavoriteProduct> {
    private final Activity context;
    private List<FavoriteProduct> favorites;

    public FavoriteProductsArrayAdapter(Activity context, List<FavoriteProduct> objects) {
        super(context, R.layout.favorite_products, objects);
        this.context = context;
        this.favorites = new ArrayList<>();
        this.favorites.addAll(objects);
    }

    public List<FavoriteProduct> getFavorites() {
        return favorites;
    }

    static class ViewHolder {
        TextView name;
        CheckBox checkBox;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        Log.v("ConvertView", String.valueOf(position));

        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.favorite_products, null, true);
            holder = new ViewHolder();
            holder.name = (TextView) rowView.findViewById(R.id.txtName);
            holder.checkBox = (CheckBox) rowView.findViewById(R.id.checkBox1);
            holder.checkBox.setTag(favorites.get(position));
            rowView.setTag(holder);

            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    FavoriteProduct fav = (FavoriteProduct) cb.getTag();
                    fav.setSelected(cb.isChecked());
                }
            });
        }
        else {
            holder = (ViewHolder) rowView.getTag();
        }

        FavoriteProduct fav = favorites.get(position);
        holder.name.setText(fav.getName());
        holder.checkBox.setChecked(fav.isSelected());

        return rowView;

    }
}
