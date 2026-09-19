package com.playwright.learn.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

/**
 * TestNG entry point for Cucumber.
 * features = Gherkin files, glue = Java packages with steps + hooks.
 */
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"com.playwright.learn.steps", "com.playwright.learn.hooks"},
        plugin = {
                "pretty",
                "html:target/cucumber-report.html",
                "json:target/cucumber.json"
        },
        monochrome = true
)
public class CucumberTestRunner extends AbstractTestNGCucumberTests {
}
