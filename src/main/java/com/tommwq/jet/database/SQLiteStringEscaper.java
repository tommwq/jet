package com.tommwq.jet.database;

public class SQLiteStringEscaper implements SQLStringEscaper {
  // https://www.sqlite.org/faq.html#q14
  @Override
  public String escape(String text) {
    text = text.replace("\"", "\"\"");
    text = text.replace("'", "''");

    text = text.replace("/", "//");
    text = text.replace("'", "''");
    text = text.replace("[", "/[");
    text = text.replace("]", "/]");
    text = text.replace("%", "/%");
    text = text.replace("&", "/&");
    text = text.replace("_", "/_");
    text = text.replace("(", "/(");
    text = text.replace(")", "/)");

    return text;
  }
}


