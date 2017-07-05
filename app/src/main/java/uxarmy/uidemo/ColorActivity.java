package uxarmy.uidemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.util.Base64;

/**
 * Created by Aditi on 7/5/2017.
 */

public class ColorActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_color);
        String btimapString = getIntent().getExtras().getString("bitmap");
        Bitmap bitmap = StringToBitMap(btimapString);
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            public void onGenerated(Palette palette) {
                Palette.Swatch vibrantSwatch = palette.getVibrantSwatch();
                if (vibrantSwatch != null) {
//                    outerLayout.setBackgroundColor(vibrantSwatch.getRgb());
//                    titleText.setTextColor(vibrantSwatch.getTitleTextColor());
//                    bodyText.setTextColor(vibrantSwatch.getBodyTextColor());
                }
            }
        });
    }


    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
}
