package com.seitenbau.testing;

import lombok.Data;
import lombok.Value;
import lombok.experimental.Accessors;
import lombok.experimental.Builder;

@Value
@Builder
public class ReportOverview
{
  int countTesters;

  int countSessions;

  int countBugs;

  public static ReportOverview generateFor(Report report)
  {
    int sessions=0, testers=0, bugs=0;
    for (SessionReport session : report.sessions())
    {
      ++sessions;
      testers += session.getNumberOfTesters();
      bugs += session.getNumberOfBugs();
    }

    return ReportOverview.builder()
        .countBugs(bugs)
        .countSessions(sessions)
        .countTesters(testers)
        .build();
  }
}
