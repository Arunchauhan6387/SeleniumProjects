Feature: Error Validations

  @Regression
  Scenario Outline: Login with invalid credentials
    Given I landed on Ecommerce Page
    Given Logged in with Username <email> and password <password>
    Then "Incorrect email or password." message is displayed

    Examples:
      | email               | password         |
      | invalid@example.com | invalid-password |
