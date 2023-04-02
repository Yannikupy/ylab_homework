package io.ylab.intensive.lesson05.messagefilter;

import com.rabbitmq.client.ConnectionFactory;
import io.ylab.intensive.lesson05.DbUtil;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeoutException;

@Configuration
@ComponentScan("io.ylab.intensive.lesson05.messagefilter")
public class Config {
    @Bean
    public MessageFilter messageFilter() {
        try {
            return new MessageFilter(connectionToRabbit(), connectionToDb());
        } catch (IOException | TimeoutException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Bean
    public com.rabbitmq.client.Connection connectionToRabbit() throws IOException, TimeoutException {
        return connectionFactory().newConnection();
    }

    @Bean
    Connection connectionToDb() throws SQLException {
        return dataSource().getConnection();
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setVirtualHost("/");
        return connectionFactory;
    }

    @Bean
    public DataSource dataSource() {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setServerName("localhost");
        dataSource.setUser("postgres");
        dataSource.setPassword("postgres");
        dataSource.setDatabaseName("postgres");
        dataSource.setPortNumber(5432);
        String truncateQuery = "TRUNCATE words";
        Connection connectionToDb = null;
        try {
            connectionToDb = dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try (ResultSet resultSet = connectionToDb.getMetaData()
                .getTables(null, null, "words", new String[]{"TABLE"});
             PreparedStatement preparedStatement = connectionToDb.prepareStatement(truncateQuery)) {
            String ddl = ""
                    + "CREATE TABLE words (\n"
                    + "word varchar primary key)";
            if (!resultSet.next()) {
                DbUtil.applyDdl(ddl, dataSource);
            } else {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataSource;
    }
}
