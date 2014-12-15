package com.seitenbau.testing;

import com.google.common.collect.Sets;
import com.seitenbau.testing.model.Testsheet;
import org.assertj.core.util.Lists;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;

public class TestSheetUtils
{
  private TestSheetUtils()
  {
  }

  public static HashSet<String> getAllTestedAreas() throws IOException, URISyntaxException
  {
    HashSet<String> areas = Sets.newHashSet();
    for (Testsheet testsheet : getAllTestSheets())
    {
      areas.addAll(testsheet.getComponents());
      areas.add(testsheet.getVersion());
      areas.addAll(testsheet.getBrowser());
    }
    return areas;
  }

  public static ArrayList<SessionReport> getAllSessionReports() throws URISyntaxException, IOException
  {
    ArrayList<SessionReport> reports = Lists.newArrayList();
    for (Testsheet testsheet : getAllTestSheets())
    {
      reports.add(new SessionReport(testsheet));
    }
    return reports;
  }

  public static ArrayList<Testsheet> getAllTestSheets() throws URISyntaxException, IOException
  {
    ArrayList<Testsheet> sheets = Lists.newArrayList();
    File[] sheetFiles = new File(ReportTest.class.getResource("/sheets").toURI()).listFiles();
    TestSheetParser sheetParser = new TestSheetParser();
    if (sheetFiles != null)
    {
      for (File sheetFile : sheetFiles)
      {
        sheets.add(sheetParser.parse(sheetFile));
      }
    }
    return sheets;
  }
}
