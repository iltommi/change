package com.ceppa.ilbibi.change;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.text.TextWatcher;
import android.text.Editable;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.TextView;
import android.view.WindowManager;
import android.util.Log;

public class change extends AppCompatActivity {

    EditText val1;
    EditText tasso;
    EditText percent;
    TextView val2;
    TextView valPerc;

    SharedPreferences sharedPref;

    TextWatcher tw = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void afterTextChanged(Editable s) {
            calculate();
        }
    };

    public static boolean isNumeric(EditText et) {
        try {
            double d = Double.parseDouble(et.getText().toString());
        }
        catch(NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public void calculate(){
        if( isNumeric(val1) && isNumeric(tasso) && isNumeric(percent)) {
            Double v = Double.valueOf(val1.getText().toString()) * Double.valueOf(tasso.getText().toString());
            val2.setText(String.format("%.2f", v));
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("tasso", tasso.getText().toString());
            editor.putString("val1", val1.getText().toString());
            Double v2 = v * Double.valueOf(percent.getText().toString()) / 100;
            Log.d("calculate", v2.toString());
            valPerc.setText(String.format("%.2f", v + v2));
            editor.putString("percent", percent.getText().toString());
            editor.apply();
        }
    }

    public void swap(View target) {
        if (isNumeric(tasso) && isNumeric(percent)) {
            Double v1 = Double.valueOf(tasso.getText().toString());
            if (v1 != 0) {
                percent.setText(String.format("%d", -Integer.valueOf(percent.getText().toString())));
                val1.setText(val2.getText().toString());
                tasso.setText(String.format("%.5f", 1.0 / v1));
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);

        val1 = (EditText) findViewById(R.id.val1);
        tasso = (EditText) findViewById(R.id.tasso);
        percent = (EditText) findViewById(R.id.percent);
        val2 = (TextView) findViewById(R.id.val2);
        valPerc = (TextView) findViewById(R.id.valPerc);

        sharedPref = getSharedPreferences("change", Context.MODE_PRIVATE);

        val1.setText(sharedPref.getString("val1", "1.0"));
        tasso.setText(sharedPref.getString("tasso", "1.0"));
        percent.setText(sharedPref.getString("percent", "10"));

        val1.addTextChangedListener(tw);
        tasso.addTextChangedListener(tw);
        percent.addTextChangedListener(tw);

        calculate();
        val1.requestFocus();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

    }

}