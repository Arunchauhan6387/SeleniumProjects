package SeleniumFramworkDesign.tests;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import SeleniumFramworkDesign.TestComponents.BaseTest;
import SeleniumFramworkDesign.pageObjects.CartPage;
import SeleniumFramworkDesign.pageObjects.CheckoutPage;
import SeleniumFramworkDesign.pageObjects.ConfirmationPage;
import SeleniumFramworkDesign.pageObjects.OrderPage;
import SeleniumFramworkDesign.pageObjects.ProductCatalogue;

public class SubmitOrderTest extends BaseTest {

	String productName = "ZARA COAT 3";
//String productName = "ADIDAS ORIGINAL";
	//String countryName = "india";

	@Test(dataProvider = "getData", groups = "Purchase")
	public void SubmitOrder(HashMap<String, String> input) throws IOException, InterruptedException {
		ProductCatalogue productcatalogue = landingpage.loginApplication(input.get("email"), input.get("password"));
		List<WebElement> products = productcatalogue.getProductList();
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
	public void OrderHistoryTest() {
		ProductCatalogue productcatalogue = landingpage.loginApplication("arunchauhan000786@gmail.com",
				"@Arunchauhan6387739490");
		OrderPage orderpage = productcatalogue.goToOrderPage();
		Assert.assertTrue(orderpage.verifyProductDisplay(productName));
	}

	@DataProvider
	public Object[][] getData() throws IOException {

//using HashMap--
// HashMap<String,String> map = new HashMap<String,String>();
// map.put("email", "arunchauhan000786@gmail.com");
// map.put("password", "@Arunchauhan6387739490");
// map.put("product", "ZARA COAT 3");
//
// HashMap<String,String> map1= new HashMap<String,String>();
// map1.put("email", "arunchauhan@gmail.com");
// map1.put("password", "@Arun12345");
// map1.put("product", "ADIDAS ORIGINAL");
//Taking data from Json for that --DataReader then PurchaseOrder.json
		List<HashMap<String, String>> data = getJsonDataToMap(
				System.getProperty("user.dir") + "//src//test//java//SeleniumFramworkDesign//Data//PurchaseOrder.json");
		return new Object[][] { { data.get(0) }, { data.get(1) } };
	}
	/*
	 * @DataProvider public void getData() { return new Object[][]
	 * {{"arunchauhan000786@gmail.com","@Arunchauhan6387739490","ZARA COAT 3"},{
	 * "arunchauhan@gmail.com","@Arun12345","ADIDAS ORIGINAL"}}; }
	 */

}
