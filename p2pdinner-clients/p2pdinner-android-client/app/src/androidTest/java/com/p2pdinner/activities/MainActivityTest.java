package com.p2pdinner.activities;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.ListView;


import com.robotium.solo.Solo;

import java.util.List;

/**
 * Created by rajaniy on 1/31/16.
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private Solo solo;

    public MainActivityTest(){
        super(MainActivity.class);
    }

    public void testMainOptions() throws Exception {
        List<ListView> listView = solo.getCurrentViews(ListView.class);
        assert(!listView.isEmpty());
        assert(listView.get(0).getAdapter().getCount() > 0);
    }

    public void testIHaveDinner() throws Exception {
        solo.clickInList(0);
    }

    @Override
    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
        //solo.clickOnButton("Get Google Services");
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}
