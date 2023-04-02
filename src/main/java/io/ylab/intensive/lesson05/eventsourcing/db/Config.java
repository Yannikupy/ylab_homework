package io.ylab.intensive.lesson05.eventsourcing.db;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import io.ylab.intensive.lesson04.DbUtil;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.TimeoutException;

@Configuration
public class Config {

    @Bean
    public DataProcessor dataProcessor() throws SQLException, IOException, TimeoutException {
        return new DataProcessor(connectionToRabbitMq(), connectionToDb());
    }

    @Bean
    public java.sql.Connection connectionToDb() throws SQLException {
        return dataSource().getConnection();
    }

    @Bean
    public Connection connectionToRabbitMq() throws IOException, TimeoutException {
        return connectionFactory().newConnection();
    }

    @Bean
    public DataSource dataSource() throws SQLException {
        String ddl = ""
                + "drop table if exists person;"
                + "create table if not exists person (\n"
                + "person_id bigint primary key,\n"
                + "first_name varchar,\n"
                + "last_name varchar,\n"
                + "middle_name varchar\n"
                + ")";
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setServerName("localhost");
        dataSource.setUser("postgres");
        dataSource.setPassword("postgres");
        dataSource.setDatabaseName("postgres");
        dataSource.setPortNumber(5432);
        DbUtil.applyDdl(ddl, dataSource);
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
