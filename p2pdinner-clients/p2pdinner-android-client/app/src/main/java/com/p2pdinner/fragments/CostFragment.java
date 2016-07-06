package com.p2pdinner.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.p2pdinner.R;
import com.p2pdinner.activities.ListDinnerActivity;
import com.p2pdinner.common.Constants;
import com.p2pdinner.entities.DinnerMenuItem;
import com.p2pdinner.restclient.MenuServiceManager;

import org.springframework.util.StringUtils;

import java.text.DecimalFormat;

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

    private DinnerMenuItem dinnerMenuItem;

    @Inject
    MenuServiceManager menuServiceManager;

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
        dinnerMenuItem = (DinnerMenuItem) getActivity().getIntent().getSerializableExtra(Constants.CURRENT_DINNER_ITEM);
        int availableQuantity = 1;
        if (dinnerMenuItem.getAvailableQuantity() != null && dinnerMenuItem.getAvailableQuantity() > 0) {
            availableQuantity = dinnerMenuItem.getAvailableQuantity();
        }
        mAvailableQuantity.setText(String.valueOf(availableQuantity));
        DecimalFormat decimalFormat = new DecimalFormat("00.00");
        if (dinnerMenuItem.getCost() != null) {
            String costPerItem = decimalFormat.format(dinnerMenuItem.getCost());
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
                Double price = Double.parseDouble(mCostPerPlate.getText().toString());
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

}
