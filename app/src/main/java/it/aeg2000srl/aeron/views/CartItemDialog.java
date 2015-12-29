package it.aeg2000srl.aeron.views;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import it.aeg2000srl.aeron.R;

/**
 * Created by tiziano.michelessi on 29/12/2015.
 */
public class CartItemDialog extends OrderItemDialog {
    public CartItemDialog(Context context) {
        super(context);
        setContentView(R.layout.cart_item_dialog);
        setTitle("Carrello");

        txtQty = (NumberPicker)findViewById(R.id.txtQty);
        txtQty.setMinValue(1);
        txtQty.setMaxValue(50);
//        txtQty.setValue(1);
        txtNotes = (EditText)findViewById(R.id.txtNotes);
        txtDiscount = (EditText)findViewById(R.id.txtDiscount);
        lblProductName = (TextView)findViewById(R.id.lblProductName);
        btnOk = (Button)findViewById(R.id.dialogButtonOK);
        btnCancel = (Button)findViewById(R.id.dialogButtonCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    public void setOnOkListener(View.OnClickListener listener) {
        btnOk.setOnClickListener(listener);
    }

    public void setOnCancelListener(View.OnClickListener listener) {
        btnCancel.setOnClickListener(listener);
    }
}
