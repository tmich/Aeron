package it.aeg2000srl.aeron.views.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import it.aeg2000srl.aeron.R;
import it.aeg2000srl.aeron.core.Order;
import it.aeg2000srl.aeron.repositories.CustomerRepository;

/**
 * Created by tiziano.michelessi on 07/10/2015.
 */
public class OrdersArrayAdapter extends ArrayAdapter<Order> {
    private final Activity context;
    private final List<Order> orders;

    public OrdersArrayAdapter(Activity context, int resource, List<Order> objects) {
        super(context, resource, objects);
        this.context = context;
        orders = objects;
    }

    // static to save the reference to the outer class and to avoid access to
    // any members of the containing class
    static class ViewHolder {
        public TextView txtCustomerName;
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
            rowView = inflater.inflate(R.layout.order_items, null, true);
            holder = new ViewHolder();
            holder.txtCustomerName = (TextView) rowView.findViewById(R.id.txtCustomerName);
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        Order order = orders.get(position);
        if(order != null) {
            CustomerRepository customerRepository = new CustomerRepository();
            holder.txtCustomerName.setText(customerRepository.findById(order.getCustomerId()).getName());
        }
        return rowView;
    }
}
