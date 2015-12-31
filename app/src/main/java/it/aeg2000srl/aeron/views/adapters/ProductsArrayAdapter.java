package it.aeg2000srl.aeron.views.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;
import java.util.Locale;

import it.aeg2000srl.aeron.R;
import it.aeg2000srl.aeron.core.IProduct;
import it.aeg2000srl.aeron.core.Product;

/**
 * Created by tiziano.michelessi on 06/10/2015.
 */
public class ProductsArrayAdapter extends ArrayAdapter<Product> {
    private final Activity context;
    private final List<Product> products;

    public ProductsArrayAdapter(Activity context, List<Product> products) {
        super(context, R.layout.products, products);
        this.context = context;
        this.products = products;
    }

    // static to save the reference to the outer class and to avoid access to
    // any members of the containing class
    static class ViewHolder {
        public TextView textView;
        public TextView priceView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // ViewHolder will buffer the assess to the individual fields of the row
        // layout

        ViewHolder holder;
        // Recycle existing productsView if passed as parameter
        // This will save memory and time on Android
        // This only works if the base layout for all classes are the same
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.products, null, true);
            holder = new ViewHolder();
            holder.textView = (TextView) rowView.findViewById(R.id.txtName);
            holder.priceView = (TextView) rowView.findViewById(R.id.txtPrice);
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        IProduct product = products.get(position);
        holder.textView.setText(product.getName());
        holder.priceView.setText(String.format(Locale.ITALIAN, "%.2f €", product.getPrice()));
        return rowView;
    }
}
