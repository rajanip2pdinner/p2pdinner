package com.p2pdinner.fragments;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.p2pdinner.R;
import com.p2pdinner.activities.ListDinnerActivity;
import com.p2pdinner.common.Constants;
import com.p2pdinner.entities.DinnerListing;
import com.p2pdinner.entities.DinnerMenuItem;
import com.p2pdinner.restclient.DinnerListingManager;
import com.p2pdinner.restclient.MenuServiceManager;

import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.content.DialogInterface.*;

/**
 * Created by rajaniy on 9/24/15.
 */
public class FoodItemFragment extends BaseFragment {

    private static final String TAG = "FoodItemFragment";

    private DinnerMenuItem dinnerMenuItem;
    private EditText mTitle;
    private EditText mDescription;
    private Button mNextButton;
    private EditText mCategory;
    private List<String> categoriesList;
    @Inject
    MenuServiceManager menuServiceManager;

    @Inject
    DinnerListingManager dinnerListingManager;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser && getActivity() != null) {
            dinnerMenuItem = (DinnerMenuItem) getActivity().getIntent().getSerializableExtra(Constants.CURRENT_DINNER_ITEM);
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Inject
    Tracker mTracker;

    @Override
    public void onResume() {
        super.onResume();
        mTracker.setScreenName("SellerHome.FoodItem");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        dinnerMenuItem = (DinnerMenuItem) getActivity().getIntent().getSerializableExtra(Constants.CURRENT_DINNER_ITEM);
        View view = layoutInflater.inflate(R.layout.description_layout, viewGroup, false);
        mCategory = (EditText) view.findViewById(R.id.category);
        if (StringUtils.hasText(dinnerMenuItem.getCategories())) {
            mCategory.setText(dinnerMenuItem.getCategories());
        }
        mCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTracker.send(new HitBuilders.EventBuilder().setCategory("Action").setAction("Food Item - Next").build());
                final List<String> selectedOptions = new ArrayList<String>();
                {
                    boolean[] isSelected = new boolean[categoriesList.size()];
                    final CharSequence[] items = new CharSequence[categoriesList.size()];
                    for (int idx = 0; idx < categoriesList.size(); idx++) {
                        items[idx] = categoriesList.get(idx);
                        if (StringUtils.hasText(mCategory.getText().toString()) && mCategory.getText().toString().contains(items[idx])) {
                            selectedOptions.add(categoriesList.get(idx));
                            isSelected[idx] = Boolean.TRUE;
                        } else {
                            isSelected[idx] = Boolean.FALSE;
                        }

                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(R.string.select_category);
                    builder.setMultiChoiceItems(items, isSelected, new android.content.DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                            if (isChecked) {
                                String item = items[which].toString();
                                selectedOptions.add(item);
                            } else {
                                if (selectedOptions.contains(items[which])) {
                                    selectedOptions.remove(items[which]);
                                }
                            }
                        }
                    });
                    builder.setPositiveButton(R.string.ok, new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String categories = StringUtils.collectionToCommaDelimitedString(selectedOptions);
                            mCategory.setText(categories);
                        }
                    });
                    builder.create().show();
                }
            }
        });
        mTitle = (EditText) view.findViewById(R.id.title);
        if (StringUtils.hasText(dinnerMenuItem.getTitle())) {
            mTitle.setText(dinnerMenuItem.getTitle());
        }
        mDescription = (EditText) view.findViewById(R.id.description);
        if (StringUtils.hasText(dinnerMenuItem.getDescription())) {
            mDescription.setText(dinnerMenuItem.getDescription());
        }
        mNextButton = (Button) view.findViewById(R.id.btnNext);

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = mTitle.getText().toString();
                if (StringUtils.hasText(title)) {
                    dinnerMenuItem.setTitle(title);
                    String category = mCategory.getText().toString();
                    dinnerMenuItem.setCategories(category);
                    String description = mDescription.getText().toString();
                    if (StringUtils.hasText(description)) {
                        dinnerMenuItem.setDescription(description);
                    }

                    menuServiceManager.saveMenuItem(dinnerMenuItem)
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<DinnerMenuItem>() {
                                private DinnerMenuItem item;

                                @Override
                                public void onCompleted() {
                                    dinnerMenuItem.setId(item.getId());
                                    Log.i(TAG, "Item saved successfully");
                                    //Toast.makeText(getActivity().getBaseContext(), "Item saved successfully", Toast.LENGTH_SHORT).show();
                                    ListDinnerActivity listDinnerActivity = (ListDinnerActivity) getActivity();
                                    listDinnerActivity.moveToNextTab();
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Toast.makeText(getActivity().getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onNext(DinnerMenuItem item) {
                                    this.item = item;
                                }
                            });
                }

            }
        });
        dinnerListingManager.categories().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String[]>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getActivity().getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(String[] categories) {
                        categoriesList = Arrays.asList(categories);
                    }
                });
        viewGroup.setTag(dinnerMenuItem);
        return view;
    }
}
