package SeleniumFramworkDesign.base;

import SeleniumFramworkDesign.pages.LandingPage;

public class CucumberTestContext {
	private BaseTest baseTest;
	private LandingPage landingPage;

	public BaseTest getBaseTest() {
		if (baseTest == null) {
			throw new IllegalStateException("Cucumber test context has not been initialized.");
		}
		return baseTest;
	}

	public void setBaseTest(BaseTest baseTest) {
		this.baseTest = baseTest;
	}

	public LandingPage getLandingPage() {
		if (landingPage == null) {
			throw new IllegalStateException("Landing page has not been initialized for this scenario.");
		}
		return landingPage;
	}

	public void setLandingPage(LandingPage landingPage) {
		this.landingPage = landingPage;
	}

	public void clear() {
		baseTest = null;
		landingPage = null;
	}
}
