package com.playwright.learn.steps;

import com.playwright.learn.browser.PlaywrightManager;
import com.playwright.learn.pages.FlipkartHomePage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

/**
 * Each method is glued to a line in the .feature file.
 * Gherkin: Given / When / Then  →  Java step definition.
 */
public class FlipkartSearchSteps {

    private FlipkartHomePage flipkartHomePage;

    @Given("I launch the Flipkart website")
    public void iLaunchTheFlipkartWebsite() {
        flipkartHomePage = new FlipkartHomePage(PlaywrightManager.page());
        flipkartHomePage.openHomePage();
        Assert.assertTrue(
                PlaywrightManager.page().url().contains("flipkart.com"),
                "Expected Flipkart to open. Actual URL: " + PlaywrightManager.page().url()
        );
    }

    @When("I search for {string}")
    public void iSearchFor(String productName) {
        flipkartHomePage.searchFor(productName);
    }

    @Then("I should see search results for {string}")
    public void iShouldSeeSearchResultsFor(String productName) {
        Assert.assertTrue(
                flipkartHomePage.resultsContain(productName),
                "Expected Flipkart search results for: " + productName
                        + ". Actual URL: " + PlaywrightManager.page().url()
        );
    }
}
