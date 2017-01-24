package sachin.seobox.common;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.TestNG;

import sachin.seobox.exception.SEOException;
import sachin.seobox.helpers.HelperUtils;
import sachin.seobox.reporter.ExtentReporterNG;

public class EntryPoint {
	protected static final Logger logger = LoggerFactory.getLogger(EntryPoint.class);

	public static void main(String[] args) {
		// System.setProperty("SiteAddress",
		// "http://www.dove.com/in/home.html");
		// System.setProperty("Username", "d2showcase");
		// System.setProperty("Password", "D2$0wca$3");
		try {
			if (null == System.getProperty("SiteAddress") || System.getProperty("SiteAddress").isEmpty()) {
				throw new SEOException("Site url is missing");
			}
			List<String> suites = new ArrayList<>();
			suites.add(HelperUtils.getResourceFile("testng.xml"));
			TestNG testng = new TestNG();
			testng.setTestSuites(suites);
			testng.addListener(new ExtentReporterNG());
			testng.setUseDefaultListeners(false);
			testng.setPreserveOrder(true);
			testng.setVerbose(0);
			testng.run();
		} catch (SEOException e) {
			logger.error("\n\nError in application: " + e + "\n\n");
		}
	}
}
