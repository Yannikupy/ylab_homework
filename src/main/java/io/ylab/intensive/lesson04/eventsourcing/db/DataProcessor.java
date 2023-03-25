package io.ylab.intensive.lesson04.eventsourcing.db;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;

public class DataProcessor {
    private DataSource dataSource;
    private ConnectionFactory connectionFactory;

    public DataProcessor(DataSource dataSource, ConnectionFactory connectionFactory) {
        this.dataSource = dataSource;
        this.connectionFactory = connectionFactory;
    }

    public void getMessages() {
        final String insertQuery = "INSERT INTO person VALUES(?,?,?,?)";
        final String deleteQuery = "DELETE FROM person WHERE person_id = ?";
        try (Connection connectionToRabbitMq = connectionFactory.newConnection();
             Channel channel = connectionToRabbitMq.createChannel();
             java.sql.Connection connectionToDb = dataSource.getConnection();
             PreparedStatement insertStatement = connectionToDb.prepareStatement(insertQuery);
             PreparedStatement deleteStatement = connectionToDb.prepareStatement(deleteQuery)) {
            channel.queueDeclare("query_queue", false, false, false, null);
            while (!Thread.currentThread().isInterrupted()) {
                GetResponse message = channel.basicGet("query_queue", true);
                if (message != null) {
                    String[] received = new String(message.getBody()).split(";");
                    System.out.println(Arrays.toString(received));
                    if (received.length != 2 && received.length != 5) {
                        throw new UnsupportableCommandException("Unsupportable command");
                    }
                    if (received[0].equals("delete")) {
                        long personId = Long.parseLong(received[1]);
                        deleteStatement.setLong(1, personId);
                        deleteStatement.executeUpdate();
                    } else if (received[0].equals("save")) {
                        long personId = Long.parseLong(received[1]);
                        String firstName = received[2];
                        String lastName = received[3];
                        String secondName = received[4];
                        insertStatement.setLong(1, personId);
                        insertStatement.setString(2, firstName);
                        insertStatement.setString(3, lastName);
                        insertStatement.setString(4, secondName);
                        insertStatement.executeUpdate();
                    } else {
                        throw new UnsupportableCommandException("Unsupportable command");
                    }
                }
            }
        } catch (IOException | TimeoutException | SQLException | UnsupportableCommandException e) {
            e.printStackTrace();
        }
    }
}
