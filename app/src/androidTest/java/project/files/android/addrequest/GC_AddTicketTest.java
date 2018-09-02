package project.files.android.addrequest;

import android.support.test.rule.ActivityTestRule;

import com.mauriciotogneri.greencoffee.GreenCoffeeConfig;
import com.mauriciotogneri.greencoffee.GreenCoffeeTest;
import com.mauriciotogneri.greencoffee.ScenarioConfig;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.Locale;

import project.files.android.addrequest.Activity.TicketList.TicketListActivity;

@RunWith(Parameterized.class)
public class GC_AddTicketTest extends GreenCoffeeTest
{
    @Rule
    public ActivityTestRule<TicketListActivity> activity = new ActivityTestRule<>(TicketListActivity.class);

    public GC_AddTicketTest(ScenarioConfig scenarioConfig)
    {
        super(scenarioConfig);
    }

    @Parameterized.Parameters(name = "{0}")
    public static Iterable<ScenarioConfig> scenarios() throws IOException
    {
        return new GreenCoffeeConfig()
                .withFeatureFromAssets("assets/addticket.feature")
                .takeScreenshotOnFail()
                .scenarios(
                        new Locale("en", "GB"),
                        new Locale("es", "ES")
                ); // the locales used to run the scenarios (optional)
    }

    @Test
    public void test()
    {
        start(new GC_AddTicketSteps());
    }
}
