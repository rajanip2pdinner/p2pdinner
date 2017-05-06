package com.p2pdinner.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.p2pdinner.R;
import com.p2pdinner.common.Constants;
import com.p2pdinner.entities.DinnerMenuItem;
import com.p2pdinner.restclient.MenuServiceManager;
import com.p2pdinner.seller.dinner.CreateDinnerOption;

import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class CreateDinnerActivity extends BaseAppCompatActivity {


    private ListView listView;
    private ProgressBar favourtiesProgress;
    private SharedPreferences sharedPreferences;

    @Inject
    MenuServiceManager menuServiceManager;

    @Inject
    Tracker mTracker;

    @Override
    public void onResume() {
        super.onResume();
        mTracker.setScreenName("SellerHome");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        listView = (ListView) findViewById(R.id.createDinnerListView);
        favourtiesProgress = (ProgressBar) findViewById(R.id.favouritesProgress);
        sharedPreferences = getSharedPreferences(Constants.PREFS_PRIVATE, Context.MODE_PRIVATE);
        populateOptions();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_dinner);
    }
    private void populateOptions() {
        final List<CreateDinnerOption> createDinnerOptionList = new ArrayList<>();
        createDinnerOptionList.add(new CreateDinnerOption(getString(R.string.create_new), true, "", null));
        createDinnerOptionList.add(new CreateDinnerOption(getString(R.string.select_favorites), false, "Previously created by you", null));
        final SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(Constants.PREFS_PRIVATE, MODE_PRIVATE);
        favourtiesProgress.setVisibility(ProgressBar.VISIBLE);
        menuServiceManager.favouritesByProfileId(sharedPreferences.getLong(Constants.PROFILE_ID, 0L))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<DinnerMenuItem>>() {
                    @Override
                    public void onCompleted() {
                        //populateView();
                        favourtiesProgress.setVisibility(ProgressBar.INVISIBLE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG);
                    }

                    @Override
                    public void onNext(List<DinnerMenuItem> dinnerMenuItems) {
                        Collections.sort(dinnerMenuItems, new Comparator<DinnerMenuItem>() {
                            @Override
                            public int compare(DinnerMenuItem lhs, DinnerMenuItem rhs) {
                                return lhs.getTitle().compareToIgnoreCase(rhs.getTitle());
                            }
                        });
                        for (DinnerMenuItem dinnerMenuItem : dinnerMenuItems) {
                            CreateDinnerOption createDinnerOption = new CreateDinnerOption(dinnerMenuItem.getTitle(), true, dinnerMenuItem.getDescription(), dinnerMenuItem.getId());
                            createDinnerOptionList.add(createDinnerOption);
                        }
                        populateView(createDinnerOptionList);
                    }
                });
    }

    private void populateView(List<CreateDinnerOption> options) {
        ArrayAdapter<CreateDinnerOption> createDinnerOptionArrayAdapter = new CreateDinnerOptionsListAdapter(options);
        listView.setAdapter(createDinnerOptionArrayAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_dinner, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class CreateDinnerOptionsListAdapter extends ArrayAdapter<CreateDinnerOption> {

        private List<CreateDinnerOption> createDinnerOptions;

        public CreateDinnerOptionsListAdapter(List<CreateDinnerOption> createDinnerOptionList) {
            super(getApplicationContext(), R.layout.item_createdinner_options, createDinnerOptionList);
            this.createDinnerOptions = createDinnerOptionList;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.item_createdinner_options, parent, false);
            }
            CreateDinnerOption option = createDinnerOptions.get(position);
            if (option.getTitle().equalsIgnoreCase(getString(R.string.create_new)) ||
                    option.getTitle().equalsIgnoreCase(getString(R.string.select_favorites))) {
                TextView txtView = (TextView) itemView.findViewById(R.id.item_title);
                txtView.setPadding(0, 0, 0, 0);
                txtView.setText(option.getTitle());
            } else {
                TextView txtView = (TextView) itemView.findViewById(R.id.item_title);
                txtView.setPadding(50, 0, 0, 0);
                txtView.setText(option.getTitle());
            }

            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
            TextView subTitleTextView = (TextView) itemView.findViewById(R.id.item_short_description);
            if (option.getTitle().equalsIgnoreCase(getString(R.string.create_new)) ||
                    option.getTitle().equalsIgnoreCase(getString(R.string.select_favorites))) {
                if (option.getSubTitle() != null && option.getSubTitle().length() != 0) {
                    subTitleTextView.setText(option.getSubTitle());
                } else {
                    subTitleTextView.setText("");
                }
            } else {
                if (option.getSubTitle() != null && option.getSubTitle().length() != 0) {
                    subTitleTextView.setPadding(50, 0, 0, 0);
                    subTitleTextView.setText(option.getSubTitle());
                } else {
                    subTitleTextView.setText("");
                }
            }

            if (option.getItemId() != null) {
                itemView.setId(option.getItemId().intValue());
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView txtView = (TextView) v.findViewById(R.id.item_title);
                    if (txtView.getText().toString().equalsIgnoreCase(getString(R.string.create_new))) {
                        String certificates = sharedPreferences.getString(Constants.CERTIFICATES, "");
                        if (!StringUtils.hasText(certificates)) {
                            Intent foodAndSafetyIntent = new Intent(getApplicationContext(), FoodAndSafetyActivity.class);
                            startActivity(foodAndSafetyIntent);
                        } else {
                            mTracker.send(new HitBuilders.EventBuilder().setCategory("Action").setAction("Create New").build());
                            Intent intent = new Intent(getApplicationContext(), ListDinnerActivity.class);
                            intent.putExtra(Constants.CURRENT_DINNER_ITEM, new DinnerMenuItem());
                            startActivity(intent);
                        }
                    } else if (!txtView.getText().toString().equalsIgnoreCase(getString(R.string.select_favorites))) {
                        final Long viewId = new Long(v.getId());
                        mTracker.send(new HitBuilders.EventBuilder().setCategory("Action").setAction("Favourites").build());
                        menuServiceManager.menuItemById(viewId).subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Observer<DinnerMenuItem>() {
                                    @Override
                                    public void onCompleted() {
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG);
                                    }

                                    @Override
                                    public void onNext(DinnerMenuItem dinnerMenuItem) {
                                        Intent intent = new Intent(getApplicationContext(), ListDinnerActivity.class);
                                        intent.putExtra(Constants.CURRENT_DINNER_ITEM, dinnerMenuItem);
                                        startActivity(intent);
                                    }
                                });
                    }
                }
            });
            return itemView;
        }
    }
}
