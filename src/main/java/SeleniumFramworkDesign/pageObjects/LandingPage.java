package SeleniumFramworkDesign.pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import SeleniumFramworkDesign.AbstractComponents.AbstractComponents;

public class LandingPage extends AbstractComponents {
	WebDriver driver;

	public LandingPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	@FindBy(id = "userEmail")
	WebElement userEmail;
	@FindBy(id = "userPassword")
	WebElement userPwd;
	@FindBy(id = "login")
	WebElement Submit;
//class="ng-tns-c4-12 ng-star-inserted ng-trigger ng-trigger-flyInOut ngx-toastr toast-error"
	@FindBy(css = "[class*='flyInOut ']")
	WebElement loginErrorTxt;

	public ProductCatalogue loginApplication(String userTxt, String pwdTxt) {
		userEmail.sendKeys(userTxt);
		userPwd.sendKeys(pwdTxt);
		Submit.click();
		ProductCatalogue productcatalogue = new ProductCatalogue(driver);
		return productcatalogue;

	}

	public void goTo() {
		driver.get("https://rahulshettyacademy.com/client");

	}

	public String getLoginErrorMsg() {
		waitForWebElementToAppear(loginErrorTxt);
		return loginErrorTxt.getText();
	}
}
