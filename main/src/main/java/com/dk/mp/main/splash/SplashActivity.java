package com.dk.mp.main.splash;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.main.R;
import com.dk.mp.main.home.ui.HomeActivity;

/**
 * 作者：janabo on 2016/12/14 15:08
 */
public class SplashActivity extends MyActivity {
    private Handler mHandler = new Handler();
    private ImageView imageView;
    private RelativeLayout layout;

    @Override
    protected int getLayoutID() {
        return R.layout.mp_splash;
    }

    @Override
    protected void initialize() {
        super.initialize();

        imageView = (ImageView) findViewById(R.id.splashImageview);
        layout = (RelativeLayout) findViewById(R.id.splash);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent ( SplashActivity.this, HomeActivity.class );
                startActivity ( intent );
                finish();
            }
        },2000);

    }

    public static void releaseImageViewResouce(ImageView imageView) {
        if (imageView == null) return;
        Drawable drawable = imageView.getDrawable();
        if (drawable != null && drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        releaseImageViewResouce(imageView);

        BitmapDrawable bd = (BitmapDrawable)layout.getBackground();
        layout.setBackgroundResource(0);
        bd.setCallback(null);
        bd.getBitmap().recycle();
        layout = null;
    }
}
