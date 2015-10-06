package it.aeg2000srl.aeron.views.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import it.aeg2000srl.aeron.R;
import it.aeg2000srl.aeron.core.Customer;

/**
 * Created by tiziano.michelessi on 06/10/2015.
 */
public class CustomersArrayAdapter extends ArrayAdapter<Customer> {
    private final Activity context;
    private final List<Customer> customers;

    public CustomersArrayAdapter(Activity context, List<Customer> customers) {
        super(context, R.layout.customers, customers);
        this.context = context;
        this.customers = customers;
    }

    // static to save the reference to the outer class and to avoid access to
    // any members of the containing class
    static class ViewHolder {
        public TextView textView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // ViewHolder will buffer the assess to the individual fields of the row
        // layout

        ViewHolder holder;
        // Recycle existing customersView if passed as parameter
        // This will save memory and time on Android
        // This only works if the base layout for all classes are the same
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.customers, null, true);
            holder = new ViewHolder();
            holder.textView = (TextView) rowView.findViewById(R.id.txtName);
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        holder.textView.setText(customers.get(position).getName());
        return rowView;
    }
}
