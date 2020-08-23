package neptune.storage.MySQL;

import neptune.storage.GuildStorageHandler;
import neptune.storage.guildObject;
import neptune.storage.logObject;
import neptune.storage.logsStorageHandler;
import neptune.storage.Enum.GuildOptionsEnum;
import neptune.storage.Enum.LoggingOptionsEnum;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.*;
public class SettingsStorage {
    private String DatabaseURL = "jdbc:mysql://10.0.0.52/Neptune?user=Neptune&password=Neptune";
    protected static final Logger log = LogManager.getLogger();

    public void convertToYAML() {
        Connection connection = null;
        ResultSet resultSet = null;
        GuildStorageHandler guildStorageHandler = new GuildStorageHandler();
        try {
            connection = DriverManager.getConnection(DatabaseURL);
            resultSet = connection.prepareStatement("SELECT GuildID, CustomSounds, LoggingChannel, LoggingEnabled, TextChannelLogging, VoiceChannelLogging, MemberActivityLogging, ServerModificationLogging, CustomRoleEnabled, LeaderboardsEnabled,LeaderboardLevelUpNotificationsEnabled FROM GuildOptions").executeQuery();

            while (resultSet.next()){
                guildObject guildEntity = new guildObject(resultSet.getString(1));
                guildEntity.getGuildOptions().setOption(GuildOptionsEnum.customSounds, resultSet.getString(3).equalsIgnoreCase("enabled"));
                guildEntity.getLogOptions().setChannel(resultSet.getString(4));
                guildEntity.getLogOptions().setOption(LoggingOptionsEnum.GlobalLogging, resultSet.getString(5).equalsIgnoreCase("enabled"));
                guildEntity.getLogOptions().setOption(LoggingOptionsEnum.TextChannelLogging, resultSet.getString(6).equalsIgnoreCase("enabled"));
                guildEntity.getLogOptions().setOption(LoggingOptionsEnum.VoiceChannelLogging, resultSet.getString(7).equalsIgnoreCase("enabled"));
                guildEntity.getLogOptions().setOption(LoggingOptionsEnum.MemberActivityLogging, resultSet.getString(8).equalsIgnoreCase("enabled"));
                guildEntity.getLogOptions().setOption(LoggingOptionsEnum.ServerModificationLogging, resultSet.getString(9).equalsIgnoreCase("enabled"));
                guildEntity.getGuildOptions().setOption(GuildOptionsEnum.CustomRoleEnabled, resultSet.getString(9).equalsIgnoreCase("enabled"));
                guildEntity.getGuildOptions().setOption(GuildOptionsEnum.leaderboardEnabled, resultSet.getString(10).equalsIgnoreCase("enabled"));
                guildEntity.getGuildOptions().setOption(GuildOptionsEnum.LeaderboardLevelUpNotification, resultSet.getString(11).equalsIgnoreCase("enabled"));
                guildStorageHandler.writeFile(guildEntity);
            }
        } catch (SQLException e) {
            log.error("Error Code= " + e.getErrorCode());
            log.error(e.toString());
        } catch (IOException e){
            log.error(e.toString());
        }
        finally {
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
    public void convertLogsToYAML(){
        Connection connection = null;
        ResultSet resultSet = null;
        logsStorageHandler logsStorageHandler = new logsStorageHandler();


        try{
            connection = DriverManager.getConnection(DatabaseURL);
            resultSet = connection.prepareStatement("SELECT GuildID, ChannelID, AuthorID, MessageID, MessageContent FROM Log").executeQuery();
            
            while (resultSet.next()){
                logObject logEntity = new logObject();
                logEntity.setGuildID(resultSet.getString(1));
                logEntity.setChannelID(resultSet.getString(2));
                logEntity.setMemberID(resultSet.getString(3));
                logEntity.setMessageID(resultSet.getString(4));
                logEntity.setMessageContent(resultSet.getString(5));
                logsStorageHandler.writeFile(logEntity);
            }

        }
        catch (IOException e){
            log.error(e.toString());
        }
        catch (SQLException e){
            log.error("Error Code= " + e.getErrorCode());
            log.error(e.toString());
        }
        finally {
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
}
