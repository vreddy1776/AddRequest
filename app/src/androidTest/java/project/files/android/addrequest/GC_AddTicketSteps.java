package project.files.android.addrequest;

import com.mauriciotogneri.greencoffee.GreenCoffeeSteps;
import com.mauriciotogneri.greencoffee.annotations.Given;
import com.mauriciotogneri.greencoffee.annotations.Then;
import com.mauriciotogneri.greencoffee.annotations.When;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class GC_AddTicketSteps extends GreenCoffeeSteps
{
    @Given("^I see an empty login form$")
    public void iSeeAnEmptyLoginForm()
    {
        onView(withId(R.id.fab)).perform(click());

    }

    @When("^I introduce an invalid username$")
    public void iIntroduceAnInvalidUsername()
    {
        onView(withId(R.id.fab)).perform(click());
    }

    @When("^I introduce an invalid password$")
    public void iIntroduceAnInvalidPassword()
    {
        onView(withId(R.id.fab)).perform(click());
    }

    @When("^I press the login button$")
    public void iPressTheLoginButton()
    {
        onView(withId(R.id.fab)).perform(click());
    }

    @Then("^I see an error message saying 'Invalid credentials'$")
    public void iSeeAnErrorMessageSayingInvalidCredentials()
    {
        onView(withId(R.id.fab)).perform(click());
    }
}
