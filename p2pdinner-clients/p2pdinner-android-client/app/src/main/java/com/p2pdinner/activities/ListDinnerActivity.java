package com.p2pdinner.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.p2pdinner.R;
import com.p2pdinner.common.Constants;
import com.p2pdinner.entities.DinnerMenuItem;
import com.p2pdinner.seller.dinner.ListDinnerFragmentAdapter;

import java.util.Map;


public class ListDinnerActivity extends AppCompatActivity {

    //private ActionBar actionBar;
    private ViewPager viewPager;
    private ListDinnerFragmentAdapter fragmentPageAdapter;
    private DinnerMenuItem dinnerMenuItem;
    private SharedPreferences sharedPreferences;

    private Integer[] icons = {
            R.drawable.food_item_icon_1x,
            R.drawable.photo_icon_1x,
            R.drawable.time_icon_1x,
            R.drawable.location_icon_1x,
            R.drawable.cost_icon_bw_1x,
            R.drawable.special_needs_1x
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_dinner);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.pager);
        setUpViewPager();
        tabLayout.setupWithViewPager(viewPager);
        dinnerMenuItem = (DinnerMenuItem) getIntent().getSerializableExtra(Constants.CURRENT_DINNER_ITEM);
        sharedPreferences = getSharedPreferences(Constants.PREFS_PRIVATE, Context.MODE_PRIVATE);
        Long profileId = sharedPreferences.getLong(Constants.PROFILE_ID, 0L);
        if (profileId == null || profileId == 0L) {
            // This should never happen.
            Toast.makeText(getBaseContext(), "Application data seems to be corrupted.. ", Toast.LENGTH_SHORT).show();
            finish();
            System.exit(0);
        }
        dinnerMenuItem.setProfileId(profileId);
        getIntent().putExtra(Constants.CURRENT_DINNER_ITEM, dinnerMenuItem);
        for(int idx = 0; idx < fragmentPageAdapter.getCount(); idx++) {
            //ImageView imageView = (ImageView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
            //imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            //imageView.setImageResource(icons[idx]);
            //tabLayout.getTabAt(idx).setCustomView(imageView);
            //tabLayout.getTabAt(idx).setText(getResources().getStringArray(R.array.tab_labels)[idx]);
            //tabLayout.getTabAt(idx).setIcon(icons[idx]);
            TextView tab = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
            tab.setText(getResources().getStringArray(R.array.tab_labels)[idx]);
            tab.setCompoundDrawablesWithIntrinsicBounds(icons[idx], 0, 0, 0);
            tabLayout.getTabAt(idx).setCustomView(tab);

        }
    }

    public void moveToNextTab() {
        int currentItem = viewPager.getCurrentItem();
        if (currentItem + 1 > fragmentPageAdapter.getCount()) {
            viewPager.setCurrentItem(0, true);

        } else {
            viewPager.setCurrentItem(currentItem + 1, true);
        }
    }


    private void setUpViewPager() {
        fragmentPageAdapter = new ListDinnerFragmentAdapter(getSupportFragmentManager(), getApplicationContext());
        fragmentPageAdapter.createTabs();
        viewPager.setAdapter(fragmentPageAdapter);
    }

}

