package com.playwright.learn.pages;

import com.microsoft.playwright.Page;

/**
 * Shared actions every page can use: go to a URL, read the title, click, type.
 * Page Object Model = locators and actions live here, not inside the test.
 */
public class BasePage {

    protected final Page page;

    public BasePage(Page page) {
        this.page = page;
    }

    public void open(String url) {
        page.navigate(url);
    }

    public String getTitle() {
        return page.title();
    }

    public void click(String selector) {
        page.locator(selector).click();
    }

    public void type(String selector, String text) {
        page.locator(selector).fill(text);
    }
}
