package io.ylab.intensive.lesson04.filesort;

import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FileSortImpl implements FileSorter {
    private DataSource dataSource;

    public FileSortImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private void doSimpleQueries(BufferedReader bufferedReader, PreparedStatement preparedStatement) throws IOException, SQLException {
        String line = bufferedReader.readLine();
        while (line != null) {
            preparedStatement.setLong(1, Long.parseLong(line));
            preparedStatement.executeUpdate();
            line = bufferedReader.readLine();
        }
    }

    private void doBatchQueries(BufferedReader bufferedReader, PreparedStatement preparedStatement) throws IOException, SQLException {
        String line = bufferedReader.readLine();
        while (line != null) {
            for (int i = 0; i < 10000 && line != null; ++i) {
                preparedStatement.setLong(1, Long.parseLong(line));
                preparedStatement.addBatch();
                line = bufferedReader.readLine();
            }
            preparedStatement.executeBatch();
        }
    }

    private void writeResultFile(File resultFile, ResultSet sortedNumbers) throws IOException {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(resultFile))) {
            while (sortedNumbers.next()) {
                bufferedWriter.write(sortedNumbers.getString(1));
                bufferedWriter.newLine();
            }
            bufferedWriter.flush();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public File sort(File data) {
        final String insertQuery = "INSERT INTO numbers values (?)";
        final String sortQuery = "SELECT val FROM numbers ORDER BY val";
        File resultFile = new File("src/main/java/io/ylab/intensive/lesson04/filesort/result.txt");
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(data));
             Connection connection = dataSource.getConnection();
             PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
             PreparedStatement sortStatement = connection.prepareStatement(sortQuery)) {
            doBatchQueries(bufferedReader, insertStatement);
            //doSimpleQueries(bufferedReader, insertStatement);
            ResultSet sortedNumbers = sortStatement.executeQuery();
            writeResultFile(resultFile, sortedNumbers);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return resultFile;
    }
}
