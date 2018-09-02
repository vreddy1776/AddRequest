package project.files.android.addrequest;

import com.mauriciotogneri.greencoffee.GreenCoffeeSteps;
import com.mauriciotogneri.greencoffee.annotations.Given;
import com.mauriciotogneri.greencoffee.annotations.Then;
import com.mauriciotogneri.greencoffee.annotations.When;

import project.files.android.addrequest.Utils.StringUtils;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


public class GC_AddTicketSteps extends GreenCoffeeSteps
{

    String newTitle = StringUtils.randomTitle();
    String newDescription = StringUtils.randomDescription();

    @Given("^I see an empty login form$")
    public void iSeeAnEmptyLoginForm()
    {
        onView(withText(newDescription)).check(doesNotExist());
    }

    @When("^I introduce an invalid username$")
    public void iIntroduceAnInvalidUsername()
    {
        onView(withId(R.id.fab)).perform(click());
    }

    @When("^I introduce an invalid password$")
    public void iIntroduceAnInvalidPassword()
    {
        //onView(withId(R.id.fab)).perform(click());
    }

    @When("^I press the login button$")
    public void iPressTheLoginButton()
    {
        //onView(withId(R.id.fab)).perform(click());
    }

    @Then("^I see an error message saying 'Invalid credentials'$")
    public void iSeeAnErrorMessageSayingInvalidCredentials()
    {
        //onView(withId(R.id.fab)).perform(click());
    }
}
