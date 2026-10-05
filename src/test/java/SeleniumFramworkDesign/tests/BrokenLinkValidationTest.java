package SeleniumFramworkDesign.tests;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import SeleniumFramworkDesign.base.BaseTest;
import SeleniumFramworkDesign.pages.ProductCatalogue;

public class BrokenLinkValidationTest extends BaseTest {
	private static final int REQUEST_TIMEOUT_SECONDS = 8;

	@Test(groups = "LinkValidation")
	public void validateDashboardLinks() throws IOException, InterruptedException {
		ProductCatalogue catalogue = landingpage.loginApplication(
				getRequiredSetting("TEST_USER_EMAIL"), getRequiredSetting("TEST_USER_PASSWORD"));
		catalogue.getProductList();

		Set<String> links = new LinkedHashSet<>();
		for (WebElement anchor : driver.findElements(By.cssSelector("a[href]"))) {
			String href = anchor.getAttribute("href");
			if (href != null && !href.isBlank() && !href.startsWith("#")) {
				links.add(href);
			}
		}

		HttpClient client = HttpClient.newBuilder()
				.connectTimeout(Duration.ofSeconds(REQUEST_TIMEOUT_SECONDS))
				.followRedirects(HttpClient.Redirect.NORMAL)
				.build();
		List<String> failures = new ArrayList<>();
		int checkedLinks = 0;
		for (String link : links) {
			try {
				URI uri = URI.create(link);
				String scheme = uri.getScheme();
				if (!"http".equalsIgnoreCase(scheme) && !"https".equalsIgnoreCase(scheme)) {
					continue;
				}

				checkedLinks++;
				int statusCode = requestStatus(client, uri);
				if (statusCode >= 400) {
					failures.add(uri.getHost() + uri.getPath() + " returned HTTP " + statusCode);
				}
			} catch (IllegalArgumentException | IOException e) {
				failures.add(safeLinkDescription(link) + " could not be checked: " + e.getMessage());
			}
		}

		Assert.assertTrue(checkedLinks > 0, "No HTTP(S) links were found to validate.");
		Assert.assertTrue(failures.isEmpty(), "Broken or unreachable links found: " + String.join("; ", failures));
	}

	private int requestStatus(HttpClient client, URI uri) throws IOException, InterruptedException {
		HttpRequest headRequest = HttpRequest.newBuilder(uri)
				.timeout(Duration.ofSeconds(REQUEST_TIMEOUT_SECONDS))
				.method("HEAD", HttpRequest.BodyPublishers.noBody())
				.build();
		HttpResponse<Void> response = client.send(headRequest, HttpResponse.BodyHandlers.discarding());
		if (response.statusCode() != 403 && response.statusCode() != 405 && response.statusCode() != 501) {
			return response.statusCode();
		}

		HttpRequest getRequest = HttpRequest.newBuilder(uri)
				.timeout(Duration.ofSeconds(REQUEST_TIMEOUT_SECONDS))
				.GET()
				.build();
		return client.send(getRequest, HttpResponse.BodyHandlers.discarding()).statusCode();
	}

	private String safeLinkDescription(String link) {
		try {
			URI uri = URI.create(link);
			return uri.getHost() + uri.getPath();
		} catch (IllegalArgumentException e) {
			return "Invalid href";
		}
	}
}
