package SeleniumFramworkDesign.stepDefinitions.ui;

import java.io.IOException;

import org.testng.Assert;

import SeleniumFramworkDesign.base.CucumberTestContext;
import SeleniumFramworkDesign.pages.CartPage;
import SeleniumFramworkDesign.pages.CheckoutPage;
import SeleniumFramworkDesign.pages.ConfirmationPage;
import SeleniumFramworkDesign.pages.ProductCatalogue;
import io.cucumber.java.en.*;

public class StepDefinitionsImp {

	private final CucumberTestContext context;
	private ProductCatalogue productcatalogue;
	private CartPage cartpage;
	private CheckoutPage checkout;
	private ConfirmationPage confirmationpage;
	private String noProductsToastText;

	public StepDefinitionsImp(CucumberTestContext context) {
		this.context = context;
	}

	@Given("I landed on Ecommerce Page")
	public void I_landed_on_Ecommerce_Page() {
		Assert.assertNotNull(context.getLandingPage(), "Scenario setup did not open the application.");
	}

	@Given("^Logged in with Username (.+) and password (.+)$")
	public void logged_in_with_username_and_password(String username, String password) throws IOException {
		productcatalogue = context.getLandingPage().loginApplication(resolveSetting(username), resolveSetting(password));

	}

	private String resolveSetting(String value) throws IOException {
		if (value.matches("\\$\\{[A-Z][A-Z0-9_]*\\}")) {
			return context.getBaseTest().getRequiredSetting(value.substring(2, value.length() - 1));
		}
		return value;
	}

	@When("^I add product (.+) to Cart$")
	public void I_add_product_to_cart(String productName) throws InterruptedException, IOException {
		productcatalogue.addProductToCart(resolveSetting(productName));
	}

	@And("^Checkout (.+) and submit the order$")
	public void checkout_and_submit_the_order(String productName) throws InterruptedException, IOException {
		cartpage = productcatalogue.goTocartPage();
		Boolean match = cartpage.verifyProductDisplay(resolveSetting(productName));
		Assert.assertTrue(match);
		checkout = cartpage.goToCheckout();
		checkout.selectCountry("india");
		confirmationpage = checkout.submitOrder();

	}

	@Then("{string} message is dispalyed on the confirmationPage")
	public void message_is_dispalyed_on_the_confirmation_page(String string) {
		String ConfirmMessage = confirmationpage.getConfirmationMessage();
		Assert.assertTrue(ConfirmMessage.equalsIgnoreCase(string));
	}

	@Then("{string} message is displayed")
	public void message_is_displayed(String string) {
		Assert.assertEquals("Incorrect email or password.", context.getLandingPage().getLoginErrorMsg());
	}

	@When("I select the sub-category {string} from the category {string}")
	public void i_select_the_subcategory_from_the_category(String subCategory, String category) {
		noProductsToastText = productcatalogue.selectCategoryAndSubCategory(category, subCategory);
	}

	@Then("the {string} message is displayed on the product page")
	public void the_message_is_displayed_on_the_product_page(String expectedMessage) {
		Assert.assertNotNull(noProductsToastText, "The no-products toast was not captured after selecting filters.");
		Assert.assertTrue(noProductsToastText.contains(expectedMessage),
				"Expected message was not displayed. Actual message: " + noProductsToastText);
	}

	@Then("the message disappears")
	public void the_message_disappears() {
		productcatalogue.waitForNoProductsMessageToDisappear();
	}

}
