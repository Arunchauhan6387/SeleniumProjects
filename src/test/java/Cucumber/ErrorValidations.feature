@tag
Feature: Error Validations
  I want to use this template for my feature file

  @ErrorValidations
  Scenario Outline: Login with invalid credentials
    Given I landed on Ecommerce Page
    Given Logged in with Username <email> and password <password>
    Then "Incorrect email or password." message is displayed

   	Examples:
		|	email													| password 								|
		|	arunchauhan000786@gmail.com		|	@Arunchauhan638773949	|
