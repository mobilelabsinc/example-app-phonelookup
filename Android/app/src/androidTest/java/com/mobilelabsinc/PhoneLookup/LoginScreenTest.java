package com.mobilelabsinc.PhoneLookup;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isNotChecked;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.AllOf.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class LoginScreenTest {

    @Rule
    public ActivityTestRule<SplashScreen> mActivityTestRule = new ActivityTestRule<>(SplashScreen.class);

    @Test
    public void loginScreenTest() {
        ViewInteraction userNameEdit = onView(withId(R.id.login_username_edit));
        ViewInteraction passwordEdit = onView(withId(R.id.login_password_edit));
        ViewInteraction signInButton = onView(withId(R.id.login_sign_in_button));

        userNameEdit.perform(replaceText("mobilelabs"), closeSoftKeyboard());
        passwordEdit.perform(replaceText("demo"), closeSoftKeyboard());

        signInButton.check(matches(isDisplayed()));

        signInButton.perform(click());

        ViewInteraction itemNameEdit = onView(withId(R.id.search_item_name_edit));
        ViewInteraction manufacturerSelect = onView(withId(R.id.search_manufacturer_spinner));
        ViewInteraction osIOSCheckbox = onView(withId(R.id.search_os_ios_checkbox));
        ViewInteraction osAndroidCheckbox = onView(withId(R.id.search_os_android_checkbox));
        ViewInteraction osBlackBerryCheckbox = onView(withId(R.id.search_os_blackberry_checkbox));
        ViewInteraction osWindowCheckbox = onView(withId(R.id.search_os_windows_checkbox));
        ViewInteraction allRadio = onView(withId(R.id.search_inventory_all_radio_button));
        ViewInteraction searchButton = onView(withId(R.id.search_search_button));

        itemNameEdit.perform(replaceText("Droid Charge"), closeSoftKeyboard());



        manufacturerSelect.perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Samsung"))).perform(click());
        manufacturerSelect.check(matches(withSpinnerText(containsString("Samsung"))));

        osIOSCheckbox.check(matches(isNotChecked())).perform(click());
        //osAndroidCheckbox.check(matches(isNotChecked())).perform(click());
        osBlackBerryCheckbox.check(matches(isNotChecked())).perform(click());
        osWindowCheckbox.check(matches(isNotChecked())).perform(click());

        //allRadio.perform(click());

        searchButton.check(matches(isDisplayed()));

        searchButton.perform(click());
    }
}
