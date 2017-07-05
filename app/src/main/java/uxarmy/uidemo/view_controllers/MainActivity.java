package uxarmy.uidemo.view_controllers;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uxarmy.uidemo.CameraApp;
import uxarmy.uidemo.R;
import uxarmy.uidemo.custom.CameraSurfaceView;
import uxarmy.uidemo.shared_prefs.AppPreferences;
import uxarmy.uidemo.utilities.Util;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, Camera.PictureCallback {

    private Button btnScan;
    private Camera mCamera;
    private CameraSurfaceView mCameraView;
    String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    private FrameLayout preview;
    private ImageView imvCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            if (!hasPermission()) {
                ActivityCompat.requestPermissions(this, PERMISSIONS, 11);
            } else {
                init();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (ClassCastException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            initializeCamera();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeCamera() {
        if (mCamera == null) {
            mCamera = getCameraInstance();
            mCameraView = new CameraSurfaceView(this, mCamera);
            preview = (FrameLayout) findViewById(R.id.svCamera);
            preview.addView(mCameraView);
        }
    }

    private boolean hasPermission() {
        List<String> permissionsNeeded = new ArrayList<String>();
        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("write external storage");
        if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
            permissionsNeeded.add("Read external storage");
        if (!addPermission(permissionsList, Manifest.permission.CAMERA))
            permissionsNeeded.add("Camera");
        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    ActivityCompat.requestPermissions(this, permissionsList.toArray(new String[permissionsList.size()]), 11);

            }
            ActivityCompat.requestPermissions(this, permissionsList.toArray(new String[permissionsList.size()]), 11);
        }
        return permissionsNeeded.isEmpty();


    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permission))
                return false;
        }
        return true;
    }


    private void init() {
        initializeCamera();
        btnScan = (Button) findViewById(R.id.btnScan);
        imvCancel = (ImageView) findViewById(R.id.imvCancel);
        btnScan.setOnClickListener(this);
        imvCancel.setOnClickListener(this);
        btnScan.setTypeface(CameraApp.getTypeFaceSemiBold());
    }


    private Camera getCameraInstance() {
        Camera camera = null;
        try {
            camera = Camera.open();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Camera Not found", Toast.LENGTH_LONG).show();
        }
        return camera;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 11: {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                perms.put(android.Manifest.permission.ACCESS_NETWORK_STATE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                if (perms.get(Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        ) {
                    init();
                } else {
                    Toast.makeText(MainActivity.this, "Some permission is denied", Toast.LENGTH_SHORT)
                            .show();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    private void openActivity(String stringBitmap) {
        AppPreferences.setImageStringBitmap(stringBitmap);
        AppPreferences.savePreferences();
        Intent intent = new Intent(MainActivity.this, ColorActivity.class);
        startActivity(intent);
        Util.startActAnimation(this);
        Util.dismissProDialog();
    }


    private String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    @Override
    public void onClick(View v) {
        int vId = v.getId();
        switch (vId) {
            case R.id.btnScan:
                if (mCamera != null) {
                    Util.showProDialog(this);
                    mCamera.takePicture(null, null, this);
                }
                break;
            case R.id.imvCancel:
                onBackPressed();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCameraView.releaseCamera();
        mCamera = null;
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        Bitmap bitmapPicture = BitmapFactory.decodeByteArray(data, 0, data.length);

        Bitmap correctBmp = Bitmap.createBitmap(bitmapPicture, 0, 0, bitmapPicture.getWidth(), bitmapPicture.getHeight(), null, true);
        String stringBitmap = BitMapToString(correctBmp);
        try {
            openActivity(stringBitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
