package io.ylab.intensive.lesson05.eventsourcing.db;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.GetResponse;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;

@Component
public class DataProcessor implements DisposableBean {
    private final Channel channel;
    private final java.sql.Connection connectionToDb;

    @Autowired
    public DataProcessor(Connection connectionToRabbitMq, java.sql.Connection connectionToDb) {
        try {
            this.channel = connectionToRabbitMq.createChannel();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.connectionToDb = connectionToDb;
    }

    public void getMessages() {
        final String insertQuery = "INSERT INTO person VALUES(?,?,?,?)";
        final String deleteQuery = "DELETE FROM person WHERE person_id = ?";
        try (PreparedStatement insertStatement = connectionToDb.prepareStatement(insertQuery);
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
        } catch (IOException | SQLException | UnsupportableCommandException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() throws Exception {
        channel.close();
        System.out.println("Ресурсы освобождены");
    }
}
