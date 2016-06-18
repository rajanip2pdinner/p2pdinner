package com.p2pdinner.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.p2pdinner.P2PDinnerApplication;

/**
 * Created by rajaniy on 5/21/16.
 */
public abstract class BaseAppCompatActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((P2PDinnerApplication)getApplication()).inject(this);
    }

}
