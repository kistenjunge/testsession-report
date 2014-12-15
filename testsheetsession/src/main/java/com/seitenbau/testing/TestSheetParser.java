package com.seitenbau.testing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seitenbau.testing.model.Testsheet;
import lombok.NonNull;

import java.io.File;
import java.io.IOException;

public class TestSheetParser
{
  ObjectMapper mapper;

  public TestSheetParser()
  {
    mapper = new ObjectMapper();
  }

  public Testsheet parse(@NonNull File sheet) throws IOException
  {
    return mapper.readValue(sheet, Testsheet.class);
  }

}
