package io.ylab.intensive.lesson05.messagefilter;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.GetResponse;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringTokenizer;

@Component
public class MessageFilter implements DisposableBean {

    private final Channel channelForSending;
    private final Channel channelForAccepting;
    private final java.sql.Connection connectionToDb;


    @Autowired
    public MessageFilter(Connection connectionToRabbit, java.sql.Connection connectionToDb) {
        try {
            this.channelForAccepting = connectionToRabbit.createChannel();
            this.channelForSending = connectionToRabbit.createChannel();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.connectionToDb = connectionToDb;
    }

    private void saveWordsToDb() {
        final String insertQuery = "INSERT INTO words VALUES (?)";
        File file = new File("src/main/java/io/ylab/intensive/lesson05/messagefilter/word.txt");
        String word;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
             PreparedStatement preparedStatement = connectionToDb.prepareStatement(insertQuery)) {
            int queryCounter = 0;
            while ((word = bufferedReader.readLine()) != null) {
                if (queryCounter >= 150) {
                    preparedStatement.executeBatch();
                    queryCounter = 0;
                }
                preparedStatement.setString(1, word);
                preparedStatement.addBatch();
                queryCounter++;
            }
            preparedStatement.executeBatch();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private String censorBadWords(String receivedMessage, PreparedStatement preparedStatement) {
        StringBuilder processedMessage = new StringBuilder();
        StringTokenizer st = new StringTokenizer
                (receivedMessage, " .,;?!\n", true);
        while (st.hasMoreTokens()) {
            String nextToken = st.nextToken();
            try {
                preparedStatement.setString(1, nextToken);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (!resultSet.next()) {
                    processedMessage.append(nextToken);
                } else {
                    // заменяем все буквы в этом слове, кроме первой и последней на *
                    processedMessage.append(nextToken.replace
                            (nextToken.substring(1, nextToken.length() - 1), "*".repeat(nextToken.length() - 2)));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return processedMessage.toString();
    }

    public void processText() {
        saveWordsToDb();
        final String checkIfWordExistsQuery = "SELECT word FROM words WHERE LOWER(word) = LOWER(?)";
        try (PreparedStatement preparedStatement = connectionToDb.prepareStatement(checkIfWordExistsQuery)) {
            channelForAccepting.queueDeclare("input", false, false, false, null);
            channelForSending.exchangeDeclare("main_exchange1", BuiltinExchangeType.DIRECT);
            channelForSending.queueDeclare("output", false, false, false, null);
            channelForSending.queueBind("output", "main_exchange1", "*");

            while (!Thread.currentThread().isInterrupted()) {
                GetResponse input = channelForAccepting.basicGet("input", true);
                if (input != null) {
                    String receivedMessage = new String(input.getBody());
                    String processedMessage = censorBadWords(receivedMessage, preparedStatement);
                    channelForSending.basicPublish("main_exchange1", "*", null,
                            processedMessage.getBytes());
                }
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() throws Exception {
        channelForSending.close();
        channelForAccepting.close();
    }
}