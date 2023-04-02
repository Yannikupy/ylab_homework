package io.ylab.intensive.lesson05.sqlquerybuilder;

import java.sql.SQLException;
import java.util.List;

public interface SQLQueryBuilder {
    String queryForTable(String tableName) throws SQLException, EmptyTableException;

    List<String> getTables() throws SQLException;
}
