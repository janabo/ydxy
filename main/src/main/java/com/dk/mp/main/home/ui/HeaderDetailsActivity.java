package com.dk.mp.main.home.ui;

import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.main.R;

/**
 * Created by dongqs on 16/8/8.
 */
public class HeaderDetailsActivity extends MyActivity{

//    private DraweeView imageView;


    @Override
    protected int getLayoutID() {
        return R.layout.headerdetails;
    }

    @Override
    protected void initialize() {
        super.initialize();
//        imageView = (DraweeView) findViewById(R.id.imageview);
//        imageView.setImageURI(Uri.parse(getIntent().getStringExtra("uri")));
    }

}
