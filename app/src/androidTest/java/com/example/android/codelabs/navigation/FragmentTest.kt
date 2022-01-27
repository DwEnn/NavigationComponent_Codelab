package com.example.android.codelabs.navigation

import android.os.Bundle
import androidx.fragment.app.testing.launchFragment
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.junit.jupiter.api.Test

class FragmentTest {

    @Test
    fun testEventFragment() {
        // The "fragmentArgs" and "factory" arguments are optional.
        val fragmentArgs = Bundle().apply {
            putInt("numElements", 0)
        }
        val scenario = launchFragmentInContainer<HomeFragment>(fragmentArgs)
        onView(withId(R.id.text)).check(ViewAssertions.matches(withText("Home")))
//        onView(withId(R.id.navigate_destination_button)).perform(ViewActions.click())

        `homeFragment 메뉴 선택`()
    }

    private fun `homeFragment 메뉴 선택`() {
        val scenario = launchFragmentInContainer<HomeFragment>()
        scenario.onFragment {
            it.onDestroy()
        }
    }

    @Test
    private fun `Dilaog 테스트`() {
        // Assumes that "MyDialogFragment" extends the DialogFragment class.
        with(launchFragment<HomeFragment>()) {
//            onFragment { fragment ->
//                assertThat(fragment.dialog).isNotNull()
//                assertThat(fragment.requireDialog().isShowing).isTrue()
//                fragment.dismiss()
//                fragment.requireFragmentManager().executePendingTransactions()
//                assertThat(fragment.dialog).isNull()
//            }

            // Assumes that the dialog had a button
            // containing the text "Cancel".
            onView(withText("Cancel")).check(ViewAssertions.doesNotExist())
        }
    }
}