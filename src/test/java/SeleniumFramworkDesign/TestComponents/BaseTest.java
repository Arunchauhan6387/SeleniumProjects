package SeleniumFramworkDesign.TestComponents;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

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

	    // This logic is good! It prioritizes the Jenkins parameter.
	    String browserName = System.getProperty("browser") != null ? System.getProperty("browser") : prop.getProperty("browser");

	    if (browserName.equalsIgnoreCase("chrome")) {
	        WebDriverManager.chromedriver().setup();
	        driver = new ChromeDriver();
	        driver.manage().window().setSize(new Dimension(1440, 900)); // Specific to Chrome in your code

	    } else if (browserName.equalsIgnoreCase("edge")) {
	        // ✅ Added WebDriverManager for Edge
	        WebDriverManager.edgedriver().setup();
	        driver = new EdgeDriver();

	    } else if (browserName.equalsIgnoreCase("firefox")) { // ✅ Changed to ignore case
	        // ✅ Added WebDriverManager for Firefox
	        WebDriverManager.firefoxdriver().setup();
	        driver = new FirefoxDriver();
	    }

	    driver.manage().window().maximize();
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
		Path reportDirectory = Paths.get(System.getProperty("user.dir"), "reportss");
		Files.createDirectories(reportDirectory);
		String safeTestCaseName = TestCaseName.replaceAll("[^A-Za-z0-9._-]", "_");
		Path screenshotPath = reportDirectory.resolve(
				safeTestCaseName + "_" + UUID.randomUUID() + ".png");
		FileUtils.copyFile(source, screenshotPath.toFile());
		return screenshotPath.toAbsolutePath().toString();
	}

}