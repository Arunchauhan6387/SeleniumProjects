package SeleniumFramworkDesign.pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import SeleniumFramworkDesign.AbstractComponents.AbstractComponents;

public class CheckoutPage extends AbstractComponents {

	WebDriver driver;

	public CheckoutPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	@FindBy(css = "[placeholder='Select Country']")
	WebElement Country;
	@FindBy(css = "button[class*='ta-item']:nth-of-type(2)")
	WebElement SelectCountry;
	@FindBy(css = ".action__submit ")
	WebElement submit;
	By results = By.cssSelector(".ta-results");

	public void selectCountry(String countryName) throws InterruptedException {
		Actions a = new Actions(driver);
		a.sendKeys(Country, countryName).build().perform();
		waitForElementToAppear(results);
		SelectCountry.click();
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
		Thread.sleep(2000);
	}

	public ConfirmationPage submitOrder() {
		submit.click();
		ConfirmationPage confirmationpage = new ConfirmationPage(driver);
		return confirmationpage;
	}
}
