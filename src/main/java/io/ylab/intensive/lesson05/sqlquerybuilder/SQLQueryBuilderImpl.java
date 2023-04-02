package io.ylab.intensive.lesson05.sqlquerybuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class SQLQueryBuilderImpl implements SQLQueryBuilder {

    DatabaseMetaData databaseMetaData;

    @Autowired
    public SQLQueryBuilderImpl(DatabaseMetaData databaseMetaData) {
        this.databaseMetaData = databaseMetaData;
    }

    @Override
    public String queryForTable(String tableName) throws EmptyTableException {
        StringBuilder query = new StringBuilder("SELECT ");
        try (ResultSet resultSetTables = databaseMetaData.getTables
                (null, null, tableName, null)) {
            if (!resultSetTables.next()) return null;

            List<String> columns = new ArrayList<>();
            try (ResultSet resultSetColumns = databaseMetaData.getColumns
                    (null, null, tableName, "%")) {
                while (resultSetColumns.next()) {
                    columns.add(resultSetColumns.getString(4));
                }
                for (int i = 0; i < columns.size() - 1; ++i) {
                    query.append(columns.get(i)).append(", ");
                }
                if (columns.size() == 0) {
                    throw new EmptyTableException
                            ("Не могу построить запрос, попалась таблица без колонок." +
                                    " Если это создали вы - обратитесь за квалифицированой помощью");
                }
                query.append(columns.get(columns.size() - 1)).append(" FROM ").append(tableName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return query.toString();
    }

    @Override
    public List<String> getTables() throws SQLException {
        List<String> tables = new ArrayList<>();
        try (ResultSet resultSet = databaseMetaData
                .getTables(null, null, "%", null)) {
            while (resultSet.next()) {
                tables.add(resultSet.getString(3));
            }
        }
        return tables;
    }
}
