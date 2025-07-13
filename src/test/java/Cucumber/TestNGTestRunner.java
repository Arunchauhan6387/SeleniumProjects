package Cucumber;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features="src/test/java/Cucumber",
				 glue="SeleniumFramworkDesign.stepDefinitions",
				 tags="@Regression",
				 monochrome=true,
				 plugin= {"html:target/cucmber.html"})
public class TestNGTestRunner extends AbstractTestNGCucumberTests{
	
	
}
