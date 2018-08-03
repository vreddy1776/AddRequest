package project.files.android.addrequest.IntegrationTests;

import android.app.Activity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;

import project.files.android.addrequest.MVVM.AddTicket.AddTicketActivity;
import project.files.android.addrequest.MVVM.Chat.ChatActivity;


@RunWith(RobolectricTestRunner.class)
public class ChatActivityTest {

    @Test
    public void menuTest(){

        //Activity activity = Robolectric.buildActivity(ChatActivity.class).create().get();

        ActivityController controller = Robolectric.buildActivity(ChatActivity.class).create().start();
        //Activity activity = (Activity) controller.get();
        //activity.onsa
        //Activity activity = Robolectric.setupActivity(AddTicketActivity.class);
        //assertNotNull(shadowOf(activity));
        //assertNotNull(activity);



        //ImageView fab = activity.findViewById(R.id.fab);
        //assertNotNull(fab);

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

