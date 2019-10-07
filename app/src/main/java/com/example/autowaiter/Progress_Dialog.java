package com.example.autowaiter;

import android.content.Context;
import android.graphics.Color;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Progress_Dialog {
   public SweetAlertDialog pDialog ,pDialog2;

Context context;

    public Progress_Dialog(Context context) {
        this.context = context;
    }

     public void SweetAlertDialog(){

        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
//        pDialog.getProgressHelper().setBarColor(Color.parseColor("#ee6e2c"));
        pDialog.setTitleText("Loading...");
        pDialog.setCancelable(false);

    }
    public void SweetAlertDialogDone(){

        pDialog2 = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE);
//        pDialog.getProgressHelper().setBarColor(Color.parseColor("#ee6e2c"));
        pDialog2.setTitleText("Done");
        pDialog2.setCancelable(false);

    }
}
