package it.aeg2000srl.aeron.views.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import it.aeg2000srl.aeron.R;
import it.aeg2000srl.aeron.core.DiscountProduct;
import it.aeg2000srl.aeron.core.IOrderItem;
import it.aeg2000srl.aeron.core.OrderItem;
import it.aeg2000srl.aeron.services.UseCasesService;

/**
 * Created by tiziano.michelessi on 07/10/2015.
 */
public class OrderItemsArrayAdapter extends ArrayAdapter<IOrderItem> {
    private final Activity context;
    private final List<IOrderItem> items;
    private UseCasesService useCasesService = new UseCasesService();

    public OrderItemsArrayAdapter(Activity context, List<IOrderItem> items) {
        super(context, R.layout.order_items, items);
        this.context = context;
        this.items = items;
    }

    // static to save the reference to the outer class and to avoid access to
    // any members of the containing class
    static class ViewHolder {
        public TextView txtProductName;
        public TextView txtQty;
        public TextView txtDiscount;
//        public ImageButton btnDeleteItem;
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
            holder.txtProductName = (TextView) rowView.findViewById(R.id.txtName);
            holder.txtQty = (TextView) rowView.findViewById(R.id.txtQty);
            holder.txtDiscount = (TextView) rowView.findViewById(R.id.txtDiscount);
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        IOrderItem item = items.get(position);

        holder.txtProductName.setText(item.getProductName());
        holder.txtQty.setText(String.valueOf(item.getQuantity()));
        // sconto?
//        UseCasesService useCasesService = new UseCasesService();
//
        if (item.getOrder() != null) {
            DiscountProduct discountProduct = useCasesService.getDiscountedProductForCustomerId(item.getOrder().getCustomerId(), item.getProductCode());
            if (discountProduct != null) {
                //holder.txtProductName.setBackgroundColor(Color.GREEN);
                holder.txtDiscount.setText(discountProduct.getDiscount().getDescription());
                holder.txtDiscount.setBackgroundColor(Color.GREEN);
            }
        }
        return rowView;
    }
}
