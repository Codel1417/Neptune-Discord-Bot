package neptune.storage.MySQL;

import neptune.Main;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class LoggingHandler {
    private String DatabaseURL = Main.DatabaseURL;
    private final String LogTableName = "Log";
    protected static final Logger log = LogManager.getLogger();

    public boolean newLogEntry(String GuildID,String ChannelID, String AuthorID, String MessageID, String MessageContent) {
        Connection connection = null;
        log.debug("SQL: Adding new Log entry for guild: " + GuildID + " Channel ID: " + ChannelID + " Author ID: " + AuthorID + " Message ID: " + MessageID + " Message Content: " + MessageContent);
        PreparedStatement preparedStatement = null;
        try {
            connection = DriverManager.getConnection(DatabaseURL);
            preparedStatement = connection.prepareStatement("INSERT INTO " + LogTableName + "(GuildID, ChannelID, AuthorID, MessageID, MessageContent, PreviousMessage) VALUES(?,?,?,?,?,?)");
            preparedStatement.setString(1, GuildID);
            preparedStatement.setString(2, ChannelID);
            preparedStatement.setString(3, AuthorID);
            preparedStatement.setString(4, MessageID);
            preparedStatement.setString(5, MessageContent);
            preparedStatement.setString(6, "");
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                deleteLogEntry(MessageID);
                newLogEntry(GuildID, ChannelID, AuthorID, MessageID, MessageContent);
            } else {
                log.error("SQL: Error Code= " + e.getErrorCode());
                log.error(e.toString());
            }
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                log.error(e.toString());
            }
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                log.error(e.toString());
            }
        }
        return false;
    }
    public boolean updateLogEntry(String MessageID, String MessageContent, String PreviousMessage) {
        log.debug("SQL: Updating Log entry ; " + MessageID);
        PreparedStatement preparedStatement = null;
        Connection connection = null;
        boolean result = false;
        try {
            connection = DriverManager.getConnection(DatabaseURL);
            preparedStatement = connection.prepareStatement("UPDATE " + LogTableName + " SET MessageContent = ?, PreviousMessage = ? WHERE MessageID = " + MessageID);
            preparedStatement.setString(1, MessageContent);
            preparedStatement.setString(2, PreviousMessage);
            result = preparedStatement.execute();
        } catch (SQLException e) {
            log.error(e.toString());
        } finally {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                log.error(e.toString());
            }
            try {
                connection.close();
            } catch (SQLException e) {
                log.error(e.toString());
            }
        }
        return result;
    }
    public boolean deleteLogEntry(String MessageID) {
        log.debug("SQL: Deleting log entry; " + MessageID);
        boolean result = false;
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DatabaseURL);
            result = connection.createStatement().execute("DELETE FROM " + LogTableName +
                    " WHERE MessageID = " + MessageID + ";");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                log.error(e.toString());
            }
        }
        return result;
    }
    public boolean deleteChannelMessages(String ChannelID){
        log.debug("SQL: Deleting log entries for channel ID; " + ChannelID);

        try {
            Connection connection = DriverManager.getConnection(DatabaseURL);
            boolean result = connection.createStatement().execute("DELETE FROM "+ LogTableName +
                    " WHERE ChannelID = " + ChannelID + ";");
            connection.close();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Map<String, String> getLogEntry(String MessageID) {
        log.debug("SQL: Retrieving Log Entry for message ID: " + MessageID);
        Connection connection = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection(DatabaseURL);
            resultSet = connection.prepareStatement("SELECT GuildID, ChannelID, AuthorID, MessageID, MessageContent, PreviousMessage FROM " + LogTableName + " Where MessageID = " + MessageID).executeQuery();
            Map<String, String> results = new HashMap<>();
            if (!resultSet.next()) {
                connection.close();
                throw new SQLException();
            }
            results.put("GuildID", resultSet.getString(1));
            results.put("ChannelID", resultSet.getString(2));
            results.put("AuthorID", resultSet.getString(3));
            results.put("MessageID", resultSet.getString(4));
            results.put("MessageContent", resultSet.getString(5));
            results.put("PreviousMessage", resultSet.getString(6));
            System.out.println(resultSet.getString(6));
            return results;

        } catch (SQLException e) {
            log.error("SQL: Error Code= " + e.getErrorCode());
            log.error("SQL: Log entry not found");
            //e.printStackTrace();
            return null;
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                log.error(e.toString());
            }
            try {
                resultSet.close();
            } catch (SQLException e) {
                log.error(e.toString());
            }
        }
    }
}