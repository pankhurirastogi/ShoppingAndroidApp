package com.example.ebaysearchapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class MyAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;
    public MyAdapter(FragmentManager fm, int NoofTabs){
        super(fm);
        this.mNumOfTabs = NoofTabs;
    }
    @Override
    public int getCount() {
        return mNumOfTabs;
    }
    @Override
    public Fragment getItem(int position){
        switch (position){
            case 0:
                SearchPage searchPage = new SearchPage();
                return searchPage;
            case 1:
                WishList wishList = new WishList();
                return wishList;

            default:
                return null;
        }
    }


}
