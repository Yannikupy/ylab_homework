package io.ylab.intensive.lesson04.movie;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Objects;
import java.util.Scanner;

public class MovieLoaderImpl implements MovieLoader {
    private DataSource dataSource;
    private static final String insertMovie = "INSERT INTO movie (year, length, title, subject, actors, actress," +
            " director, popularity, awards) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    public MovieLoaderImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private Movie parseLineToMovie(String[] movieData) {
        Movie movie = new Movie();
        movie.setYear(Integer.parseInt(movieData[0]));
        movie.setLength(movieData[1].isEmpty() ? null : Integer.parseInt(movieData[1]));
        movie.setTitle(movieData[2]);
        movie.setSubject(movieData[3].isEmpty() ? null : movieData[3]);
        movie.setActors(movieData[4].isEmpty() ? null : movieData[4]);
        movie.setActress(movieData[5].isEmpty() ? null : movieData[5]);
        movie.setDirector(movieData[6].isEmpty() ? null : movieData[6]);
        movie.setPopularity(movieData[7].isEmpty() ? null : Integer.parseInt(movieData[7]));
        movie.setAwards(movieData[8].isEmpty() ? null : Objects.equals(movieData[8], "Yes"));
        return movie;
    }

    private void insertMovieIntoDb(Movie movie, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setInt(1, movie.getYear());
        if (movie.getLength() == null) {
            preparedStatement.setNull(2, Types.INTEGER);
        } else {
            preparedStatement.setInt(2, movie.getLength());
        }
        preparedStatement.setString(3, movie.getTitle());
        if (movie.getSubject() == null) {
            preparedStatement.setNull(4, Types.VARCHAR);
        } else {
            preparedStatement.setString(4, movie.getSubject());
        }
        if (movie.getActors() == null) {
            preparedStatement.setNull(5, Types.VARCHAR);
        } else {
            preparedStatement.setString(5, movie.getActors());
        }
        if (movie.getActress() == null) {
            preparedStatement.setNull(6, Types.VARCHAR);
        } else {
            preparedStatement.setString(6, movie.getActress());
        }
        if (movie.getDirector() == null) {
            preparedStatement.setNull(7, Types.VARCHAR);
        } else {
            preparedStatement.setString(7, movie.getDirector());
        }
        if (movie.getPopularity() == null) {
            preparedStatement.setNull(8, Types.INTEGER);
        } else {
            preparedStatement.setInt(8, movie.getPopularity());
        }
        if (movie.getAwards() == null) {
            preparedStatement.setNull(9, Types.BOOLEAN);
        } else {
            preparedStatement.setBoolean(9, movie.getAwards());
        }
        preparedStatement.addBatch();
    }

    @Override
    public void loadData(File file) {
        try (Scanner sc = new Scanner(new FileInputStream(file), "UTF-8");
             Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertMovie)) {
            sc.nextLine();
            sc.nextLine();
            int batchSize = 0;
            while (sc.hasNextLine()) {
                if(batchSize == 100) {
                    preparedStatement.executeBatch();
                    batchSize = 0;
                }
                String[] movieData = sc.nextLine().split(";");
                Movie movie = parseLineToMovie(movieData);
                insertMovieIntoDb(movie, preparedStatement);
                batchSize++;
            }
            preparedStatement.executeBatch();
        } catch (SQLException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
