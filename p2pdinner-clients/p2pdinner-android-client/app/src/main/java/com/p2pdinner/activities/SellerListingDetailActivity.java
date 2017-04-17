package com.p2pdinner.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.p2pdinner.R;
import com.p2pdinner.common.Constants;
import com.p2pdinner.common.RatingParty;
import com.p2pdinner.entities.DinnerMenuItem;
import com.p2pdinner.entities.Order;
import com.p2pdinner.entities.SellerListing;
import com.p2pdinner.restclient.DinnerCartManager;
import com.p2pdinner.seller.dinner.CreateDinnerOption;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SellerListingDetailActivity extends BaseAppCompatActivity {

    private static final String TAG = "SLDetailActivity";
    private SellerListing mSellerListing = null;
    private ListView mlvSellerItems;
    private TextView mtxtTitle;
    private Handler handler;

    @Inject
    DinnerCartManager dinnerCartManager;

    @Inject
    Tracker mTracker;

    @Override
    protected void onResume() {
        super.onResume();
        mTracker.setScreenName(getClass().getName());
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSellerListing = (SellerListing) getIntent().getExtras().get(Constants.SELL_LISTING_ITEM);
        setContentView(R.layout.activity_seller_listing_detail);
        mlvSellerItems = (ListView) findViewById(R.id.listView);
        mtxtTitle = (TextView) findViewById(R.id.titleView);
        mtxtTitle.setText(mSellerListing.getTitle().toUpperCase());

        dinnerCartManager.receivedOrders(mSellerListing.getDinnerListing().getDinnerListingId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Observer<List<Order>>() {
                    private List<Order> orders;

                    @Override
                    public void onCompleted() {
                        if (orders != null) {
                            mlvSellerItems.setAdapter(new OrderListAdapter(getApplicationContext(), R.layout.items_seller_listing_detail, orders));
                        } else {
                            mlvSellerItems.setAdapter(null);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<Order> orders) {
                        this.orders = orders;
                    }
                });

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
            View itemView = getLayoutInflater().inflate(resource, null, true);
            final Order order = orders.get(position);
            TextView txtBuyerProfileName = (TextView) itemView.findViewById(R.id.buyerProfileName);
            txtBuyerProfileName.setText(order.getProfileName());
            TextView txtConfirmationCode = (TextView) itemView.findViewById(R.id.confirmationCode);
            txtConfirmationCode.setText("Conf #" + order.getPassCode());
            TextView txtNoofGuests = (TextView) itemView.findViewById(R.id.noOfGuests);
            txtNoofGuests.setText(order.getOrderQuantity() + " Plates");
            RatingBar ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
            if (order.getBuyerRating() != null) {
                ratingBar.setRating(Float.valueOf(order.getBuyerRating()));
            }
            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                    dinnerCartManager.updateRating(RatingParty.BUYER, order.getCartId().longValue(), Float.valueOf(v).intValue())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe(new Observer<String>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {
                                    Log.e(TAG, e.getMessage());
                                    Toast.makeText(SellerListingDetailActivity.this.getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onNext(String s) {
                                    Toast.makeText(SellerListingDetailActivity.this.getApplicationContext(), s, Toast.LENGTH_LONG).show();
                                }
                            });
                }
            });
            return itemView;
        }
    }

}
