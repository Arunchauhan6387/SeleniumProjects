package SeleniumFramworkDesign.TestComponents;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import SeleniumFramworkDesign.pageObjects.LandingPage;
import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseTest {

	public WebDriver driver;
	public LandingPage landingpage;

	public WebDriver initializeDriver() throws IOException {
		Properties prop = new Properties();
		FileInputStream fls = new FileInputStream(System.getProperty("user.dir")
				+ "//src//main//java//SeleniumFramworkDesign//resources//GlobalData.properties");
		prop.load(fls);
		
		String browserName = System.getProperty("browser")!=null ? System.getProperty("browser") : prop.getProperty("browser");
		
		//prop.getProperty("browser");
		if (browserName.contains("chrome")) {
			//ChromeOptions options = new ChromeOptions();
			WebDriverManager.chromedriver().setup();
//			if(browserName.contains("headless"))
//			{
//				options.addArguments("headless");
//			}
			
			driver = new ChromeDriver();
			driver.manage().window().setSize(new Dimension(1440,900));
			
		} else if (browserName.equals("edge")) {
			 driver = new EdgeDriver();
		} else if (browserName.equals("Firefox")) {
			 driver = new FirefoxDriver();

		}
		driver.manage().window().maximize();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		return driver;
	}

	@BeforeMethod(alwaysRun = true)
	public LandingPage launchApplication() throws IOException {
		driver = initializeDriver();
		landingpage = new LandingPage(driver);
		landingpage.goTo();
		return landingpage;
	}

	@AfterMethod(alwaysRun = true)
	public void tearDown() {
		driver.close();
	}

	public List<HashMap<String, String>> getJsonDataToMap(String Filepath) throws IOException {
//read json to string
		String jsonContent = FileUtils.readFileToString(new File(Filepath), StandardCharsets.UTF_8);
//String to HashMap Jackson Databind
		ObjectMapper mapper = new ObjectMapper();
		List<HashMap<String, String>> data = mapper.readValue(jsonContent,
				new TypeReference<List<HashMap<String, String>>>() {
				});
		return data;
	}

	public String getScreenshot(String TestCaseName, WebDriver driver) throws IOException {
		TakesScreenshot ts = (TakesScreenshot) driver;
		File source = ts.getScreenshotAs(OutputType.FILE);
		File fls = new File(System.getProperty("user.dir") + "//reportss//" + TestCaseName + ".png");
		FileUtils.copyFile(source, fls);
		return System.getProperty("user.dir") + "//reportss//" + TestCaseName + ".png";
	}

}