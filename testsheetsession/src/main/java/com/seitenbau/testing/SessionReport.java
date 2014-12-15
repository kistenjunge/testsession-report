package com.seitenbau.testing;

import com.seitenbau.testing.model.TaskBreakdown;
import com.seitenbau.testing.model.Testsheet;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@ToString
@Getter
/**
 * A test session reports holds the vital statistics taken form the actual test sheet.<br>
 * All numbers except Bugs, Issues, and Testers represent normal sessions. A normal session is about 90 minutes of uninterrupted test time by a single tester. An actual session may be worth more or less than a normal session depending on the duration of the session and the number of testers involved with it.
 */
public class SessionReport
{

  public static final double SESSION_LENGTH = 90;

  final Testsheet sheet;

  public SessionReport(@NonNull Testsheet sheet)
  {
    this.sheet = sheet;
    analyseSheetAndBuildReport();
  }

  /**
   * <b>title of the session report</b>
   */
  String title;

  /**
   * <b>the day the session started</b>
   */
  String date;

  /**
   * <b>the time the session started</b>
   */
  String time;

  /**
   * <b>the approximate duration of the session</b><br>
   * Duration is specified in terms of normal session units. Each session is worth about 90 minutes of uninterrupted tester attention.
   */
  Double duration;

  /**
   * <b>total amount of on-charter work</b><br>
   * The total amount of session work that was within the charter of each session.
   * This value plus the Opportunity value should equal the total amount of session work associated
   * with the corresponding coverage area. CHTR + OPP = TOTAL
   */
  Double timeOnCharter;

  /**
   * <b>total amount of off-charter work</b><br>
   * The amount of session work that was not within the charter of each session.
   * The TBS breakdown for opportunity testing is not reported. All we know is that the work was off the
   * subject of the specific charter. CHTR + OPP = TOTAL
   */
  Double timeOffCharter;

  /**
   * <b>amount of on-charter test design and execution</b><br>
   * The amount of on-charter work that was devoted to searching for bugs.
   * The higher this value, the more time was spent by testers productively testing. TEST + BUG + SETUP = CHTR.
   */
  Double timeTesting;

  /**
   * <b>amount of on-charter bug investigation and reporting that interrupted testing</b><br>
   * The amount of on-charter work that was devoted to investigating and reporting bugs.
   * This work is only reported if it interrupts bug searching. Thus, the fewer problems there are,
   * or the easier they are to investigate, the lower this value will be, and the more testing will get done. TEST + BUG + SETUP = CHTR.
   */
  Double timeBugHandling;

  /**
   * <b>amount of session setup work that interrupted testing</b><br>
   * The amount of on-charter work that was devoted to anything other than bug searching or bug investigation and reporting.
   * This work is only reported if it interrupts bug searching.
   * Typically, this category includes gathering information for testing, setting up equipment, or filling out the session reports.
   * Thus, the more organized the test process is, the lower this value will be, and the more testing will get done.
   * Chronically high setup values probably indicate that the test project is still getting up to speed. TEST + BUG + SETUP = CHTR.
   */
  Double timeSessionSetup;

  /**
   * <b>total number of bugs found in sessions associated with this coverage area</b><br>
   * The total number of bugs reported to the test lead during the session.
   * Not every bug reported to the test lead will be proper to report in the bug tracking system.
   */
  Integer numberOfBugs;

  /**
   * <b>total number of issues found in sessions associated with this coverage area</b><br>
   * The total number of issues reported in the session. Issues can be problems with the test
   * process or questions about the product that are escalated to the test lead.
   */
  Integer numberOfIssues;

  /**
   * <b>number of testers on the session</b><br>
   * The number of testers who were devoted to the session. A session with two testers counts as two sessions worth of work.
   */
  Integer numberOfTesters;

  /**
   * parses the actual sheet to provide the fields actually needed to produce a Report
   */
  private void analyseSheetAndBuildReport()
  {
    DateTime testTime = ISODateTimeFormat.dateTime().parseDateTime(sheet.getStart());
    date = testTime.toString("dd.MM.yyyy");
    time = testTime.toString("HH:mm");

    numberOfTesters = sheet.getTesters().size();
    double totalDuration = SESSION_LENGTH * numberOfTesters;

    generateAndSetTitle();

    TaskBreakdown breakdown = sheet.getTaskBreakdown();
    Integer totalCharter = breakdown.getDesignAndExecution() + breakdown.getSessionSetup() + breakdown.getBugInvestigationAndReporting();
    timeOnCharter = totalCharter / totalDuration;
    timeOffCharter = breakdown.getOpportunity() / totalDuration;
    this.duration = timeOnCharter + timeOffCharter;
    timeBugHandling = breakdown.getBugInvestigationAndReporting() / totalDuration;
    timeSessionSetup = breakdown.getSessionSetup() / totalDuration;
    timeTesting = breakdown.getDesignAndExecution() / totalDuration;

    numberOfBugs = sheet.getBugs().size();
    numberOfIssues = sheet.getIssues().size();
  }

  /**
   * Uses the execution date and the first tester, if any,  to generate a title
   */
  private void generateAndSetTitle()
  {
    DateTime testTime = ISODateTimeFormat.dateTime().parseDateTime(sheet.getStart());
    StringBuilder titleBuilder = new StringBuilder();
    titleBuilder
        .append(testTime.toString("yyyyMMdd"))
        .append("_")
    ;
    List<String> testers = sheet.getTesters();
    if(testers.size() > 0)
    {
      String firstTester = testers.get(0);
      String[] names = StringUtils.split(firstTester, " ");
      boolean isFirstName = true;
      for (String name : names)
      {
        String toAppend = name;
        if (isFirstName) {
          toAppend = name.substring(0, 1);
          isFirstName = false;
        }
        titleBuilder.append(toAppend.toLowerCase());
      }
    }
    else
    {
      titleBuilder.append("unknown");
    }
    title = titleBuilder.toString();
  }
}
