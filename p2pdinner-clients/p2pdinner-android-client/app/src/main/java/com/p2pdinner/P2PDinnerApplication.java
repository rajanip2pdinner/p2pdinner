package com.p2pdinner;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import com.google.android.gms.analytics.Tracker;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import dagger.ObjectGraph;

/**
 * Created by rajaniy on 10/21/15.
 */
public class P2PDinnerApplication extends Application {

    private static final String TAG = "P2PDINNER";
    private ObjectGraph objectGraph;

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.p2pdinner",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("P2PDINNER", "KeyHash: ---- " + Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
            objectGraph = ObjectGraph.create(getModules().toArray());
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

    }

    protected List getModules() {
        return Arrays.asList(
                new P2PDinnerApplicationModule(this)
        );
    }

    public void inject(Object object) {
        objectGraph.inject(object);
    }
}
