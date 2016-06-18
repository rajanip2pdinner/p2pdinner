package com.p2pdinner.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.p2pdinner.P2PDinnerApplication;

/**
 * Created by rajaniy on 5/21/16.
 */
public abstract class BaseFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((P2PDinnerApplication)this.getActivity().getApplication()).inject(this);
    }
}
