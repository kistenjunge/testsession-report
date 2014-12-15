package com.seitenbau.testing;

import com.seitenbau.testing.model.Testsheet;
import org.junit.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.*;

public class TestSheetParserTest
{
  @Test
  public void testParsing() throws Exception
  {
    // given
    File testFile = new File(TestSheetParserTest.class.getResource("/sheets/fpf_antisocial.json").toURI());
    TestSheetParser sut = new TestSheetParser();

    // when
    Testsheet parsedSheet = sut.parse(testFile);

    // then
    assertThat(parsedSheet).isNotNull();
  }
}
