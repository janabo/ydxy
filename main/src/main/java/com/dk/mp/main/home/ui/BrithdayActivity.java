package com.dk.mp.main.home.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dk.mp.core.util.CoreSharedPreferencesHelper;
import com.dk.mp.main.R;
import com.dk.mp.main.message.ui.BrithdayMessageActivity;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by dongqs on 2017/2/20.
 */

public class BrithdayActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.mp_main_brithday);

        CoreSharedPreferencesHelper helper = new CoreSharedPreferencesHelper(this);
        String photo = helper.getUser().getPhoto();
        String name = helper.getUser().getUserName();

        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        SimpleDraweeView image = (SimpleDraweeView)findViewById(R.id.image);
        if (photo != null && !photo.equals("")) {
            image.setImageURI(Uri.parse(photo));
        }

        TextView username = (TextView)findViewById(R.id.textname);
        if (name != null && !name.equals("")) {
            username.setText(name);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }

    public void tomessinfo(View view){
        Intent intent = new Intent(this, BrithdayMessageActivity.class);
        startActivity(intent);
        finish();
    }
}
