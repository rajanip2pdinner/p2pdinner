package com.p2pdinner.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderResult;
import com.google.code.geocoder.model.GeocoderStatus;
import com.p2pdinner.R;
import com.p2pdinner.activities.ListDinnerActivity;
import com.p2pdinner.common.Constants;
import com.p2pdinner.entities.DinnerMenuItem;
import com.p2pdinner.restclient.DinnerListingManager;
import com.p2pdinner.restclient.GoogleApiService;
import com.p2pdinner.restclient.MenuServiceManager;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 * Created by rajaniy on 9/24/15.
 */
public class PlaceFragment extends BaseFragment {

    private static final String TAG = PlaceFragment.class.getName();
    private Handler handler;
    private LinearLayout mCurrentView;
    private EditText mDeliverOptions;
    private EditText mAddress;
    private DinnerMenuItem dinnerMenuItem;
    private Button mBtnNext;
    List<String> deliveryOptionsList;

    @Inject
    LocationManager locationManager;
    @Inject
    GoogleApiService googleApiService;
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
        dinnerMenuItem = (DinnerMenuItem) getActivity().getIntent().getSerializableExtra(Constants.CURRENT_DINNER_ITEM);
        View view = inflater.inflate(R.layout.place_layout, container, false);
        mDeliverOptions = (EditText) view.findViewById(R.id.deliveryOptions);
        mAddress = (EditText) view.findViewById(R.id.current_address);
        StringBuffer address = new StringBuffer();
        if (StringUtils.hasText(dinnerMenuItem.getAddressLine1())) {
            address.append(dinnerMenuItem.getAddressLine1()).append(" ");
        }
        if (StringUtils.hasText(dinnerMenuItem.getAddressLine2())) {
            address.append(dinnerMenuItem.getAddressLine2()).append(" ");
        }
        if (StringUtils.hasText(dinnerMenuItem.getCity())) {
            address.append(dinnerMenuItem.getCity()).append(" ");
        }
        if (StringUtils.hasText(dinnerMenuItem.getState())) {
            address.append(dinnerMenuItem.getState());
        }
        if (StringUtils.hasText(address)) {
            mAddress.setText(address.toString());
        }
        if (StringUtils.hasText(dinnerMenuItem.getDeliveryOptions())) {
            mDeliverOptions.setText(dinnerMenuItem.getDeliveryOptions());
        } else {
            mDeliverOptions.setText("To-Go");
        }
        mDeliverOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final List<String> selectedOptions = new ArrayList<String>();
                boolean[] isSelected = new boolean[deliveryOptionsList.size()];
                final CharSequence[] items = new CharSequence[deliveryOptionsList.size()];
                for (int idx = 0; idx < deliveryOptionsList.size(); idx++) {
                    items[idx] = deliveryOptionsList.get(idx);
                    if (StringUtils.hasText(mDeliverOptions.getText().toString()) && mDeliverOptions.getText().toString().contains(items[idx])) {
                        selectedOptions.add(deliveryOptionsList.get(idx));
                        isSelected[idx] = Boolean.TRUE;
                    } else {
                        isSelected[idx] = Boolean.FALSE;
                    }
                    items[idx] = deliveryOptionsList.get(idx);
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.select_delivery);
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
                        String deliveryopts = StringUtils.collectionToCommaDelimitedString(selectedOptions);
                        mDeliverOptions.setText(deliveryopts);
                    }
                });
                builder.create().show();
            }
        });
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        if (!StringUtils.hasText(dinnerMenuItem.getAddressLine1()) && !StringUtils.hasText(dinnerMenuItem.getAddressLine2())) {
            Location lastKnownLocation = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, true));
            if (lastKnownLocation == null) {
                lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
            if (lastKnownLocation != null) {
                final Location finalLocation = lastKnownLocation;
                googleApiService.getAddress(getString(R.string.google_client_id), getString(R.string.google_client_key), finalLocation)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<GeocodeResponse>() {
                            private GeocodeResponse geocodeResponse;

                            @Override
                            public void onCompleted() {
                                if (geocodeResponse.getStatus() == GeocoderStatus.OK && geocodeResponse.getResults() != null && !geocodeResponse.getResults().isEmpty()) {
                                    Log.d(TAG, geocodeResponse.getResults().get(0).getFormattedAddress());
                                    GeocoderResult geocoderResult = geocodeResponse.getResults().get(0);
                                    mAddress.setText(geocoderResult.getFormattedAddress().toString());
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(getContext(), "Error getting location. Enable location services", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNext(GeocodeResponse geocodeResponse) {
                                this.geocodeResponse = geocodeResponse;
                            }
                        });
            }

        }
        locationManager.requestLocationUpdates(locationManager.getBestProvider(criteria, true), 0, 0, new LocationListener() {
            @Override
            public void onLocationChanged(final Location location) {
                try {
                    if (StringUtils.hasText(dinnerMenuItem.getAddressLine1()) || StringUtils.hasText(dinnerMenuItem.getAddressLine2())) {
                        return;
                    }
                    googleApiService.getAddress(getString(R.string.google_client_id), getString(R.string.google_client_key), location)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe(new Subscriber<GeocodeResponse>() {
                                private GeocodeResponse geocodeResponse;

                                @Override
                                public void onCompleted() {
                                    if (geocodeResponse.getStatus() == GeocoderStatus.OK && geocodeResponse.getResults() != null && !geocodeResponse.getResults().isEmpty()) {
                                        Log.d(TAG, geocodeResponse.getResults().get(0).getFormattedAddress());
                                        GeocoderResult geocoderResult = geocodeResponse.getResults().get(0);
                                        mAddress.setText(geocoderResult.getFormattedAddress().toString());
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Toast.makeText(getContext(), "Error getting location. Enable location services", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onNext(GeocodeResponse geocodeResponse) {
                                    this.geocodeResponse = geocodeResponse;
                                }
                            });
                } catch (Exception e) {
                    Log.e(TAG, "Error getting address....");
                    Log.e(TAG, e.getMessage());
                }

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.d(TAG, "Called onStatusChanged");
            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.d(TAG, "Called onProviderEnabled");
            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.d(TAG, "Called onProviderDisabled");
            }
        });
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case Constants.Message.DELIVERY_OPTIONS:
                        String[] options = (String[]) msg.obj;
                        deliveryOptionsList = Arrays.asList(options);
                        break;
                    case Constants.Message.GEOCODE_REVERSE_LOOKUP:
                        GeocodeResponse geocodeResponse = (GeocodeResponse) msg.obj;
                        Log.d(TAG, geocodeResponse.getResults().get(0).getFormattedAddress());
                        if (geocodeResponse.getStatus() == GeocoderStatus.OK && geocodeResponse.getResults() != null && !geocodeResponse.getResults().isEmpty()) {
                            GeocoderResult geocoderResult = geocodeResponse.getResults().get(0);
                            mAddress.setText(geocoderResult.getFormattedAddress().toString());
                        }
                        break;
                    case Constants.Message.SAVE_MENU_ITEM_SUCCESS:
                        //Toast.makeText(getActivity().getBaseContext(), (String) msg.obj, Toast.LENGTH_SHORT).show();
                        ListDinnerActivity listDinnerActivity = (ListDinnerActivity) getActivity();
                        listDinnerActivity.moveToNextTab();
                        break;
                    default:
                        super.handleMessage(msg);
                }
            }
        };
        dinnerListingManager.deliveryOptions().subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<String[]>() {
            private String[] deliveryOpts;
            @Override
            public void onCompleted() {
                deliveryOptionsList = Arrays.asList(deliveryOpts);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String[] strings) {
               this.deliveryOpts = strings;
            }
        });
        mCurrentView = (LinearLayout) view.findViewById(R.id.place_linear_layout);
        mAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dinnerMenuItem.setAddressLine1(s.toString());
                dinnerMenuItem.setAddressLine2("");
                dinnerMenuItem.setCity("");
                dinnerMenuItem.setState("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mBtnNext = (Button) view.findViewById(R.id.btnNext);
        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.hasText(mDeliverOptions.getText())) {
                    dinnerMenuItem.setDeliveryOptions(mDeliverOptions.getText().toString());
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
        });
        return view;
    }


}
