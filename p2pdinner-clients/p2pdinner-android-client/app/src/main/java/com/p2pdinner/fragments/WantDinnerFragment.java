package com.p2pdinner.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.common.base.Charsets;
import com.p2pdinner.R;
import com.p2pdinner.common.Constants;
import com.p2pdinner.common.RatingParty;
import com.p2pdinner.entities.Order;
import com.p2pdinner.restclient.DinnerCartManager;
import com.p2pdinner.restclient.ImageDownloadTask;

import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriUtils;

import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by rajaniy on 10/24/15.
 */
public class WantDinnerFragment extends BaseFragment {


    private static final String TAG = "WantDinnerFragment";

    private LinearLayout mDateLayout = null;
    private ListView mlvBuyerItems;
    private Integer defaultCalendarUid;
    private String defaultCalendarTitle;

    private static final int LOADER_LIST = 100;

    @Inject
    DinnerCartManager dinnerCartManager;

    @Inject
    Tracker mTracker;


    @Override
    public void onResume() {
        super.onResume();
        mTracker.setScreenName("MyOrder.IWantDinner");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        DateTime startTime = DateTime.now();
        // Joda clock 1 - 24
        startTime = startTime.withField(DateTimeFieldType.clockhourOfDay(), 1);
        startTime = startTime.withField(DateTimeFieldType.minuteOfDay(), 0);
        startTime = startTime.withField(DateTimeFieldType.secondOfDay(), 0);
        final DateTime endTime = startTime.plusDays(1);
        final Long profileId = getActivity().getIntent().getLongExtra(Constants.PROFILE_ID, -1);
        dinnerCartManager.receivedOrdersByBuyer(profileId, startTime.toDateTime(DateTimeZone.UTC), endTime.toDateTime(DateTimeZone.UTC)).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<List<Order>>() {
            private List<Order> orders = new ArrayList<>();

            @Override
            public void onCompleted() {
                if (orders != null) {
                    if (getActivity() != null && getActivity().getApplicationContext() != null) {
                        mlvBuyerItems.setAdapter(new OrderListAdapter(getActivity().getApplicationContext(), R.layout.items_buyer_items_detail, orders));
                    }
                } else {
                    mlvBuyerItems.setAdapter(null);
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<Order> orders) {
                this.orders.addAll(orders);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wantdinner_layout, container, false);
        mDateLayout = (LinearLayout) view.findViewById(R.id.date_layout);
        mlvBuyerItems = (ListView) view.findViewById(R.id.buyer_items_list_view);
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd(E)");
        List<String> menuLabels = new ArrayList<>();
        dateTime = dateTime.plus(Period.hours(48));
        for (int i = 0; i < 5; i++) {
            menuLabels.add(formatter.print(dateTime));
            dateTime = dateTime.minus(Period.hours(24));
        }
        //pastFiveDays.add(Calendar.DATE, -4)
        final Long profileId = getActivity().getIntent().getLongExtra(Constants.PROFILE_ID, -1);
        Collections.reverse(menuLabels);
        for (String menuLabel : menuLabels) {
            TextView txtView = (TextView) inflater.inflate(R.layout.havedinner_text_layout, container, false);
            txtView.setText(menuLabel);
            //pastFiveDays.add(Calendar.DATE, 1);
            if (formatter.print(LocalDateTime.now()).equalsIgnoreCase(menuLabel)) {
                txtView.setTextColor(Color.BLACK);
            }
            mDateLayout.addView(txtView);
            txtView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int idx = 0; idx < mDateLayout.getChildCount(); idx++) {
                        if (mDateLayout.getChildAt(idx) instanceof TextView) {
                            TextView t = (TextView) mDateLayout.getChildAt(idx);
                            t.setTextColor(Color.WHITE);
                        }
                    }
                    TextView tv = (TextView) v;
                    tv.setTextColor(Color.BLACK);
                    Calendar now = Calendar.getInstance();
                    final StringBuffer inputDate = new StringBuffer();
                    inputDate
                            .append(now.get(Calendar.MONTH) + 1)
                            .append("/")
                            .append(tv.getText().subSequence(0, 2).toString())
                            .append("/")
                            .append(now.get(Calendar.YEAR));

                    DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("MM/dd/yyyy");
                    final DateTime startTime = dateTimeFormatter.parseDateTime(inputDate.toString());
                    final DateTime endTime = startTime.plusDays(1);
                    dinnerCartManager.receivedOrdersByBuyer(profileId, startTime.toDateTime(DateTimeZone.UTC), endTime.toDateTime(DateTimeZone.UTC)).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<List<Order>>() {
                        private List<Order> orders = new ArrayList<>();

                        @Override
                        public void onCompleted() {
                            if (orders != null) {
                                mlvBuyerItems.setAdapter(new OrderListAdapter(getActivity().getApplicationContext(), R.layout.items_buyer_items_detail, orders));
                            } else {
                                mlvBuyerItems.setAdapter(null);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(List<Order> orders) {
                            this.orders.addAll(orders);
                        }
                    });
                }
            });
        }
        return view;
    }

    private class OrderListAdapter extends ArrayAdapter<Order> {
        private final Context context;
        private final List<Order> orders;
        private int resource;

        public OrderListAdapter(Context context, int resource, List<Order> orders) {
            super(context, resource, orders);
            this.context = context;
            this.orders = orders;
            this.resource = resource;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Locale locale = getResources().getConfiguration().locale;
            DecimalFormat decimalFormat = new DecimalFormat(Currency.getInstance(locale).getSymbol() + "#0.00");
            View itemView = getActivity().getLayoutInflater().inflate(resource, null, true);
            final Order order = orders.get(position);
            ImageView imageView = (ImageView) itemView.findViewById(R.id.food_images);
            if (StringUtils.hasText(order.getImageUri())) {
                String[] images = order.getImageUri().split(",");
                String uri = StringUtils.replace(images[0], "\"", "");
                ImageDownloadTask imageDownloadTask = new ImageDownloadTask(imageView);
                imageDownloadTask.execute(uri);
            }
            TextView mTxtTitleView = (TextView) itemView.findViewById(R.id.title);
            mTxtTitleView.setText(order.getTitle());
            TextView mTxtTotalPrice = (TextView) itemView.findViewById(R.id.total_amount);
            mTxtTotalPrice.setText(decimalFormat.format(order.getTotalPrice()));
            TextView mTxtProfileName = (TextView) itemView.findViewById(R.id.profilename);
            mTxtProfileName.setText(order.getProfileName());
            final String address = StringUtils.arrayToDelimitedString(new Object[]{order.getAddressLine1(), order.getAddressLine2(), order.getCity(), order.getState()}, " ");
            TextView mTxtAddress = (TextView) itemView.findViewById(R.id.address);
            mTxtAddress.setText(address);
            TextView mTxtCC = (TextView) itemView.findViewById(R.id.confirmation_code);
            mTxtCC.setText("Conf# " + order.getPassCode() + " for " + order.getOrderQuantity() + " plates");
            TextView timings = (TextView) itemView.findViewById(R.id.timings);
            DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/yyyy hh:mm:ss");
            final DateTime startDtTime = new DateTime(order.getStartTime());
            final DateTime endDtTime = new DateTime(order.getEndTime());
            DateTimeFormatter printFormatter = new DateTimeFormatterBuilder()
                    .appendHourOfHalfday(2)
                    .appendLiteral(":")
                    .appendMinuteOfHour(2)
                    .appendLiteral(" ")
                    .appendHalfdayOfDayText().toFormatter();
            timings.setText("Served between " + printFormatter.print(startDtTime) + " and " + printFormatter.print(endDtTime));
            RatingBar ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
            if (order.getSellerRating() != null) {
                ratingBar.setRating(order.getSellerRating().intValue());
            }
            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                    dinnerCartManager.updateRating(RatingParty.SELLER, order.getCartId().longValue(), Float.valueOf(v).intValue())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe(new Observer<String>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {
                                    Log.e(TAG, e.getMessage());
                                    Toast.makeText(WantDinnerFragment.this.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onNext(String s) {
                                    Toast.makeText(WantDinnerFragment.this.getContext(), s, Toast.LENGTH_LONG).show();
                                }
                            });
                }
            });

            ImageView calendarImageView = (ImageView) itemView.findViewById(R.id.calendar);
            ImageView mapsView = (ImageView) itemView.findViewById(R.id.directions);

            if (startDtTime.isBeforeNow() && endDtTime.isBeforeNow()) {
                calendarImageView.setVisibility(View.INVISIBLE);
                mapsView.setVisibility(View.INVISIBLE);
            }

            calendarImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calendar calendar = Calendar.getInstance();
                    Intent intent = new Intent(Intent.ACTION_EDIT)
                            .setData(CalendarContract.Events.CONTENT_URI)
                            .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startDtTime.toDate().getTime())
                            .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endDtTime.toDate().getTime())
                            .putExtra(CalendarContract.Events.TITLE, "Dinner Pickup " + order.getTitle() + " -- conf# " + order.getPassCode())
                            .putExtra(CalendarContract.Events.DESCRIPTION, "Dinner pickup")
                            .putExtra(CalendarContract.Events.EVENT_LOCATION, address);
                    startActivity(intent);
                }
            });

            mapsView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    try {
                        Uri geoLocation = Uri.parse("geo:0,0?q=" + UriUtils.encodePath(address, Charsets.UTF_8.displayName()));
                        intent.setData(geoLocation);
                        startActivity(intent);
                    } catch (Throwable t) {
                        Log.e(TAG, t.getMessage(), t);
                    }

                }
            });
            return itemView;
        }
    }
}
