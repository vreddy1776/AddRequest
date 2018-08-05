package project.files.android.addrequest;

import android.content.Intent;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;

import project.files.android.addrequest.MVVM.AddTicket.AddTicketActivity;
import project.files.android.addrequest.MVVM.TicketList.TicketListActivity;
import project.files.android.addrequest.R;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.robolectric.Shadows.shadowOf;


@RunWith(RobolectricTestRunner.class)
public class AddTicketActivityTest {

    @Test
    public void clickingFAB_shouldStartAddTicketActivity() {
        TicketListActivity activity = Robolectric.setupActivity(TicketListActivity.class);
        activity.findViewById(R.id.fab).performClick();

        Intent expectedIntent = new Intent(activity, AddTicketActivity.class);
        Intent actual = shadowOf(RuntimeEnvironment.application).getNextStartedActivity();
        assertEquals(expectedIntent.getComponent(), actual.getComponent());
    }

    @Test
    public void clickingPhotoButton_shouldStartCamera() {
        ActivityController controller = Robolectric.buildActivity(AddTicketActivity.class).create().start();
        //Activity activity = (Activity) controller.get();
        /*
        activity.findViewById(R.id.videoButton).performClick();

        Intent expectedIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        Intent actual = shadowOf(RuntimeEnvironment.application).getNextStartedActivity();
        assertEquals(expectedIntent.getComponent(), actual.getComponent());
        */

    }

}

