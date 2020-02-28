package neptune.storage.MySQL;

import neptune.Main;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class LoggingHandler {
    private String DatabaseURL = Main.DatabaseURL;
    private final String LogTableName = "Log";

    public boolean newLogEntry(String GuildID,String ChannelID, String AuthorID, String MessageID, String MessageContent){
        Connection connection = null;
        System.out.println("SQL: Adding new Log entry for guild: " + GuildID +  " Channel ID: " + ChannelID + " Author ID: " + AuthorID + " Message ID: " + MessageID + " Message Content: " + MessageContent);
        try {
            connection = DriverManager.getConnection(DatabaseURL);
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO "+ LogTableName +"(GuildID, ChannelID, AuthorID, MessageID, MessageContent, PreviousMessage) VALUES(?,?,?,?,?,?)");
            preparedStatement.setString(1,GuildID);
            preparedStatement.setString(2,ChannelID);
            preparedStatement.setString(3,AuthorID);
            preparedStatement.setString(4,MessageID);
            preparedStatement.setString(5,MessageContent);
            preparedStatement.setString(6,"");
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            if(e.getErrorCode()==1062){
                deleteLogEntry(MessageID);
                newLogEntry(GuildID,ChannelID,AuthorID,MessageID, MessageContent);
            }
            else{
                System.out.println("SQL: Error Code= "+ e.getErrorCode());
                e.printStackTrace();
            }
        }
        finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
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
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DatabaseURL);
            ResultSet resultSet = connection.prepareStatement("SELECT GuildID, ChannelID, AuthorID, MessageID, MessageContent, PreviousMessage FROM "+ LogTableName +" Where MessageID = " + MessageID).executeQuery();
            Map<String, String> results = new HashMap<>();
            if (!resultSet.next()) {
                connection.close();
                throw new SQLException();
            }
            results.put("GuildID",resultSet.getString(1));
            results.put("ChannelID", resultSet.getString(2));
            results.put("AuthorID", resultSet.getString(3));
            results.put("MessageID", resultSet.getString(4));
            results.put("MessageContent",resultSet.getString(5));
            results.put("PreviousMessage", resultSet.getString(6));
            System.out.println(resultSet.getString(6));
            resultSet.close();
            connection.close();
            return results;

        } catch (SQLException e) {
            System.out.println("SQL: Error Code= "+ e.getErrorCode());
            System.out.println("SQL: Log entry not found");
            //e.printStackTrace();
            return null;
        }
        finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public Map<String,String> getAllLogEntrys(){
        System.out.println("SQL: Retrieving All Log Entrys");
        Connection connection;
        try {
            connection = DriverManager.getConnection(DatabaseURL);
            ResultSet resultSet = connection.prepareStatement("SELECT MessageID, GuildID FROM "+ LogTableName).executeQuery();
            Map<String,String> results = new HashMap<>();
            while (resultSet.next()){
                results.put(resultSet.getString(1),resultSet.getString(2));
            }
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
