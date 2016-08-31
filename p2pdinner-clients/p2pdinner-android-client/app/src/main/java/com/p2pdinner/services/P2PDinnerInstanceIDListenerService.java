package com.p2pdinner.services;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by rajaniy on 1/4/16.
 */
public class P2PDinnerInstanceIDListenerService extends InstanceIDListenerService {
    private static final String TAG = "P2PDinnerInstanceIDLS";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. This call is initiated by the
     * InstanceID provider.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Fetch updated Instance ID token and notify our app's server of any changes (if applicable).
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId) {
        Intent registrationIntent = new Intent(this, RegistrationIntentService.class);
        startService(registrationIntent);
        return startId;
    }
    // [END refresh_token]
}
