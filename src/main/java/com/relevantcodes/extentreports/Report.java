/*
* Copyright (c) 2015, Anshoo Arora (Relevant Codes).  All rights reserved.
* 
* Copyrights licensed under the New BSD License.
* 
* See the accompanying LICENSE file for terms.
*/

package com.relevantcodes.extentreports;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.relevantcodes.extentreports.converters.TimeConverter;
import com.relevantcodes.extentreports.model.Log;
import com.relevantcodes.extentreports.model.SuiteTimeInfo;
import com.relevantcodes.extentreports.model.Test;
import com.relevantcodes.extentreports.utils.DateTimeUtil;

import sachin.seobox.crawler.CrawlerConstants;

// Report abstract
abstract class Report extends LogSettings {
	protected static final Logger LOGGER = Logger.getLogger(Report.class.getName());

	/**
	 * <p>
	 * Path to the HTML report
	 */
	private String filePath;

	/**
	 * <p>
	 * Order in which tests will be displayed
	 * 
	 * <ul>
	 * <li>DisplayOrder.OLDEST_FIRST - Displays the oldest (1st run) test at the
	 * top</li>
	 * <li>DisplayOrder.NEWEST_FIRST - Displays the latest (last run) test at
	 * the top</li>
	 * </ul>
	 */
	private DisplayOrder displayOrder;

	/**
	 * <p>
	 * NetworkMode setting for the HTML report
	 * 
	 * <ul>
	 * <li>ONLINE - creates a single report file with all artifacts</li>
	 * <li>OFFLINE - all report artifacts will be stored locally in
	 * <code>%reportFolder%/extentreports</code> with the following structure:
	 * <br>
	 * - extentreports/css <br>
	 * - extentreports/js</li>
	 * </ul>
	 */
	private NetworkMode networkMode;

	/**
	 * <p>
	 * Setting to overwrite (TRUE) the existing file or append (FALSE) to it
	 * 
	 * <ul>
	 * <li>true - the file will be replaced with brand new markup, and all
	 * existing data will be lost. Use this option to create a brand new
	 * report</li>
	 * <li>false - existing data will remain, new tests will be appended to the
	 * existing report. If the the supplied path does not exist, a new file will
	 * be created.</li>
	 * </ul>
	 */
	private Boolean replaceExisting;

	/**
	 * <p>
	 * Default status of the report. This will be replace as logs are created.
	 * The final report status will be as per the top-most log status hierarchy
	 * as shown here:
	 * http://extentreports.relevantcodes.com/faqs.html#log-hierarchy
	 */
	private LogStatus reportStatus = LogStatus.UNKNOWN;

	/**
	 * <p>
	 * Report startime
	 */
	private Date startedTime;

	/**
	 * <p>
	 * Defines the total run duration in the past run, if the report is being
	 * appended
	 * 
	 * <p>
	 * Only applicable when <code>replaceExisting = false</code>
	 * 
	 * <p>
	 * Default value = 0
	 */
	private long totalDurationPastRun = 0; // millis

	/**
	 * <p>
	 * Run duration of the current suite
	 */
	private String currentSuiteRunDuration;

	/**
	 * <p>
	 * List of all logs created by the test runner
	 * 
	 * <p>
	 * Example: TestNG logs are created using <code>Reporter.log()</code> and
	 * retrieved using <code>Reporter.getOutput()</code>. It is possible to
	 * retrieve the logs and also update Extent using:
	 * 
	 * <p>
	 * <code>for (String log : Reporter.getOutput()) {</code>
	 * <code>    extent.setTestRunnerOutput(log)</code> <code>}</code>
	 */
	private List<String> testRunnerLogList;

	/**
	 * <p>
	 * List of all unique status logged
	 */
	private List<LogStatus> logStatusList;

	/**
	 * <p>
	 * List of active reporters
	 * 
	 * <p>
	 * HTMLReporter is the default report and will be started automatically
	 */
	private List<IReporter> reporters;

	/**
	 * <p>
	 * Current executing test
	 */
	private Test test;

	/**
	 * <p>
	 * UUID of the report
	 */
	private UUID reportId;

	/**
	 * <p>
	 * Flag to indicate if the execution is ended using the <code>close()</code>
	 * method. If true, prevent from creating/adding more tests.
	 */
	private Boolean terminated = false;

	/**
	 * <p>
	 * Default locale of the HTML report
	 */
	private Locale locale = Locale.ENGLISH;

	/**
	 * <p>
	 * Report ID from MongoDB->Report collection, used by ExtentX
	 */
	private String mongoDBID = "";

	/**
	 * <p>
	 * Name of the project, "Default" if nothing set
	 */
	private String projectName = "Default";

	protected SuiteTimeInfo suiteTimeInfo;
	protected SystemInfo systemInfo;

	protected List<LogStatus> getLogStatusList() {
		return logStatusList;
	}

	public Date getStartedTime() {
		return startedTime;
	}

	public String getRunDuration() {
		currentSuiteRunDuration = DateTimeUtil.getDiff(Calendar.getInstance().getTime(), new Date(suiteTimeInfo.getSuiteStartTimestamp()));
		return currentSuiteRunDuration;
	}

	public String getRunDurationOverall() {
		if (totalDurationPastRun == 0) {
			if (currentSuiteRunDuration == null) {
				getRunDuration();
			}
			return currentSuiteRunDuration;
		}

		long millis = (Calendar.getInstance().getTime().getTime() - startedTime.getTime());
		millis += CrawlerConstants.CRAWLING_TIME;
		long hours = TimeUnit.MILLISECONDS.toHours(millis);
		long mins = TimeUnit.MILLISECONDS.toMinutes(millis);
		long secs = TimeUnit.MILLISECONDS.toSeconds(millis);
		millis -= secs * 1000;

		return DateTimeUtil.getHMS(hours, mins, secs, millis);
	}

	protected void convertUpdateLastRunDuration() {
		totalDurationPastRun = new TimeConverter(filePath).getLastRunDurationMillis();
	}

	protected List<String> getTestRunnerLogList() {
		return testRunnerLogList;
	}

	protected SystemInfo getSystemInfo() {
		return systemInfo;
	}

	public Map<String, String> getSystemInfoMap() {
		return getSystemInfo().getInfo();
	}

	protected void attach(IReporter reporter) {
		if (reporters == null) {
			reporters = new ArrayList<>();
		}

		reporters.add(reporter);
		reporter.start(this);
	}

	protected void detach(IReporter reporter) {
		reporter.stop();
		reporters.remove(reporter);
	}

	protected void finalizeTest(Test test) {
		if (test.getEndedTime() == null) {
			test.setEndedTime(Calendar.getInstance().getTime());
		}
		Iterator<Log> logIter = test.logIterator();

		while (logIter.hasNext()) {
			Log log = logIter.next();

			if (log.getTimestamp().after(test.getEndedTime())) {
				log.setTimestamp(test.getEndedTime());
			}
		}

		test.prepareFinalize();

		this.test = test;

		for (IReporter reporter : reporters) {
			reporter.addTest(test);
		}

		updateReportStatus(test.getStatus());

		updateReportStartedTime(test);
	}

	private void updateReportStartedTime(Test test) {
		long testStartedTime = test.getStartedTime().getTime();

		if (suiteTimeInfo.getSuiteStartTimestamp() > testStartedTime) {
			suiteTimeInfo.setSuiteStartTimestamp(testStartedTime);
		}
	}

	protected void terminate() {
		flush();
		Iterator<IReporter> iter = reporters.iterator();
		while (iter.hasNext()) {
			iter.next().stop();
			iter.remove();
		}
		terminated = true;
	}

	protected void flush() {
		if (terminated) {
			try {
				throw new IOException("Unable to write source: Stream closed.");
			} catch (IOException e) {
				LOGGER.log(Level.SEVERE, "Stream closed", e);
			}
			return;
		}
		suiteTimeInfo.setSuiteEndTimestamp(new Date().getTime());
		for (IReporter reporter : reporters) {
			reporter.flush();
		}

	}

	protected void setTestRunnerLogs(String logs) {
		testRunnerLogList.add(logs);
	}

	protected Test getCurrentTest() {
		return test;
	}

	protected void setFilePath(String filePath) {
		this.filePath = filePath;
		File reportFile = new File(filePath);
		if (reportFile.getParentFile() != null && !reportFile.getParentFile().exists()) {
			reportFile.getParentFile().mkdirs();
		}
	}

	protected String getFilePath() {
		return filePath;
	}

	protected void setReplaceExisting(Boolean replaceExisting) {
		this.replaceExisting = replaceExisting;
	}

	protected Boolean getReplaceExisting() {
		return replaceExisting;
	}

	protected void setDisplayOrder(DisplayOrder displayOrder) {
		this.displayOrder = displayOrder;
	}

	protected DisplayOrder getDisplayOrder() {
		return displayOrder;
	}

	protected void setNetworkMode(NetworkMode networkMode) {
		this.networkMode = networkMode;
	}

	protected NetworkMode getNetworkMode() {
		return networkMode;
	}

	protected UUID getId() {
		return reportId;
	}

	protected LogStatus getStatus() {
		return reportStatus;
	}

	protected SuiteTimeInfo getSuiteTimeInfo() {
		return suiteTimeInfo;
	}

	protected void setStartedTime(long startTime) {
		suiteTimeInfo.setSuiteStartTimestamp(startTime);
	}

	protected void setDocumentLocale(Locale locale) {
		this.locale = locale;
	}

	protected Locale getDocumentLocale() {
		return locale;
	}

	protected void setProjectName(String name) {
		projectName = name;
	}

	public String getProjectName() {
		return projectName;
	}

	protected void setMongoDBObjectID(String id) {
		this.mongoDBID = id;
	}

	public String getMongoDBObjectID() {
		return mongoDBID;
	}

	protected Report() {
		systemInfo = new SystemInfo();
		suiteTimeInfo = new SuiteTimeInfo();
		testRunnerLogList = new ArrayList<>();
		logStatusList = new ArrayList<>();

		reportId = UUID.randomUUID();
		startedTime = new Date(suiteTimeInfo.getSuiteStartTimestamp());
	}

	private void updateReportStatus(LogStatus logStatus) {
		if (reportStatus == LogStatus.FATAL)
			return;

		if (logStatus == LogStatus.FATAL) {
			reportStatus = logStatus;
			return;
		}

		if (reportStatus == LogStatus.FAIL)
			return;

		if (logStatus == LogStatus.FAIL) {
			reportStatus = logStatus;
			return;
		}

		if (reportStatus == LogStatus.ERROR)
			return;

		if (logStatus == LogStatus.ERROR) {
			reportStatus = logStatus;
			return;
		}

		if (reportStatus == LogStatus.WARNING)
			return;

		if (logStatus == LogStatus.WARNING) {
			reportStatus = logStatus;
			return;
		}

		if (reportStatus == LogStatus.SKIP)
			return;

		if (logStatus == LogStatus.SKIP) {
			reportStatus = LogStatus.SKIP;
			return;
		}

		if (reportStatus == LogStatus.PASS)
			return;

		if (logStatus == LogStatus.PASS) {
			reportStatus = LogStatus.PASS;
			return;
		}

		if (reportStatus == LogStatus.INFO)
			return;

		if (logStatus == LogStatus.INFO) {
			reportStatus = LogStatus.INFO;
			return;
		}

		reportStatus = LogStatus.UNKNOWN;
	}
}
