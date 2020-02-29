package neptune.storage.MySQL;

import neptune.Main;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class SettingsStorage {
    private String DatabaseURL = Main.DatabaseURL;
    private final String GuildOptions = "GuildOptions";
    public boolean addGuild(String GuildID) {
        System.out.println("SQL: Adding Guild " + GuildID);
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DriverManager.getConnection(DatabaseURL);
            preparedStatement = connection.prepareStatement("INSERT INTO " + GuildOptions + "(GuildID) VALUES(?)");
            preparedStatement.setString(1, GuildID);
            preparedStatement.execute();

            return true;
        } catch (SQLException e) {
            if (e.getErrorCode() == 19) {
                //System.out.println("    Data Exists :)");
                return true;
            } else {
                System.out.println("    Error Code= " + e.getErrorCode());
                e.printStackTrace();
                return false;
            }
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public Map<String,String> getGuildSettings(String GuildID) {
        System.out.println("SQL: Retrieving Guild " + GuildID);
        Connection connection = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection(DatabaseURL);
            resultSet = connection.prepareStatement("SELECT GuildID, TTS, CustomSounds, LoggingChannel, LoggingEnabled, TextChannelLogging, VoiceChannelLogging, MemberActivityLogging, ServerModificationLogging, CustomRoleEnabled, LeaderboardsEnabled FROM " + GuildOptions + " Where GuildID = " + GuildID).executeQuery();
            Map<String, String> results = new HashMap<>();

            if (!resultSet.next()) {
                System.out.println("SQL: Guild does not Exist in database!");
                if (addGuild(GuildID)) {
                    return getGuildSettings(GuildID);
                } else return null;
            }

            results.put("GuildID", resultSet.getString(1));
            results.put("TTS", resultSet.getString(2));
            results.put("CustomSounds", resultSet.getString(3));
            results.put("LoggingChannel", resultSet.getString(4));
            results.put("LoggingEnabled", resultSet.getString(5));
            results.put("TextChannelLogging", resultSet.getString(6));
            results.put("VoiceChannelLogging", resultSet.getString(7));
            results.put("MemberActivityLogging", resultSet.getString(8));
            results.put("ServerModificationLogging", resultSet.getString(9));
            results.put("CustomRoleEnabled", resultSet.getString(9));
            results.put("LeaderboardsEnabled", resultSet.getString(10));


            return results;

        } catch (SQLException e) {
            System.out.println("    Error Code= " + e.getErrorCode());
            e.printStackTrace();
            return null;
        } finally {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public boolean deleteGuild(String GuildID) {
        System.out.println("SQL: Deleting Guild " + GuildID);
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DatabaseURL);
            boolean result = connection.createStatement().execute("DELETE FROM " + GuildOptions +
                    " WHERE GuildID = " + GuildID + ";");
            connection.close();
            System.out.println("    Success");
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public boolean updateGuild(String GuildID, String Field, String Value) {
        System.out.println("SQL: Setting Field: " + Field + " to the value: " + Value + " for the Guild: " + GuildID);
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DriverManager.getConnection(DatabaseURL);
            preparedStatement = connection.prepareStatement("UPDATE " + GuildOptions + " SET " + Field + " = ? WHERE GuildID = " + GuildID);
            preparedStatement.setString(1, Value);
            boolean result = preparedStatement.execute();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
