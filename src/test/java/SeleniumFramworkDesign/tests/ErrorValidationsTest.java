package SeleniumFramworkDesign.tests;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import SeleniumFramworkDesign.TestComponents.BaseTest;
import SeleniumFramworkDesign.TestComponents.Retry;
import SeleniumFramworkDesign.pageObjects.CartPage;
import SeleniumFramworkDesign.pageObjects.ProductCatalogue;

public class ErrorValidationsTest extends BaseTest {

	@Test(groups = { "ErrorHandling" }, retryAnalyzer = Retry.class)
	public void LoginErrorValidation() {
		String productName = "ZARA COAT 3";
		String countryName = "india";
		ProductCatalogue productcatalogue = landingpage.loginApplication("arun786@gmail.com", "@Arun563");
		Assert.assertEquals("Incorrect email or password.", landingpage.getLoginErrorMsg());
//class="ng-tns-c4-12 ng-star-inserted ng-trigger ng-trigger-flyInOut ngx-toastr toast-error"
	}

	@Test
	public void ProductErrorValidation() throws InterruptedException {
		String productName = "ZARA COAT 3";
		String countryName = "india";
		ProductCatalogue productcatalogue = landingpage.loginApplication("arunchauhan000786@gmail.com",
				"@Arunchauhan6387739490");
		List<WebElement> products = productcatalogue.getProductList();
		productcatalogue.addProductToCart(productName);
		CartPage cartpage = productcatalogue.goTocartPage();
		Boolean match = cartpage.verifyProductDisplay("ZARA COAT 33");
		Assert.assertFalse(match);
	}
}
