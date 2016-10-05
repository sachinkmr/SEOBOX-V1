package sachin.seobox.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeTest;

import sachin.seobox.helpers.HelperUtils;

public class SEOConfig {

	public static int URL_CHARACTERS_LIMIT;
	public static int H1_CHARACTERS_LIMIT;
	public static int H2_CHARACTERS_LIMIT;
	public static int TITLE_CHARACTERS_LIMIT;
	public static int TITLE_CHARACTERS_LIMIT_MIN;
	public static int META_DESCRIPTION_CHARACTERS_LIMIT;
	public static int META_KEYWORDS_CHARACTERS_LIMIT;
	public static int IMAGE_ALT_TEXT_CHARACTERS_LIMIT;
	public static int CANONICAL_URL_CHARACTERS_LIMIT;
	public static int MAXIMUM_LINKS_COUNTS;
	public static int MAXIMUM_EXTERNAL_LINKS_COUNTS;
	public static byte CONTENT_HTML_RATIO;
	public static int MAXIMUM_RESPONSE_TIME;
	public static int MAXIMUM_IMAGE_SIZE;
	public static boolean MULTI_LINGUAL;
	public static String site;
	public static String user;
	public static String pass;
	public static Properties PROPERTIES;
	public static List<String> SKIPPED_URLS;
	public static String crawlStorageFolder;
	public static String dataLocation;
	public static Pattern pattern;
	public static Pattern shouldVisitPattern;
	public static boolean caseSensitive;
	public static String PROPERTIES_LOC = "";
	public static String reportPath;
	public static Pattern IMAGE_PATTERN = Pattern.compile("([^\\s]+(\\.(?i)(jpg|jpeg|png|gif|bmp)))",
			Pattern.CASE_INSENSITIVE);
	public static Pattern ASSETS_PATTERN = Pattern.compile("([^\\s]+(\\.(?i)(jpg|jpeg|png|gif|bmp|js|css)))",
			Pattern.CASE_INSENSITIVE);
	static {
		String outputDirectory = new File(
				System.getProperty("user.dir") + File.separator + "output" + File.separator + "Reports")
						.getAbsolutePath();
		SEOConfig.reportPath = outputDirectory + File.separator + "SEOBOX_Report" + HelperUtils.generateUniqueString()
				+ ".html";
		File storage = new File(System.getProperty("user.dir") + File.separator + "temp");
		storage.mkdirs();
		crawlStorageFolder = storage.getAbsolutePath();
	}

	@BeforeTest
	public void initSEOConfig() {
		PROPERTIES = new Properties();
		SKIPPED_URLS = new ArrayList<>();
		PROPERTIES_LOC = System.getProperty("CrawlerConfigFile");
		try {
			if (PROPERTIES_LOC == null || PROPERTIES_LOC.isEmpty() || !PROPERTIES_LOC.contains(".properties")
					|| !new File(PROPERTIES_LOC).exists()) {
				System.out.println("Loading default config file");
				PROPERTIES_LOC = HelperUtils.getResourceFile("Config.properties");
			}
			FileInputStream in = new FileInputStream(new File(PROPERTIES_LOC));
			PROPERTIES.load(in);
			in.close();
			String[] sites = PROPERTIES.getProperty("crawler.skipDomains").split(",");
			for (String site : sites) {
				SKIPPED_URLS.add(new URL(site).getHost().replaceAll("www.", ""));
			}
		} catch (IOException e) {
			LoggerFactory.getLogger(SEOConfig.class).debug("Error in loading config file", e);
		}
		String host = "host";
		try {
			host = new URL(site).getHost().replaceAll("www.", "");
		} catch (MalformedURLException e) {
			LoggerFactory.getLogger(SEOConfig.class).debug("Error in loading config file", e);
		}
		dataLocation = crawlStorageFolder + File.separator + host;
		new File(dataLocation).mkdirs();
		pattern = Pattern.compile(PROPERTIES.getProperty("crawler.domainRegex", "."), Pattern.CASE_INSENSITIVE);
		shouldVisitPattern = Pattern.compile(PROPERTIES.getProperty("crawler.linksToVisit", "."),
				Pattern.CASE_INSENSITIVE);
		caseSensitive = Boolean.parseBoolean(PROPERTIES.getProperty("crawler.caseSensitiveUrl", "false"));
		URL_CHARACTERS_LIMIT = Integer.parseInt(PROPERTIES.getProperty("page.url.word.count", "115"));
		H1_CHARACTERS_LIMIT = Integer.parseInt(PROPERTIES.getProperty("page.h1.word.count", "70"));
		H2_CHARACTERS_LIMIT = Integer.parseInt(PROPERTIES.getProperty("page.h2.word.count", "70"));
		TITLE_CHARACTERS_LIMIT = Integer.parseInt(PROPERTIES.getProperty("page.title.word.count", "65"));
		TITLE_CHARACTERS_LIMIT_MIN = Integer.parseInt(PROPERTIES.getProperty("page.title.word.countMin", "65"));
		META_DESCRIPTION_CHARACTERS_LIMIT = Integer
				.parseInt(PROPERTIES.getProperty("page.meta.description.word.count", "156"));
		META_KEYWORDS_CHARACTERS_LIMIT = Integer
				.parseInt(PROPERTIES.getProperty("page.meta.keywords.word.count", "156"));
		IMAGE_ALT_TEXT_CHARACTERS_LIMIT = Integer
				.parseInt(PROPERTIES.getProperty("page.image.alt.text.word.count", "100"));
		CANONICAL_URL_CHARACTERS_LIMIT = Integer
				.parseInt(PROPERTIES.getProperty("page.canonical.url.word.count", "115"));
		MAXIMUM_LINKS_COUNTS = Integer.parseInt(PROPERTIES.getProperty("page.maximum.links.count", "1000"));
		MAXIMUM_EXTERNAL_LINKS_COUNTS = Integer
				.parseInt(PROPERTIES.getProperty("page.maximum.externalLink.count", "100"));
		MAXIMUM_RESPONSE_TIME = Integer.parseInt(PROPERTIES.getProperty("link.maximumLoadTime", "2000"));
		MAXIMUM_IMAGE_SIZE = Integer.parseInt(PROPERTIES.getProperty("link.image.maxSize", "2000"));
		CONTENT_HTML_RATIO = Byte.parseByte(PROPERTIES.getProperty("page.contentAndHTML.ratio", "80"));
		MULTI_LINGUAL = Boolean.parseBoolean(PROPERTIES.getProperty("site.multilingual", "false"));
	}
}
