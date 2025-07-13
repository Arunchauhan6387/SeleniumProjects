package SeleniumFramworkDesign.AbstractComponents;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import SeleniumFramworkDesign.pageObjects.CartPage;
import SeleniumFramworkDesign.pageObjects.OrderPage;

public class AbstractComponents {
	WebDriver driver;
	WebDriverWait wait;

	public AbstractComponents(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

// driver.findElement(By.cssSelector("button[routerlink*='cart']")).click();
	@FindBy(css = "button[routerlink*='cart']")
	WebElement cartPagebtn;
	@FindBy(css = "button[routerlink*='myorders']")
	WebElement orderPagebtn;

	public void waitForElementToAppear(By FindBy) {
		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(FindBy));
	}

	public void waitForWebElementToAppear(WebElement FindBy) {
		wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOf(FindBy));
	}

	public void waitForElementToDisappear(WebElement ele) throws InterruptedException {
		Thread.sleep(1000);
// wait.until(ExpectedConditions.invisibilityOf(ele));

	}

	public CartPage goTocartPage() {
		cartPagebtn.click();
		CartPage cartpage = new CartPage(driver);
		return cartpage;
	}

	public OrderPage goToOrderPage() {
		orderPagebtn.click();
		OrderPage orderpage = new OrderPage(driver);
		return orderpage;
	}

}