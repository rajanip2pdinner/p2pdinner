package com.p2pdinner.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.p2pdinner.R;
import com.p2pdinner.activities.ListDinnerActivity;
import com.p2pdinner.common.Constants;
import com.p2pdinner.common.DecimalDigitsInputFilter;
import com.p2pdinner.entities.DinnerMenuItem;
import com.p2pdinner.restclient.MenuServiceManager;

import org.springframework.util.StringUtils;

import java.text.DecimalFormat;
import java.util.Currency;
import java.util.Locale;

import javax.inject.Inject;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by rajaniy on 9/24/15.
 */
public class CostFragment extends BaseFragment {

    private static final String TAG = CostFragment.class.getName();


    private Button mBtnNext = null;
    private EditText mCostPerPlate;
    private EditText mAvailableQuantity;
    private TextView mCostPerPlateLabel;
    private CheckBox mFreeFood;

    private DinnerMenuItem dinnerMenuItem;

    @Inject
    MenuServiceManager menuServiceManager;

    @Inject
    Tracker mTracker;

    @Override
    public void onResume() {
        super.onResume();
        mTracker.setScreenName("SellerHome.Cost");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser && getActivity() != null) {
            dinnerMenuItem = (DinnerMenuItem) getActivity().getIntent().getSerializableExtra(Constants.CURRENT_DINNER_ITEM);
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    private void initializeControls(View view) {
        mCostPerPlate = (EditText) view.findViewById(R.id.cost_per_item);
        mAvailableQuantity = (EditText) view.findViewById(R.id.available_quantity);
        mCostPerPlateLabel = (TextView) view.findViewById(R.id.lbl_cost_per_item);
        mFreeFood = (CheckBox) view.findViewById(R.id.chkFreeFood);
        String costLabel = mCostPerPlateLabel.getText().toString();
        Locale current = getResources().getConfiguration().locale;
        costLabel = costLabel + " ( " + Currency.getInstance(current).getSymbol() + " )";
        mCostPerPlateLabel.setText(costLabel);
        dinnerMenuItem = (DinnerMenuItem) getActivity().getIntent().getSerializableExtra(Constants.CURRENT_DINNER_ITEM);
        int availableQuantity = 1;
        if (dinnerMenuItem.getAvailableQuantity() != null && dinnerMenuItem.getAvailableQuantity() > 0) {
            availableQuantity = dinnerMenuItem.getAvailableQuantity();
        }
        mAvailableQuantity.setText(String.valueOf(availableQuantity));
        updateCostPerPlate(dinnerMenuItem.getCost());
        mCostPerPlate.setFilters(new InputFilter[] {new DecimalDigitsInputFilter()});
        if (dinnerMenuItem.getCost() != null && dinnerMenuItem.getCost() == 0) {
            mFreeFood.setChecked(true);
            mCostPerPlate.setEnabled(false);
        } else {
            mFreeFood.setChecked(false);
            mCostPerPlate.setEnabled(true);
        }
        mFreeFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFreeFood.isChecked()) {
                    mCostPerPlate.setEnabled(false);
                    updateCostPerPlate(0d);
                } else {
                    mCostPerPlate.setEnabled(true);
                    updateCostPerPlate(dinnerMenuItem.getCost());
                }
            }
        });
    }

    @NonNull
    private void updateCostPerPlate(Double cost) {
        final DecimalFormat decimalFormat = new DecimalFormat("00.00");
        if (cost != null) {
            String costPerItem = decimalFormat.format(cost);
            if (StringUtils.hasText(costPerItem)) {
                mCostPerPlate.setText(costPerItem);
            }
        } else {
            mCostPerPlate.setText(decimalFormat.format(0d));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cost_layout, container, false);
        initializeControls(view);
        mBtnNext = (Button) view.findViewById(R.id.btnNext);
        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTracker.send(new HitBuilders.EventBuilder().setCategory("Action").setAction("Cost - Next").build());
                Double price = StringUtils.hasText(mCostPerPlate.getText()) ? Double.parseDouble(mCostPerPlate.getText().toString()) : 0;
                int availableQuantity = Integer.parseInt(mAvailableQuantity.getText().toString());
                dinnerMenuItem.setCost(price);
                dinnerMenuItem.setAvailableQuantity(availableQuantity);
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
        });

        return view;
    }

}
