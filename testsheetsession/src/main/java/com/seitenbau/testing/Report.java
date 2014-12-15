package com.seitenbau.testing;

import com.google.common.collect.*;
import com.seitenbau.testing.model.Project;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.Delegate;

import java.util.*;

@Data
@Accessors(fluent = true)
public class Report
{
  final Project project;

  @Delegate
  List<SessionReport> sessions = new ArrayList<>();

  public ArrayList<AreaSummary> getSummaryForAllAreas()
  {
    ArrayList<AreaSummary> summaries = Lists.newArrayList();
    ArrayListMultimap<String, SessionReport> areasToSessions = getAreasToSessions();
    for (String area : areasToSessions.keySet())
    {
      AreaSummary summary = AreaSummary.generateFor(area, areasToSessions.get(area));
      summaries.add(summary);
    }
    return summaries;
  }

  public ArrayListMultimap<String, SessionReport> getAreasToSessions()
  {
    ArrayListMultimap<String, SessionReport> multimap = ArrayListMultimap.create();

    for (SessionReport session : sessions)
    {
      for (String area : session.getSheet().getComponents())
      {
        multimap.put(area, session);
      }
      multimap.put(session.getSheet().getVersion(), session);
      for (String browser : session.getSheet().getBrowser())
      {
        multimap.put(browser, session);
      }

    }

    Sets.SetView<String> missingKeys =
        Sets.difference(new HashSet<String>(project.getComponents()), multimap.keySet());
    for (String missingKey : missingKeys)
    {
      multimap.put(missingKey, null); // naughty
    }

    return multimap;
  }
}
