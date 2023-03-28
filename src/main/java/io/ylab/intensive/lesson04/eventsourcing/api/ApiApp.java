package io.ylab.intensive.lesson04.eventsourcing.api;

import com.rabbitmq.client.ConnectionFactory;
import io.ylab.intensive.lesson04.DbUtil;
import io.ylab.intensive.lesson04.RabbitMQUtil;

import javax.sql.DataSource;
import java.sql.SQLException;

public class ApiApp {
    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = initMQ();
        DataSource dataSource = initDb();
        PersonApi personApi = new PersonApiImpl(connectionFactory, dataSource);
        personApi.savePerson(1L, "Yan", "Borisov", "Arturovich");
        personApi.savePerson(2L, "Artem", "Moskvin", "Aleksandrovich");
        System.out.println(personApi.findPerson(1L));
        System.out.println(personApi.findAll());
        personApi.deletePerson(1L);
        personApi.closeApi();
    }

    private static ConnectionFactory initMQ() throws Exception {
        return RabbitMQUtil.buildConnectionFactory();
    }

    private static DataSource initDb() throws SQLException {
        return DbUtil.buildDataSource();
    }
}
