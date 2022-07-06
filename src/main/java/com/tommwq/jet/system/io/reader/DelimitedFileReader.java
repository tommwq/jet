package com.tommwq.jet.system.io.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

public class DelimitedFileReader {

  private final String rowDelimiter;
  private final String columnDelimiter;

  public DelimitedFileReader(String aRowDelimiter, String aColumnDelimiter) {
    if (aRowDelimiter == null || aRowDelimiter.length() == 0) {
      throw new IllegalArgumentException("row delimiter must not be null or empty");
    }

    if (aColumnDelimiter == null || aColumnDelimiter.length() == 0) {
      throw new IllegalArgumentException("column delimiter must not be null or empty");
    }

    rowDelimiter = aRowDelimiter;
    columnDelimiter = aColumnDelimiter;
  }

  public String[][] read(File file) throws IOException {
    return read(file, Charset.defaultCharset());
  }

  public String[][] read(File file, Charset charset) throws IOException {
    String content;

    try (FileInputStream inputStream = new FileInputStream(file)) {
      int dataSize = inputStream.available();
      byte[] buffer = new byte[dataSize];
      inputStream.read(buffer);
      content = new String(buffer, charset);
    }

    String[] rows = content.split(rowDelimiter);
    String[][] result = new String[rows.length][];

    for (int i = 0; i < rows.length; i++) {
      result[i] = rows[i].split(columnDelimiter);
    }

    return result;
  }
}
