package com.playwright.learn.browser;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.PlaywrightException;
import com.playwright.learn.config.ConfigReader;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * One Playwright session per test thread.
 * Cucumber Hooks and TestNG BaseTest both use this class.
 */
public class PlaywrightManager {

    private static final ThreadLocal<Playwright> PLAYWRIGHT = new ThreadLocal<>();
    private static final ThreadLocal<Browser> BROWSER = new ThreadLocal<>();
    private static final ThreadLocal<BrowserContext> CONTEXT = new ThreadLocal<>();
    private static final ThreadLocal<Page> PAGE = new ThreadLocal<>();

    public static void start() {
        if (PAGE.get() != null) {
            return;
        }

        Playwright playwright = createPlaywrightWithRetry();
        Browser browser = BrowserFactory.createBrowser(playwright);
        BrowserContext context = browser.newContext(new Browser.NewContextOptions()
                .setViewportSize(1366, 768)
                .setLocale("en-IN")
                .setTimezoneId("Asia/Kolkata")
                .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
                        + "(KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36"));
        Page page = context.newPage();
        page.setDefaultTimeout(ConfigReader.getDouble("timeout"));

        PLAYWRIGHT.set(playwright);
        BROWSER.set(browser);
        CONTEXT.set(context);
        PAGE.set(page);
    }

    public static Page page() {
        return PAGE.get();
    }

    public static void stop() {
        closeQuietly(CONTEXT.get());
        closeQuietly(BROWSER.get());
        closeQuietly(PLAYWRIGHT.get());
        CONTEXT.remove();
        BROWSER.remove();
        PLAYWRIGHT.remove();
        PAGE.remove();
    }

    private static Playwright createPlaywrightWithRetry() {
        PlaywrightException lastError = null;
        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                return Playwright.create(new Playwright.CreateOptions().setEnv(playwrightEnv()));
            } catch (PlaywrightException e) {
                lastError = e;
                sleep(1500L * attempt);
            }
        }
        throw new PlaywrightException(
                "Could not start Playwright. Close other running tests, then install browsers with: "
                        + "mvn exec:java \"-Dexec.mainClass=com.microsoft.playwright.CLI\" \"-Dexec.args=install chromium\"",
                lastError
        );
    }

    /**
     * Keep browsers in a stable Windows folder so IntelliJ and Maven use the same install.
     * Copy the current process environment so the Playwright driver still finds PATH, HOME, etc.
     */
    private static Map<String, String> playwrightEnv() {
        Map<String, String> env = new HashMap<>(System.getenv());
        env.put("PLAYWRIGHT_BROWSERS_PATH", browsersDirectory().toString());
        return env;
    }

    static Path browsersDirectory() {
        Path browsers = Path.of(System.getProperty("user.home"), "AppData", "Local", "ms-playwright");
        try {
            Files.createDirectories(browsers);
        } catch (Exception ignored) {
            // Launch will fail later with a clearer Playwright message if this folder is unusable.
        }
        return browsers;
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static void closeQuietly(AutoCloseable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (Exception ignored) {
            // Best-effort cleanup after a test
        }
    }
}
