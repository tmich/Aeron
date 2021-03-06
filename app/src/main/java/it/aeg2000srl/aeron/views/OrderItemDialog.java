package it.aeg2000srl.aeron.views;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import it.aeg2000srl.aeron.R;

/**
 * Created by tiziano.michelessi on 07/10/2015.
 */
public class OrderItemDialog extends Dialog {
    NumberPicker txtQty;
    EditText txtNotes;
    TextView txtDiscount;
    TextView lblSconto;
    TextView lblProductName;
    Button btnOk;
    Button btnCancel;

    String productName;

    public OrderItemDialog(Context context) {
        super(context);
        setContentView(R.layout.order_item_dialog);
        setTitle("Aggiungi");

        txtQty = (NumberPicker)findViewById(R.id.txtQty);
        txtQty.setMinValue(1);
        txtQty.setMaxValue(50);
//        txtQty.setValue(1);
        txtNotes = (EditText)findViewById(R.id.txtNotes);
        txtDiscount = (TextView)findViewById(R.id.txtDiscount);
        lblSconto = (TextView)findViewById(R.id.lblSconto);
        lblSconto.setVisibility(View.INVISIBLE);
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

    public void setProductName(String productName) {
        this.productName = productName;
        setTitle(this.productName);
        //lblProductName.setText(productName);
    }

    public String getProductName() {
        return this.productName;
    }

    public void setQuantity(int quantity) {
        txtQty.setValue(quantity);
    }

    public int getQuantity() {
        return txtQty.getValue();
    }

    public void setNotes(String notes) {
        txtNotes.setText(notes);
    }

    public String getNotes() {
        return String.valueOf(txtNotes.getText());
    }


    public String getDiscount() {
        return String.valueOf(txtDiscount.getText());
    }

    public void setDiscount(String discount) {
        txtDiscount.setText(discount);
        lblSconto.setText("Prodotto scontato: " + discount);
        lblSconto.setVisibility(View.VISIBLE);
    }
}
