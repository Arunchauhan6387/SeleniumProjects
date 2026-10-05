package SeleniumFramworkDesign.base;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.openqa.selenium.WebDriver;

import SeleniumFramworkDesign.pages.LandingPage;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class CucumberHooks {
	private final CucumberTestContext context;

	public CucumberHooks(CucumberTestContext context) {
		this.context = context;
	}

	@Before
	public void setUpScenario() throws IOException {
		BaseTest baseTest = new BaseTest();
		context.setBaseTest(baseTest);
		LandingPage landingPage = baseTest.launchApplication();
		context.setLandingPage(landingPage);
	}

	@After
	public void tearDownScenario(Scenario scenario) throws IOException {
		BaseTest baseTest = context.getBaseTest();
		WebDriver driver = baseTest.driver;
		try {
			if (scenario.isFailed() && driver != null) {
				String screenshotPath = baseTest.getScreenshot("Cucumber_" + scenario.getName(), driver);
				scenario.attach(Files.readAllBytes(Paths.get(screenshotPath)), "image/png", "Failure screenshot");
			}
		} finally {
			baseTest.tearDown();
			context.clear();
		}
	}
}
