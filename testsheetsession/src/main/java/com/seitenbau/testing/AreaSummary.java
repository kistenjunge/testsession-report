package com.seitenbau.testing;

import lombok.Data;
import lombok.NonNull;

import java.util.List;
import java.util.Set;

@Data
public class AreaSummary
{
  /**
   * <b>coverage area</b><br>
   * The coverage area to which the metrics apply. What we call "coverage areas" goes beyond areas of the product itself.
   * Types of testing, build numbers, and platforms are also reported. However, mostly the areas correspond to functional,
   * structural or data elements of the product. These areas are predefined and controlled by the test lead. We selected
   * these specific areas to allow reasonably granular reporting while not getting so granular that the accuracy suffers.
   */
  String area;

  /**
   * <b>total amount of associated session work</b><br>
   * The total amount of session work, expressed in normal sessions, that is associated with a coverage area.
   * If the Total column reports "15" for a particular area, it means that the specified area was mentioned on
   * session reports that totaled to a duration equivalent to 15 normal sessions worth of testing. Not that you
   * cannot add totals from different areas, since session totals from different areas may well have sessions in common.
   */
  Double total;

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

  public static AreaSummary generateFor(@NonNull String area, List<SessionReport> reports)
  {
    AreaSummary summary = new AreaSummary();
    summary.setArea(area);

    double totalOnCharter=0, totalOffCharter=0, totalBugHandling=0, totalSessionSetup=0, totalTesting=0, total=0;
    int totalBugs=0, totalIssues=0;
    for (SessionReport report : reports)
    {
      if (report != null)
      {
        total += report.getDuration();
        totalOnCharter += report.getTimeOnCharter();
        totalOffCharter += report.getTimeOffCharter();
        totalBugHandling += report.getTimeBugHandling();
        totalSessionSetup += report.getTimeSessionSetup();
        totalTesting += report.getTimeTesting();
        totalBugs += report.getNumberOfBugs();
        totalIssues += report.getNumberOfIssues();
      }
    }

    summary.setTimeOnCharter(totalOnCharter);
    summary.setTimeOffCharter(totalOffCharter);
    summary.setTimeBugHandling(totalBugHandling);
    summary.setTimeSessionSetup(totalSessionSetup);
    summary.setTimeTesting(totalTesting);
    summary.setNumberOfBugs(totalBugs);
    summary.setNumberOfIssues(totalIssues);
    summary.setTotal(total);

    return summary;
  }
}
