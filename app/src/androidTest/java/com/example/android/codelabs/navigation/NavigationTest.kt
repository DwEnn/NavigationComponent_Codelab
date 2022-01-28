package com.example.android.codelabs.navigation

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import org.junit.jupiter.api.Test

class NavigationTest {

    @Test
    fun `HomeFragment next_action dest 이동`() {
        // Create a TestNavHostController
        val navController = TestNavHostController(
            ApplicationProvider.getApplicationContext()
        )

        // Create a graphical FragmentScenario for the TitleScreen
        with(launchFragmentInContainer<HomeFragment>()) {
            onFragment {
                // Set the graph on the TestNavHostController
                navController.setGraph(R.navigation.mobile_navigation)

                // Make the NavController available via the findNavController() APIs
                Navigation.setViewNavController(it.requireView(), navController)
            }
        }

        Espresso.onView(ViewMatchers.withId(R.id.navigate_destination_button)).perform(ViewActions.click())
        assert(navController.currentDestination?.id == R.id.flow_step_one_dest)
    }

    @Test
    fun `HomeFragment NavigationUI 테스트`() {
        val navController = TestNavHostController(
            ApplicationProvider.getApplicationContext()
        )

        val scenario = launchFragmentInContainer {
            HomeFragment().also {
                // In addition to returning a new instance of our Fragment,
                // get a callback whenever the fragment's view is created
                // or destroyed so that we can set the NavController
                it.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                    if (viewLifecycleOwner != null) {
                        // The fragment's view has just been created
                        navController.setGraph(R.navigation.mobile_navigation)
                        Navigation.setViewNavController(it.requireView(), navController)
                    }
                }
            }
        }

        Espresso.onView(ViewMatchers.withId(R.id.navigate_destination_button)).perform(ViewActions.click())
        assert(navController.currentDestination?.id == R.id.flow_step_one_dest)
    }
}