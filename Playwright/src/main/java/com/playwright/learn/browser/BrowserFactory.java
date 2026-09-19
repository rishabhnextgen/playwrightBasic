package com.playwright.learn.browser;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;
import com.playwright.learn.config.ConfigReader;

/**
 * Creates a Playwright browser from config.
 *
 * Playwright objects you will see a lot:
 * - Playwright: the engine
 * - Browser: Chromium / Firefox / WebKit
 * - BrowserContext: a clean isolated session (like incognito)
 * - Page: one tab
 */
public class BrowserFactory {

    public static Browser createBrowser(Playwright playwright) {
        String browserName = ConfigReader.get("browser").toLowerCase();
        boolean headless = ConfigReader.getBoolean("headless");
        double slowMo = ConfigReader.getDouble("slowMo");

        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions()
                .setHeadless(headless)
                .setSlowMo(slowMo);

        return switch (browserName) {
            case "firefox" -> playwright.firefox().launch(options);
            case "webkit" -> playwright.webkit().launch(options);
            default -> playwright.chromium().launch(options);
        };
    }
}
