package io.ylab.intensive.lesson04.filesort;

import io.ylab.intensive.lesson04.DbUtil;

import javax.sql.DataSource;
import java.io.File;
import java.sql.SQLException;

public class FileSorterTest {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        DataSource dataSource = null;
        try {
            dataSource = initDb();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        File data = new File("src/main/java/io/ylab/intensive/lesson04/filesort/data.txt");
        FileSorter fileSorter = new FileSortImpl(dataSource);
        File res = fileSorter.sort(data);
        System.out.println("Время работы " + (System.currentTimeMillis() - startTime) / 1000 + "s ");
    }

    public static DataSource initDb() throws SQLException {
        String createSortTable = ""
                + "drop table if exists numbers;"
                + "CREATE TABLE if not exists numbers (\n"
                + "\tval bigint\n"
                + ");";
        DataSource dataSource = DbUtil.buildDataSource();
        DbUtil.applyDdl(createSortTable, dataSource);
        return dataSource;
    }
}
