package com.tommwq.jet.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteHelper extends DBHelper {

    public SQLiteHelper(Connection aConnection) {
        super(aConnection, new SQLiteDataTypeTranslater(), new SQLiteStringEscaper());
    }

    @Override
    public boolean tableExist(String tableName) throws SQLException {
        String sql = String.format("select count(*) as exist from sqlite_master where type='table' and name='%s'",
                tableName);

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            if (!resultSet.next()) {
                throw new RuntimeException("fail to query records from sqlite_master table");
            }

            boolean exist = (resultSet.getInt("exist") == 1);
            return exist;
        }
    }

}
