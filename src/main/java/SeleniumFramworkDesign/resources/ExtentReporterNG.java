package SeleniumFramworkDesign.resources;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentReporterNG {

	public static ExtentReports getReportObject() {
		String paths = System.getProperty("user.dir") + "//reportss//index.html";
		ExtentSparkReporter reporter = new ExtentSparkReporter(paths);
		reporter.config().setReportName("WebAutomation Results");
		reporter.config().setDocumentTitle("Test Results");
		ExtentReports extent = new ExtentReports();
		extent.attachReporter(reporter);
		extent.setSystemInfo("Tester", "Arun kumar Chauhan");
		return extent;
	}
}