package com.dk.mp.xg.wsjc.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.dk.mp.core.ui.BaseFragment;

import java.util.List;


/**
 * 作者：janabo on 2017/1/4 14:41
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    List<String> titles;
    private List<BaseFragment> mListFragments = null;
    public MyFragmentPagerAdapter(FragmentManager fm, List< BaseFragment > fragments , List<String> titles) {
        super(fm);
        this.mListFragments = fragments;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        if ( mListFragments != null && position >= 0 && position < mListFragments.size ( ) ) {
            return mListFragments.get ( position );
        } else {
            return null;
        }
    }

    @Override
    public int    getCount() {
        if ( mListFragments != null ) {
            return mListFragments.size ( );
        } else {
            return 0;
        }
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}
