package io.ylab.intensive.lesson05.sqlquerybuilder;

import org.postgresql.ds.PGSimpleDataSource;
import org.postgresql.jdbc.PgConnection;
import org.postgresql.jdbc.PgDatabaseMetaData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
@ComponentScan("io.ylab.intensive.lesson05.sqlquerybuilder")
public class Config {
    @Bean
    public PgDatabaseMetaData pgDatabaseMetaData() throws SQLException {
        return new PgDatabaseMetaData(dataSource().getConnection().unwrap(PgConnection.class));
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
}
