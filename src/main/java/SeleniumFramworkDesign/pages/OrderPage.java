package SeleniumFramworkDesign.pages;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import SeleniumFramworkDesign.base.AbstractComponents;

public class OrderPage extends AbstractComponents {

	WebDriver driver;

	public OrderPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	@FindBy(css = "tr td:nth-child(3)")
	List<WebElement> productsNames;

	public Boolean verifyProductDisplay(String productName) {
		Boolean match = productsNames.stream()
				.anyMatch(cartproduct -> cartproduct.getText().equalsIgnoreCase(productName));
		return match;
	}
}