package uxarmy.uidemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.blurry.Blurry;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnScan;
    private Camera mCamera;
    private CameraSurfaceView mCameraView;
    String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    private ScriptIntrinsicBlur blur;
    private View activity_main;
    private FrameLayout preview;

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

        } catch (Exception e) {
            e.printStackTrace();
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
        if (permissionsNeeded.isEmpty()) {
            return true;
        }
        return false;


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
        activity_main = (View) findViewById(R.id.activity_main);
        mCamera = getCameraInstance();
        mCameraView = new CameraSurfaceView(this, mCamera);
        preview = (FrameLayout) findViewById(R.id.svCamera);
        preview.addView(mCameraView);
        btnScan = (Button) findViewById(R.id.btnScan);
        btnScan.setOnClickListener(this);
    }


    private Camera getCameraInstance() {
        Camera camera = null;
        try {
            camera = Camera.open();
        } catch (Exception e) {
            // cannot get camera or does not exist
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

    Camera.ShutterCallback myShutterCallback = new Camera.ShutterCallback(){

        public void onShutter() {
            // TODO Auto-generated method stub
        }};

    Camera.PictureCallback myPictureCallback_RAW = new Camera.PictureCallback(){

        public void onPictureTaken(byte[] arg0, Camera arg1) {
            // TODO Auto-generated method stub
        }};

    Camera.PictureCallback myPictureCallback_JPG = new Camera.PictureCallback(){

        public void onPictureTaken(byte[] arg0, Camera arg1) {
            // TODO Auto-generated method stub
            Bitmap bitmapPicture = BitmapFactory.decodeByteArray(arg0, 0, arg0.length);

            Bitmap correctBmp = Bitmap.createBitmap(bitmapPicture, 0, 0, bitmapPicture.getWidth(), bitmapPicture.getHeight(), null, true);

        }};
    @Override
    public void onClick(View v) {
        int vId = v.getId();
        switch (vId) {
            case R.id.btnScan:
                if (mCamera != null) {
                    mCamera.takePicture(myShutterCallback, myPictureCallback_RAW, myPictureCallback_JPG);
                }
             //   Blurry.with(this).sampling(10).onto((ViewGroup) preview);
//                getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
                break;
        }
    }

    Bitmap BlurImage(Bitmap input) {
        android.renderscript.RenderScript rsScript = RenderScript.create(this);
        android.renderscript.Allocation alloc = Allocation.createFromBitmap(rsScript, input);

        blur = ScriptIntrinsicBlur.create(rsScript, alloc.getElement());
        blur.setRadius(12);
        blur.setInput(alloc);

        Bitmap result = Bitmap.createBitmap(input.getWidth(), input.getHeight(), input.getConfig());
        android.renderscript.Allocation outAlloc = Allocation.createFromBitmap(rsScript, result);
        blur.forEach(outAlloc);
        outAlloc.copyTo(result);

        rsScript.destroy();
        return result;
    }
}
