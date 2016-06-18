package com.p2pdinner.fragments;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
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
    private Handler handler;

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
    public void setSelectedDate(int resourceId, Calendar calendar) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        EditText editText = (EditText) getActivity().findViewById(resourceId);
        editText.setText(simpleDateFormat.format(calendar.getTime()));
        dinnerMenuItem.setAvailableDate(editText.getText().toString());
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case Constants.Message.SAVE_MENU_ITEM_SUCCESS:
                        //Toast.makeText(getActivity().getBaseContext(), (String) msg.obj, Toast.LENGTH_SHORT).show();
                        ListDinnerActivity listDinnerActivity = (ListDinnerActivity) getActivity();
                        listDinnerActivity.moveToNextTab();
                        break;
                    case Constants.Message.UNKNOWN_ERROR:
                        break;
                }
            }
        };
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
        mBtnNext = (Button) view.findViewById(R.id.btnNext);
        mBtnNext.setOnClickListener(  new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dinnerMenuItem.setAvailableDate(DateTimeFormat.forPattern("MM/dd/yyyy").withZoneUTC().print(P2PDinnerUtils.convert(mAvailabilityDateTxt.getText().toString()).toDate()));
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
        initializeView(dinnerMenuItem);
        return view;
    }

    private void initializeView(DinnerMenuItem dinnerMenuItem) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss");
        DateTimeFormatter timeFormatter = DateTimeFormat.forPattern("HH:mm");
        //set availability date to current date
        DateTime currentTime = DateTime.now();
        mAvailabilityDateTxt.setText(P2PDinnerUtils.convert(currentTime).toDateString());
        dinnerMenuItem.setAvailableDate(mAvailabilityDateTxt.getText().toString());
        if (StringUtils.hasText(dinnerMenuItem.getToTime())) {
            try {
                DateTime toDateTime = formatter.withZoneUTC().parseDateTime(dinnerMenuItem.getToTime());
                mToTime.setText(timeFormatter.print(toDateTime.toLocalDateTime()));
                dinnerMenuItem.setToTime(mToTime.getText().toString());
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
        if (StringUtils.hasText(dinnerMenuItem.getFromTime())) {
            try {
                DateTime fromDateTime = formatter.withZoneUTC().parseDateTime(dinnerMenuItem.getFromTime());
                mFormTime.setText(timeFormatter.print(fromDateTime.toLocalDateTime()));
                dinnerMenuItem.setToTime(mFormTime.getText().toString());
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
        if (StringUtils.hasText(dinnerMenuItem.getCloseTime())) {
            try {
                DateTime closeDateTime = formatter.withZoneUTC().parseDateTime(dinnerMenuItem.getCloseTime());
                mAcceptOrdersTillTime.setText(timeFormatter.print(closeDateTime.toLocalDateTime()));
                dinnerMenuItem.setToTime(mAcceptOrdersTillTime.getText().toString());
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        private Calendar selectedDate;


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
            selectedDate = Calendar.getInstance();
            selectedDate.set(year, monthOfYear, dayOfMonth);
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
