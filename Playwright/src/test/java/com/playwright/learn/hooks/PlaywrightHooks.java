package com.playwright.learn.hooks;

import com.playwright.learn.browser.PlaywrightManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;

/**
 * Cucumber Hooks = @Before / @After around each scenario.
 * Same idea as TestNG @BeforeMethod / @AfterMethod.
 */
public class PlaywrightHooks {

    @Before
    public void beforeScenario() {
        PlaywrightManager.start();
    }

    @After
    public void afterScenario() {
        PlaywrightManager.stop();
    }
}
