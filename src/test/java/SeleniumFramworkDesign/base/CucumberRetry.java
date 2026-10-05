package SeleniumFramworkDesign.base;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import io.cucumber.testng.PickleWrapper;

public class CucumberRetry implements IRetryAnalyzer {
	private static final Set<String> RETRIED_SCENARIOS = ConcurrentHashMap.newKeySet();

	@Override
	public boolean retry(ITestResult result) {
		Object[] parameters = result.getParameters();
		if (parameters.length == 0 || !(parameters[0] instanceof PickleWrapper)) {
			return false;
		}

		PickleWrapper pickle = (PickleWrapper) parameters[0];
		String scenarioId = pickle.getPickle().getUri() + ":" + pickle.getPickle().getLine();
		return RETRIED_SCENARIOS.add(scenarioId);
	}
}
