package io.ylab.intensive.lesson05.eventsourcing.api;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import io.ylab.intensive.lesson05.eventsourcing.PersonLesson05;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Тут пишем реализацию
 */
@Component
public class PersonApiImpl implements PersonApi {
    Connection connectionToRabbitMQ;

    Channel channel;


    java.sql.Connection connectionToDb;


    @Autowired
    public PersonApiImpl(Connection connectionToRabbitMQ, java.sql.Connection connectionToDb) {
        try {
            this.connectionToRabbitMQ = connectionToRabbitMQ;
            this.channel = connectionToRabbitMQ.createChannel();
            channel.exchangeDeclare("main_exchange", BuiltinExchangeType.DIRECT);
            channel.queueBind("query_queue", "main_exchange", "*");
            this.connectionToDb = connectionToDb;
        } catch (IOException e) {
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
    public PersonLesson05 findPerson(Long personId) {
        final String findPersonQuery = "SELECT * FROM person WHERE person_id = ?";
        PersonLesson05 person = null;
        try (PreparedStatement preparedStatement = connectionToDb.prepareStatement(findPersonQuery)) {
            preparedStatement.setLong(1, personId);
            ResultSet personSet = preparedStatement.executeQuery();
            if (!personSet.next()) return null;
            person = new PersonLesson05(personSet.getLong(1), personSet.getString(2), personSet.getString(3),
                    personSet.getString(4));
            personSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return person;
    }

    @Override
    public List<PersonLesson05> findAll() {
        final String findPersonQuery = "SELECT * FROM person";
        List<PersonLesson05> personList = new ArrayList<>();
        try (PreparedStatement preparedStatement = connectionToDb.prepareStatement(findPersonQuery);
             ResultSet personSet = preparedStatement.executeQuery()) {
            if (!personSet.next()) return personList;
            personList.add(new PersonLesson05(personSet.getLong(1), personSet.getString(2),
                    personSet.getString(3), personSet.getString(4)));
            while (personSet.next()) {
                personList.add(new PersonLesson05(personSet.getLong(1), personSet.getString(2),
                        personSet.getString(3), personSet.getString(4)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return personList;
    }

    @Override
    public void destroy() throws Exception {
        channel.close();
        System.out.println("Ресурсы освобождены");
    }
}
