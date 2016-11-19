package com.androidessence.cashcaretaker;

import android.app.Activity;
import android.content.Context;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.FailureHandler;
import android.support.test.espresso.base.DefaultFailureHandler;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.view.View;

import com.jraska.falcon.FalconSpoon;

import org.hamcrest.Matcher;

import java.util.Collection;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.runner.lifecycle.Stage.RESUMED;

/**
 * Created by adam.mcneilly on 11/19/16.
 */
public class TestUtils {
    public static void setFailureHandler(final Context context) {
        Espresso.setFailureHandler(new FailureHandler() {
            @Override
            public void handle(Throwable error, Matcher<View> viewMatcher) {
                takeScreenshot("test_failed");
                new DefaultFailureHandler(context).handle(error, viewMatcher);
            }
        });
    }

    public static void takeScreenshot(String description) {
        FalconSpoon.screenshot(getCurrentActivityInstance(), description);
    }

    private static Activity getCurrentActivityInstance() {
        final Activity[] activity = new Activity[1];
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                Collection resumedActivities = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(RESUMED);
                if(resumedActivities.iterator().hasNext()) {
                    Activity currentActivity = (Activity) resumedActivities.iterator().next();
                    activity[0] = currentActivity;
                }
            }
        });

        return activity[0];
    }
}
