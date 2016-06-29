package com.p2pdinner.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.p2pdinner.R;
import com.p2pdinner.activities.FacebookLoginActivity;
import com.p2pdinner.common.Constants;
import com.p2pdinner.entities.UserProfile;
import com.p2pdinner.restclient.UserProfileManager;

import org.json.JSONObject;

import java.util.Arrays;

import javax.inject.Inject;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 * A placeholder fragment containing a simple view.
 */
public class FacebookLoginActivityFragment extends BaseFragment {

    private TextView txtView;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private ProfileTracker profileTracker;
    private AccessTokenTracker accessTokenTracker;
    private SharedPreferences sharedPreferences;
    private Handler handler;

    @Inject
    UserProfileManager userProfileManager;

    private static final String TAG = FacebookLoginActivity.class.getName();

    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {

        @Override
        public void onSuccess(LoginResult loginResult) {
            final AccessToken accessToken = loginResult.getAccessToken();
            GraphRequest graphrequest = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {
                    Log.i(TAG, "Request completed successfully ----- " + object.toString());
                    sharedPreferences = getActivity().getSharedPreferences(Constants.PREFS_PRIVATE, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    try {
                        editor.putString(Constants.ACCESS_TOKEN, accessToken.getToken());
                        editor.putString(Constants.USER_ID, object.getString("id"));
                        final String email = object.getString("email");
                        editor.putString(Constants.EMAIL_ADDRESS, object.getString("id"));
                        editor.putString(Constants.PROFILE_NAME, object.getString("name"));
                        editor.putString(Constants.AUTHENTICATION_PROVIDER, "facebook");

                        userProfileManager.validateProfile(email, accessToken.getToken())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .doOnError(new Action1<Throwable>() {
                                    @Override
                                    public void call(Throwable throwable) {
                                        final UserProfile userProfile = new UserProfile();
                                        userProfile.setEmailAddress(sharedPreferences.getString(Constants.EMAIL_ADDRESS, ""));
                                        userProfile.setPassword(sharedPreferences.getString(Constants.ACCESS_TOKEN, ""));
                                        userProfile.setName(sharedPreferences.getString(Constants.PROFILE_NAME, ""));
                                        userProfile.setAuthenticationProvider(sharedPreferences.getString(Constants.AUTHENTICATION_PROVIDER, ""));
                                        userProfileManager.createProfile(userProfile)
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribeOn(Schedulers.newThread())
                                                .subscribe(new Observer<Void>() {
                                                    @Override
                                                    public void onCompleted() {
                                                        getActivity().finish();
                                                    }

                                                    @Override
                                                    public void onError(Throwable e) {
                                                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }

                                                    @Override
                                                    public void onNext(Void aVoid) {

                                                    }
                                                });
                                    }
                                })
                                .subscribe(new Observer<UserProfile>() {
                                    @Override
                                    public void onCompleted() {
                                        getActivity().finish();
                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onNext(UserProfile userProfile) {

                                    }
                                });

                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                    } finally {
                        editor.commit();
                    }
                }
            });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,link,email");
            graphrequest.setParameters(parameters);
            graphrequest.executeAsync();
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {

        }
    };

    public FacebookLoginActivityFragment() {
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtView = (TextView) view.findViewById(R.id.login_message);
        loginButton = (LoginButton) view.findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile, email"));
        loginButton.setFragment(this);
        loginButton.registerCallback(callbackManager, callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_facebook_login, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());

        callbackManager = CallbackManager.Factory.create();
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                if (newProfile != null) {
                    sharedPreferences = getActivity().getSharedPreferences(Constants.PREFS_PRIVATE, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(Constants.EMAIL_ADDRESS, newProfile.getId());
                    editor.commit();
                }
            }
        };
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken newAccessToken) {
                if (newAccessToken != null) {
                    updatePreferences(newAccessToken);
                }
            }
        };
        profileTracker.startTracking();
        accessTokenTracker.startTracking();
    }

    @Override
    public void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
    }

    @Override
    public void onStop() {
        super.onStop();
        profileTracker.stopTracking();
        accessTokenTracker.startTracking();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void updatePreferences(AccessToken accessToken) {
        sharedPreferences = getActivity().getSharedPreferences(Constants.PREFS_PRIVATE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        try {
            editor.putString(Constants.ACCESS_TOKEN, accessToken.getToken());
            //editor.putLong(Constants.PROFILE_ID, Long.valueOf(accessToken.getUserId()));
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        } finally {
            editor.commit();
        }
    }

}
