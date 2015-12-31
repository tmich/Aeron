package it.aeg2000srl.aeron.views.adapters;

import android.app.Activity;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Locale;
import it.aeg2000srl.aeron.R;
import it.aeg2000srl.aeron.core.DiscountProduct;
import it.aeg2000srl.aeron.core.IProduct;
import it.aeg2000srl.aeron.repositories.ProductRepository;

/**
 * Created by tiziano.michelessi on 12/10/2015.
 */
public class DiscountProductsArrayAdapter extends ArrayAdapter<DiscountProduct> {
    Activity context;
    List<DiscountProduct> objects;

    public DiscountProductsArrayAdapter(Activity context, List<DiscountProduct> objects) {
        super(context, R.layout.pricelist, objects);
        this.context = context;
        this.objects = objects;
    }

    // static to save the reference to the outer class and to avoid access to
    // any members of the containing class
    static class ViewHolder {
        public TextView textView;
        public TextView priceView;
        public TextView discountedPriceView;
        public TextView discountDesc;
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
            rowView = inflater.inflate(R.layout.pricelist, null, true);
            holder = new ViewHolder();
            holder.textView = (TextView) rowView.findViewById(R.id.txtName);
            holder.priceView = (TextView) rowView.findViewById(R.id.txtPrice);
//            holder.discountDesc = (TextView) rowView.findViewById(R.id.txtDiscountDesc);
            holder.discountedPriceView = (TextView) rowView.findViewById(R.id.txtDiscountedPrice);
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }
        DiscountProduct product = objects.get(position);
        holder.textView.setText(product.getName());

        // Prezzo originale non scontato
        ProductRepository productRepository = new ProductRepository();
        IProduct origProd = productRepository.findByCode(product.getCode());
        SpannableString price = new SpannableString(String.format(Locale.ITALIAN, "%.2f €", origProd.getPrice()));
        price.setSpan(new android.text.style.StrikethroughSpan(), 0, price.length(), 0);
        holder.priceView.setText(price);

//        holder.discountDesc.setText(product.getDiscount().getDescription());
        holder.discountedPriceView.setText(String.format(Locale.ITALIAN, "%.2f €", product.getPrice()));
        return rowView;
    }
}
