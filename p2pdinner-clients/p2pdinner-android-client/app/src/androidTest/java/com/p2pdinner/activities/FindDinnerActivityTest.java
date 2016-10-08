package com.p2pdinner.activities;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.p2pdinner.R;
import com.robotium.solo.Solo;

import java.util.List;

/**
 * Created by rajaniy on 7/21/16.
 */

public class FindDinnerActivityTest extends ActivityInstrumentationTestCase2<FindDinnerActivity> {
    private Solo solo;

    public FindDinnerActivityTest() {
        super(FindDinnerActivity.class);
    }

    public void testFindDinner() throws Exception {
        solo.enterText(0, "5338 Piazza Ct, Pleasanton, CA");
        solo.clickOnText("Find Dinner");
        solo.waitForActivity(DinnerListingActivity.class);
        ListView view = (ListView) solo.getCurrentActivity().findViewById(R.id.searchResultsView);
        assertNotNull(view);
        //assertTrue(view.getAdapter().getCount() == 0);
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
