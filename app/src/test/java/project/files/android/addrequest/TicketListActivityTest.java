package project.files.android.addrequest;

import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;
import android.widget.ImageView;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.fakes.RoboMenuItem;

import project.files.android.addrequest.BuildConfig;
import project.files.android.addrequest.MVVM.AddTicket.AddTicketActivity;
import project.files.android.addrequest.MVVM.TicketList.TicketListActivity;
import project.files.android.addrequest.R;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.robolectric.Shadows.shadowOf;



@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class TicketListActivityTest {


    @Test
    public void activityCreatedTest() {

        Activity activity = Robolectric.buildActivity(TicketListActivity.class).create().visible().get();
        assertNotNull(activity);

        ImageView fab = activity.findViewById(R.id.fab);
        assertNotNull(fab);

        MenuItem menuItem = new RoboMenuItem(R.id.user_name_menu);
        assertNotNull(menuItem);

    }


    @Test
    public void fabClickTest() {
        Activity activity = Robolectric.setupActivity(TicketListActivity.class);
        activity.findViewById(R.id.fab).performClick();

        Intent expectedIntent = new Intent(activity, AddTicketActivity.class);
        Intent actual = shadowOf(RuntimeEnvironment.application).getNextStartedActivity();
        assertEquals(expectedIntent.getComponent(), actual.getComponent());
    }


}

