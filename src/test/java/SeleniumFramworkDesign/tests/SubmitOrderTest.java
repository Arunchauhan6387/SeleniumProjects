package SeleniumFramworkDesign.tests;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import SeleniumFramworkDesign.base.BaseTest;
import SeleniumFramworkDesign.pages.CartPage;
import SeleniumFramworkDesign.pages.CheckoutPage;
import SeleniumFramworkDesign.pages.ConfirmationPage;
import SeleniumFramworkDesign.pages.OrderPage;
import SeleniumFramworkDesign.pages.ProductCatalogue;

public class SubmitOrderTest extends BaseTest {

	@Test(dataProvider = "getData", groups = "Purchase")
	public void SubmitOrder(HashMap<String, String> input) throws IOException, InterruptedException {
		ProductCatalogue productcatalogue = landingpage.loginApplication(input.get("email"), input.get("password"));
		WebElement product = productcatalogue.getProductByName(input.get("productName"));
		Assert.assertNotNull(product, "Product is not visible: " + input.get("productName"));
		getElementScreenshot("Product_" + input.get("productName"), product);
		productcatalogue.addProductToCart(input.get("productName"));
		CartPage cartpage = productcatalogue.goTocartPage();
		Boolean match = cartpage.verifyProductDisplay(input.get("productName"));
		Assert.assertTrue(match);
		CheckoutPage checkout = cartpage.goToCheckout();
		checkout.selectCountry("india");
		ConfirmationPage confirmationpage = checkout.submitOrder();
		String ConfirmMessage = confirmationpage.getConfirmationMessage();
		Assert.assertTrue(ConfirmMessage.equalsIgnoreCase("Thankyou for the order."));

	}

//Verify ZARA COAT 3 is displaying in OrderPage.
	@Test(dependsOnMethods = { "SubmitOrder" })
	public void OrderHistoryTest() throws IOException {
		ProductCatalogue productcatalogue = landingpage.loginApplication(
				getRequiredSetting("TEST_USER_EMAIL"), getRequiredSetting("TEST_USER_PASSWORD"));
		OrderPage orderpage = productcatalogue.goToOrderPage();
		Assert.assertTrue(orderpage.verifyProductDisplay(getRequiredSetting("TEST_PRODUCT_1")));
	}

	@DataProvider
	public Object[][] getData() throws IOException {

		List<HashMap<String, String>> data = getJsonDataToMap(getTestDataPath("PurchaseOrder.json").toString());
		return new Object[][] { { data.get(0) }, { data.get(1) } };
	}

}
