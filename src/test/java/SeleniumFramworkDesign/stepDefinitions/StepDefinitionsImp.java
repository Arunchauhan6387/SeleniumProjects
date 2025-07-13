package SeleniumFramworkDesign.stepDefinitions;

import java.io.IOException;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.Assert;

import SeleniumFramworkDesign.TestComponents.BaseTest;
import SeleniumFramworkDesign.pageObjects.CartPage;
import SeleniumFramworkDesign.pageObjects.CheckoutPage;
import SeleniumFramworkDesign.pageObjects.ConfirmationPage;
import SeleniumFramworkDesign.pageObjects.LandingPage;
import SeleniumFramworkDesign.pageObjects.ProductCatalogue;
import io.cucumber.java.en.*;

public class StepDefinitionsImp extends BaseTest {

	public LandingPage landingpage;
	public ProductCatalogue productcatalogue;
	public CartPage cartpage;
	public CheckoutPage checkout;
	public ConfirmationPage confirmationpage;

	@Given("I landed on Ecommerce Page")
	public void I_landed_on_Ecommerce_Page() throws IOException {
		landingpage = launchApplication();
	}

	@Given("^Logged in with Username (.+) and password (.+)$")
	public void logged_in_with_username_and_password(String Username, String password) {
		productcatalogue = landingpage.loginApplication(Username, password);

	}

	@When("^I add product (.+) to Cart$")
	public void I_add_product_to_cart(String productName) throws InterruptedException {
		List<WebElement> products = productcatalogue.getProductList();
		productcatalogue.addProductToCart(productName);
	}

	@And("^Checkout (.+) and submit the order$")
	public void checkout_and_submit_the_order(String productName) throws InterruptedException {
		cartpage = productcatalogue.goTocartPage();
		Boolean match = cartpage.verifyProductDisplay(productName);
		Assert.assertTrue(match);
		checkout = cartpage.goToCheckout();
		checkout.selectCountry("india");
		confirmationpage = checkout.submitOrder();

	}

	@Then("{string} message is dispalyed on the confirmationPage")
	public void message_is_dispalyed_on_the_confirmation_page(String string) {
		String ConfirmMessage = confirmationpage.getConfirmationMessage();
		Assert.assertTrue(ConfirmMessage.equalsIgnoreCase(string));
		driver.close();
	}

	@Then("{string} message is displayed")
	public void message_is_displayed(String string) {
		Assert.assertEquals("Incorrect email or password.", landingpage.getLoginErrorMsg());
		driver.close();
	}

}
