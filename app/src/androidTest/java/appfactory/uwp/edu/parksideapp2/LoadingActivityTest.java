package appfactory.uwp.edu.parksideapp2;

import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class LoadingActivityTest {
    @Rule
    public ActivityTestRule<LoadingActivity> loadingActivityActivityTestRule = new ActivityTestRule<LoadingActivity>(LoadingActivity.class);

    // Activity
    private LoadingActivity loadingActivity;
    @Before
    public void setUp() throws Exception {
        loadingActivity = loadingActivityActivityTestRule.getActivity();
    }

    @After
    public void tearDown() throws Exception {
        loadingActivity = null;
    }


    /**
     * Test if UI component is null or not
     */
    @Test
    public void onCreate() {
        assertNotNull(loadingActivity.findViewById(R.id.loading_image));
    }
}