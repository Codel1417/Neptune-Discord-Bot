package Neptune.Storage.SQLite;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class SettingsStorage {
    private String DatabaseURL = "jdbc:sqlite:NepDB.db";
    private final String GuildOptions = "Guilds";
    public SettingsStorage(){
        try {
            Connection connection = DriverManager.getConnection(DatabaseURL);
            connection.createStatement().execute("CREATE TABLE IF NOT EXISTS `" + GuildOptions + "` (\n" +
                    "  `GuildID` INT NOT NULL,\n" +
                    "  `TTS` VARCHAR(45) NULL DEFAULT 'disabled',\n" +
                    "  `CustomSounds` VARCHAR(45) NULL DEFAULT 'disabled',\n" +
                    "  `LoggingChannel` INT NULL DEFAULT NULL,\n" +
                    "  `LoggingEnabled` VARCHAR(45) NULL DEFAULT 'disabled',\n" +
                    "  `TextChannelLogging` VARCHAR(45) NULL DEFAULT 'disabled',\n" +
                    "  `VoiceChannelLogging` VARCHAR(45) NULL DEFAULT 'disabled',\n" +
                    "  `MemberActivityLogging` VARCHAR(45) NULL DEFAULT 'disabled',\n" +
                    "  `ServerModificationLogging` VARCHAR(45) NULL DEFAULT 'disabled',\n" +
                    "  PRIMARY KEY (`GuildID`))");
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean addGuild(String GuildID){
        System.out.println("SQL: Adding Guild " + GuildID);
        try {
            Connection connection;
            connection = DriverManager.getConnection(DatabaseURL);
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO "+ GuildOptions +"(GuildID, TTS, CustomSounds, TextChannelLogging, VoiceChannelLogging, MemberActivityLogging, ServerModificationLogging, LoggingEnabled) VALUES(?,?,?,?,?,?,?,?)");
            preparedStatement.setString(1, GuildID);
            preparedStatement.setString(2,"disabled");
            preparedStatement.setString(3,"disabled");
            preparedStatement.setString(4,"enabled");
            preparedStatement.setString(5,"enabled");
            preparedStatement.setString(6,"enabled");
            preparedStatement.setString(7,"enabled");
            preparedStatement.setString(8,"disabled");
            preparedStatement.execute();
            connection.close();
            return true;
        } catch (SQLException e) {
            if(e.getErrorCode() == 19){
                //System.out.println("    Data Exists :)");
                return true;
            }
            else{
                System.out.println("    Error Code= "+ e.getErrorCode());
                e.printStackTrace();
                return false;
            }
        }
    }
    public Map<String,String> getGuildSettings(String GuildID){
        System.out.println("SQL: Retrieving Guild " + GuildID);
        Connection connection;
        try {
            connection = DriverManager.getConnection(DatabaseURL);
            ResultSet resultSet = connection.prepareStatement("SELECT GuildID, TTS, CustomSounds, LoggingChannel, LoggingEnabled, TextChannelLogging, VoiceChannelLogging, MemberActivityLogging, ServerModificationLogging FROM "+ GuildOptions +" Where GuildID = " + GuildID).executeQuery();
            Map<String, String> results = new HashMap<>();
            results.put("GuildID",resultSet.getString(1));
            results.put("TTS", resultSet.getString(2));
            results.put("CustomSounds", resultSet.getString(3));
            results.put("LoggingChannel", resultSet.getString(4));
            results.put("LoggingEnabled",resultSet.getString(5));
            results.put("TextChannelLogging", resultSet.getString(6));
            results.put("VoiceChannelLogging",resultSet.getString(7));
            results.put("MemberActivityLogging", resultSet.getString(8));
            results.put("ServerModificationLogging", resultSet.getString(9));
            resultSet.close();
            connection.close();
            return results;

        } catch (SQLException e) {
            System.out.println("    Error Code= "+ e.getErrorCode());
            e.printStackTrace();
            return null;
        }
    }
    public boolean deleteGuild(String GuildID){
        System.out.println("SQL: Deleting Guild " + GuildID);
        try {
            Connection connection = DriverManager.getConnection(DatabaseURL);
            boolean result = connection.createStatement().execute("DELETE FROM "+ GuildOptions +
                    "WHERE GuildID = " + GuildID + ";");
            connection.close();
            System.out.println("    Success");
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean updateGuild(String GuildID, String Field, String Value){
        System.out.println("SQL: Setting Field: " + Field + " to the value: "+Value+" for the Guild: " + GuildID);
        try {
            Connection connection = DriverManager.getConnection(DatabaseURL);
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE "+GuildOptions+" SET "+Field+" = ? WHERE GuildID = " + GuildID);
            preparedStatement.setString(1,Value);
            boolean result =  preparedStatement.execute();
            connection.close();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
