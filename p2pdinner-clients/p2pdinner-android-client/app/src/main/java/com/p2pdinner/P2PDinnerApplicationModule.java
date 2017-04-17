package com.p2pdinner;

/**
 * Created by rajaniy on 5/21/16.
 */

import android.content.Context;
import android.location.Criteria;
import android.location.LocationManager;
import android.provider.ContactsContract;
import android.view.ViewDebug;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.GsonBuilder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.p2pdinner.activities.CreateDinnerActivity;
import com.p2pdinner.activities.DinnerListingActivity;
import com.p2pdinner.activities.DinnerListingDetailActivity;
import com.p2pdinner.activities.FindDinnerActivity;
import com.p2pdinner.activities.ListDinnerActivity;
import com.p2pdinner.activities.MainActivity;
import com.p2pdinner.activities.SellerListingDetailActivity;
import com.p2pdinner.common.Constants;
import com.p2pdinner.deserializers.DinnerMenuItemDeserializer;
import com.p2pdinner.entities.DinnerMenuItem;
import com.p2pdinner.fragments.CostFragment;
import com.p2pdinner.fragments.FacebookLoginActivityFragment;
import com.p2pdinner.fragments.FoodItemFragment;
import com.p2pdinner.fragments.HaveDinnerFragment;
import com.p2pdinner.fragments.PhotosFragment;
import com.p2pdinner.fragments.PlaceFragment;
import com.p2pdinner.fragments.SplNeedsFragment;
import com.p2pdinner.fragments.TimeFragment;
import com.p2pdinner.fragments.WantDinnerFragment;
import com.p2pdinner.restclient.DeviceManager;
import com.p2pdinner.restclient.DinnerCartManager;
import com.p2pdinner.restclient.DinnerListingManager;
import com.p2pdinner.restclient.GoogleApiService;
import com.p2pdinner.restclient.MenuServiceManager;
import com.p2pdinner.restclient.P2PDinnerAuthenticationInterceptor;
import com.p2pdinner.restclient.PlacesServiceManager;
import com.p2pdinner.restclient.UserProfileManager;
import com.p2pdinner.services.P2PDinnerOAuthTokenRefreshService;
import com.p2pdinner.services.RegistrationIntentService;

import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = {
                MainActivity.class,
                FindDinnerActivity.class,
                PlaceFragment.class,
                CreateDinnerActivity.class,
                FoodItemFragment.class,
                PhotosFragment.class,
                CostFragment.class,
                MenuServiceManager.class,
                TimeFragment.class,
                SplNeedsFragment.class,
                DinnerListingManager.class,
                HaveDinnerFragment.class,
                WantDinnerFragment.class,
                FacebookLoginActivityFragment.class,
                DinnerListingDetailActivity.class,
                SellerListingDetailActivity.class,
                RegistrationIntentService.class,
                DinnerListingActivity.class,
                ListDinnerActivity.class
        },
        complete = false,
        library = true
)
public class P2PDinnerApplicationModule {

    private final P2PDinnerApplication p2PDinnerApplication;

    public P2PDinnerApplicationModule(P2PDinnerApplication p2PDinnerApplication) {
        this.p2PDinnerApplication = p2PDinnerApplication;
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return p2PDinnerApplication;
    }

    @Provides
    @Singleton
    RestTemplate restTemplate(Context context,P2PDinnerOAuthTokenRefreshService pDinnerOAuthTokenRefreshService) {
        RestTemplate restTemplate = new RestTemplate();
        List<ClientHttpRequestInterceptor> interceptorList = new ArrayList<>();
        interceptorList.add(new P2PDinnerAuthenticationInterceptor(pDinnerOAuthTokenRefreshService));
        restTemplate.setInterceptors(interceptorList);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(DinnerMenuItem.class, new DinnerMenuItemDeserializer());
        GsonHttpMessageConverter jsonConverter = new GsonHttpMessageConverter();
        jsonConverter.setGson(gsonBuilder.create());
        final List<HttpMessageConverter<?>> listHttpMessageConverters = restTemplate.getMessageConverters();
        listHttpMessageConverters.add(jsonConverter);
        restTemplate.setMessageConverters(listHttpMessageConverters);
        return restTemplate;
    }


    @Provides
    @Singleton
    LocationManager provideLocationManager() {
        return (LocationManager) p2PDinnerApplication.getSystemService(Context.LOCATION_SERVICE);
    }

    @Provides
    @Singleton
    GoogleApiService googleApiService() {
        return new GoogleApiService();
    }

    @Provides
    @Singleton
    MenuServiceManager menuServiceManager(RestTemplate restTemplate) {
        return new MenuServiceManager(restTemplate);
    }

    @Provides
    @Singleton
    DinnerListingManager dinnerListingManager(RestTemplate restTemplate) {
        return new DinnerListingManager(restTemplate);
    }

    @Provides
    @Singleton
    UserProfileManager userProfileManager(RestTemplate restTemplate, Context context) {
        return new UserProfileManager(restTemplate, context);
    }

    @Provides
    @Singleton
    DinnerCartManager dinnerCartManager(RestTemplate restTemplate) {
        return new DinnerCartManager(restTemplate);
    }

    @Provides
    @Singleton
    PlacesServiceManager placesServiceManager(RestTemplate restTemplate) {
        return new PlacesServiceManager(restTemplate);
    }

    @Provides
    @Singleton
    DeviceManager deviceManager(RestTemplate restTemplate) {
        return new DeviceManager(restTemplate);
    }

    @Provides
    @Singleton
    P2PDinnerOAuthTokenRefreshService p2PDinnerOAuthTokenRefreshService(Context context) {
        RestTemplate oauthRequestTemplate = new RestTemplate();
        return new P2PDinnerOAuthTokenRefreshService(context, oauthRequestTemplate);
    }

    @Provides
    @Singleton
    ImageLoader imageLoader() {
        ImageLoaderConfiguration imageLoaderConfiguration = new ImageLoaderConfiguration.Builder(p2PDinnerApplication.getApplicationContext())
                .build();
        ImageLoader.getInstance().init(imageLoaderConfiguration);
        return ImageLoader.getInstance();
    }

    @Provides
    @Singleton
    Tracker tracker() {
        GoogleAnalytics googleAnalytics = GoogleAnalytics.getInstance(p2PDinnerApplication.getApplicationContext());
        Tracker tracker = googleAnalytics.newTracker(R.xml.global_tracker);
        return tracker;
    }


}
