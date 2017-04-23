package com.p2pdinner.activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.p2pdinner.R;
import com.p2pdinner.common.Constants;
import com.p2pdinner.entities.UserProfile;
import com.p2pdinner.entities.UserOption;
import com.p2pdinner.restclient.UserProfileManager;
import com.p2pdinner.services.P2PDinnerOAuthTokenRefreshService;
import com.p2pdinner.services.RegistrationIntentService;

import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.inject.Inject;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class MainActivity extends BaseAppCompatActivity {

    private static final String TAG = "P2PDinner";
    private ListView userOptionsView = null;
    private TextView mFooter = null;
    private List<UserOption> userOptions = new ArrayList<UserOption>();
    private SharedPreferences sharedPreferences;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private Boolean playServicesVerified = false;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private static final int POLL_INTERVAL = 60 * 1000;

    @Inject
    UserProfileManager userProfileManager;

    @Inject
    Tracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences(Constants.PREFS_PRIVATE, Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(Constants.IS_VALID_PROFILE, Boolean.FALSE);
        setContentView(R.layout.activity_main);
        intializeControls();
        Log.i(TAG, "Populating user options");
        populateUserOptions();
        Log.i(TAG, "Populating list view");

        populateListView();
        registerUserOptionClickListener();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        initializeNavigationDrawer();
        //startOAuthService();
    }

    private void intializeControls() {
        mFooter = (TextView) findViewById(R.id.footer);
        StringBuffer footerText  = new StringBuffer("By using this app, I agree to the <a href=")
                .append(Constants.P2PDINNER_WEB_BASE_URI +  getString(R.string.tcUri))
                .append(">")
                .append("Terms & Conditions")
                .append("</a>");
        mFooter.setText(Html.fromHtml(footerText.toString()));
        mFooter.setMovementMethod(LinkMovementMethod.getInstance());
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "Setting screen name " + "Home");
        tracker.setScreenName("Home");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
        if (!playServicesVerified) {
            checkPlayServices();
            playServicesVerified = true;
        }

    }

    private void populateUserOptions() {
        userOptions.add(new UserOption(R.drawable.iwantdinner, "I Want Dinner", "Find the perfect home made dinner!"));
        userOptions.add(new UserOption(R.drawable.ihavedinner, "I Have Dinner", "Share your food for extra money!"));
        userOptions.add(new UserOption(R.drawable.myorders, "My Orders", "View your current, previous orders!"));
    }

    private void populateListView() {
        ArrayAdapter<UserOption> arrayAdapter = new UserOptionsAdapter();
        userOptionsView = (ListView) findViewById(R.id.userOptionsView);
        userOptionsView.setAdapter(arrayAdapter);
    }

    private void registerUserOptionClickListener() {
        userOptionsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserOption option = userOptions.get(position);
                //Toast.makeText(MainActivity.this, "You click on " + option.getTitle(), Toast.LENGTH_LONG).show();
                Boolean isValidProfile = sharedPreferences.getBoolean(Constants.IS_VALID_PROFILE, Boolean.FALSE);
                switch (option.getIcon()) {
                    case R.drawable.ihavedinner:
                        tracker.send(new HitBuilders.EventBuilder()
                            .setCategory("Action")
                            .setAction("I Have Dinner")
                            .build());
                        if (!sharedPreferences.contains(Constants.ACCESS_TOKEN) &&
                                !StringUtils.hasText(sharedPreferences.getString(Constants.EMAIL_ADDRESS, ""))) {
                            Intent intent = new Intent(getApplicationContext(), FacebookLoginActivity.class);
                            startActivity(intent);
                        } else {
                            final String emailAddress = sharedPreferences.getString(Constants.EMAIL_ADDRESS, "");
                            final String accessToken = sharedPreferences.getString(Constants.ACCESS_TOKEN, "");
                            Intent intent = new Intent(getApplicationContext(), CreateDinnerActivity.class);
                            validateAndProceed(emailAddress, accessToken, intent);
                        }
                        break;
                    case R.drawable.myorders:
                        tracker.send(new HitBuilders.EventBuilder()
                                .setCategory("Action")
                                .setAction("MyOrders")
                                .build());
                        sharedPreferences = getSharedPreferences(Constants.PREFS_PRIVATE, Context.MODE_PRIVATE);
                        if (!sharedPreferences.contains(Constants.ACCESS_TOKEN)) {
                            Intent intent = new Intent(getApplicationContext(), FacebookLoginActivity.class);
                            startActivity(intent);
                        } else {
                            final String emailAddress = sharedPreferences.getString(Constants.EMAIL_ADDRESS, "");
                            final String accessToken = sharedPreferences.getString(Constants.ACCESS_TOKEN, "");
                            Intent intent = new Intent(getApplicationContext(), SellerOrdersListingActivity.class);
                            validateAndProceed(emailAddress, accessToken, intent);
                        }
                        break;
                    case R.drawable.iwantdinner:
                        tracker.send(new HitBuilders.EventBuilder()
                                .setCategory("Action")
                                .setAction("I Want Dinner")
                                .build());
                        Intent intent = new Intent(getApplicationContext(), FindDinnerActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        Toast.makeText(MainActivity.this, "You click on " + option.getTitle(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void validateAndProceed(String emailAddress, String accessToken, final Intent intent) {
        userProfileManager.validateProfile(emailAddress, accessToken)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserProfile>() {
                    private UserProfile userProfile;

                    @Override
                    public void onCompleted() {
                        sharedPreferences = getSharedPreferences(Constants.PREFS_PRIVATE, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(Constants.EMAIL_ADDRESS, userProfile.getEmailAddress());
                        editor.putLong(Constants.PROFILE_ID, userProfile.getId());
                        editor.putBoolean(Constants.IS_VALID_PROFILE, Boolean.TRUE);
                        tracker.set("&uid", userProfile.getId().toString());
                        editor.commit();
                        boolean sentToken = sharedPreferences.getBoolean(Constants.SENT_TOKEN_TO_SERVER, false);
                        if (!sentToken) {
                            Intent registrationIntent = new Intent(MainActivity.this, RegistrationIntentService.class);
                            startService(registrationIntent);
                        }
                        editor.putBoolean(Constants.SENT_TOKEN_TO_SERVER, sentToken);
                        editor.commit();
                        startActivity(intent);
                    }

                    @Override
                    public void onError(Throwable e) {
                        final UserProfile userProfile = new UserProfile();
                        userProfile.setEmailAddress(sharedPreferences.getString(Constants.EMAIL_ADDRESS, ""));
                        userProfile.setPassword(sharedPreferences.getString(Constants.ACCESS_TOKEN, ""));
                        userProfile.setName(sharedPreferences.getString(Constants.PROFILE_NAME, ""));
                        userProfile.setAuthenticationProvider(sharedPreferences.getString(Constants.AUTHENTICATION_PROVIDER, ""));
                        createProfileAndProceed(userProfile, intent);
                    }

                    @Override
                    public void onNext(UserProfile userProfile) {
                        this.userProfile = userProfile;
                    }
                });
    }

    private void createProfileAndProceed(final UserProfile userProfile, final Intent intent) {
        userProfileManager.createProfile(userProfile)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Void>() {
                    @Override
                    public void onCompleted() {
                        sharedPreferences = getSharedPreferences(Constants.PREFS_PRIVATE, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(Constants.EMAIL_ADDRESS, userProfile.getEmailAddress());
                        if (userProfile != null && userProfile.getId() != null) {
                            editor.putLong(Constants.PROFILE_ID, userProfile.getId());
                            tracker.set("&uid", userProfile.getId().toString());
                        }
                        editor.commit();
                        boolean sentToken = sharedPreferences.getBoolean(Constants.SENT_TOKEN_TO_SERVER, false);
                        if (!sentToken) {
                            Intent registrationIntent = new Intent(MainActivity.this, RegistrationIntentService.class);
                            startService(registrationIntent);
                        }
                        editor.putBoolean(Constants.SENT_TOKEN_TO_SERVER, sentToken);
                        editor.commit();
                        startActivity(intent);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Void aVoid) {

                    }
                });
    }

//    private void startOAuthService() {
//        Intent startServiceIntent = new Intent(this, P2PDinnerOAuthTokenRefreshService.class);
//        PendingIntent pi = PendingIntent.getService(this, 0, startServiceIntent, 0);
//        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), POLL_INTERVAL, pi);
//    }

    private class UserOptionsAdapter extends ArrayAdapter<UserOption> {
        public UserOptionsAdapter() {
            super(MainActivity.this, R.layout.item_user_options, userOptions);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.item_user_options, parent, false);
            }
            UserOption currentOption = userOptions.get(position);
            ImageView itemIcon = (ImageView) itemView.findViewById(R.id.item_icon);
            itemIcon.setImageResource(currentOption.getIcon());
            TextView txtItemTitle = (TextView) itemView.findViewById(R.id.item_title);
            txtItemTitle.setText(currentOption.getTitle());
            TextView txtItemDescription = (TextView) itemView.findViewById(R.id.item_short_description);
            txtItemDescription.setText(currentOption.getShortDescription());
            return itemView;
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openBrowser(String uri) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(browserIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.FACEBOOK_REQUEST_CODE) {
            Toast.makeText(getApplicationContext(), "Facebook activity returned result", Toast.LENGTH_LONG);
        }
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }


    private void initializeNavigationDrawer() {
        ListView drawListView = (ListView) drawerLayout.findViewById(R.id.left_drawer);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,
                drawerLayout,
                R.string.drawer_open,
                R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                Log.d(TAG, "Received event -- onDrawerClosed");
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                Log.d(TAG, "Received event -- onDrawerOpened");
            }
        };
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        drawListView.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, getResources().getStringArray(R.array.navigation_bar_options)));
        drawListView.setOnItemClickListener(new LegalItemClickListener());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    private class LegalItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            int pos = position;

            switch (pos) {
                case 0:
                    tracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Action")
                    .setAction("CopyrightAgreement")
                    .build());
                    showDocument(Constants.P2PDINNER_WEB_BASE_URI + getString(R.string.copyRightUri));
                    break;
                case 1:
                    tracker.send(new HitBuilders.EventBuilder()
                            .setCategory("Action")
                            .setAction("PrivacyPolicy")
                            .build());
                    showDocument(Constants.P2PDINNER_WEB_BASE_URI +  getString(R.string.privacyUri));
                    break;
                case 2:
                    tracker.send(new HitBuilders.EventBuilder()
                            .setCategory("Action")
                            .setAction("TermsAndConditions")
                            .build());
                    showDocument(Constants.P2PDINNER_WEB_BASE_URI +  getString(R.string.tcUri));
                    break;
                case 3:
                    clearCache();
                    break;
                default:
            }
        }

        private void showDocument(String legalDocumentUri) {
            Intent intent = new Intent(getApplicationContext(), LegalViewActivity.class);
            intent.putExtra(Constants.LEGAL_LOAD_URI,  legalDocumentUri);
            startActivity(intent);
        }
    }

    private void clearCache() {
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.remove(Constants.ACCESS_TOKEN);
        editor.remove(Constants.USER_ID);
        editor.remove(Constants.EMAIL_ADDRESS);
        editor.remove(Constants.PROFILE_NAME);
        editor.remove(Constants.AUTHENTICATION_PROVIDER);
        editor.commit();
    }


}
