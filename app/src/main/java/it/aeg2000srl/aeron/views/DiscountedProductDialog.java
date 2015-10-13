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

import java.math.BigDecimal;
import java.math.RoundingMode;

import it.aeg2000srl.aeron.R;
import it.aeg2000srl.aeron.core.Customer;
import it.aeg2000srl.aeron.core.DiscountProduct;
import it.aeg2000srl.aeron.core.IDiscount;
import it.aeg2000srl.aeron.core.IProduct;
import it.aeg2000srl.aeron.core.PercentDiscount;
import it.aeg2000srl.aeron.core.Product;
import it.aeg2000srl.aeron.core.ValueDiscount;
import it.aeg2000srl.aeron.repositories.PriceListRepository;

/**
 * Created by tiziano.michelessi on 12/10/2015.
 */
public class DiscountedProductDialog extends Dialog {

    TextView txtOriginalPrice;
    NumberPicker numDiscountValue;
    RadioButton rbValueDiscount;
    RadioButton rbPercentDiscount;
    TextView txtDiscountedPrice;
    IProduct mProduct;
    Button btnOk;
    Button btnCancel;
    Customer mCustomer;

    public DiscountedProductDialog(Context context, Product product, Customer customer) {
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
        mProduct = product;
        mCustomer = customer;

        numDiscountValue.setMinValue(1);
        numDiscountValue.setMaxValue(100);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // pre-carico la UI con i dati dell'eventuale prodotto scontato già esistente
        PriceListRepository priceListRepository = new PriceListRepository();
        DiscountProduct discountProduct = priceListRepository.getDiscountedProductForCustomerId(mCustomer.getId(), mProduct.getCode());

        if (discountProduct != null) {
            double dvalue = discountProduct.getDiscount().getValue();
            numDiscountValue.setValue((int)Math.round(dvalue));

            rbPercentDiscount.setChecked(discountProduct.getDiscount() instanceof PercentDiscount);
            rbValueDiscount.setChecked(discountProduct.getDiscount() instanceof ValueDiscount);
        }

        updateUI();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        rbValueDiscount.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateUI();
            }
        });

        rbPercentDiscount.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateUI();
            }
        });

        numDiscountValue.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                updateUI();
            }
        });
    }


    public void setOnOkListener(View.OnClickListener listener) {
        btnOk.setOnClickListener(listener);
    }

    protected void updateUI() {
        double originalPrice = mProduct.getPrice();

        DiscountProduct dummyDiscountedProductForUI = new DiscountProduct(mProduct.getCode(), mProduct.getName(), originalPrice, getDiscount());
        dummyDiscountedProductForUI.setCustomerId(mCustomer.getId());

        txtOriginalPrice.setText(String.valueOf(new BigDecimal(originalPrice).setScale(2, RoundingMode.HALF_UP).doubleValue()));
        txtDiscountedPrice.setText(String.valueOf(new BigDecimal(dummyDiscountedProductForUI.getPrice()).setScale(2, RoundingMode.HALF_UP).doubleValue()));

        if (dummyDiscountedProductForUI.getPrice() <= 0) {
            txtDiscountedPrice.setError("L'importo deve essere maggiore di 0");
            btnOk.setEnabled(false);
        } else {
            txtDiscountedPrice.setError(null);
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
