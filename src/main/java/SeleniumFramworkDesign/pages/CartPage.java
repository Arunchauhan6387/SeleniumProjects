package SeleniumFramworkDesign.pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import SeleniumFramworkDesign.base.AbstractComponents;

public class CartPage extends AbstractComponents {

	WebDriver driver;

	public CartPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	@FindBy(css = ".cartSection h3")
	List<WebElement> cartProducts;
	@FindBy(css = ".totalRow button")
	WebElement checkoutbtn;

	public Boolean verifyProductDisplay(String productName) {
		Boolean match = cartProducts.stream()
				.anyMatch(cartproduct -> cartproduct.getText().equalsIgnoreCase(productName));
		return match;
	}

	public CheckoutPage goToCheckout() {
		((JavascriptExecutor) driver).executeScript(
				"arguments[0].scrollIntoView({block: 'center'});", checkoutbtn);
		new WebDriverWait(driver, Duration.ofSeconds(10))
				.until(ExpectedConditions.elementToBeClickable(checkoutbtn));
		try {
			checkoutbtn.click();
		} catch (ElementClickInterceptedException e) {
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkoutbtn);
		}
		CheckoutPage checkout = new CheckoutPage(driver);
		return checkout;

	}
}