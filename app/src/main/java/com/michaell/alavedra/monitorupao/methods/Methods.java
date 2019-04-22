package com.michaell.alavedra.monitorupao.methods;

import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.michaell.alavedra.monitorupao.R;
import com.michaell.alavedra.monitorupao.R;

import java.util.Date;

public class Methods {
    public static void showSnackbar(Snackbar snackbar) {
        snackbar.show();
        View view = snackbar.getView();
        TextView textView = view.findViewById(R.id.snackbar_text);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
    }

    public static String getSemestre() {
        Date today = new Date();
        int month = Integer.parseInt((String) DateFormat.format("MM", today));
        String year = (String) DateFormat.format("yyyy", today);
        return (year.concat((month <= 6) ? "01" : "02"));
    }
}
