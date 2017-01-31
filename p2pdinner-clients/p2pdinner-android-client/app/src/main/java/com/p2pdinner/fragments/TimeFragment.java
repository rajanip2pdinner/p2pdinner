package com.p2pdinner.fragments;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;


import com.p2pdinner.R;
import com.p2pdinner.activities.ListDinnerActivity;
import com.p2pdinner.common.Constants;
import com.p2pdinner.common.P2PDinnerUtils;
import com.p2pdinner.entities.DinnerMenuItem;
import com.p2pdinner.restclient.MenuServiceManager;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.inject.Inject;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by rajaniy on 9/24/15.
 */
public class TimeFragment extends BaseFragment implements DateDialogDataTransferInterface, TimeDialogDataTransferInterface {

    private static final String TAG = TimeFragment.class.getName();

    private EditText mAvailabilityDateTxt;
    private EditText mFormTime;
    private EditText mToTime;
    private EditText mAcceptOrdersTillTime;
    private static final int DATE_DIALOG_ID = 0;
    private DinnerMenuItem dinnerMenuItem = null;
    private Button mBtnNext = null;

    @Inject
    MenuServiceManager menuServiceManager;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser && getActivity() != null) {
            dinnerMenuItem = (DinnerMenuItem) getActivity().getIntent().getSerializableExtra(Constants.CURRENT_DINNER_ITEM);
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void setSelectedDate(int resourceId, DateTime dateTime) {
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        EditText editText = (EditText) getActivity().findViewById(resourceId);
        editText.setText(P2PDinnerUtils.convert(dateTime).toDateString());
        dinnerMenuItem.setAvailableDate(DateTimeFormat.forPattern("MM/dd/yyyy").print(P2PDinnerUtils.convert(editText.getText().toString()).toDate()));
    }

    @Override
    public void setSelectedTime(int resourceId, String time) {
        EditText editText = (EditText) getActivity().findViewById(resourceId);
        editText.setText(time);
        switch (resourceId) {
            case R.id.from_time:
                dinnerMenuItem.setFromTime(editText.getText().toString());
                break;
            case R.id.to_time:
                dinnerMenuItem.setToTime(editText.getText().toString());
                break;
            case R.id.accept_orders_till:
                dinnerMenuItem.setCloseTime(editText.getText().toString());
                break;
            default:
                Log.w(TAG, "Invalid resource id");
        }
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
        initializeView(dinnerMenuItem);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        dinnerMenuItem = (DinnerMenuItem) getActivity().getIntent().getSerializableExtra(Constants.CURRENT_DINNER_ITEM);
        View view = inflater.inflate(R.layout.time_layout, container, false);
        mAvailabilityDateTxt = (EditText) view.findViewById(R.id.available_date);
        final DialogFragment dialogFragment = new DatePickerFragment();
        dialogFragment.setTargetFragment(this, 0);
        mAvailabilityDateTxt.setOnClickListener(new EditText.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DatePickerFragment.instantiate(getActivity(), "Select Availability Date");
                Bundle bundle = new Bundle();
                bundle.putInt("resourceId", R.id.available_date);
                dialogFragment.setArguments(bundle);
                dialogFragment.show(getActivity().getSupportFragmentManager(), "Select Availability Date");

            }
        });
        final DialogFragment timeDialogFragment = new TimePickerFragment();
        timeDialogFragment.setTargetFragment(this, 0);
        mFormTime = (EditText) view.findViewById(R.id.from_time);
        mToTime = (EditText) view.findViewById(R.id.to_time);
        mAcceptOrdersTillTime = (EditText) view.findViewById(R.id.accept_orders_till);

        EditText.OnClickListener listener = new EditText.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("resourceId", v.getId());
                timeDialogFragment.setArguments(bundle);
                timeDialogFragment.show(getActivity().getSupportFragmentManager(), "Select Time");
            }
        };

        mFormTime.setOnClickListener(listener);
        mToTime.setOnClickListener(listener);
        mAcceptOrdersTillTime.setOnClickListener(listener);
        //initializeView(dinnerMenuItem);


        mBtnNext = (Button) view.findViewById(R.id.btnNext);
        mBtnNext.setOnClickListener(  new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dinnerMenuItem.setAvailableDate(DateTimeFormat.forPattern("MM/dd/yyyy").print(P2PDinnerUtils.convert(mAvailabilityDateTxt.getText().toString()).toDate()));
                dinnerMenuItem.setFromTime(mFormTime.getText().toString());
                dinnerMenuItem.setToTime(mToTime.getText().toString());
                dinnerMenuItem.setCloseTime(mAcceptOrdersTillTime.getText().toString());
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

    private void initializeView(DinnerMenuItem dinnerMenuItem) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss");
        DateTimeFormatter timeFormatter = DateTimeFormat.forPattern("HH:mm");
        //set availability date to current date
        DateTime currentTime = DateTime.now();
        mAvailabilityDateTxt.setText(P2PDinnerUtils.convert(currentTime).toDateString());
        dinnerMenuItem.setAvailableDate(DateTimeFormat.forPattern("MM/dd/yyyy").print(P2PDinnerUtils.convert(mAvailabilityDateTxt.getText().toString()).toDate()));
        Log.d(TAG, dinnerMenuItem.toString());
        DateTime toDateTime = null;
        DateTime fromDateTime = null;
        DateTime closeDateTime = null;
        if (StringUtils.hasText(dinnerMenuItem.getToTime())) {
            toDateTime = formatter.withZoneUTC().parseDateTime(dinnerMenuItem.getToTime());
        } else {
            toDateTime = DateTime.now().plusHours(2);
        }
        if (StringUtils.hasText(dinnerMenuItem.getFromTime())) {
            fromDateTime = formatter.withZoneUTC().parseDateTime(dinnerMenuItem.getFromTime());
        } else {
            fromDateTime = DateTime.now().plusHours(1);
        }
        if (StringUtils.hasText(dinnerMenuItem.getCloseTime())) {
            closeDateTime = formatter.withZoneUTC().parseDateTime(dinnerMenuItem.getCloseTime());
        } else {
            closeDateTime = DateTime.now().plusHours(2).minusMinutes(1);
        }
        if (toDateTime != null) {
            try {
                mToTime.setText(timeFormatter.withZone(DateTimeZone.getDefault()).print(toDateTime));
                dinnerMenuItem.setToTime(mToTime.getText().toString());
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
        Log.d(TAG, dinnerMenuItem.toString());
        if (fromDateTime != null) {
            try {
                mFormTime.setText(timeFormatter.withZone(DateTimeZone.getDefault()).print(fromDateTime));
                dinnerMenuItem.setFromTime(mFormTime.getText().toString());
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
        Log.d(TAG, dinnerMenuItem.toString());
        if (closeDateTime != null) {
            try {
                mAcceptOrdersTillTime.setText(timeFormatter.withZone(DateTimeZone.getDefault()).print(closeDateTime));
                dinnerMenuItem.setCloseTime(mAcceptOrdersTillTime.getText().toString());
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
        Log.d(TAG, dinnerMenuItem.toString());
        mFormTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!StringUtils.hasText(s.toString())) {
                    return;
                }
                DateTimeFormatter timeFormatter = DateTimeFormat.forPattern("HH:mm");
                DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("MMM dd, yyyy HH:mm");
                DateTime fromDateTime = dateTimeFormatter.parseDateTime(mAvailabilityDateTxt.getText().toString() + " " + s.toString());
                DateTime toDateTime;
                if (StringUtils.hasText(mToTime.getText().toString())) {
                    toDateTime = dateTimeFormatter.parseDateTime(mAvailabilityDateTxt.getText().toString() + " " + mToTime.getText().toString());
                } else {
                    toDateTime = dateTimeFormatter.parseDateTime(mAvailabilityDateTxt.getText().toString() + " 00:00");
                }
                DateTime closeTime;
                if (StringUtils.hasText(mAcceptOrdersTillTime.getText().toString())) {
                    closeTime = dateTimeFormatter.parseDateTime(mAvailabilityDateTxt.getText().toString() + " " + mAcceptOrdersTillTime.getText().toString());
                } else {
                    closeTime = dateTimeFormatter.parseDateTime(mAvailabilityDateTxt.getText().toString() + " 00:00");
                }

                if (fromDateTime.isAfter(closeTime)) {
                    closeTime = fromDateTime;
                    closeTime.plusMinutes(1);
                    mAcceptOrdersTillTime.setText(timeFormatter.print(closeTime));
                }
                if (fromDateTime.isAfter(toDateTime)) {
                    toDateTime = closeTime.plusMinutes(1);
                    mToTime.setText(timeFormatter.print(toDateTime));
                }
            }
        });

        mToTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!StringUtils.hasText(s.toString())) {
                    return;
                }
                DateTimeFormatter timeFormatter = DateTimeFormat.forPattern("HH:mm");
                DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("MMM dd, yyyy HH:mm");
                DateTime fromDateTime;
                if (StringUtils.hasText(mFormTime.getText().toString())) {
                    fromDateTime = dateTimeFormatter.parseDateTime(mAvailabilityDateTxt.getText().toString() + " "+ mFormTime.getText().toString());
                } else {
                    fromDateTime = dateTimeFormatter.parseDateTime(mAvailabilityDateTxt.getText().toString() + " 00:00");
                }
                DateTime toDateTime;
                if (StringUtils.hasText(mToTime.getText().toString())) {
                    toDateTime = dateTimeFormatter.parseDateTime(mAvailabilityDateTxt.getText().toString() + " " + mToTime.getText().toString());
                } else {
                    toDateTime = dateTimeFormatter.parseDateTime(mAvailabilityDateTxt.getText().toString() + " 00:00");
                }
                DateTime closeTime;
                if (StringUtils.hasText(mAcceptOrdersTillTime.getText().toString())) {
                    closeTime = dateTimeFormatter.parseDateTime(mAvailabilityDateTxt.getText().toString() + " " + mAcceptOrdersTillTime.getText().toString());
                } else {
                    closeTime = dateTimeFormatter.parseDateTime(mAvailabilityDateTxt.getText().toString() + " 00:00");
                }
                if (toDateTime.isBefore(closeTime)) {
                    closeTime = toDateTime.minusMinutes(1);
                    mAcceptOrdersTillTime.setText(timeFormatter.print(closeTime));
                }
                if (closeTime.isBefore(fromDateTime)) {
                    fromDateTime = closeTime.minusMillis(1);
                    mFormTime.setText(timeFormatter.print(fromDateTime));
                }
            }
        });

        mAcceptOrdersTillTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!StringUtils.hasText(s.toString())) {
                    return;
                }
                DateTimeFormatter timeFormatter = DateTimeFormat.forPattern("HH:mm");
                DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("MMM dd, yyyy HH:mm");
                DateTime fromDateTime = dateTimeFormatter.parseDateTime(mAvailabilityDateTxt.getText().toString() + " "+ mFormTime.getText().toString());
                DateTime toDateTime;
                if (StringUtils.hasText(mToTime.getText().toString())) {
                    toDateTime = dateTimeFormatter.parseDateTime(mAvailabilityDateTxt.getText().toString() + " " + mToTime.getText().toString());
                } else {
                    toDateTime = dateTimeFormatter.parseDateTime(mAvailabilityDateTxt.getText().toString() + " 00:00");
                }
                DateTime closeTime;
                if (StringUtils.hasText(mAcceptOrdersTillTime.getText().toString())) {
                    closeTime = dateTimeFormatter.parseDateTime(mAvailabilityDateTxt.getText().toString() + " " + mAcceptOrdersTillTime.getText().toString());
                } else {
                    closeTime = dateTimeFormatter.parseDateTime(mAvailabilityDateTxt.getText().toString() + " 00:00");
                }
                if (toDateTime.isBefore(closeTime)) {
                    toDateTime = closeTime.plusMinutes(1);
                    mToTime.setText(timeFormatter.print(toDateTime));
                }
                if (closeTime.isBefore(fromDateTime)) {
                    fromDateTime = closeTime.minusMillis(1);
                    mFormTime.setText(timeFormatter.print(fromDateTime));
                }
            }
        });
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        private DateTime selectedDate;


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int date = calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, year, month, date);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            selectedDate = DateTime.now();
            selectedDate = selectedDate.withDate(year, monthOfYear + 1, dayOfMonth);
            DateDialogDataTransferInterface dateDialogDataTransferInterface = (DateDialogDataTransferInterface) this.getTargetFragment();
            Bundle bundle = getArguments();
            int resourceId = bundle.getInt("resourceId");
            dateDialogDataTransferInterface.setSelectedDate(resourceId, selectedDate);
        }
    }

    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            return new TimePickerDialog(getActivity(), this, hour, minute, android.text.format.DateFormat.is24HourFormat(getActivity()));
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String selectedTime = String.format("%02d:%02d", hourOfDay, minute);
            TimeDialogDataTransferInterface timeDialogDataTransferInterface = (TimeDialogDataTransferInterface) this.getTargetFragment();
            Bundle bundle = getArguments();
            int resourceId = bundle.getInt("resourceId");
            timeDialogDataTransferInterface.setSelectedTime(resourceId, selectedTime);
        }
    }
}
