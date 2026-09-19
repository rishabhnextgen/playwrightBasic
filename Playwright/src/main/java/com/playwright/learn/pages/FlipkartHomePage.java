package com.playwright.learn.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.playwright.learn.config.ConfigReader;

/**
 * Flipkart home page: close login popup, type in search, press Enter.
 */
public class FlipkartHomePage extends BasePage {

    public FlipkartHomePage(Page page) {
        super(page);
    }

    public FlipkartHomePage openHomePage() {
        open(ConfigReader.get("flipkartUrl"));
        closeLoginPopupIfShown();
        return this;
    }

    public void closeLoginPopupIfShown() {
        Locator closeButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("✕"));
        try {
            closeButton.first().click(new Locator.ClickOptions().setTimeout(5000));
        } catch (Exception ignored) {
            page.keyboard().press("Escape");
        }
    }

    public void searchFor(String productName) {
        Locator searchBox = page.locator("input[name='q']").first();
        searchBox.waitFor();
        searchBox.click();
        searchBox.fill(productName);
        searchBox.press("Enter");
    }

    public boolean resultsContain(String productName) {
        page.waitForURL(url -> url.contains("search"), new Page.WaitForURLOptions().setTimeout(20000));
        String url = page.url().toLowerCase();
        boolean urlLooksRight = url.contains("search")
                && url.contains(productName.toLowerCase().split(" ")[0]);

        String pattern = productName.replace(" ", "\\s*");
        Locator resultText = page.locator("text=/" + pattern + "/i").first();
        return urlLooksRight && resultText.isVisible();
    }
}
