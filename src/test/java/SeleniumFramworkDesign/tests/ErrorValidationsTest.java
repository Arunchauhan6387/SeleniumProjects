package SeleniumFramworkDesign.tests;

import java.io.IOException;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import SeleniumFramworkDesign.base.BaseTest;
import SeleniumFramworkDesign.base.Retry;
import SeleniumFramworkDesign.pages.CartPage;
import SeleniumFramworkDesign.pages.ProductCatalogue;

public class ErrorValidationsTest extends BaseTest {

	@Test(groups = { "ErrorHandling" }, retryAnalyzer = Retry.class)
	public void LoginErrorValidation() {
		landingpage.loginApplication("invalid@example.com", "invalid-password");
		Assert.assertEquals("Incorrect email or password.", landingpage.getLoginErrorMsg());
	}

	@Test
	public void ProductErrorValidation() throws InterruptedException, IOException {
		String productName = getRequiredSetting("TEST_PRODUCT_1");
		ProductCatalogue productcatalogue = landingpage.loginApplication(
				getRequiredSetting("TEST_USER_EMAIL"), getRequiredSetting("TEST_USER_PASSWORD"));
		List<WebElement> products = productcatalogue.getProductList();
		productcatalogue.addProductToCart(productName);
		CartPage cartpage = productcatalogue.goTocartPage();
		Boolean match = cartpage.verifyProductDisplay("ZARA COAT 33");
		Assert.assertFalse(match);
	}
}
