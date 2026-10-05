package SeleniumFramworkDesign.pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import SeleniumFramworkDesign.base.AbstractComponents;

public class ProductCatalogue extends AbstractComponents {

	WebDriver driver;

	public ProductCatalogue(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	@FindBy(css = ".mb-3")
	List<WebElement> products;
	By spinner = By.cssSelector(".ngx-spinner-overlay");
	By productsBy = By.cssSelector(".mb-3");
	By addToCart = By.cssSelector(".card-body button:last-of-type");
	By toastMessage = By.cssSelector("#toast-container");
	private static final By NO_PRODUCTS_TOAST =
			By.cssSelector("#toast-container .toast-title[aria-label='No Products Found']");

	public List<WebElement> getProductList() {
		waitForElementToAppear(productsBy);
		return products;
	}

	public WebElement getProductByName(String productName) {
		WebElement prod = getProductList().stream()
				.filter(product -> product.findElement(By.cssSelector("b")).getText().equals(productName)).findFirst()
				.orElse(null);
		return prod;
	}

	public void addProductToCart(String productName) throws InterruptedException {
		waitForElementToDisappear(spinner);
		WebElement prod = getProductByName(productName);
		prod.findElement(addToCart).click();
		waitForElementToAppear(toastMessage);
		waitForElementToDisappear(spinner);

	}

	private String filterOptionLabelXpath(String sectionName, String optionName) {
		return "//section[@id='sidebar']//div[contains(@class,'py-2') "
				+ "and contains(@class,'border-bottom')][.//h6[normalize-space()="
				+ xpathLiteral(sectionName) + "]]//label[normalize-space(.)="
				+ xpathLiteral(optionName) + "]";
	}

	public String selectCategoryAndSubCategory(String category, String subCategory) {
		selectFilterOption("Categories", category, false);
		return selectFilterOption("Sub Categories", subCategory, true);
	}

	private String selectFilterOption(String sectionName, String optionName, boolean waitForNoProductsToast) {
		String labelXpath = filterOptionLabelXpath(sectionName, optionName);
		By checkboxLocator = By.xpath("(" + labelXpath + ")/preceding-sibling::input[@type='checkbox']");
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		waitForElementToDisappear(spinner);
		WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(checkboxLocator));
		if (!checkbox.isSelected()) {
			try {
				checkbox.click();
			} catch (ElementClickInterceptedException e) {
				((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkbox);
			}
			wait.until(ExpectedConditions.elementSelectionStateToBe(checkboxLocator, true));
		}
		if (waitForNoProductsToast) {
			return wait.until(ExpectedConditions.visibilityOfElementLocated(NO_PRODUCTS_TOAST))
					.getText().trim();
		}
		waitForElementToDisappear(spinner);
		return null;
	}

	public void waitForNoProductsMessageToDisappear() {
		new WebDriverWait(driver, Duration.ofSeconds(15))
				.until(ExpectedConditions.invisibilityOfElementLocated(NO_PRODUCTS_TOAST));
	}

	private String xpathLiteral(String value) {
		if (!value.contains("'")) {
			return "'" + value + "'";
		}
		if (!value.contains("\"")) {
			return "\"" + value + "\"";
		}

		String[] parts = value.split("'", -1);
		StringBuilder literal = new StringBuilder("concat(");
		for (int i = 0; i < parts.length; i++) {
			if (i > 0) {
				literal.append(", \"'\", ");
			}
			literal.append("'").append(parts[i]).append("'");
		}
		return literal.append(")").toString();
	}
}
