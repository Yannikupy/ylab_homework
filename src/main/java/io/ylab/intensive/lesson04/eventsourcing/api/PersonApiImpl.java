package io.ylab.intensive.lesson04.eventsourcing.api;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import io.ylab.intensive.lesson04.eventsourcing.Person;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * Тут пишем реализацию
 */
public class PersonApiImpl implements PersonApi {
    ConnectionFactory connectionFactory;
    Connection connectionToRabbitMQ;

    Channel channel;

    DataSource dataSource;

    java.sql.Connection connectionToDb;


    public PersonApiImpl(ConnectionFactory connectionFactory, DataSource dataSource) {
        this.connectionFactory = connectionFactory;
        try {
            this.connectionToRabbitMQ = connectionFactory.newConnection();
            this.channel = connectionToRabbitMQ.createChannel();
            channel.exchangeDeclare("main_exchange", BuiltinExchangeType.DIRECT);
            channel.queueBind("query_queue", "main_exchange", "*");
            this.dataSource = dataSource;
            this.connectionToDb = dataSource.getConnection();
        } catch (IOException | TimeoutException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void closeApi() {
        try {
            channel.close();
            connectionToRabbitMQ.close();
            connectionToDb.close();
        } catch (IOException | TimeoutException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deletePerson(Long personId) {
        try {
            channel.basicPublish("main_exchange", "*", null, ("delete;" + personId).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void savePerson(Long personId, String firstName, String lastName, String middleName) {
        try {
            channel.basicPublish("main_exchange", "*", null,
                    ("save;" + personId + ";" + firstName + ";" + lastName + ";" + middleName).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Person findPerson(Long personId) {
        final String findPersonQuery = "SELECT * FROM person WHERE person_id = ?";
        Person person = null;
        try (PreparedStatement preparedStatement = connectionToDb.prepareStatement(findPersonQuery)) {
            preparedStatement.setLong(1, personId);
            ResultSet personSet = preparedStatement.executeQuery();
            if (!personSet.next()) return null;
            person = new Person(personSet.getLong(1), personSet.getString(2), personSet.getString(3),
                    personSet.getString(4));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return person;
    }

    @Override
    public List<Person> findAll() {
        final String findPersonQuery = "SELECT * FROM person";
        List<Person> personList = new ArrayList<>();
        try (PreparedStatement preparedStatement = connectionToDb.prepareStatement(findPersonQuery)) {
            ResultSet personSet = preparedStatement.executeQuery();
            if (!personSet.next()) return personList;
            personList.add(new Person(personSet.getLong(1), personSet.getString(2),
                    personSet.getString(3), personSet.getString(4)));
            while (personSet.next()) {
                personList.add(new Person(personSet.getLong(1), personSet.getString(2),
                        personSet.getString(3), personSet.getString(4)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return personList;
    }
}
