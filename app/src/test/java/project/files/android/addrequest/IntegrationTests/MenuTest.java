package project.files.android.addrequest.IntegrationTests;

import android.app.Activity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.fakes.RoboMenu;
import org.robolectric.shadows.ShadowActivity;

import project.files.android.addrequest.BuildConfig;
import project.files.android.addrequest.MVVM.TicketList.TicketListActivity;
import project.files.android.addrequest.R;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;


@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class MenuTest {

    private Activity mActivity;

    @Before
    public void activityCreatedTest() {

        if (mActivity == null) {
            mActivity = Robolectric.buildActivity(TicketListActivity.class).create().visible().get();
        }
    }


    @Test
    public void clickMenuUserName() {

        Toolbar toolbar = mActivity.findViewById(R.id.toolbar);

        //Menu menu = toolbar.getMenu();

        ShadowActivity shadowActivity = shadowOf(mActivity);
        //shadowActivity.onCreateOptionsMenu(toolbar.getMenu());

        Menu menu = new RoboMenu(mActivity.getApplicationContext());
        assertNotNull(menu);

        toolbar.performClick();

        menu.getItem(1);
        //toolbar.performClick();

        //toolbar.inflateMenu(R.menu.main_menu);

        //assertTrue(shadowActivity.getOptionsMenu().hasVisibleItems());
        //assertEquals(shadowActivity.getOptionsMenu().findItem(R.id.user_name_menu).isVisible(), true);

    }


}

