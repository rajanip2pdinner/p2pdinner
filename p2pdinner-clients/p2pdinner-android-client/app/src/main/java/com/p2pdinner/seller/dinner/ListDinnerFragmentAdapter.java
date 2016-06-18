package com.p2pdinner.seller.dinner;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.p2pdinner.R;
import com.p2pdinner.fragments.CostFragment;
import com.p2pdinner.fragments.FoodItemFragment;
import com.p2pdinner.fragments.PhotosFragment;
import com.p2pdinner.fragments.PlaceFragment;
import com.p2pdinner.fragments.SplNeedsFragment;
import com.p2pdinner.fragments.TimeFragment;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rajaniy on 10/3/15.
 */
public class ListDinnerFragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> tabTitles = new ArrayList<>();

    private Map<Integer, Fragment> fragmentMap = new LinkedHashMap<>();

    private Context context;

    public ListDinnerFragmentAdapter(FragmentManager fragmentManager, Context context) {
        super(fragmentManager);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        List<Fragment> fragments = new ArrayList<>(fragmentMap.values().size());
        fragments.addAll(fragmentMap.values());
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragmentMap.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }

    public void createTabs() {
        fragmentMap.put(R.drawable.food_item, new FoodItemFragment());
        fragmentMap.put(R.drawable.photo, new PhotosFragment());
        fragmentMap.put(R.drawable.time, new TimeFragment());
        fragmentMap.put(R.drawable.place, new PlaceFragment());
        fragmentMap.put(R.drawable.cost, new CostFragment());
        fragmentMap.put(R.drawable.splneeds, new SplNeedsFragment());
    }

    public Map<Integer, Fragment> getFragmentMap() {
        return fragmentMap;
    }
}
