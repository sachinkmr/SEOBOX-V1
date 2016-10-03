package sachin.seobox.seo;

import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import edu.uci.ics.crawler4j.url.WebURL;
import sachin.seobox.crawler.CrawlerConfig;
import sachin.seobox.helpers.HelperUtils;
import sachin.seobox.reporter.BaseReporting;

public class PageLevel extends BaseReporting {
	protected static final Logger logger = LoggerFactory.getLogger(PageLevel.class);

	@Test(description = "Verify that site does have all og tags", groups = { "OG Tags" })
	public void verifyOGTags() {
		for (SEOPage page : HelperUtils.getInternalPages()) {
			try {
				logger.debug("Verifying for: ", page.getPage().getWebURL());
				List<Element> list = page.getOgTags();
				boolean flag = true;
				for (Element element : list) {
					if (element.attr("property").equals("og:title") || element.attr("property").equals("og:type")
							|| element.attr("property").equals("og:url")
							|| element.attr("property").equals("og:image")) {
						flag = false;
					}
					if (null == element.attr("content") || element.attr("content").isEmpty()) {
						test.log(LogStatus.FAIL, "OG Tag content missing.<br/>" + element.attr("property"),
								page.getPage().getWebURL().getURL());
					}
				}
				if (flag) {
					test.log(LogStatus.FAIL, "Mendatory OG TAG Missing", page.getPage().getWebURL().getURL());
				} else {
					test.log(LogStatus.PASS, "All OG Tags are found.", page.getPage().getWebURL().getURL());
				}

			} catch (Exception e) {
				logger.debug("Error " + e);
				test.log(LogStatus.FAIL, "URL: " + page.getPage().getWebURL().getURL());
			}
		}
	}

	@Test(description = "Verify that page has NOODP Robots meta tags", groups = { "Robots Tags" })
	public void verifyNOODPTags() {
		for (SEOPage page : HelperUtils.getInternalPages()) {
			try {
				logger.debug("Verifying for: ", page.getPage().getWebURL());
				List<Element> list = page.getRobotsTags();
				for (Element e : list) {
					if (e.attr("content").isEmpty()) {
						test.log(LogStatus.FAIL, "Meta Robots Tags content is blank.<br/><b>URL: </b>"
								+ page.getPage().getWebURL().getURL());
					}
					if (e.attr("content").toUpperCase().contains("NOODP")) {
						test.log(LogStatus.INFO, "Meta Robots Tags NOODP is found.<br/><b>URL: </b>"
								+ page.getPage().getWebURL().getURL());
					}
				}
			} catch (Exception e) {
				logger.debug("Error occoured " + e);
				test.log(LogStatus.FAIL, "URL: " + page.getPage().getWebURL().getURL());
			}
		}
	}

	@Test(description = "Verify that page has NOYDIR Robots meta tags", groups = { "Robots Tags" })
	public void verifyNOYDIRTags() {

		for (SEOPage page : HelperUtils.getInternalPages()) {
			try {
				logger.debug("Verifying for: ", page.getPage().getWebURL());
				List<Element> list = page.getRobotsTags();
				for (Element e : list) {
					if (e.attr("content").isEmpty()) {
						test.log(LogStatus.FAIL, "Meta Robots Tags content is blank.<br/><b>URL: </b>"
								+ page.getPage().getWebURL().getURL());
					}
					if (e.attr("content").toUpperCase().contains("NOYDIR")) {
						test.log(LogStatus.INFO, "Meta Robots Tags NOYDIR is found.<br/><b>URL: </b>"
								+ page.getPage().getWebURL().getURL());
					}
				}
			} catch (Exception e) {
				logger.debug("Error " + e);
				test.log(LogStatus.FAIL, "URL: " + page.getPage().getWebURL().getURL());
			}
		}
	}

	@Test(description = "Verify that page has NOINDEX Robots meta tags", groups = { "Robots Tags" })
	public void verifyNOINDEXTags() {
		for (SEOPage page : HelperUtils.getInternalPages()) {
			try {
				logger.debug("Verifying for: ", page.getPage().getWebURL());
				List<Element> list = page.getRobotsTags();
				for (Element e : list) {
					if (e.attr("content").isEmpty()) {
						test.log(LogStatus.FAIL, "Meta Robots Tags content is blank.<br/><b>URL: </b>"
								+ page.getPage().getWebURL().getURL());
					}
					if (e.attr("content").toUpperCase().contains("NOINDEX")) {
						test.log(LogStatus.INFO, "Meta Robots Tags NOINDEX is found.<br/><b>URL: </b>"
								+ page.getPage().getWebURL().getURL());
					}
				}
			} catch (Exception e) {
				logger.debug("Error " + e);
				test.log(LogStatus.FAIL, "URL: " + page.getPage().getWebURL().getURL());
			}
		}
	}

	@Test(description = "Verify that page has NOFOLLOW Robots meta tags", groups = { "Robots Tags" })
	public void verifyNOFOLLOWTags() {
		for (SEOPage page : HelperUtils.getInternalPages()) {
			try {
				logger.debug("Verifying for: ", page.getPage().getWebURL());
				List<Element> list = page.getRobotsTags();
				for (Element e : list) {
					if (e.attr("content").isEmpty()) {
						test.log(LogStatus.FAIL, "Meta Robots Tags content is blank.<br/><b>URL: </b>"
								+ page.getPage().getWebURL().getURL());
					}
					if (e.attr("content").toUpperCase().contains("NOFOLLOW")) {
						test.log(LogStatus.INFO, "Meta Robots Tags NOYDIR is found.<br/><b>URL: </b>"
								+ page.getPage().getWebURL().getURL());
					}
				}
			} catch (Exception e) {
				logger.debug("Error " + e);
				test.log(LogStatus.FAIL, "URL: " + page.getPage().getWebURL().getURL());
			}
		}
	}

	@Test(description = "Verify that multilingual site does have HREF Language Tag in head tag", groups = {
			"HREF Language Tags" })
	public void verifyHREFLanguageTags() {
		if (SEOConfig.MULTI_LINGUAL) {
			for (SEOPage page : HelperUtils.getInternalPages()) {
				try {
					logger.debug("Verifying for: ", page.getPage().getWebURL());
					if (page.getPage().getStatusCode() == 200
							&& page.getPage().getContentType().contains("text/html")) {
						Document document = Jsoup.parse(page.getHtml(), CrawlerConfig.site);
						Element head = document.getElementsByTag("head").first();
						List<Element> links = head.select("link[rel=alternate]");
						if (links.isEmpty()) {
							test.log(LogStatus.FAIL, "HREF Language tag is missing.",
									page.getPage().getWebURL().getURL());
						} else {
							for (Element element : links) {
								if (element.hasAttr("hreflang") && !element.attr("hreflang").isEmpty()
										&& element.hasAttr("href") && !element.attr("href").isEmpty()) {
									test.log(LogStatus.PASS,
											"URL has HREF Language Tag.<br/>"
													+ element.toString().substring(1, element.toString().length() - 1),
											page.getPage().getWebURL().getURL());
								} else {
									test.log(LogStatus.FAIL, "hreflang or href are missing or empty.",
											page.getPage().getWebURL().getURL());
								}

							}
						}
					}
				} catch (Exception e) {
					logger.debug("Error " + e);
					test.log(LogStatus.FAIL, "URL: " + page.getPage().getWebURL().getURL());
				}
			}
		} else {
			test.log(LogStatus.PASS, "Site is not multi lengual. So this test case is not applicable");
		}
	}

	@Test(description = "Verify that page has only one H1 Tag", groups = { "H1 Tag" })
	public void verifyMultipleH1Tags() {
		for (SEOPage page : HelperUtils.getInternalPages()) {
			try {
				logger.debug("Verifying for: ", page.getPage().getWebURL());
				List<Element> list = page.getH1Tags();
				if (!list.isEmpty()) {
					if (list.size() == 1) {
						test.log(LogStatus.PASS, "Only one H1 Tags found", page.getPage().getWebURL().getURL());
					} else {
						test.log(LogStatus.FAIL, "Multiple H1 Tags on page", page.getPage().getWebURL().getURL());
					}
				}
			} catch (Exception e) {
				logger.debug("Error " + e);
				test.log(LogStatus.FAIL, "URL: " + page.getPage().getWebURL().getURL());
			}
		}
	}

	@Test(description = "Verify that page do not has missing H1 Tag", groups = { "H1 Tag" })
	public void verifyMissingH1Tags() {
		for (SEOPage page : HelperUtils.getInternalPages()) {
			try {
				logger.debug("Verifying for: ", page.getPage().getWebURL());
				List<Element> list = page.getH1Tags();
				if (!list.isEmpty()) {
					test.log(LogStatus.PASS, "Page has H1 Tag(s).", page.getPage().getWebURL().getURL());
				} else {
					test.log(LogStatus.FAIL, "H1 tags is missing from page", page.getPage().getWebURL().getURL());
				}
			} catch (Exception e) {
				logger.debug("Error " + e);
				test.log(LogStatus.FAIL, "URL: " + page.getPage().getWebURL().getURL());
			}
		}
	}

	@Test(description = "Verify that H1 Tag is not over character", groups = { "H1 Tag" })
	public void verifyOverCharacterH1Tags() {
		for (SEOPage page : HelperUtils.getInternalPages()) {
			try {
				logger.debug("Verifying for: ", page.getPage().getWebURL());
				List<Element> list = page.getH1Tags();
				if (!list.isEmpty()) {
					for (Element e : list) {
						if (e.text().length() <= SEOConfig.H1_CHARACTERS_LIMIT) {
							test.log(LogStatus.PASS,
									"H1 text is not over character.<br/><b>H1: </b>" + e.text() + "<br/><b>URl: </b>"
											+ page.getPage().getWebURL().getURL(),
									"<b>H1 Character Count: </b>" + e.text().length()
											+ "<br/><b>H1 Character Limit: </b>" + SEOConfig.H1_CHARACTERS_LIMIT);
						} else {
							test.log(LogStatus.FAIL,
									"H1 text is over character.<br/><b>H1: </b>" + e.text() + "<br/><b>URl: </b>"
											+ page.getPage().getWebURL().getURL(),
									"<b>H1 Character Count: </b>" + e.text().length()
											+ "<br/><b>H1 Character Limit: </b>" + SEOConfig.H1_CHARACTERS_LIMIT);
						}
					}
				}
			} catch (Exception e) {
				logger.debug("Error " + e);
				test.log(LogStatus.FAIL, "URL: " + page.getPage().getWebURL().getURL());
			}
		}
	}

	@Test(description = "Verify that H1 Tag is not blank", groups = { "H1 Tag" })
	public void verifyBlankH1Tags() {
		for (SEOPage page : HelperUtils.getInternalPages()) {
			try {
				logger.debug("Verifying for: ", page.getPage().getWebURL());
				List<Element> list = page.getH1Tags();
				if (!list.isEmpty()) {
					for (Element e : list) {
						if (!e.text().isEmpty()) {
							test.log(LogStatus.PASS, "H1 text is not blank.<br/><b>H1: </b>" + e.text()
									+ "<br/><b>URl: </b>" + page.getPage().getWebURL().getURL());
						} else {
							test.log(LogStatus.FAIL,
									"H1 text is blank.<br/><b>URl: </b>" + page.getPage().getWebURL().getURL());
						}
					}
				}
			} catch (Exception e) {
				logger.debug("Error " + e);
				test.log(LogStatus.FAIL, "URL: " + page.getPage().getWebURL().getURL());
			}
		}
	}

	@Test(description = "Verify that H2 Tag is not over character", groups = { "H2 Tag" })
	public void verifyOverCharacterH2Tags() {
		for (SEOPage page : HelperUtils.getInternalPages()) {
			try {
				logger.debug("Verifying for: ", page.getPage().getWebURL());
				List<Element> list = page.getH2Tags();
				if (!list.isEmpty()) {
					for (Element e : list) {
						if (e.text().length() <= SEOConfig.H2_CHARACTERS_LIMIT) {
							test.log(LogStatus.PASS,
									"H2 text is not over character.<br/><b>H2: </b>" + e.text() + "<br/><b>URL: </b>"
											+ page.getPage().getWebURL().getURL(),
									"<b>H2 Character Count: </b>" + e.text().length()
											+ "<br/><b>H2 Character Limit: </b>" + SEOConfig.H2_CHARACTERS_LIMIT);
						} else {
							test.log(LogStatus.FAIL,
									"H2 text is over character.<br/><b>H2: </b>" + e.text() + "<br/><b>URL: </b>"
											+ page.getPage().getWebURL().getURL(),
									"<b>H2 Character Count: </b>" + e.text().length()
											+ "<br/><b>H2 Character Limit: </b>" + SEOConfig.H2_CHARACTERS_LIMIT);
						}
					}
				}
			} catch (Exception e) {
				logger.debug("Error " + e);
				test.log(LogStatus.FAIL, "URL: " + page.getPage().getWebURL().getURL());
			}
		}
	}

	@Test(description = "Verify that H2 Tag is not blank", groups = { "H2 Tag" })
	public void verifyBlankH2Tags() {
		for (SEOPage page : HelperUtils.getInternalPages()) {
			try {
				logger.debug("Verifying for: ", page.getPage().getWebURL());
				List<Element> list = page.getH2Tags();
				if (!list.isEmpty()) {
					for (Element e : list) {
						if (!e.text().isEmpty()) {
							test.log(LogStatus.PASS, "H2 text is not blank.<br/><b>H2: </b>" + e.text()
									+ "<br/><b>URL: </b>" + page.getPage().getWebURL().getURL());
						} else {
							test.log(LogStatus.FAIL,
									"H2 text is blank.<br/><b>URL: </b>" + page.getPage().getWebURL().getURL());
						}
					}
				}
			} catch (Exception e) {
				logger.debug("Error " + e);
				test.log(LogStatus.FAIL, "URL: " + page.getPage().getWebURL().getURL());
			}
		}
	}

	@Test(description = "Verify that image alt text is not missing", groups = { "Image Alt Text" })
	public void verifyImageAltText() {
		for (SEOPage page : HelperUtils.getInternalPages()) {
			try {
				logger.debug("Verifying for: ", page.getPage().getWebURL());
				List<Element> list = page.getImages();
				if (!list.isEmpty()) {
					for (Element e : list) {
						if (e.hasAttr("alt") && !e.attr("alt").isEmpty()) {
							test.log(LogStatus.PASS,
									"Image has alt Text.<br/><b>Alt Text: </b>" + e.attr("alt")
											+ "<br/><b>Page URL: </b>" + page.getPage().getWebURL().getURL()
											+ "<br/><b>Image URL: </b>" + e.attr("abs:src"));

						} else {
							test.log(LogStatus.FAIL,
									"Image does not has alt Text.<br/><b>Page URL: </b>"
											+ page.getPage().getWebURL().getURL() + "<br/><b>Image URL: </b>"
											+ e.attr("abs:src"));
						}
					}
				}
			} catch (Exception e) {
				logger.debug("Error " + e);
				test.log(LogStatus.FAIL, "URL: " + page.getPage().getWebURL().getURL());
			}
		}
	}

	@Test(description = "Verify that Content/HTML Ratio does not exceed on page", groups = { "Content/HTML Ratio" })
	public void contentAndHTMLRatio() {
		for (SEOPage page : HelperUtils.getInternalPages()) {
			try {
				logger.debug("Verifying for: ", page.getPage().getWebURL());
				String html = page.getHtml();
				String content = Jsoup.parse(html).text();
				byte ratio = (byte) ((content.length() * 100) / html.length());
				if (ratio <= SEOConfig.CONTENT_HTML_RATIO) {
					test.log(LogStatus.PASS,
							"Content/HTML Ratio is within range. <br/><b>URL: </b>"
									+ page.getPage().getWebURL().getURL(),
							"<b>Page Ratio: </b>" + ratio + "<br/><b>Recommended: </b>" + SEOConfig.CONTENT_HTML_RATIO);
				} else {
					test.log(LogStatus.FAIL,
							"Content/HTML Ratio is not within range. <br/><b>URL: </b>"
									+ page.getPage().getWebURL().getURL(),
							"<b>Page Ratio: </b>" + ratio + "<br/><b>Recommended: </b>" + SEOConfig.CONTENT_HTML_RATIO);
				}
			} catch (Exception e) {
				logger.debug("Error " + e);
				test.log(LogStatus.FAIL, "URL: " + page.getPage().getWebURL().getURL());
			}
		}
	}

	@Test(description = "Verify Internal outgoing links count on the page", groups = { "Links" })
	public void verifyInternalOutgoingLinksCount() {
		for (SEOPage page : HelperUtils.getInternalPages()) {
			try {
				logger.debug("Verifying for: ", page.getPage().getWebURL());
				Set<WebURL> links = page.getPage().getParseData().getOutgoingUrls();
				int x = 0;
				for (WebURL url : links) {
					if (url.isInternalLink())
						x++;
				}
				if (x > SEOConfig.MAXIMUM_LINKS_COUNTS) {
					test.log(LogStatus.FAIL,
							"Internal outgoing links count is exceeding. <br/><b>URL: </b>"
									+ page.getPage().getWebURL().getURL(),
							"<b>Links Count: </b>" + x + "<br/><b>Recommended: </b>" + SEOConfig.MAXIMUM_LINKS_COUNTS);
				} else {
					test.log(LogStatus.PASS,
							"Internal outgoing links count is withing range. <br/><b>URL: </b>"
									+ page.getPage().getWebURL().getURL(),
							"<b>Links Count: </b>" + x + "<br/><b>Recommended: </b>" + SEOConfig.MAXIMUM_LINKS_COUNTS);
				}
			} catch (Exception e) {
				logger.debug("Error " + e);
				test.log(LogStatus.FAIL, "URL: " + page.getPage().getWebURL().getURL());
			}
		}
	}

	@Test(description = "Verify External outgoing links count on the page", groups = { "Links" })
	public void verifyExternalOutgoingLinksCount() {
		for (SEOPage page : HelperUtils.getInternalPages()) {
			try {
				logger.debug("Verifying for: ", page.getPage().getWebURL());
				Set<WebURL> links = page.getPage().getParseData().getOutgoingUrls();
				int x = 0;
				for (WebURL url : links) {
					if (!url.isInternalLink())
						x++;
				}
				if (x > SEOConfig.MAXIMUM_EXTERNAL_LINKS_COUNTS) {
					test.log(LogStatus.FAIL,
							"External outgoing links count is exceeding. <br/><b>URL: </b>"
									+ page.getPage().getWebURL().getURL(),
							"<b>Links Count: </b>" + x + "<br/><b>Recommended: </b>"
									+ SEOConfig.MAXIMUM_EXTERNAL_LINKS_COUNTS);
				} else {
					test.log(LogStatus.PASS,
							"External outgoing links count is withing range. <br/><b>URL: </b>"
									+ page.getPage().getWebURL().getURL(),
							"<b>Links Count: </b>" + x + "<br/><b>Recommended: </b>"
									+ SEOConfig.MAXIMUM_EXTERNAL_LINKS_COUNTS);
				}
			} catch (Exception e) {
				logger.debug("Error " + e);
				test.log(LogStatus.FAIL, "URL: " + page.getPage().getWebURL().getURL());
			}
		}

	}

	@Test(description = "Verify missing title tag on page", groups = { "Title Tag" })
	public void verifyMissingTitle() {
		for (SEOPage page : HelperUtils.getInternalPages()) {
			try {
				logger.debug("Verifying for: ", page.getPage().getWebURL());
				List<Element> links = page.getTitle();
				if (links.isEmpty()) {
					test.log(LogStatus.FAIL,
							"Title tag is missing.<br/><b>URL: </b>" + page.getPage().getWebURL().getURL());
				} else {
					test.log(LogStatus.PASS, "Title tag found.<br/><b>URL: </b>" + page.getPage().getWebURL().getURL());

				}
			} catch (Exception e) {
				logger.debug("Error " + e);
				test.log(LogStatus.FAIL, "URL: " + page.getPage().getWebURL().getURL());
			}
		}
	}

	@Test(description = "Verify multiple title tag on page", groups = { "Title Tag" })
	public void verifyMultipleTitle() {

		for (SEOPage page : HelperUtils.getInternalPages()) {
			try {
				logger.debug("Verifying for: ", page.getPage().getWebURL());
				List<Element> links = page.getTitle();
				if (links.size() > 1) {
					test.log(LogStatus.FAIL,
							"Multiple title tags found.<br/><b>URL: </b>" + page.getPage().getWebURL().getURL());
				} else {
					test.log(LogStatus.PASS,
							"There are no multiple title tags.<br/><b>URL: </b>" + page.getPage().getWebURL().getURL());

				}
			} catch (Exception e) {
				logger.debug("Error " + e);
				test.log(LogStatus.FAIL, "URL: " + page.getPage().getWebURL().getURL());
			}
		}
	}

	@Test(description = "Verify minimum length for title tag", groups = { "Title Tag" })
	public void verifyTitleMinimumLength() {
		for (SEOPage page : HelperUtils.getInternalPages()) {
			try {
				logger.debug("Verifying for: ", page.getPage().getWebURL());
				List<Element> links = page.getTitle();
				for (Element e : links) {
					if (e.text().length() < SEOConfig.TITLE_CHARACTERS_LIMIT_MIN) {
						test.log(LogStatus.FAIL,
								"Title Length is less than required length.<br/><b>Title: </b>" + e.text()
										+ " <br/><b>URL: </b>" + page.getPage().getWebURL().getURL(),
								"<b>Title Length: </b>" + e.text().length() + "<br/><b>Recommended: </b>"
										+ SEOConfig.TITLE_CHARACTERS_LIMIT_MIN);
					} else {
						test.log(LogStatus.PASS,
								"Title Length is as expected.<br/><b>Title: </b>" + e.text() + " <br/><b>URL: </b>"
										+ page.getPage().getWebURL().getURL(),
								"<b>Title Length: </b>" + e.text().length() + "<br/><b>Recommended: </b>"
										+ SEOConfig.TITLE_CHARACTERS_LIMIT_MIN);
					}
				}
			} catch (Exception e) {
				logger.debug("Error " + e);
				test.log(LogStatus.FAIL, "URL: " + page.getPage().getWebURL().getURL());
			}
		}
	}

	@Test(description = "Verify maximum length for title tag", groups = { "Title Tag" })
	public void verifyTitleMaximumLength() {
		for (SEOPage page : HelperUtils.getInternalPages()) {
			try {
				logger.debug("Verifying for: ", page.getPage().getWebURL());
				List<Element> links = page.getTitle();
				for (Element e : links) {
					if (e.text().length() > SEOConfig.TITLE_CHARACTERS_LIMIT) {
						test.log(LogStatus.FAIL,
								"Title Length is greater than required length.<br/><b>Title: </b>" + e.text()
										+ " <br/><b>URL: </b>" + page.getPage().getWebURL().getURL(),
								"<b>Title Length: </b>" + e.text().length() + "<br/><b>Recommended: </b>"
										+ SEOConfig.TITLE_CHARACTERS_LIMIT);
					} else {
						test.log(LogStatus.PASS,
								"Title Length is as expected.<br/><b>Title: </b>" + e.text() + " <br/><b>URL: </b>"
										+ page.getPage().getWebURL().getURL(),
								"<b>Title Length: </b>" + e.text().length() + "<br/><b>Recommended: </b>"
										+ SEOConfig.TITLE_CHARACTERS_LIMIT);
					}
				}
			} catch (Exception e) {
				logger.debug("Error " + e);
				test.log(LogStatus.FAIL, "URL: " + page.getPage().getWebURL().getURL());
			}
		}

	}

	@Test(description = "Verify length for description tag", groups = { "Meta Description" })
	public void verifyDescriptionLength() {
		for (SEOPage page : HelperUtils.getInternalPages()) {
			try {
				logger.debug("Verifying for: ", page.getPage().getWebURL());
				List<Element> links = page.getMetaDescription();
				for (Element e : links) {
					if (e.attr("content").length() > SEOConfig.META_DESCRIPTION_CHARACTERS_LIMIT) {
						test.log(LogStatus.FAIL,
								"Meta Description Length is greater than required length.<br/><b>Description: </b>"
										+ e.attr("content") + " <br/><b>URL: </b>"
										+ page.getPage().getWebURL().getURL(),
								"<b>Length: </b>" + e.attr("content").length() + "<br/><b>Recommended: </b>"
										+ SEOConfig.META_DESCRIPTION_CHARACTERS_LIMIT);
					} else {
						test.log(LogStatus.PASS,
								"Meta Description is as expected.<br/><b>Description: </b>" + e.attr("content")
										+ " <br/><b>URL: </b>" + page.getPage().getWebURL().getURL(),
								"<b>Length: </b>" + e.attr("content").length() + "<br/><b>Recommended: </b>"
										+ SEOConfig.META_DESCRIPTION_CHARACTERS_LIMIT);
					}
				}
			} catch (Exception e) {
				logger.debug("Error " + e);
				test.log(LogStatus.FAIL, "URL: " + page.getPage().getWebURL().getURL());
			}
		}

	}

	@Test(description = "Verify that description tag content is not blank", groups = { "Meta Description" })
	public void verifyBlankDescription() {
		for (SEOPage page : HelperUtils.getInternalPages()) {
			try {
				logger.debug("Verifying for: ", page.getPage().getWebURL());
				List<Element> links = page.getMetaDescription();
				for (Element e : links) {
					if (e.attr("content").isEmpty()) {
						test.log(LogStatus.FAIL, "Meta Description content is blank.<br/><b>URL: </b>"
								+ page.getPage().getWebURL().getURL());
					} else {
						test.log(LogStatus.PASS, "Meta Description is not blank.<br/><b>Description: </b>"
								+ e.attr("content") + " <br/><b>URL: </b>" + page.getPage().getWebURL().getURL());
					}
				}
			} catch (Exception e) {
				logger.debug("Error " + e);
				test.log(LogStatus.FAIL, "URL: " + page.getPage().getWebURL().getURL());
			}
		}
	}

	@Test(description = "Verify for missing description for page", groups = { "Meta Description" })
	public void verifyMissingDescription() {
		for (SEOPage page : HelperUtils.getInternalPages()) {
			try {
				logger.debug("Verifying for: ", page.getPage().getWebURL());
				List<Element> links = page.getMetaDescription();
				if (links.isEmpty()) {
					test.log(LogStatus.FAIL,
							"Meta Description is missing <br/><b>URL: </b>" + page.getPage().getWebURL().getURL());
				} else {
					test.log(LogStatus.PASS,
							"Meta Description found<br/><b>URL: </b>" + page.getPage().getWebURL().getURL());
				}
			} catch (Exception e) {
				logger.debug("Error " + e);
				test.log(LogStatus.FAIL, "URL: " + page.getPage().getWebURL().getURL());
			}
		}
	}

	@Test(description = "Verify for multiple description for page", groups = { "Meta Description" })
	public void verifyMultipleDescription() {
		for (SEOPage page : HelperUtils.getInternalPages()) {
			try {
				logger.debug("Verifying for: ", page.getPage().getWebURL());
				List<Element> links = page.getMetaDescription();
				if (links.size() > 1) {
					test.log(LogStatus.FAIL,
							"Meta Description are multiple <br/><b>URL: </b>" + page.getPage().getWebURL().getURL());
				} else {
					test.log(LogStatus.PASS, "There are no multiple description tags.<br/><b>URL: </b>"
							+ page.getPage().getWebURL().getURL());
				}
			} catch (Exception e) {
				logger.debug("Error " + e);
				test.log(LogStatus.FAIL, "URL: " + page.getPage().getWebURL().getURL());
			}
		}
	}

	@Test(description = "Verify length for canonical tag", groups = { "Canonical Tag" })
	public void verifyCanonicalLength() {
		for (SEOPage page : HelperUtils.getInternalPages()) {
			try {
				logger.debug("Verifying for: ", page.getPage().getWebURL());
				List<Element> links = page.getCanonical();
				for (Element e : links) {
					if (e.attr("href").length() > SEOConfig.CANONICAL_URL_CHARACTERS_LIMIT) {
						test.log(LogStatus.FAIL,
								"Canonical URL Length is greater than required length.<br/><b>Canonical URL: </b>"
										+ e.attr("href") + " <br/><b>Page URL: </b>"
										+ page.getPage().getWebURL().getURL(),
								"<b>Length: </b>" + e.attr("href").length() + "<br/><b>Recommended: </b>"
										+ SEOConfig.CANONICAL_URL_CHARACTERS_LIMIT);
					} else {
						test.log(LogStatus.PASS,
								"Canonical URL Length is as expected.<br/><b>Canonical URL: </b>" + e.attr("href")
										+ " <br/><b>Page URL: </b>" + page.getPage().getWebURL().getURL(),
								"<b>Length: </b>" + e.attr("href").length() + "<br/><b>Recommended: </b>"
										+ SEOConfig.CANONICAL_URL_CHARACTERS_LIMIT);
					}
				}
			} catch (Exception e) {
				logger.debug("Error " + e);
				test.log(LogStatus.FAIL, "URL: " + page.getPage().getWebURL().getURL());
			}
		}
	}

	@Test(description = "Verify for blank canonical tag url", groups = { "Canonical Tag" })
	public void verifyBlankCanonicalURL() {

		for (SEOPage page : HelperUtils.getInternalPages()) {
			try {
				logger.debug("Verifying for: ", page.getPage().getWebURL());
				List<Element> links = page.getCanonical();
				for (Element e : links) {
					if (e.attr("href").isEmpty()) {
						test.log(LogStatus.FAIL,
								"Canonical URL is empty. <br/><b>Page URL: </b>" + page.getPage().getWebURL().getURL(),
								"<b>Length: </b>" + e.attr("href").length() + "<br/><b>Recommended: </b>"
										+ SEOConfig.CANONICAL_URL_CHARACTERS_LIMIT);
					} else {
						test.log(LogStatus.PASS,
								"Canonical URL is not empty.<br/><b>Canonical URL: </b>" + e.attr("href")
										+ " <br/><b>Page URL: </b>" + page.getPage().getWebURL().getURL(),
								"<b>Length: </b>" + e.attr("href").length() + "<br/><b>Recommended: </b>"
										+ SEOConfig.CANONICAL_URL_CHARACTERS_LIMIT);
					}
				}
			} catch (Exception e) {
				logger.debug("Error " + e);
				test.log(LogStatus.FAIL, "URL: " + page.getPage().getWebURL().getURL());
			}
		}
	}

	@Test(description = "Verify for missing canonical tags for page", groups = { "Canonical Tag" })
	public void verifyMissingCanonicalTags() {
		for (SEOPage page : HelperUtils.getInternalPages()) {
			try {
				logger.debug("Verifying for: ", page.getPage().getWebURL());
				List<Element> links = page.getCanonical();
				if (links.isEmpty()) {
					test.log(LogStatus.FAIL,
							"Canonical tag is missing <br/><b>URL: </b>" + page.getPage().getWebURL().getURL());
				} else {
					test.log(LogStatus.PASS,
							"Canonical tag found<br/><b>URL: </b>" + page.getPage().getWebURL().getURL());
				}
			} catch (Exception e) {
				logger.debug("Error " + e);
				test.log(LogStatus.FAIL, "URL: " + page.getPage().getWebURL().getURL());
			}
		}
	}

	@Test(description = "Verify for multiple canonical tags for page", groups = { "Canonical Tag" })
	public void verifyMultipleCanonicalTags() {
		for (SEOPage page : HelperUtils.getInternalPages()) {
			try {
				logger.debug("Verifying for: ", page.getPage().getWebURL());
				List<Element> links = page.getCanonical();
				if (links.size() > 1) {
					test.log(LogStatus.FAIL,
							"Canonical tags are multiple <br/><b>URL: </b>" + page.getPage().getWebURL().getURL());
				} else {
					test.log(LogStatus.PASS, "There are no multiple canonical tags.<br/><b>URL: </b>"
							+ page.getPage().getWebURL().getURL());
				}
			} catch (Exception e) {
				logger.debug("Error " + e);
				test.log(LogStatus.FAIL, "URL: " + page.getPage().getWebURL().getURL());
			}
		}
	}

	@Test(description = "Verify that if page has tables", groups = { "Div instead of Table" })
	public void useDivInsteadOfTable() {
		for (SEOPage page : HelperUtils.getInternalPages()) {
			try {
				logger.debug("Verifying for: ", page.getPage().getWebURL());
				List<Element> links = Jsoup.parse(page.getHtml()).select("table");
				if (links.size() > 0) {
					test.log(LogStatus.FAIL, "page has tables <br/><b>URL: </b>" + page.getPage().getWebURL().getURL());
				} else {
					test.log(LogStatus.PASS,
							"There are no tables in page.<br/><b>URL: </b>" + page.getPage().getWebURL().getURL());
				}
			} catch (Exception e) {
				logger.debug("Error " + e);
				test.log(LogStatus.FAIL, "URL: " + page.getPage().getWebURL().getURL());
			}
		}
	}

	@Test(description = "Verify that if page has frames", groups = { "No Frames" })
	public void verifyFrames() {
		for (SEOPage page : HelperUtils.getInternalPages()) {
			try {
				logger.debug("Verifying for: ", page.getPage().getWebURL());
				List<Element> links = Jsoup.parse(page.getHtml()).select("frame");
				if (links.size() > 0) {
					test.log(LogStatus.FAIL, "page has frames <br/><b>URL: </b>" + page.getPage().getWebURL().getURL());
				} else {
					test.log(LogStatus.PASS,
							"There are no frames in page.<br/><b>URL: </b>" + page.getPage().getWebURL().getURL());
				}
			} catch (Exception e) {
				logger.debug("Error " + e);
				test.log(LogStatus.FAIL, "URL: " + page.getPage().getWebURL().getURL());
			}
		}

	}

	@Test(description = "Verify that if page has flash content", groups = { "No Flash" })
	public void verifyFlash() {
		for (SEOPage page : HelperUtils.getInternalPages()) {
			try {
				logger.debug("Verifying for: ", page.getPage().getWebURL());
				List<Element> links = Jsoup.parse(page.getHtml()).select("*[data]");
				if (links.size() > 0) {
					boolean flag = false;
					for (Element e : links) {
						if (e.attr("data").toLowerCase().contains(".swf")) {
							test.log(LogStatus.FAIL,
									"page has flash content <br/><b>URL: </b>" + page.getPage().getWebURL().getURL());
							flag = true;
						}
					}
					if (!flag) {
						test.log(LogStatus.PASS, "There is no flash content in page.<br/><b>URL: </b>"
								+ page.getPage().getWebURL().getURL());
					}
				} else {
					test.log(LogStatus.PASS, "There is no flash content in page.<br/><b>URL: </b>"
							+ page.getPage().getWebURL().getURL());
				}
			} catch (Exception e) {
				logger.debug("Error " + e);
				test.log(LogStatus.FAIL, "URL: " + page.getPage().getWebURL().getURL());
			}
		}
	}
}