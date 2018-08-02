package project.files.android.addrequest.IntegrationTests;

import android.app.Activity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import project.files.android.addrequest.BuildConfig;
import project.files.android.addrequest.MVVM.TicketList.TicketListActivity;

import static org.junit.Assert.assertNotNull;


@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class TicketListActivityTest {

    @Test
    public void onCreateShouldInflateTheMenu() {
        Activity activity = Robolectric.buildActivity(TicketListActivity.class).create().get();
        assertNotNull(activity);
    }


}

