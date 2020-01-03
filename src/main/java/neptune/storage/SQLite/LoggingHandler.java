package neptune.storage.SQLite;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class LoggingHandler {
    private String DatabaseURL = Database.DatabaseURL;
    private final String LogTableName = "Log";

    public LoggingHandler(){
        Connection connection;
        try {
            connection = DriverManager.getConnection(DatabaseURL);
            connection.createStatement().execute("CREATE TABLE IF NOT EXISTS `"+LogTableName+"` (\n" +
                    "  `GuildID` INT NOT NULL,\n" +
                    "  `ChannelID` INT NOT NULL,\n" +
                    "  `AuthorID` INT NOT NULL,\n" +
                    "  `MessageID` INT NOT NULL,\n" +
                    "  `MessageContent` VARCHAR(45) NOT NULL,\n" +
                    "  `PreviousMessage` VARCHAR(45) NULL,\n" +
                    "  PRIMARY KEY (`MessageID`),\n" +
                    "  CONSTRAINT `GuildID`\n" +
                    "    FOREIGN KEY (`GuildID`)\n" +
                    "    REFERENCES `Guilds` (`GuildID`)\n" +
                    "    ON DELETE CASCADE\n" +
                    "    ON UPDATE CASCADE)");
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean newLogEntry(String GuildID,String ChannelID, String AuthorID, String MessageID, String MessageContent){
        System.out.println("SQL: Adding new Log entry for guild: " + GuildID +  " Channel ID: " + ChannelID + " Author ID: " + AuthorID + " Message ID: " + MessageID + " Message Content: " + MessageContent);
        try {
            Connection connection;
            connection = DriverManager.getConnection(DatabaseURL);
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO "+ LogTableName +"(GuildID, ChannelID, AuthorID, MessageID, MessageContent, PreviousMessage) VALUES(?,?,?,?,?,?)");
            preparedStatement.setString(1,GuildID);
            preparedStatement.setString(2,ChannelID);
            preparedStatement.setString(3,AuthorID);
            preparedStatement.setString(4,MessageID);
            preparedStatement.setString(5,MessageContent);
            preparedStatement.setString(6,"");
            preparedStatement.execute();
            connection.close();
            return true;
        } catch (SQLException e) {
                System.out.println("SQL: Error Code= "+ e.getErrorCode());
                e.printStackTrace();
            }
        return false;
    }
    public boolean updateLogEntry(String MessageID, String MessageContent, String PreviousMessage){
        System.out.println("SQL: Updating Log entry ; " + MessageID);
        try {
            Connection connection = DriverManager.getConnection(DatabaseURL);
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE "+LogTableName+" SET MessageContent = ?, PreviousMessage = ? WHERE MessageID = " + MessageID);
            preparedStatement.setString(1,MessageContent);
            preparedStatement.setString(2,PreviousMessage);
            boolean result =  preparedStatement.execute();
            connection.close();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean deleteLogEntry(String MessageID){
        System.out.println("SQL: Deleting log entry; " + MessageID);
        try {
            Connection connection = DriverManager.getConnection(DatabaseURL);
            boolean result = connection.createStatement().execute("DELETE FROM "+ LogTableName +
                    " WHERE MessageID = " + MessageID + ";");
            connection.close();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean deleteChannelMessages(String ChannelID){
        System.out.println("SQL: Deleting log entries for channel ID; " + ChannelID);

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

    public Map<String, String> getLogEntry(String MessageID){
        System.out.println("SQL: Retrieving Log Entry for message ID: " + MessageID);
        Connection connection;
        try {
            connection = DriverManager.getConnection(DatabaseURL);
            ResultSet resultSet = connection.prepareStatement("SELECT GuildID, ChannelID, AuthorID, MessageID, MessageContent, PreviousMessage FROM "+ LogTableName +" Where MessageID = " + MessageID).executeQuery();
            Map<String, String> results = new HashMap<>();
            results.put("GuildID",resultSet.getString(1));
            results.put("ChannelID", resultSet.getString(2));
            results.put("AuthorID", resultSet.getString(3));
            results.put("MessageID", resultSet.getString(4));
            results.put("MessageContent",resultSet.getString(5));
            results.put("PreviousMessage", resultSet.getString(6));
            resultSet.close();
            connection.close();
            return results;

        } catch (SQLException e) {
            System.out.println("SQL: Error Code= "+ e.getErrorCode());
            System.out.println("SQL: Log entry not found");
            //e.printStackTrace();
            return null;
        }
    }
}
