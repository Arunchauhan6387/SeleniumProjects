package SeleniumFramworkDesign.tests;

import java.io.IOException;

import SeleniumFramworkDesign.base.BaseTest;

public class StandaloneTest2 extends BaseTest {

	public static void main(String[] args) throws IOException {
		StandaloneTest2 test = new StandaloneTest2();
		try {
			test.launchApplication();
			test.landingpage.loginApplication(
					test.getRequiredSetting("TEST_USER_EMAIL"),
					test.getRequiredSetting("TEST_USER_PASSWORD")).getProductList();
		} finally {
			test.tearDown();
		}
	}
}
