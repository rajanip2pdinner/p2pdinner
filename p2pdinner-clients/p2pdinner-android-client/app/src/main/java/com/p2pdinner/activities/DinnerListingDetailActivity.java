package com.p2pdinner.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.p2pdinner.R;
import com.p2pdinner.common.Constants;
import com.p2pdinner.common.ErrorResponse;
import com.p2pdinner.entities.DinnerListingViewContent;
import com.p2pdinner.entities.UserProfile;
import com.p2pdinner.restclient.UserProfileManager;
import com.p2pdinner.restclient.DinnerCartManager;
import com.p2pdinner.restclient.ImageDownloadTask;
import com.p2pdinner.services.RegistrationIntentService;

import org.springframework.util.StringUtils;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Currency;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

import javax.inject.Inject;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class DinnerListingDetailActivity extends BaseAppCompatActivity {

    private ImageView mFoodImages = null;
    private TextView mProfileName = null;
    private TextView mAddress = null;
    private TextView mCostPerPlate = null;
    private TextView mTimings = null;
    private GridView mDeliveryOptions = null;
    private Button mIncrementPlates = null;
    private Button mDecrementPlates = null;
    private TextView mTotalAmount = null;
    private Button mBuy = null;
    private TextView mGuests = null;
    private DinnerListingViewContent dinnerListingViewContent;
    private int noOfGuests = 2;
    private int maxAvailableQuantity = 0;
    private Boolean isValidProfile = false;
    private SharedPreferences sharedPreferences;
    private TextView mTitle = null;
    private Handler handler;
    private TextView mSpecialNeeds = null;

    @Inject
    DinnerCartManager dinnerCartManager;

    @Inject
    UserProfileManager userProfileManager;

    @Inject
    ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dinnerListingViewContent = (DinnerListingViewContent) getIntent().getExtras().get(Constants.DINNER_LISTING_VIEW_CONTENT);
        noOfGuests = getIntent().getExtras().getInt(Constants.NO_OF_GUESTS);
        setContentView(R.layout.activity_dinner_listing_detail);
        mFoodImages = (ImageView) findViewById(R.id.food_images);
        mProfileName = (TextView) findViewById(R.id.profilename);
        mAddress = (TextView) findViewById(R.id.address);
        mCostPerPlate = (TextView) findViewById(R.id.cost_per_plate);
        mTimings = (TextView) findViewById(R.id.timings);
        mDeliveryOptions = (GridView) findViewById(R.id.deliveryOptions);
        mIncrementPlates = (Button) findViewById(R.id.increment_plates);
        mDecrementPlates = (Button) findViewById(R.id.decrement_plates);
        mTotalAmount = (TextView) findViewById(R.id.total_amount);
        mBuy = (Button) findViewById(R.id.btnBuy);
        mGuests = (TextView) findViewById(R.id.noOfGuests);
        mTitle = (TextView) findViewById(R.id.title);
        mSpecialNeeds = (TextView) findViewById(R.id.txt_special_needs);
        maxAvailableQuantity = dinnerListingViewContent.getAvailableQuantity();
        if (sharedPreferences == null) {
            sharedPreferences = getSharedPreferences(Constants.PREFS_PRIVATE, Context.MODE_PRIVATE);
        }
        isValidProfile = sharedPreferences.getBoolean(Constants.IS_VALID_PROFILE, Boolean.FALSE);
        initializeControls();
        handler = new Handler();
        final Set<String> imageUris = new HashSet<>(5);
        if (StringUtils.hasText(dinnerListingViewContent.getImageUri())) {
            String images = StringUtils.replace(dinnerListingViewContent.getImageUri(), "\"", "");
            imageUris.addAll(StringUtils.commaDelimitedListToSet(images));
        }
        if (imageUris != null && !imageUris.isEmpty()) {
            Runnable runnable = new Runnable() {
                Iterator<String> iterator = imageUris.iterator();
                @Override
                public void run() {
                    if (iterator.hasNext()) {
                        imageLoader.displayImage(iterator.next(), mFoodImages);
                    } else {
                        iterator = imageUris.iterator();
                    }
                    handler.postDelayed(this, 2000);
                }
            };
            handler.postDelayed(runnable, 2000);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sharedPreferences == null) {
            sharedPreferences = getSharedPreferences(Constants.PREFS_PRIVATE, Context.MODE_PRIVATE);
        }
        isValidProfile = sharedPreferences.getBoolean(Constants.IS_VALID_PROFILE, Boolean.FALSE);
    }

    private void initializeControls() {
        if (dinnerListingViewContent == null) {
            return;
        }
        //TODO: set food images

        if (StringUtils.hasText(dinnerListingViewContent.getTitle())) {
            mTitle.setText(dinnerListingViewContent.getTitle());
        }
        if (StringUtils.hasText(dinnerListingViewContent.getProfile())) {
            mProfileName.setText(dinnerListingViewContent.getProfile());
        }
        if (StringUtils.hasText(dinnerListingViewContent.getAddress())) {
            mAddress.setText(new StringBuffer(dinnerListingViewContent.getAddress())
                    .append("(")
                    .append(StringUtils.hasText(dinnerListingViewContent.getDistance()) ? dinnerListingViewContent.getDistance() : "")
                    .append(")")
                    .toString());
        }
        if (StringUtils.hasText(dinnerListingViewContent.getCost())) {
            mCostPerPlate.setText(dinnerListingViewContent.getCost());
        }
        if (StringUtils.hasText(dinnerListingViewContent.getTimings())) {
            mTimings.setText(dinnerListingViewContent.getTimings());
        }
        if (StringUtils.hasText(dinnerListingViewContent.getSpecialNeeds())) {
            mSpecialNeeds.setText(dinnerListingViewContent.getSpecialNeeds());
        } else {
            mSpecialNeeds.setText("");
        }

        if (StringUtils.hasText(dinnerListingViewContent.getDeliveryOptions())) {
            String[] options = dinnerListingViewContent.getDeliveryOptions().split(",");
            Arrays.sort(options);
            Integer[] images = new Integer[options.length];
            int imageIdx = 0;
            for (String option : options) {
                if (option.equalsIgnoreCase("Eat-In")) {
                    images[imageIdx++] = R.drawable.dinein2;
                } else if (option.equalsIgnoreCase("To-Go")) {
                    images[imageIdx++] = R.drawable.togo;
                } else if (option.equalsIgnoreCase("Will Deliver")) {
                    images[imageIdx++] = R.drawable.deliver;
                }

            }
            ImageAdapter imageAdapter = new ImageAdapter(getApplicationContext(), images);
            mDeliveryOptions.setAdapter(imageAdapter);
        }
        Locale locale = getResources().getConfiguration().locale;
        final DecimalFormat decimalFormat = new DecimalFormat(Currency.getInstance(locale).getSymbol() + "#0.00");
        try {
            noOfGuests = noOfGuests == 0 ? Integer.parseInt(mGuests.getText().toString()) : noOfGuests;
            float price = decimalFormat.parse(dinnerListingViewContent.getCost()).floatValue();
            price = price * noOfGuests;
            mTotalAmount.setText(decimalFormat.format(price));
        } catch (Exception e) {
            Log.e("P2PDinner", e.getMessage());
        }
        mIncrementPlates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer noOfGuests = Integer.parseInt(mGuests.getText().toString());
                if (noOfGuests + 1 <= maxAvailableQuantity) {
                    noOfGuests++;
                }
                mGuests.setText(noOfGuests.toString());
                try {
                    float price = decimalFormat.parse(dinnerListingViewContent.getCost()).floatValue();
                    price = price * noOfGuests;
                    mTotalAmount.setText(decimalFormat.format(price));
                } catch (Exception e) {
                    //DO nothing
                }

            }
        });
        mGuests.setText(String.valueOf(noOfGuests));
        mDecrementPlates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer noOfGuests = Integer.parseInt(mGuests.getText().toString());
                if (noOfGuests - 1 > 0) {
                    noOfGuests--;
                }
                mGuests.setText(noOfGuests.toString());
                try {
                    float price = decimalFormat.parse(dinnerListingViewContent.getCost()).floatValue();
                    price = price * noOfGuests;
                    mTotalAmount.setText(decimalFormat.format(price));
                } catch (Exception e) {
                    //DO nothing
                }
            }
        });

        mBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBuy.setEnabled(false);
                final Integer listingId = dinnerListingViewContent.getDinnerListingId();
                final Integer quantity = Integer.valueOf(mGuests.getText().toString());
                final String deliveryType = "To-Go";
                final Long profileId = sharedPreferences.getLong(Constants.PROFILE_ID, -1);
                if (!sharedPreferences.contains(Constants.ACCESS_TOKEN)) {
                    mBuy.setEnabled(true);
                    Intent intent = new Intent(getApplicationContext(), FacebookLoginActivity.class);
                    startActivity(intent);
                } else {
                    final String emailAddress = sharedPreferences.getString(Constants.EMAIL_ADDRESS, "");
                    final String accessToken = sharedPreferences.getString(Constants.ACCESS_TOKEN, "");
                    if (!isValidProfile) {
                        userProfileManager.validateProfile(emailAddress, accessToken)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .subscribe(new Observer<UserProfile>() {
                                    private UserProfile userProfile;

                                    @Override
                                    public void onCompleted() {
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putLong(Constants.PROFILE_ID, userProfile.getId());
                                        editor.commit();
                                        boolean sentToken = sharedPreferences.getBoolean(Constants.SENT_TOKEN_TO_SERVER, false);
                                        if (!sentToken) {
                                            Intent registrationIntent = new Intent(DinnerListingDetailActivity.this, RegistrationIntentService.class);
                                            startService(registrationIntent);
                                        }
                                        placeOrder(Long.valueOf(userProfile.getId()), listingId, quantity, deliveryType);
                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onNext(UserProfile userProfile) {
                                        this.userProfile = userProfile;
                                    }
                                });
                    } else {
                        placeOrder(profileId, listingId, quantity, deliveryType);
                    }
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.FACEBOOK_REQUEST_CODE) {
            mBuy.setEnabled(true);
        }
    }

    private void placeOrder(final long profileId, Integer listingId, Integer quantity, String deliveryType) {
        dinnerCartManager.addToCart(profileId, listingId, quantity, deliveryType)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnNext(new Action1<Integer>() {
                    @Override
                    public void call(Integer cartId) {
                        dinnerCartManager.placeOrder(profileId, cartId)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .subscribe(new Observer<String>() {
                                    private String message;

                                    @Override
                                    public void onCompleted() {
                                        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onNext(String s) {
                                        this.message = s;
                                    }
                                });
                    }
                })
                .subscribe(new Observer<Integer>() {
                    private Integer cartId;

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(Integer cartId) {
                        this.cartId = cartId;
                    }
                });
    }

    private class ImageAdapter extends BaseAdapter {
        private Context mContext;
        private Integer[] mThumbs;

        public ImageAdapter(Context context, Integer[] imageIds) {
            mContext = context;
            mThumbs = imageIds;
        }

        @Override
        public int getCount() {
            return mThumbs == null ? 0 : mThumbs.length;
        }

        @Override
        public Object getItem(int position) {
            return mThumbs[position];
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(100, 100));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(8, 8, 8, 8);
            } else {
                imageView = (ImageView) convertView;
            }

            imageView.setImageResource(mThumbs[position]);
            return imageView;
        }
    }

}
