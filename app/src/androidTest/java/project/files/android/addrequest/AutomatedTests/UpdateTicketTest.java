package project.files.android.addrequest.AutomatedTests;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import project.files.android.addrequest.MVVM.TicketList.TicketListActivity;
import project.files.android.addrequest.R;
import project.files.android.addrequest.Utils.StringGenerator;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollTo;
import static android.support.test.espresso.core.internal.deps.guava.base.Preconditions.checkArgument;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class UpdateTicketTest {


    private Matcher<View> withItemText(final String itemText) {
        checkArgument(!TextUtils.isEmpty(itemText), "itemText cannot be null or empty");
        return new TypeSafeMatcher<View>() {
            @Override
            public boolean matchesSafely(View item) {
                return allOf(
                        isDescendantOfA(isAssignableFrom(RecyclerView.class)),
                        withText(itemText)).matches(item);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("is isDescendantOfA RV with text " + itemText);
            }
        };
    }


    @Rule
    public IntentsTestRule<TicketListActivity> mActivityRule = new IntentsTestRule<>(
            TicketListActivity.class);


    @Before
    public void clickItem_opensAddTicketUi(){

        try {
            onView(withId(R.id.user_name_menu)).perform(click());
        } catch (NoMatchingViewException e) {
            openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
            onView(withText(R.string.my_requests)).perform(click());
        }

        onView(withId(R.id.recyclerViewTickets)).perform
                (RecyclerViewActions.actionOnItemAtPosition(2, click()));
    }


    @Test
    public void updateTicketOnTicketList() throws Exception {

        String newTitle = StringGenerator.randomTitle();
        String newDescription = StringGenerator.randomDescription();

        onView(withId(R.id.editTextTicketTitle)).perform(clearText(), typeText(newTitle), closeSoftKeyboard());
        onView(withId(R.id.editTextTicketDescription)).perform(clearText(), typeText(newDescription),
                closeSoftKeyboard());
        onView(withId(R.id.saveButton)).perform(click());

        onView(withId(R.id.recyclerViewTickets)).perform(
                scrollTo(hasDescendant(withText(newDescription))));
        onView(withItemText(newDescription)).check(matches(isDisplayed()));

    }

}
