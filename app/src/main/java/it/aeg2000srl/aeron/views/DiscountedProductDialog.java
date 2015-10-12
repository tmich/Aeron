package it.aeg2000srl.aeron.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.TextView;

import it.aeg2000srl.aeron.R;
import it.aeg2000srl.aeron.core.DiscountProduct;
import it.aeg2000srl.aeron.core.IDiscount;
import it.aeg2000srl.aeron.core.IProduct;
import it.aeg2000srl.aeron.core.PercentDiscount;
import it.aeg2000srl.aeron.core.Product;
import it.aeg2000srl.aeron.core.ValueDiscount;
import it.aeg2000srl.aeron.repositories.ProductRepository;

/**
 * Created by tiziano.michelessi on 12/10/2015.
 */
public class DiscountedProductDialog extends Dialog {

    TextView txtOriginalPrice;
    NumberPicker numDiscountValue;
    RadioButton rbValueDiscount;
    RadioButton rbPercentDiscount;
    TextView txtDiscountedPrice;
    IProduct product;
    Button btnOk;
    Button btnCancel;

    public DiscountedProductDialog(Context context, IProduct product) {
        super(context);
        setContentView(R.layout.discounted_product_dialog);
        setTitle(product.getName());

        txtOriginalPrice = (TextView)findViewById(R.id.txtOriginalPrice);
        numDiscountValue = (NumberPicker)findViewById(R.id.numDiscountValue);
        rbValueDiscount = (RadioButton)findViewById(R.id.rbValueDiscount);
        rbPercentDiscount = (RadioButton)findViewById(R.id.rbPercentDiscount);
        txtDiscountedPrice = (TextView)findViewById(R.id.txtDiscountedPrice);
        btnOk = (Button)findViewById(R.id.dialogButtonOK);
        btnCancel = (Button)findViewById(R.id.dialogButtonCancel);
        this.product = product;

        numDiscountValue.setMinValue(1);
        numDiscountValue.setMaxValue(100);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (product instanceof DiscountProduct) {
            double dvalue = ((DiscountProduct) product).getDiscount().getValue();
            numDiscountValue.setValue((int)Math.round(dvalue));

            rbPercentDiscount.setChecked(((DiscountProduct) product).getDiscount() instanceof PercentDiscount);
            rbValueDiscount.setChecked(((DiscountProduct) product).getDiscount() instanceof ValueDiscount);
        }


        update();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        rbValueDiscount.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                update();
            }
        });

        rbPercentDiscount.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                update();
            }
        });

        numDiscountValue.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                update();
            }
        });
    }


    public void setOnOkListener(View.OnClickListener listener) {
        btnOk.setOnClickListener(listener);
    }

    protected void update() {
        ProductRepository productRepository = new ProductRepository();
        double originalPrice = productRepository.findByCode(product.getCode()).getPrice();
        IDiscount discount = getDiscount();
        DiscountProduct discountProduct = new DiscountProduct(product.getCode(), product.getName(), originalPrice, discount);

        if (product instanceof DiscountProduct ) {
            discountProduct.setId(product.getId());
            discountProduct.setCustomerId(((DiscountProduct) product).getCustomerId());
        }

        txtOriginalPrice.setText(String.valueOf(originalPrice));
        txtDiscountedPrice.setText(String.valueOf(discountProduct.getPrice()));

        if (discountProduct.getPrice() <= 0) {
            txtDiscountedPrice.setError("L'importo deve essere maggiore di 0");
            btnOk.setEnabled(false);
        } else {
            btnOk.setEnabled(true);
        }
    }

    public IDiscount getDiscount() {
        IDiscount discount;

        if (rbValueDiscount.isChecked()) {
            discount = new ValueDiscount(numDiscountValue.getValue());
        } else {
            discount = new PercentDiscount(numDiscountValue.getValue());
        }

        return discount;
    }
}
