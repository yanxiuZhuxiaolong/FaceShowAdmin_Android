package com.yanxiu.gphone.faceshowadmin_android.main.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by lufengqing on 2017/10/31.
 */

public class MainDetailPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> mTabContents;
    private List<String> mTitleList;

    public List<String> getTitleList() {
        return mTitleList;
    }

    public void setTitleList(List<String> mTitleList) {
        this.mTitleList = mTitleList;
    }

    public MainDetailPagerAdapter(FragmentManager fm, List<Fragment> mTabContents) {
        super(fm);
        this.mTabContents = mTabContents;
    }



    @Override
    public Fragment getItem(int position) {
        return mTabContents.get(position);
    }

    @Override
    public int getCount() {
        return mTabContents==null||mTabContents.size()==0?0:mTabContents.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(mTitleList != null) {
            return mTitleList.get(position);
        }
        return "";
    }
}
