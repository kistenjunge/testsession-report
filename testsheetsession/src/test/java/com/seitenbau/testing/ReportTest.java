package com.seitenbau.testing;

import com.google.common.collect.ArrayListMultimap;
import com.seitenbau.testing.model.Project;
import org.assertj.core.util.Lists;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;

import static com.seitenbau.testing.TestSheetUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

public class ReportTest
{
  final Project project;

  {
    project = new Project();
    project.setName("Testproject");
    project.getComponents().addAll(Lists.newArrayList(
        "Ausschreibung erstellen",
        "Formulardesigner",
        "Handbuch",
        "Benutzerverwaltung",
        "Suche",
        "Gesuch einreichen",
        "Inhaltliche Prüfung",
        "Formale Prüfung",
        "Budget",
        "Jurying"));
  }

  @Test
  public void testReportAreas() throws Exception
  {
    // given
    Report sut = new Report(project);

    // when
    sut.addAll(getAllSessionReports());
    ArrayListMultimap<String, SessionReport> areasToSessions = sut.getAreasToSessions();

    // then
    HashSet<String> testedAreas = getAllTestedAreas();
    testedAreas.addAll(project.getComponents());
    assertThat(areasToSessions.keySet()).containsOnly(testedAreas.toArray(new String[] {}));
  }

  @Test
  public void testReportSummary() throws Exception
  {
    // given
    Report sut = new Report(project);
    String area = "Budget";

    // when
    sut.addAll(getAllSessionReports());
    ArrayList<AreaSummary> summaryForAllAreas = sut.getSummaryForAllAreas();

    // then
    for (AreaSummary areaSummary : summaryForAllAreas)
    {
      System.out.println(areaSummary);
    }
  }

  @Test
  public void testReportOverview() throws Exception
  {
    // given
    Report sut = new Report(project);
    ArrayList<SessionReport> sessionReports = getAllSessionReports();
    int expectedReportCount = 0, expectedTesterCount = 0, expectedBugCount = 0;
    for (SessionReport sessionReport : sessionReports)
    {
      expectedBugCount += sessionReport.getNumberOfBugs();
      expectedTesterCount += sessionReport.getNumberOfTesters();
      ++expectedReportCount;
    }

    // when
    sut.addAll(sessionReports);
    ReportOverview overview = ReportOverview.generateFor(sut);

    // then
    ReportOverviewAssert.assertThat(overview)
        .hasCountBugs(expectedBugCount)
        .hasCountSessions(expectedReportCount)
        .hasCountTesters(expectedTesterCount);
  }

}
