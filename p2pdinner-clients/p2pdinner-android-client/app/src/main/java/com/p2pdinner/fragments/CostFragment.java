package com.p2pdinner.fragments;

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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.p2pdinner.R;
import com.p2pdinner.activities.ListDinnerActivity;
import com.p2pdinner.common.Constants;
import com.p2pdinner.entities.DinnerMenuItem;
import com.p2pdinner.restclient.MenuServiceManager;

import org.springframework.util.StringUtils;

import java.text.DecimalFormat;
import java.util.Currency;

import javax.inject.Inject;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by rajaniy on 9/24/15.
 */
public class CostFragment extends BaseFragment {

    private static final String TAG = CostFragment.class.getName();

    private TextView mTxtViewCostPerPlateInDollars = null;
    private Button mBtnIncrementDollarAmount = null;
    private Button mBtnDecrementDollarAmount = null;

    private TextView mTxtViewCostPerPlateInCents = null;
    private Button mBtnIncrementCentsAmount = null;
    private Button mBtnDecrementCentsAmount = null;


    private TextView mTxtViewMaxAvailableQuantity = null;
    private Button mBtnIncrementMaxAvailableQuantity = null;
    private Button mBtnDecrementMaxAvailableQuantity = null;

    private Button mBtnNext = null;

    private DinnerMenuItem dinnerMenuItem;

    @Inject MenuServiceManager menuServiceManager;

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
        View view = inflater.inflate(R.layout.cost_layout, container, false);

        dinnerMenuItem = (DinnerMenuItem) getActivity().getIntent().getSerializableExtra(Constants.CURRENT_DINNER_ITEM);
        int availableQuantity = 1;
        if (dinnerMenuItem.getAvailableQuantity() != null && dinnerMenuItem.getAvailableQuantity() > 0) {
            availableQuantity = dinnerMenuItem.getAvailableQuantity();
        }
        String dollarAmt = "0";
        String centsAmt = "0";

        if (dinnerMenuItem.getCost() != null) {
            DecimalFormat decimalFormat = new DecimalFormat("00.00");
            String costPerItem = decimalFormat.format(dinnerMenuItem.getCost());
            String[] tokens = costPerItem.split("\\.");
            dollarAmt = tokens[0];
            centsAmt = tokens[1];
        }

        mTxtViewCostPerPlateInDollars = (TextView) view.findViewById(R.id.dollar_amt);
        mTxtViewCostPerPlateInDollars.setText(String.valueOf(dollarAmt));
        mBtnIncrementDollarAmount = (Button) view.findViewById(R.id.increment_amount);
        mBtnIncrementDollarAmount.setOnClickListener(new IncrementDecrementOnClickListener());
        mBtnDecrementDollarAmount = (Button) view.findViewById(R.id.decrement_amount);
        mBtnDecrementDollarAmount.setOnClickListener(new IncrementDecrementOnClickListener());


        mTxtViewCostPerPlateInCents = (TextView) view.findViewById(R.id.cents_amt);
        mTxtViewCostPerPlateInCents.setText(String.valueOf(centsAmt));
        mBtnIncrementCentsAmount = (Button) view.findViewById(R.id.increment_cent_amount);
        mBtnIncrementCentsAmount.setOnClickListener(new IncrementDecrementOnClickListener());
        mBtnDecrementCentsAmount = (Button) view.findViewById(R.id.decrement_cent_amount);
        mBtnDecrementCentsAmount.setOnClickListener(new IncrementDecrementOnClickListener());

        mTxtViewMaxAvailableQuantity = (TextView) view.findViewById(R.id.available_quantity);
        mTxtViewMaxAvailableQuantity.setText(String.valueOf(availableQuantity));
        mBtnIncrementMaxAvailableQuantity = (Button) view.findViewById(R.id.increment_available_quantity);
        mBtnIncrementMaxAvailableQuantity.setOnClickListener(new IncrementDecrementOnClickListener());
        mBtnDecrementMaxAvailableQuantity = (Button) view.findViewById(R.id.decrement_available_quantity);
        mBtnDecrementMaxAvailableQuantity.setOnClickListener(new IncrementDecrementOnClickListener());

        mBtnNext = (Button) view.findViewById(R.id.btnNext);
        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int dollar = Integer.parseInt(mTxtViewCostPerPlateInDollars.getText().toString());
                int cents = Integer.parseInt(mTxtViewCostPerPlateInCents.getText().toString());
                int availableQuantity = Integer.parseInt(mTxtViewMaxAvailableQuantity.getText().toString());
                dinnerMenuItem.setCost(Double.valueOf(dollar + "." + cents));
                dinnerMenuItem.setAvailableQuantity(availableQuantity);
                menuServiceManager.saveMenuItem(dinnerMenuItem)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<DinnerMenuItem>() {
                            private DinnerMenuItem item;
                            @Override
                            public void onCompleted() {
                                dinnerMenuItem.setId(item.getId());
                                Toast.makeText(getActivity().getBaseContext(), "Item saved successfully", Toast.LENGTH_SHORT).show();
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


    private class IncrementDecrementOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.increment_amount: {
                    int currentValue = Integer.parseInt(mTxtViewCostPerPlateInDollars.getText().toString());
                    currentValue++;
                    mTxtViewCostPerPlateInDollars.setText(String.valueOf(currentValue));
                }
                break;
                case R.id.decrement_amount: {
                    int currentValue = Integer.parseInt(mTxtViewCostPerPlateInDollars.getText().toString());
                    if (currentValue - 1 >= 0) {
                        currentValue--;
                    }
                    mTxtViewCostPerPlateInDollars.setText(String.valueOf(currentValue));
                }
                break;
                case R.id.increment_cent_amount: {
                    int currentValue = Integer.parseInt(mTxtViewCostPerPlateInCents.getText().toString());
                    if (currentValue + 25 < 100) {
                        currentValue += 25;
                    } else {
                        currentValue = 0;
                    }

                    mTxtViewCostPerPlateInCents.setText(String.valueOf(currentValue));
                }
                break;
                case R.id.decrement_cent_amount: {
                    int currentValue = Integer.parseInt(mTxtViewCostPerPlateInCents.getText().toString());
                    if (currentValue - 25 >= 0) {
                        currentValue -= 25;
                    }
                    mTxtViewCostPerPlateInCents.setText(String.valueOf(currentValue));
                }
                break;
                case R.id.increment_available_quantity:{
                    int currentValue = Integer.parseInt(mTxtViewMaxAvailableQuantity.getText().toString());
                    currentValue++;
                    mTxtViewMaxAvailableQuantity.setText(String.valueOf(currentValue));
                }
                break;
                case R.id.decrement_available_quantity:{
                    int currentValue = Integer.parseInt(mTxtViewMaxAvailableQuantity.getText().toString());
                    if (currentValue - 1 >= 0) {
                        currentValue--;
                    }
                    mTxtViewMaxAvailableQuantity.setText(String.valueOf(currentValue));
                }
                break;
                default:
                    Log.d(TAG, "Unknown resource id. Nothing needs to be done.");
            }
        }
    }


}
