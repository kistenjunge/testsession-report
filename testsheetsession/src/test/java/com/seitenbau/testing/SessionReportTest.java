package com.seitenbau.testing;

import com.seitenbau.testing.model.TaskBreakdown;
import com.seitenbau.testing.model.Testsheet;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

public class SessionReportTest
{

  public static final double SESSION_DURATION = 90;

  @Test
  public void testSheetAnalyser() throws Exception
  {
    // given
    Testsheet testsheet = new TestSheetParser().parse(
        new File(TestSheetParserTest.class.getResource("/sheets/report_antisocial.json").toURI()));
    DateTime time = ISODateTimeFormat.dateTime().parseDateTime(testsheet.getStart());
    double expectedOffset = (double) 0;
    int expectedNumberOfTesters= testsheet.getTesters().size();
    TaskBreakdown breakdown = testsheet.getTaskBreakdown();
    double expectedOffCharter = breakdown.getOpportunity() / SESSION_DURATION * expectedNumberOfTesters;
    double expectedBugHandling = breakdown.getBugInvestigationAndReporting() / SESSION_DURATION * expectedNumberOfTesters;
    double expectedTesting = breakdown.getDesignAndExecution() / SESSION_DURATION * expectedNumberOfTesters;
    double expectedSetup = breakdown.getSessionSetup() / SESSION_DURATION * expectedNumberOfTesters;
    double expectedOnCharter = expectedBugHandling + expectedTesting + expectedSetup;

    // when
    SessionReport sessionReport = new SessionReport(testsheet);

    System.out.println(sessionReport);
    // then
    SessionReportAssert.assertThat(sessionReport)
        .hasNumberOfBugs(testsheet.getBugs().size())
        .hasNumberOfIssues(testsheet.getIssues().size())
        .hasNumberOfTesters(expectedNumberOfTesters)
        .hasTimeOnCharter(expectedOnCharter, expectedOffset)
        .hasTimeOffCharter(expectedOffCharter, expectedOffset)
        .hasTimeBugHandling(expectedBugHandling, expectedOffset)
        .hasTimeTesting(expectedTesting, expectedOffset)
        .hasTimeSessionSetup(expectedSetup, expectedOffset)
        .hasDuration(expectedOnCharter + expectedOffCharter, expectedOffset)
        .hasDate(time.toString("dd.MM.yyyy"))
        .hasTime(time.toString("HH:mm"));
    assertThat(sessionReport.getTitle()).isNotEmpty();

  }

}