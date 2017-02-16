package com.dk.mp.xg.wsjc.ui.Sswz;

import android.Manifest;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.xg.R;
import com.dk.mp.xg.wsjc.util.BeepManager;
import com.dk.mp.xg.wsjc.util.CameraManager;
import com.dk.mp.xg.wsjc.util.InactivityTimer;
import com.dk.mp.xg.wsjc.util.SswzDecodeThread;
import com.dk.mp.xg.wsjc.util.SswzHandler;
import com.google.zxing.Result;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 宿舍违章登记
 * 作者：janabo on 2017/1/16 16:50
 */
public class SswzMainActivity extends MyActivity implements
        SurfaceHolder.Callback, EasyPermissions.PermissionCallbacks{
    TextView jcrecord;
    private SurfaceView scanPreview = null;
    private LinearLayout scanContainer;
    private RelativeLayout scanCropView;
    private ImageView scanLine;
    private InactivityTimer inactivityTimer;
    private BeepManager beepManager;
    private CameraManager cameraManager;
    private Rect mCropRect = null;
    private boolean isHasSurface = false;
    private SswzHandler handler;
    private RelativeLayout mRootView;
    private static final String TAG = SswzMainActivity.class.getSimpleName();

    @Override
    protected int getLayoutID() {
        return R.layout.app_wsjcdf;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutID();
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mRootView = (RelativeLayout) findViewById(R.id.mRootView);
        scanPreview = (SurfaceView) findViewById(R.id.capture_preview);
        scanPreview.getHolder().addCallback(this);
        setTitle(getIntent().getStringExtra("title"));
        jcrecord = (TextView) findViewById(R.id.jcrecord);
        jcrecord.setText("宿舍违章登记记录");
        cameraTask();
    }

    @Override
    protected void onResume() {
        if (scanPreview != null) {
            handler = null;
            if (isHasSurface) {
                initCamera(scanPreview.getHolder());
            }
        }
        if (inactivityTimer != null) {
            inactivityTimer.onResume();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        if (inactivityTimer != null) {
            inactivityTimer.onPause();
        }
        if (beepManager != null) {
            beepManager.close();
        }
        if (cameraManager != null) {
            cameraManager.closeDriver();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (inactivityTimer != null) {
            inactivityTimer.shutdown();
        }
        if (scanPreview != null) {
            scanPreview.getHolder().removeCallback(this);
        }
        super.onDestroy();
    }

    public Handler getHandler() {
        return handler;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    /**
     * 宿舍违章登记
     * @param v
     */
    public void towsjcdetail(View v){
//        Intent intent = new Intent(this,SswzDjMainActivity.class);
//        startActivity(intent);
    }

    /**
     * 宿舍违章登记记录
     * @param v
     */
    public void toWsjcjl(View v){
        Intent intent = new Intent(this,SswzRecordListActivity.class);
        startActivity(intent);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (holder == null) {
            Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
        }
        isHasSurface = true;
        initCamera(holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    /**
     * 初始化截取的矩形区域
     */
    @SuppressWarnings("SuspiciousNameCombination")
    private void initCrop() {
        int cameraWidth = cameraManager.getCameraResolution().y;
        int cameraHeight = cameraManager.getCameraResolution().x;

        /** 获取布局中扫描框的位置信息 */
        int[] location = new int[2];
        scanCropView.getLocationInWindow(location);

        int cropLeft = location[0];
        int cropTop = location[1] - getStatusBarHeight();

        int cropWidth = scanCropView.getWidth();
        int cropHeight = scanCropView.getHeight();

        /** 获取布局容器的宽高 */
        int containerWidth = scanContainer.getWidth();
        int containerHeight = scanContainer.getHeight();

        /** 计算最终截取的矩形的左上角顶点x坐标 */
        int x = cropLeft * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的左上角顶点y坐标 */
        int y = cropTop * cameraHeight / containerHeight;

        /** 计算最终截取的矩形的宽度 */
        int width = cropWidth * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的高度 */
        int height = cropHeight * cameraHeight / containerHeight;

        /** 生成最终的截取的矩形 */
        mCropRect = new Rect(x, y, width + x, height + y);
    }

    private int getStatusBarHeight() {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (perms != null && perms.size() == 2) {
            initCamera();
        } else {
            displayFrameworkBugMessageAndExit();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        displayFrameworkBugMessageAndExit();
    }

    private void displayFrameworkBugMessageAndExit() {
        Toast.makeText(this, R.string.permissions_camera_error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private static final int CAMERA_PERM = 1;

    @AfterPermissionGranted(CAMERA_PERM)
    private void cameraTask() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.VIBRATE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            initCamera();
        } else {
            EasyPermissions.requestPermissions(this,
                    getResources().getString(R.string.str_request_camera_message),
                    CAMERA_PERM, perms);
        }
    }

    public Rect getCropRect() {
        return mCropRect;
    }

    private void initCamera() {
        scanContainer = (LinearLayout) findViewById(R.id.capture_container);
        scanCropView = (RelativeLayout) findViewById(R.id.capture_crop_view);
        scanLine = (ImageView) findViewById(R.id.capture_scan_line);

        inactivityTimer = new InactivityTimer(this);
        beepManager = new BeepManager(this);

        TranslateAnimation animation = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.9f);
        animation.setDuration(4500);
        animation.setRepeatCount(-1);
        animation.setRepeatMode(Animation.RESTART);
        scanLine.startAnimation(animation);

        cameraManager = new CameraManager(getApplication());
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        if (cameraManager == null)
            return;

        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            Log.w(TAG,
                    "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try {
            cameraManager.openDriver(surfaceHolder);
            // Creating the handler starts the preview, which can also throw a
            // RuntimeException.
            if (handler == null) {
                handler = new SswzHandler(this, cameraManager,
                        SswzDecodeThread.ALL_MODE);
            }

            initCrop();
        } catch (IOException | RuntimeException e) {
            Log.w(TAG, "Unexpected error initializing camera", e);
            displayFrameworkBugMessageAndExit();
        }
    }

    /**
     * A valid barcode has been found, so give an indication of success and show
     * the results.
     *
     * @param rawResult The contents of the barcode.
     * @param bundle    The extras
     */
    public void handleDecode(final Result rawResult, Bundle bundle) {
        inactivityTimer.onActivity();
        beepManager.playBeepSoundAndVibrate();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                handleText(rawResult.getText());
            }
        }, 300);
    }

    private void handleText(String text) {
        //判断是否是合法的json
        if (StringUtils.isUrl(text)) {
            showErrorMsg(mRootView,"二维码有误，请重新扫描");
            if (scanPreview != null) {
                handler = null;
                if (isHasSurface) {
                    initCamera(scanPreview.getHolder());
                }
            }
            if (inactivityTimer != null) {
                inactivityTimer.onResume();
            }
        } else {
            handleOtherText(text);
        }
    }


    private void handleOtherText(final String text) {
        // 判断是否符合基本的json格式
        Intent intent = new Intent(this,SswzDjMainActivity.class);
        intent.putExtra("wsjcDetail",text);
        startActivity(intent);
    }
}
