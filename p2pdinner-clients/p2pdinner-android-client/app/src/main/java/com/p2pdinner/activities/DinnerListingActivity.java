package com.p2pdinner.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.p2pdinner.R;
import com.p2pdinner.common.Constants;
import com.p2pdinner.entities.DinnerListingViewContent;
import com.p2pdinner.entities.DinnerMenuItem;
import com.p2pdinner.entities.DinnerSearchResult;
import com.p2pdinner.entities.DinnerSearchResults;
import com.p2pdinner.restclient.ImageDownloadTask;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.springframework.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

public class DinnerListingActivity extends AppCompatActivity {

    private static final String TAG = DinnerListingActivity.class.getName();

    private ListView mSearchResultsView;
    private List<DinnerListingViewContent> dinnerListingViewContents = new ArrayList<>();
    private int noOfGuests = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dinner_listing);
        mSearchResultsView = (ListView) findViewById(R.id.searchResultsView);
        DinnerSearchResults searchResults = (DinnerSearchResults) getIntent().getExtras().get(Constants.CURRENT_DINNER_LISTINGS);
        noOfGuests = getIntent().getExtras().getInt(Constants.NO_OF_GUESTS);
        List<DinnerMenuItem> menuItems = new ArrayList<>();
        for (DinnerSearchResult result : searchResults.getSearchResults()) {
            menuItems.addAll(result.getMenuItems());
        }
        dinnerListingViewContents.addAll(buildContent(menuItems));
        final DinnerSearchResultAdapter searchResultAdapter = new DinnerSearchResultAdapter(this, R.layout.items_search_results, dinnerListingViewContents);
        mSearchResultsView.setAdapter(searchResultAdapter);
        mSearchResultsView.setEmptyView(findViewById(R.id.emptyElement));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dinner_listing, menu);
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

    public List<DinnerListingViewContent> buildContent(List<DinnerMenuItem> dinnerMenuItems) {
        List<DinnerListingViewContent> dinnerListingViewContents = new ArrayList<>();
        if (dinnerMenuItems != null && !dinnerMenuItems.isEmpty()) {
            Collections.sort(dinnerMenuItems, new Comparator<DinnerMenuItem>() {
                @Override
                public int compare(DinnerMenuItem lhs, DinnerMenuItem rhs) {
                    if (lhs.getLocation().getDistanceInMeters() < rhs.getLocation().getDistanceInMeters()) {
                        return -1;
                    } else if (lhs.getLocation().getDistanceInMeters() == rhs.getLocation().getDistanceInMeters()) {
                        return 0;
                    } else {
                        return 1;
                    }
                }
            });
            for (DinnerMenuItem dinnerMenuItem : dinnerMenuItems) {
                DinnerListingViewContent content = new DinnerListingViewContent();
                content.setDinnerListingId(dinnerMenuItem.getDinnerListing().getDinnerListingId());
                content.setAddress(dinnerMenuItem.getLocation().getAddress());
                Locale locale = getResources().getConfiguration().locale;
                DecimalFormat decimalFormat = new DecimalFormat(Currency.getInstance(locale).getSymbol() + "#0.00");
                content.setCost(decimalFormat.format(dinnerMenuItem.getDinnerListing().getCostPerItem()));
                content.setDistance(dinnerMenuItem.getLocation().getDistance());
                content.setProfile(dinnerMenuItem.getProfileName().toString());
                DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss");
                DateTime startTime = formatter.withZone(DateTimeZone.forID("UTC")).parseDateTime(dinnerMenuItem.getDinnerListing().getStartTime());
                DateTime endTime = formatter.withZone(DateTimeZone.forID("UTC")).parseDateTime(dinnerMenuItem.getDinnerListing().getEndTime());
//                DateTimeFormatter printFormatter = new DateTimeFormatterBuilder()
//                        .appendHourOfHalfday(2)
//                        .appendLiteral(":")
//                        .appendMinuteOfHour(2)
//                        .appendLiteral(" ")
//                        .appendHalfdayOfDayText().toFormatter();

                DateTimeFormatter printFormatter  = DateTimeFormat.forPattern("hh:mm aa");

                content.setTimings(printFormatter.print(startTime.toDateTime(DateTimeZone.getDefault())) + " - " + printFormatter.print(endTime.toDateTime(DateTimeZone.getDefault())));
                content.setTitle(dinnerMenuItem.getTitle());
                content.setDeliveryOptions(dinnerMenuItem.getDeliveryOptions());
                content.setAvailableQuantity(dinnerMenuItem.getDinnerListing().getAvailableQuantity());
                if (StringUtils.hasText(dinnerMenuItem.getSpecialNeeds())) {
                    content.setSpecialNeeds(dinnerMenuItem.getSpecialNeeds());
                }
                if (StringUtils.hasText(dinnerMenuItem.getImageUri())) {
                    content.setImageUri(dinnerMenuItem.getImageUri());
                }
                dinnerListingViewContents.add(content);
            }
        }

        return dinnerListingViewContents;
    }

    private class DinnerSearchResultAdapter extends ArrayAdapter<DinnerListingViewContent> {

        private final Context context;
        private final List<DinnerListingViewContent> results;
        private int resource;

        public DinnerSearchResultAdapter(Context context, int resource, List<DinnerListingViewContent> objects) {
            super(context, resource, objects);
            this.context = context;
            this.results = objects;
            this.resource = resource;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            View itemView = getLayoutInflater().inflate(resource, null, true);
            final DinnerListingViewContent content = results.get(position);
            final TextView title = (TextView) itemView.findViewById(R.id.title);
            title.setText(content.getTitle());
            TextView costPerPlate = (TextView) itemView.findViewById(R.id.cost_per_plate);
            costPerPlate.setText(content.getCost());
            TextView profile = (TextView) itemView.findViewById(R.id.profilename);
            profile.setText(content.getProfile());
            TextView address = (TextView) itemView.findViewById(R.id.address);
            address.setText(content.getAddress());
            TextView distance = (TextView) itemView.findViewById(R.id.distance);
            distance.setText(content.getDistance());
            TextView timings = (TextView) itemView.findViewById(R.id.timings);
            timings.setText(content.getTimings());
            if (StringUtils.hasText(content.getDeliveryOptions())) {
                GridView gridView = (GridView) itemView.findViewById(R.id.deliveryOptions);
                String[] options = content.getDeliveryOptions().split(",");
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
                ImageAdapter imageAdapter = new ImageAdapter(context, images);
                gridView.setAdapter(imageAdapter);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), DinnerListingDetailActivity.class);
                    intent.putExtra(Constants.DINNER_LISTING_VIEW_CONTENT, content);
                    intent.putExtra(Constants.NO_OF_GUESTS, noOfGuests);
                    startActivity(intent);
                }
            });
            final ImageView foodImageView = (ImageView) itemView.findViewById(R.id.food_images);
            if (StringUtils.hasText(content.getImageUri())) {
                String imageUri = StringUtils.replace(content.getImageUri(), "\"", "");
                String[] images = StringUtils.commaDelimitedListToStringArray(imageUri);
                ImageDownloadTask imageDownloadTask = new ImageDownloadTask(foodImageView);
                imageDownloadTask.execute(images[0]);
            }
            return itemView;
        }
    }

    private void setImageView(View v, String selectedImage) {
        if (!StringUtils.hasText(selectedImage)) {
            return;
        }
        Bitmap thumbnailBmp = null;
        try {

            final int THUMBNAIL_SIZE = 512;

            FileInputStream fis = new FileInputStream(selectedImage);
            thumbnailBmp = BitmapFactory.decodeStream(fis);

            thumbnailBmp = Bitmap.createScaledBitmap(thumbnailBmp,
                    THUMBNAIL_SIZE, THUMBNAIL_SIZE, false);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            thumbnailBmp.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);


        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }
        if (thumbnailBmp != null) {
            ImageView imageView = (ImageView) v;
            imageView.setImageBitmap(thumbnailBmp);
        }
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
                imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
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
