package project.files.android.addrequest;

import com.mauriciotogneri.greencoffee.GreenCoffeeSteps;
import com.mauriciotogneri.greencoffee.annotations.Given;
import com.mauriciotogneri.greencoffee.annotations.Then;
import com.mauriciotogneri.greencoffee.annotations.When;

import project.files.android.addrequest.Utils.StringUtils;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


public class GC_AddTicketSteps extends GreenCoffeeSteps
{

    String newTitle = StringUtils.randomTitle();
    String newDescription = StringUtils.randomDescription();

    @Given("^I do not see ticket text and description$")
    public void iSeeAnEmptyLoginForm()
    {
        onView(withText(newTitle)).check(doesNotExist());
        onView(withText(newDescription)).check(doesNotExist());
    }

    @When("^I click on add ticket button$")
    public void iIntroduceAnInvalidUsername()
    {
        onView(withId(R.id.fab)).perform(click());
    }

    @When("^I enter a ticket title$")
    public void iIntroduceAnInvalidPassword()
    {
        onView(withId(R.id.editTextTicketTitle)).
                perform(typeText(newTitle), closeSoftKeyboard());
    }

    @When("^I enter a ticket description$")
    public void iPressTheLoginButton()
    {
        onView(withId(R.id.editTextTicketDescription)).
                perform(typeText(newDescription), closeSoftKeyboard());
    }

    @When("^I click on submit button$")
    public void iPressTheSubmitButton()
    {
        onView(withId(R.id.saveButton)).
                perform(click());
    }

    @Then("^I see ticket text and description$")
    public void iSeeAnErrorMessageSayingInvalidCredentials()
    {
        onView(withId(R.id.recyclerViewTickets)).
                perform(scrollTo(hasDescendant(withText(newDescription))));
        onView(withText(newDescription)).check(matches(isDisplayed()));
    }
}
