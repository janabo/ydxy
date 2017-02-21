package com.dk.mp.main.message.ui;

import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.CoreSharedPreferencesHelper;
import com.dk.mp.main.R;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by dongqs on 2017/2/21.
 */

public class BrithdayMessageActivity extends MyActivity{
    @Override
    protected int getLayoutID() {
        return R.layout.mp_main_brithday_info;
    }

    @Override
    protected void initialize() {
        super.initialize();

        setTitle("生日贺卡");
        showBrithdayTheme();

        CoreSharedPreferencesHelper helper = new CoreSharedPreferencesHelper(this);
        String photo = helper.getUser().getPhoto();
        String name = helper.getUser().getUserName();
        SimpleDraweeView image = (SimpleDraweeView)findViewById(R.id.image);
        if (photo != null && !photo.equals("")) {
            image.setImageURI(Uri.parse(photo));
        }

        TextView username = (TextView)findViewById(R.id.textname);
        if (name != null && !name.equals("")) {
            username.setText(name);
        }

        findViewById(R.id.backlistview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
    }
}
