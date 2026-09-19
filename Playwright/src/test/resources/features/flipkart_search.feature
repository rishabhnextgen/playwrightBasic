Feature: Flipkart product search
  As a shopper
  I want to search for a phone on Flipkart
  So that I can see matching products

  Scenario: Search for iPhone 16
    Given I launch the Flipkart website
    When I search for "iPhone 16"
    Then I should see search results for "iPhone 16"
    Then I should the prices as well
