package com.tommwq.jet.database;

public class SQLiteEscaper implements SQLStringEscaper {
    // https://www.sqlite.org/faq.html#q14
    @Override
    public String escape(String text) {
        return text.replace("\"", "\"\"")
                .replace("'", "''");
    }
}


