package com.p2pdinner.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.Toast;

import com.p2pdinner.R;
import com.p2pdinner.common.Constants;
import com.p2pdinner.entities.DinnerMenuItem;
import com.p2pdinner.seller.dinner.ListDinnerFragmentAdapter;
import com.p2pdinner.seller.dinner.SellerListingFragmentAdapter;


public class SellerOrdersListingActivity extends AppCompatActivity {

    //private ActionBar actionBar;
    private ViewPager viewPager;
    private SellerListingFragmentAdapter fragmentPageAdapter;
    private DinnerMenuItem dinnerMenuItem;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_listings);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.seller_order_listing_tab_layout);
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
        getIntent().putExtra(Constants.PROFILE_ID, profileId);
    }



    private void setUpViewPager() {
        fragmentPageAdapter = new SellerListingFragmentAdapter(getSupportFragmentManager(), getApplicationContext());
        fragmentPageAdapter.createTabs();
        viewPager.setAdapter(fragmentPageAdapter);
    }

}

