package neptune.storage.MySQL;

import neptune.Main;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class SettingsStorage {
    private String DatabaseURL = Main.DatabaseURL;
    private final String GuildOptions = "GuildOptions";
    protected static final Logger log = LogManager.getLogger();

    public boolean addGuild(String GuildID) {
        log.debug("SQL: Adding Guild " + GuildID);
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
                log.error("Error Code= " + e.getErrorCode());
                log.error(e.toString());
                return false;
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
    }
    public Map<String,String> getGuildSettings(String GuildID) {
        log.debug("SQL: Retrieving Guild " + GuildID);
        Connection connection = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection(DatabaseURL);
            resultSet = connection.prepareStatement("SELECT GuildID, TTS, CustomSounds, LoggingChannel, LoggingEnabled, TextChannelLogging, VoiceChannelLogging, MemberActivityLogging, ServerModificationLogging, CustomRoleEnabled, LeaderboardsEnabled,LeaderboardLevelUpNotificationsEnabled FROM " + GuildOptions + " Where GuildID = " + GuildID).executeQuery();
            Map<String, String> results = new HashMap<>();

            if (!resultSet.next()) {
                log.error("SQL: Guild does not Exist in database!");
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
            results.put("LeaderboardLevelUpNotificationsEnabled",resultSet.getString(11));

            return results;

        } catch (SQLException e) {
            log.error("Error Code= " + e.getErrorCode());
            log.error(e.toString());
            return null;
        } finally {
            try {
                resultSet.close();
            } catch (SQLException e) {
                log.error(e.toString());
            }
            try {
                connection.close();
            } catch (SQLException e) {
                log.error(e.toString());
            }
        }
    }
    public boolean deleteGuild(String GuildID) {
        log.debug("SQL: Deleting Guild " + GuildID);
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DatabaseURL);
            boolean result = connection.createStatement().execute("DELETE FROM " + GuildOptions +
                    " WHERE GuildID = " + GuildID + ";");
            connection.close();
            return result;
        } catch (SQLException e) {
            log.error(e.toString());
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                log.error(e.toString());
            }
        }
        return false;
    }
    public boolean updateGuild(String GuildID, String Field, String Value) {
        log.debug("SQL: Setting Field: " + Field + " to the value: " + Value + " for the Guild: " + GuildID);
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DriverManager.getConnection(DatabaseURL);
            preparedStatement = connection.prepareStatement("UPDATE " + GuildOptions + " SET " + Field + " = ? WHERE GuildID = " + GuildID);
            preparedStatement.setString(1, Value);
            boolean result = preparedStatement.execute();
            return result;
        } catch (SQLException e) {
            log.error(e.toString());
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
}
