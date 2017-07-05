package uxarmy.uidemo.view_controllers;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback;

import java.util.ArrayList;

import jp.wasabeef.blurry.Blurry;
import uxarmy.uidemo.R;
import uxarmy.uidemo.adapter.ColorAdapter;
import uxarmy.uidemo.dao.ColorPalette;
import uxarmy.uidemo.shared_prefs.AppPreferences;

/**
 * Created by Aditi on 7/5/2017.
 */

public class ColorActivity extends Activity implements View.OnClickListener, Palette.PaletteAsyncListener, ColorPickerCallback {

    private ImageView imvPickImage;
    private ImageView imvCancel;
    private Button btnScan;
    private Button addColor;
    private RecyclerView rvAddColor;
    private ArrayList<ColorPalette> colors = new ArrayList<>();
    private View imvSelectedColor;
    private ColorPicker colorPicker;
    private ColorAdapter colorAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_color);
            imvPickImage = (ImageView) findViewById(R.id.imvPickImage);
            imvCancel = (ImageView) findViewById(R.id.imvCancel);
            btnScan = (Button) findViewById(R.id.btnScan);
            addColor = (Button) findViewById(R.id.addColor);
            rvAddColor = (RecyclerView) findViewById(R.id.rvAddColor);
            imvSelectedColor = findViewById(R.id.imvSelectedColor);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            rvAddColor.setLayoutManager(layoutManager);
            String bitmapString = AppPreferences.getImageStringBitmap();
            Bitmap bitmap = StringToBitMap(bitmapString);
            Palette.from(bitmap).generate(this);
            imvPickImage.setImageBitmap(bitmap);
            Blurry.with(this).radius(20).from(bitmap).into(imvPickImage);
            btnScan.setOnClickListener(this);
            imvCancel.setOnClickListener(this);
            addColor.setOnClickListener(this);
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (ClassCastException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            return bitmap;
        } catch (NullPointerException e) {
            e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onClick(View v) {
        int vId = v.getId();
        switch (vId) {
            case R.id.btnScan:
            case R.id.imvCancel:
                onBackPressed();
                break;
            case R.id.addColor:
                colorPicker = new ColorPicker(this, 0, 0, 0);
                colorPicker.setCallback(this);
                colorPicker.show();
                break;
            case R.id.viewPalette:
                ColorPalette color = (ColorPalette) v.getTag();
                if (color.paletteSwatch != null) {
                    imvSelectedColor.setBackgroundColor(color.paletteSwatch.getRgb());
                } else {
                    imvSelectedColor.setBackgroundColor(color.colorCode);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onGenerated(Palette palette) {
        try {
            int vibrant = palette.getVibrantColor(0x000000);
            int darkVibrantColor = palette.getDarkVibrantColor(0x000000);
            int lightVibrantColor = palette.getLightVibrantColor(0x000000);
            int mutedColor = palette.getMutedColor(0x000000);
            int mutedLight = palette.getLightMutedColor(0x000000);
            int mutedDark = palette.getDarkMutedColor(0x000000);
            if (vibrant != 0) {
                Palette.Swatch vibrantSwatch = palette.getVibrantSwatch();
                ColorPalette colorPalette = new ColorPalette();
                colorPalette.colorCode = vibrant;
                colorPalette.paletteSwatch = vibrantSwatch;
                colors.add(colorPalette);
            }
            if (darkVibrantColor != 0) {

                Palette.Swatch swatch = palette.getDarkVibrantSwatch();
                ColorPalette colorPalette = new ColorPalette();
                colorPalette.colorCode = darkVibrantColor;
                colorPalette.paletteSwatch = swatch;
                colors.add(colorPalette);
            }
            if (lightVibrantColor != 0) {
                Palette.Swatch swatch = palette.getLightVibrantSwatch();
                ColorPalette colorPalette = new ColorPalette();
                colorPalette.colorCode = lightVibrantColor;
                colorPalette.paletteSwatch = swatch;
                colors.add(colorPalette);

            }
            if (mutedColor != 0) {

                Palette.Swatch swatch = palette.getMutedSwatch();
                ColorPalette colorPalette = new ColorPalette();
                colorPalette.colorCode = mutedColor;
                colorPalette.paletteSwatch = swatch;
                colors.add(colorPalette);
            }
            if (mutedLight != 0) {
                Palette.Swatch swatch = palette.getLightMutedSwatch();
                ColorPalette colorPalette = new ColorPalette();
                colorPalette.colorCode = mutedLight;
                colorPalette.paletteSwatch = swatch;
                colors.add(colorPalette);
            }
            if (mutedDark != 0) {
                Palette.Swatch swatch = palette.getDarkMutedSwatch();
                ColorPalette colorPalette = new ColorPalette();
                colorPalette.colorCode = mutedDark;
                colorPalette.paletteSwatch = swatch;
                colors.add(colorPalette);
            }
            if (!colors.isEmpty()) {
                ColorPalette colorPalette = colors.get(0);
                imvSelectedColor.setBackgroundColor(colorPalette.colorCode);
            }
            colorAdapter = new ColorAdapter(ColorActivity.this, colors);
            rvAddColor.setAdapter(colorAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onColorChosen(@ColorInt int color) {
        try {
            ColorPalette colorPalette = new ColorPalette();
            colorPalette.colorCode = color;
            imvSelectedColor.setBackgroundColor(colorPalette.colorCode);
            colors.add(0, colorPalette);
            colorPicker.dismiss();
            if (colorAdapter != null) {
                colorAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
