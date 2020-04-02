package com.example.beam4;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class MainPagerAdapter extends FragmentStatePagerAdapter {
    int numOfTabs;

    public MainPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.numOfTabs = behavior;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                SortByTimeFragment tab1 = new SortByTimeFragment();
                return tab1;
            case 1:
                SortByImageFragment tab2 = new SortByImageFragment();
                return tab2;
            case 2:
                TrashCanFragment tab3 = new TrashCanFragment();
                return tab3;
            case 3:
                SettingFragment tab4 = new SettingFragment();
                return tab4;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
