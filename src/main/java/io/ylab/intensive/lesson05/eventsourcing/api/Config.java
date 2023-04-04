package io.ylab.intensive.lesson05.eventsourcing.api;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.TimeoutException;

@Configuration
@ComponentScan("io.ylab.intensive.lesson05.eventsourcing.api")

public class Config {

    @Bean
    public Connection connectionToRabbitMq() {
        try {
            return connectionFactory().newConnection();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Bean
    public java.sql.Connection connectionToDb() {
        try {
            return dataSource().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Bean
    public DataSource dataSource() {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setServerName("localhost");
        dataSource.setUser("postgres");
        dataSource.setPassword("postgres");
        dataSource.setDatabaseName("postgres");
        dataSource.setPortNumber(5432);
        return dataSource;
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
}
