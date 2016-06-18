package com.p2pdinner.seller.dinner;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.p2pdinner.R;
import com.p2pdinner.fragments.CostFragment;
import com.p2pdinner.fragments.FoodItemFragment;
import com.p2pdinner.fragments.HaveDinnerFragment;
import com.p2pdinner.fragments.PhotosFragment;
import com.p2pdinner.fragments.PlaceFragment;
import com.p2pdinner.fragments.SplNeedsFragment;
import com.p2pdinner.fragments.TimeFragment;
import com.p2pdinner.fragments.WantDinnerFragment;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rajaniy on 10/3/15.
 */
public class SellerListingFragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> tabTitles = new ArrayList<>();

    private Map<String, Fragment> fragmentMap = new LinkedHashMap<>();

    private Context context;

    public SellerListingFragmentAdapter(FragmentManager fragmentManager, Context context) {
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
        List<String> fragments = new ArrayList<>(fragmentMap.keySet().size());
        fragments.addAll(fragmentMap.keySet());
        return fragments.get(position).toString();
    }

    public void createTabs() {
        fragmentMap.put(context.getString(R.string.i_have_dinner), new HaveDinnerFragment());
        fragmentMap.put(context.getString(R.string.i_want_dinner), new WantDinnerFragment());
    }

    public Map<String, Fragment> getFragmentMap() {
        return fragmentMap;
    }
}
