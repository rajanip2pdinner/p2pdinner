package com.p2pdinner.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.p2pdinner.R;
import com.p2pdinner.activities.ListDinnerActivity;
import com.p2pdinner.common.Constants;
import com.p2pdinner.common.ErrorResponse;
import com.p2pdinner.entities.DinnerMenuItem;
import com.p2pdinner.restclient.DinnerListingManager;
import com.p2pdinner.restclient.MenuServiceManager;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by rajaniy on 9/24/15.
 */
public class SplNeedsFragment extends BaseFragment {

    private static final String TAG = SplNeedsFragment.class.getName();

    private View mView;
    private DinnerMenuItem dinnerMenuItem;
    private Button mBtnSell;
    private EditText mSplNeeds;
    private List<String> splNeedsList = new ArrayList<>();
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.splneeds_layout, container, false);
        dinnerMenuItem = (DinnerMenuItem) getActivity().getIntent().getSerializableExtra(Constants.CURRENT_DINNER_ITEM);
        mSplNeeds = (EditText) mView.findViewById(R.id.splNeeds);
        if (StringUtils.hasText(dinnerMenuItem.getSpecialNeeds())) {
            mSplNeeds.setText(dinnerMenuItem.getSpecialNeeds());
        }
        mSplNeeds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final List<String> selectedOptions = new ArrayList<String>();
                {
                    boolean[] isSelected = new boolean[splNeedsList.size()];
                    final CharSequence[] items = new CharSequence[splNeedsList.size()];
                    for (int idx = 0; idx < splNeedsList.size(); idx++) {
                        items[idx] = splNeedsList.get(idx);
                        if (StringUtils.hasText(mSplNeeds.getText().toString()) && mSplNeeds.getText().toString().contains(items[idx])) {
                            selectedOptions.add(splNeedsList.get(idx));
                            isSelected[idx] = Boolean.TRUE;
                        } else {
                            isSelected[idx] = Boolean.FALSE;
                        }

                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(R.string.select_splneed);
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
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String categories = StringUtils.collectionToCommaDelimitedString(selectedOptions);
                            mSplNeeds.setText(categories);
                        }
                    });
                    builder.create().show();
                }
            }
        });
        dinnerListingManager.specialNeeds().subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<String[]>() {
            private String[] splNeeds;
            @Override
            public void onCompleted() {
                splNeedsList.addAll(Arrays.asList(splNeeds));
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String[] splNeeds) {
                this.splNeeds = splNeeds;
            }
        });
        mBtnSell = (Button) mView.findViewById(R.id.btnFinish);
        mBtnSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBtnSell.setEnabled(false);
                dinnerMenuItem.setSpecialNeeds(mSplNeeds.getText().toString());
                Observable<DinnerMenuItem> dinnerMenuItemObservable = menuServiceManager.saveMenuItem(dinnerMenuItem).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
                dinnerMenuItemObservable
                        .subscribe(new Observer<DinnerMenuItem>() {
                            private DinnerMenuItem item;

                            @Override
                            public void onCompleted() {
                                dinnerMenuItem.setId(item.getId());
                                dinnerListingManager.addToListing(dinnerMenuItem).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<String>() {
                                    @Override
                                    public void onCompleted() {
                                        getActivity().finish();
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Toast.makeText(getActivity().getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onNext(String s) {
                                        Toast.makeText(getActivity().getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                                    }
                                });
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
        });
        return mView;
    }
}
