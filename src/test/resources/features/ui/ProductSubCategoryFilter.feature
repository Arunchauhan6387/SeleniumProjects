Feature: Filter products by subcategory

Background:
  Given I landed on Ecommerce Page

 @Regression
  Scenario Outline: Show and dismiss the no-products message when a subcategory has no matches
    Given Logged in with Username <email> and password <password>
    When I select the sub-category "<subCategory>" from the category "<category>"
    Then the "No Products Found" message is displayed on the product page
    And the message disappears

    Examples:
      | email              | password              | category | subCategory |
      | ${TEST_USER_EMAIL} | ${TEST_USER_PASSWORD} | fashion  | t-shirts    |