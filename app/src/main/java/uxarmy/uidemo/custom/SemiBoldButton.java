package uxarmy.uidemo.custom;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.Button;

import uxarmy.uidemo.CameraApp;

/**
 * Created by Aditi on 7/5/2017.
 */

public class SemiBoldButton extends Button {
    public SemiBoldButton(Context context) {
        super(context);
        init();
    }

    public SemiBoldButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SemiBoldButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SemiBoldButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setTypeface(CameraApp.getTypeFaceSemiBold());
    }
}
