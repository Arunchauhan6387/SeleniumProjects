package SeleniumFramworkDesign.pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import SeleniumFramworkDesign.base.AbstractComponents;

public class CheckoutPage extends AbstractComponents {

	WebDriver driver;

	public CheckoutPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	@FindBy(css = "[placeholder='Select Country']")
	WebElement Country;
	By countryOptions = By.cssSelector("button.ta-item");
	@FindBy(css = ".action__submit ")
	WebElement submit;
	By results = By.cssSelector(".ta-results");

	public void selectCountry(String countryName) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.elementToBeClickable(Country));
		Country.clear();
		Country.sendKeys(countryName);
		wait.until(ExpectedConditions.visibilityOfElementLocated(results));
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(countryOptions));

		List<WebElement> options = driver.findElements(countryOptions);
		WebElement selectedCountry = options.stream()
				.filter(option -> option.getText().trim().equalsIgnoreCase(countryName))
				.findFirst()
				.orElseThrow(() -> new NoSuchElementException(
						"No country suggestion matched: " + countryName));

		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView({block: 'center'});", selectedCountry);
		wait.until(ExpectedConditions.elementToBeClickable(selectedCountry)).click();
	}

	public ConfirmationPage submitOrder() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView({block: 'center'});", submit);
		new WebDriverWait(driver, Duration.ofSeconds(10))
				.until(ExpectedConditions.elementToBeClickable(submit));
		try {
			submit.click();
		} catch (ElementClickInterceptedException e) {
			js.executeScript("arguments[0].click();", submit);
		}
		ConfirmationPage confirmationpage = new ConfirmationPage(driver);
		return confirmationpage;
	}
}
