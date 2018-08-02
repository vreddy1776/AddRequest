package project.files.android.addrequest.IntegrationTests;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.fakes.RoboMenu;
import org.robolectric.fakes.RoboMenuItem;

import project.files.android.addrequest.BuildConfig;
import project.files.android.addrequest.MVVM.TicketList.TicketListActivity;
import project.files.android.addrequest.R;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;



@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class TicketListActivityTest {

    @Test
    public void menuTest() {

        Activity activity = Robolectric.buildActivity(TicketListActivity.class).create().visible().get();
        assertNotNull(activity);

        ImageView fab = activity.findViewById(R.id.fab);
        assertNotNull(fab);

        //final Menu menu = shadowOf(activity).getOptionsMenu();
        //assertNotNull(menu);


        //MenuItem menuItem = new RoboMenuItem(R.id.user_name_menu);
        //assertNotNull(menuItem);

        //CharSequence menuTitle = menuItem.getTitle();
        //assertNotNull(menuTitle);


        //assertThat(menuItem.getTitle()).isEqualTo("Sign Out");

        //final Menu menu = shadowOf(activity).getOptionsMenu();
        //assertThat(menu.findItem(R.id.sign_out_menu).getTitle()).isEqualTo("First menu item");
    }

}

