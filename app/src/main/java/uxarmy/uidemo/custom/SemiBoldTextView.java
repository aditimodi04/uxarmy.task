package uxarmy.uidemo.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import uxarmy.uidemo.CameraApp;

/**
 * Created by Aditi on 7/5/2017.
 */

public class SemiBoldTextView extends TextView {

    public SemiBoldTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public SemiBoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SemiBoldTextView(Context context) {
        super(context);
        init();
    }

    public void init() {
        setTypeface(CameraApp.getTypeFaceSemiBold(), 1);

    }
}