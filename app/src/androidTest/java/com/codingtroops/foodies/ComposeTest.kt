package com.codingtroops.foodies

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.codingtroops.foodies.model.FoodItem
import com.codingtroops.foodies.ui.feature.categories.FoodCategoriesContract
import com.codingtroops.foodies.ui.feature.categories.FoodCategoriesScreen
import com.codingtroops.foodies.ui.theme.ComposeSampleTheme
import org.junit.Rule
import org.junit.Test


class ComposeTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    // use createAndroidComposeRule<YourActivity>() if you need access to
    // an activity

    @Test
    fun MyTest() {
        // Start the app
        composeTestRule.setContent {
            ComposeSampleTheme {
                FoodCategoriesScreen(
                    state = FoodCategoriesContract.State(
                        listOf(
                            FoodItem(
                                "100",
                                "Fake item",
                                "",
                                ""
                            )
                        ),
                        isLoading = true
                    ),
                    null,
                    { },
                    { }
                )
            }
        }

        composeTestRule.onNodeWithText("Fake item").assertIsDisplayed()
        composeTestRule.onNodeWithTag("loading_tag").assertIsDisplayed()
    }
}