package com.p2pdinner.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.p2pdinner.R;

import java.text.DecimalFormat;

/**
 * Created by rajaniy on 6/18/16.
 */

public class CustomNumberPickerDialog extends DialogFragment {

    private SelectionType selectionType;

    private static final String TAG = "p2pdinner";
    private NumberPicker mNumberPicker;
    private String selectedValue;


    private CustomNumberPickerListener mCustomNumberPickerListener;

    public static CustomNumberPickerDialog newInstance(String selectionType) {
        CustomNumberPickerDialog customNumberPickerDialog = new CustomNumberPickerDialog();
        Bundle bundle = new Bundle();
        bundle.putString("selection_type", selectionType);
        customNumberPickerDialog.setArguments(bundle);
        return customNumberPickerDialog;
    }

    public interface CustomNumberPickerListener {
        public void onDialogDone(SelectionType selectionType, String selectedValue);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (this.getTargetFragment() instanceof CustomNumberPickerListener) {
            mCustomNumberPickerListener = (CustomNumberPickerListener) this.getTargetFragment();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_dialog, null);
        initialize(view);
        builder.setView(view)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i(TAG, "Negetive -- Selected value" + which);
                    }
                })
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i(TAG, "Positive -- Selected value" + selectedValue);
                        if (CustomNumberPickerDialog.this.getTargetFragment() != null) {
                            mCustomNumberPickerListener.onDialogDone(selectionType, selectedValue);
                        }
                    }
                });
        return builder.create();
    }

    public void initialize(View view) {
        TextView textView = (TextView) view.findViewById(R.id.title);
        selectionType = SelectionType.valueOf(getArguments().getString("selection_type"));
        textView.setText(selectionType.getTitle());
        mNumberPicker = (NumberPicker) view.findViewById(R.id.nm_picker);
        if (this.selectionType == SelectionType.COST_PER_PLATE) {
            Double price = 0.0;
            Double maxPrice = 25.0;
            DecimalFormat format = new DecimalFormat("#0.00");
            String[] priceValues = new String[maxPrice.intValue() * 4] ;
            int idx = 0;
            while (price < maxPrice && idx <= 100) {
                price += 0.25;
                priceValues[idx++] = format.format(price);
            }
            mNumberPicker.setWrapSelectorWheel(true);
            mNumberPicker.setMaxValue(priceValues.length - 1);
            mNumberPicker.setDisplayedValues(priceValues);
        } else {
            String[] noOfItemsValue = new String[100];
            for(int idx = 0; idx < noOfItemsValue.length; idx++) {
                noOfItemsValue[idx] = String.valueOf(idx);
            }
            mNumberPicker.setWrapSelectorWheel(true);
            mNumberPicker.setMaxValue(noOfItemsValue.length - 1);
            mNumberPicker.setDisplayedValues(noOfItemsValue);
        }

        mNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                selectedValue = picker.getDisplayedValues()[newVal];
            }
        });

    }
}
