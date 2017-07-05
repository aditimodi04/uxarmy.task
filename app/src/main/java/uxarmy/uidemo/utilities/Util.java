package uxarmy.uidemo.utilities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;

import uxarmy.uidemo.R;

/**
 * Created by user on 05/07/17.
 */

public class Util {
    private static ProgressDialog progressDialog;

    public static Dialog showProDialog(Context context) {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            progressDialog = new ProgressDialog(context, R.style.NewDialog);
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminateDrawable(ContextCompat.getDrawable(context, R.drawable.spl_pbar_drawable));
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return progressDialog;

    }

    public static Dialog dismissProDialog() {
        try {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            progressDialog = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return progressDialog;
    }

    public static void startActAnimation(Activity ctx) {
        ctx.overridePendingTransition(R.anim.right_in, R.anim.left_out);

    }

}
