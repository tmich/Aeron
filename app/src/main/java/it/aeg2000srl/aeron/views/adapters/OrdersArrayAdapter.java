package it.aeg2000srl.aeron.views.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import it.aeg2000srl.aeron.R;
import it.aeg2000srl.aeron.core.Customer;
import it.aeg2000srl.aeron.core.IOrder;
import it.aeg2000srl.aeron.core.Order;
import it.aeg2000srl.aeron.repositories.CustomerRepository;

/**
 * Created by tiziano.michelessi on 07/10/2015.
 */
public class OrdersArrayAdapter extends ArrayAdapter<IOrder> {
    private final Activity context;
    private final List<IOrder> orders;
    int resourceId;

    public OrdersArrayAdapter(Activity context, int resource, List<IOrder> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resourceId = resource;
        orders = objects;
    }

    // static to save the reference to the outer class and to avoid access to
    // any members of the containing class
    static class ViewHolder {
        public TextView txtDescription;
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
            rowView = inflater.inflate(resourceId, null, true);
            holder = new ViewHolder();
            //holder.txtCustomerName = (TextView) rowView.findViewById(R.id.txtCustomerName);
            holder.txtDescription = (TextView) rowView.findViewById(R.id.txtDescription);
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        IOrder order = orders.get(position);
        if(order != null) {
//            CustomerRepository customerRepository = new CustomerRepository();
//            Customer customer = customerRepository.findById(order.getCustomerId());
//            if (customer != null) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy H:m", Locale.ITALY);
                holder.txtDescription.setText("Ordine" + (order.getType() == IOrder.OrderType.ICEWER ? " ICEWER" : "") + " n." + order.getId() + " del " + simpleDateFormat.format(order.getCreationDate()));
//            } else {
//                holder.txtDescription.setText("");
//            }
        }
        return rowView;
    }
}
