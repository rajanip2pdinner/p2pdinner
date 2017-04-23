package com.p2pdinner.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderResult;
import com.google.code.geocoder.model.GeocoderStatus;
import com.p2pdinner.R;
import com.p2pdinner.common.Constants;
import com.p2pdinner.common.P2PDinnerUtils;
import com.p2pdinner.entities.DinnerSearchResults;
import com.p2pdinner.fragments.DateDialogDataTransferInterface;
import com.p2pdinner.restclient.GoogleApiService;
import com.p2pdinner.restclient.PlacesServiceManager;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.observers.Subscribers;
import rx.schedulers.Schedulers;

public class FindDinnerActivity extends BaseAppCompatActivity implements DateDialogDataTransferInterface {

    private static final String TAG = FindDinnerActivity.class.getName();

    private Handler handler;
    private EditText mAddress;
    private TextView mGuestNumber;
    private Button mIncrementGuests;
    private Button mDecrementGuests;
    private TextView mDateView;
    private ImageView mModifyDate;
    private Button mFindDinner;
    private ProgressBar mSearchProgress;

    @Inject
    LocationManager locationManager;
    @Inject
    GoogleApiService googleApiService;
    @Inject
    PlacesServiceManager placesServiceManager;

    @Inject
    Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_dinner);
        intializeControls();
        setupEventListeners();
        initializeLocationManager();
    }


    @Override
    protected void onResume() {
        super.onResume();
        mTracker.setScreenName("BuyerHome");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    private void intializeControls() {
        mAddress = (EditText) findViewById(R.id.current_address);
        mGuestNumber = (TextView) findViewById(R.id.guest_numbers);
        mIncrementGuests = (Button) findViewById(R.id.increment_guests);
        mDecrementGuests = (Button) findViewById(R.id.decrement_guests);
        mDateView = (TextView) findViewById(R.id.lbl_date);
        mModifyDate = (ImageView) findViewById(R.id.changeDate);
        mFindDinner = (Button) findViewById(R.id.btnFindDinner);
        mSearchProgress = (ProgressBar) findViewById(R.id.searchProgress);
    }

    private void setupEventListeners() {
        mIncrementGuests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer guestNumber = Integer.valueOf(mGuestNumber.getText().toString());
                guestNumber++;
                mGuestNumber.setText(guestNumber.toString());
            }
        });

        mDecrementGuests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer guestNumber = Integer.valueOf(mGuestNumber.getText().toString());
                if (guestNumber - 1 > 0) {
                    guestNumber--;
                    mGuestNumber.setText(guestNumber.toString());
                }
            }
        });

        mModifyDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int date = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(FindDinnerActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(year, monthOfYear, dayOfMonth);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
                        DateTime dateTime = new DateTime(selectedDate);
                        mDateView.setText(P2PDinnerUtils.convert(dateTime).toDateString());
                    }
                }, year, month, date);
                datePickerDialog.show();
            }
        });

        mFindDinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTracker.send(new HitBuilders.EventBuilder().setCategory("Action").setAction("BuyerHome.FindDinner").build());
                int guests = Integer.parseInt(mGuestNumber.getText().toString());
                DateTime startTime = DateTime.now();
                if (!mDateView.getText().toString().equalsIgnoreCase("Today")) {
                    //startTime = DateTime.parse(mDateView.getText().toString(), DateTimeFormat.forPattern("MM/dd/yyyy"));
                    startTime = P2PDinnerUtils.convert(mDateView.getText().toString()).toDate();
                }
                mSearchProgress.setVisibility(ProgressBar.VISIBLE);
                placesServiceManager.searchDinnerByLocation(mAddress.getText().toString(), startTime, guests, getResources().getConfiguration().locale)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Observer<DinnerSearchResults>() {
                            private DinnerSearchResults dinnerSearchResults;

                            @Override
                            public void onCompleted() {
                                mSearchProgress.setVisibility(ProgressBar.INVISIBLE);
                                Intent intent = new Intent(getApplicationContext(), DinnerListingActivity.class);
                                intent.putExtra(Constants.CURRENT_DINNER_LISTINGS, dinnerSearchResults);
                                intent.putExtra(Constants.NO_OF_GUESTS, Integer.parseInt(mGuestNumber.getText().toString()));
                                startActivity(intent);
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, e.getMessage(), e);
                            }

                            @Override
                            public void onNext(DinnerSearchResults dinnerSearchResults) {
                                this.dinnerSearchResults = dinnerSearchResults;
                            }
                        });

            }

        });
    }

    private void initializeLocationManager() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        String provider = locationManager.getBestProvider(criteria, true);
        if (StringUtils.hasText(provider)) {
            Location lastKnownLocation = locationManager.getLastKnownLocation(provider);
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
                                Toast.makeText(getApplicationContext(), "Error getting location. Enable location services", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNext(GeocodeResponse geocodeResponse) {
                                this.geocodeResponse = geocodeResponse;
                            }
                        });
            }
            locationManager.requestLocationUpdates(provider, 1000 * 5, 50, new LocationListener() {
                @Override
                public void onLocationChanged(final Location location) {
                    Log.i(TAG, "Received location change event... finding location with geocodes");
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
                                    Toast.makeText(getApplicationContext(), "Error getting location. Enable location services", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onNext(GeocodeResponse geocodeResponse) {
                                    this.geocodeResponse = geocodeResponse;
                                }
                            });

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
        }
    }

    @Override
    public void setSelectedDate(int resourceId, DateTime dateTime) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_find_dinner, menu);
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
            //DateDialogDataTransferInterface dateDialogDataTransferInterface = (DateDialogDataTransferInterface) this.getTargetFragment();
            //Bundle bundle = getArguments();
            //int resourceId = bundle.getInt("resourceId");
            //dateDialogDataTransferInterface.setSelectedDate(resourceId,selectedDate);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
            // mDateView.setText(simpleDateFormat.format(selectedDate.getTime()));
        }
    }
}
