package com.tommwq.jet.database;

public class SQLiteStringEscaper implements StringEscaper {
    // https://www.sqlite.org/faq.html#q14
    @Override
    public String escape(String text) {
        return text.replace("\"", "\"\"")
                .replace("'", "''");
    }
}


