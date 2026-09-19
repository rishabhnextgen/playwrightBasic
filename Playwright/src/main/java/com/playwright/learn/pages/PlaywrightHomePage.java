package com.playwright.learn.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.playwright.learn.config.ConfigReader;

/**
 * Locators and actions for https://playwright.dev
 *
 * getByRole is Playwright's recommended way to find elements:
 * it looks for what a user sees (a link named "Get started"), not a CSS class.
 */
public class PlaywrightHomePage extends BasePage {

    public PlaywrightHomePage(Page page) {
        super(page);
    }

    public PlaywrightHomePage openHomePage() {
        open(ConfigReader.get("baseUrl"));
        return this;
    }

    private Locator getStartedLink() {
        return page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Get started"));
    }

    public boolean isGetStartedVisible() {
        return getStartedLink().isVisible();
    }

    public void clickGetStarted() {
        getStartedLink().click();
    }
}
