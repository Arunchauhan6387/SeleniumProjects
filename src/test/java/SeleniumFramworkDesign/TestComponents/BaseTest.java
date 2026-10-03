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
import java.util.Locale;
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
	private String baseUrl;

	public WebDriver initializeDriver() throws IOException {
	    Properties settings = loadSettings();
	    String environment = getSetting(settings, "APP_ENV", "app.env", "qa").trim().toUpperCase(Locale.ROOT);
	    if (!environment.equals("DEV") && !environment.equals("QA")) {
	        throw new IllegalArgumentException("APP_ENV must be either 'dev' or 'qa', but was: " + environment);
	    }
	    String urlKey = environment + "_BASE_URL";
	    String baseUrlProperty = environment.toLowerCase(Locale.ROOT) + ".base.url";
	    baseUrl = getSetting(settings, urlKey, baseUrlProperty, null);
	    if (baseUrl == null || baseUrl.isBlank()) {
	        throw new IOException("Missing " + environment + "_BASE_URL in .env");
	    }

	    Properties legacyProperties = new Properties();
	    Path legacyPropertiesPath = Paths.get(System.getProperty("user.dir"),
	            "src", "main", "java", "SeleniumFramworkDesign", "resources", "GlobalData.properties");
	    try (FileInputStream input = new FileInputStream(legacyPropertiesPath.toFile())) {
	        legacyProperties.load(input);
	    }
	    String browserName = getSetting(settings, "BROWSER", "browser",
	            legacyProperties.getProperty("browser", "chrome"));
	    boolean headless = Boolean.parseBoolean(getSetting(settings, "HEADLESS", "headless", "false"));

	    if (browserName.equalsIgnoreCase("chrome")) {
	        WebDriverManager.chromedriver().setup();
	        ChromeOptions options = new ChromeOptions();
	        if (headless) {
	            options.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage");
	        }
	        driver = new ChromeDriver(options);
	        driver.manage().window().setSize(new Dimension(1440, 900));

	    } else if (browserName.equalsIgnoreCase("edge")) {
	        // ✅ Added WebDriverManager for Edge
	        WebDriverManager.edgedriver().setup();
	        driver = new EdgeDriver();

	    } else if (browserName.equalsIgnoreCase("firefox")) { // ✅ Changed to ignore case
	        // ✅ Added WebDriverManager for Firefox
	        WebDriverManager.firefoxdriver().setup();
	        driver = new FirefoxDriver();
	    } else {
	        throw new IllegalArgumentException("Unsupported browser: " + browserName
	                + ". Set BROWSER to chrome, edge, or firefox.");
	    }

	    if (!headless) {
	        driver.manage().window().maximize();
	    }
	    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
	    return driver;
	}

	private Properties loadSettings() throws IOException {
		Properties settings = new Properties();
		Path envFile = Paths.get(System.getProperty("user.dir"), ".env");
		if (Files.exists(envFile)) {
			try (java.io.Reader reader = Files.newBufferedReader(envFile, StandardCharsets.UTF_8)) {
				settings.load(reader);
			}
		}
		return settings;
	}

	private String getSetting(Properties settings, String environmentKey, String systemProperty, String defaultValue) {
		String value = System.getProperty(systemProperty);
		if (value == null || value.isBlank()) {
			value = System.getenv(environmentKey);
		}
		if (value == null || value.isBlank()) {
			value = settings.getProperty(environmentKey);
		}
		return value == null || value.isBlank() ? defaultValue : value;
	}

	@BeforeMethod(alwaysRun = true)
	public LandingPage launchApplication() throws IOException {
		driver = initializeDriver();
		landingpage = new LandingPage(driver);
		landingpage.goTo(baseUrl);
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
		Properties settings = loadSettings();
		for (HashMap<String, String> row : data) {
			for (HashMap.Entry<String, String> entry : row.entrySet()) {
				String value = entry.getValue();
				if (value != null && value.matches("\\$\\{[A-Z][A-Z0-9_]*\\}")) {
					String key = value.substring(2, value.length() - 1);
					String resolved = System.getenv(key);
					if (resolved == null || resolved.isBlank()) {
						resolved = settings.getProperty(key);
					}
					if (resolved == null || resolved.isBlank()) {
						throw new IOException("Missing required test setting " + key
								+ "; set it in .env or as an environment variable.");
					}
					entry.setValue(resolved);
				}
			}
		}
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