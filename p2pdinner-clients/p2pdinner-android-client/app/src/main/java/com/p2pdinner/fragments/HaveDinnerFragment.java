package com.p2pdinner.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.p2pdinner.P2PDinnerApplication;
import com.p2pdinner.R;
import com.p2pdinner.activities.SellerListingDetailActivity;
import com.p2pdinner.common.Constants;
import com.p2pdinner.entities.SellerListing;
import com.p2pdinner.restclient.DinnerListingManager;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

import javax.inject.Inject;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by rajaniy on 10/24/15.
 */
public class HaveDinnerFragment extends Fragment {

    private static final String TAG = "P2PDinner";

    private LinearLayout mDateLayout = null;
    private int startId = 50000;
    private MyOrdersListAdapter myOrdersListAdapter = null;
    private ListView mOrdersListView;

    class MenuTag {
        private String tag;
        private String label;

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MenuTag menuTags = (MenuTag) o;

            return label.equals(menuTags.label);

        }

        @Override
        public int hashCode() {
            return label.hashCode();
        }
    }

    @Inject
    DinnerListingManager dinnerListingManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ((P2PDinnerApplication)this.getActivity().getApplication()).inject(this);
        View view = inflater.inflate(R.layout.havedinner_layout, container, false);
        mDateLayout = (LinearLayout) view.findViewById(R.id.date_layout);
        mOrdersListView = (ListView) view.findViewById(R.id.listings_view_id);
        mOrdersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SellerListing listing = myOrdersListAdapter.getListing(position);
                Intent intent = new Intent(view.getContext().getApplicationContext(), SellerListingDetailActivity.class);
                intent.putExtra(Constants.SELL_LISTING_ITEM, listing);
                startActivity(intent);
            }
        });
        //Calendar today = Calendar.getInstance();
        //Calendar pastFiveDays = Calendar.getInstance();
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd(E)");
        DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("MM/dd/yyyy");
        List<MenuTag> menuLabels = new ArrayList<>();
        List<String> tags = new ArrayList<>();
        // start from two days after
        dateTime = dateTime.plus(Period.hours(48));
        for (int i = 0; i < 5; i++) {
            MenuTag menuTag = new MenuTag();
            menuTag.setLabel(formatter.print(dateTime));
            menuTag.setTag(dateFormatter.print(dateTime));
            menuLabels.add(menuTag);
            dateTime = dateTime.minus(Period.hours(24));
        }
        //pastFiveDays.add(Calendar.DATE, -4)
        final Long profileId = getActivity().getIntent().getLongExtra(Constants.PROFILE_ID, -1);
        Collections.reverse(menuLabels);
        for (int idx = 0; idx < menuLabels.size(); idx++) {
            TextView txtView = (TextView) inflater.inflate(R.layout.havedinner_text_layout, container, false);
            txtView.setId(startId++);
            txtView.setText(menuLabels.get(idx).getLabel());
            txtView.setTag(menuLabels.get(idx));
            //pastFiveDays.add(Calendar.DATE, 1);
            if (formatter.print(LocalDateTime.now()).equalsIgnoreCase(menuLabels.get(idx).getLabel())) {
                txtView.setTextColor(Color.BLACK);
            }
            mDateLayout.addView(txtView);
            startId++;
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
                    DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("MM/dd/yyyy");
                    MenuTag menuTag = (MenuTag) tv.getTag();
                    DateTime inputDateTime = dateTimeFormatter.parseDateTime(menuTag.getTag());
                    //final long inputDateInMillis = new DateTime(inputDateTime.toDate(), DateTimeZone.UTC).toLocalDateTime().toDate().getTime();
                    //Log.i(TAG, "Input Time in millis <<<<" + inputDateInMillis);
                    updateListingsView(profileId, inputDateTime);

                }
            });
        }

        DateTime currentDateTime = DateTime.now()
                .withHourOfDay(0)
                .withMinuteOfHour(0)
                .withSecondOfMinute(0)
                .withMillisOfSecond(0);
        //long inputTimeInMillis = currentDateTime.toDateTime(DateTimeZone.UTC).toLocalDateTime().toDate().getTime();
        //Log.i(TAG, "Input Time in millis >>>>" + inputTimeInMillis);
        updateListingsView(profileId, currentDateTime);
        return view;
    }

    private void updateListingsView(Long profileId, DateTime startDateTime) {
        dinnerListingManager.listingsByProfile(profileId.intValue(), startDateTime)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<SellerListing>>() {
                    private List<SellerListing> sellerListings = new ArrayList<SellerListing>();
                    @Override
                    public void onCompleted() {
                        if (sellerListings != null && !sellerListings.isEmpty()) {
                            myOrdersListAdapter = new MyOrdersListAdapter(sellerListings);
                            mOrdersListView.setAdapter(myOrdersListAdapter);
                        } else {
                            mOrdersListView.setAdapter(null);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mOrdersListView.setAdapter(null);
                        Toast.makeText(getActivity().getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(List<SellerListing> sellerListings) {
                        this.sellerListings.addAll(sellerListings);
                    }
                });
    }

    private class MyOrdersListAdapter extends ArrayAdapter<SellerListing> {

        private List<SellerListing> sellerListings;

        public SellerListing getListing(Integer pos) {
           return sellerListings.get(pos);
        }

        public MyOrdersListAdapter(List<SellerListing> sellerListings) {
            super(getActivity().getApplicationContext(), R.layout.item_createdinner_options, sellerListings);
            this.sellerListings = sellerListings;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getActivity().getLayoutInflater().inflate(R.layout.myorders_items_layout, parent, false);
            }
            SellerListing listing = sellerListings.get(position);
            TextView titleTxtView = (TextView) itemView.findViewById(R.id.order_title);
            StringBuffer title = new StringBuffer(listing.getTitle());
            title.append(" (").append(listing.getDinnerListing().getOrderQuantity().toString()).append(")");
            titleTxtView.setText(title.toString());
            TextView subtitleTxtView = (TextView) itemView.findViewById(R.id.orders_subtitle);
            String servingBetween = "Serving %s to %s";

            DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss");
            DateTime startTime = dateTimeFormatter.withZoneUTC().parseDateTime(listing.getDinnerListing().getStartTime());
            DateTime endTime = dateTimeFormatter.withZoneUTC().parseDateTime(listing.getDinnerListing().getEndTime());

            DateTimeFormatter printFormatter = new DateTimeFormatterBuilder()
                    .appendHourOfHalfday(2)
                    .appendLiteral(":")
                    .appendMinuteOfHour(2)
                    .appendLiteral(" ")
                    .appendHalfdayOfDayText().toFormatter();

            subtitleTxtView.setText(String.format(servingBetween, printFormatter.print(startTime.toDateTime(DateTimeZone.getDefault())), printFormatter.print(endTime.toDateTime(DateTimeZone.getDefault()))));
            //TextView orderCountTxtView = (TextView) itemView.findViewById(R.id.order_quantity);
            //orderCountTxtView.setText(listing.getDinnerListing().getOrderQuantity().toString());
            return itemView;
        }
    }
}
