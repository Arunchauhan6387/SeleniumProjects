@tag
Feature: Purchase the order from Ecommerce website

Background:
	Given I landed on Ecommerce Page

@Regression
Scenario Outline: Positive test for submitting the order

	Given Logged in with Username <email> and password <password>
	When I add product <productName> to Cart
	And Checkout <productName> and submit the order
	Then "Thankyou for the order." message is dispalyed on the confirmationPage

	Examples:
		|	email													| password 								| productName	|
		|	arunchauhan000786@gmail.com		|	@Arunchauhan6387739490	|	ZARA COAT 3	|