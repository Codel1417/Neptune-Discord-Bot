package neptune.storage.MySQL;

import neptune.Main;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class GreatSleepKingStorage {
    private String DatabaseURL = Main.DatabaseURL;
    private final String TableName = "GreatSleepKing";
    protected static final Logger log = LogManager.getLogger();

    public Map<String,String> getSleepInfo(String MemberID) {
        HashMap<String, String> result = new HashMap();
        Connection connection = null;
        ResultSet resultSet = null;
        try {
            log.debug("Getting info for GSK for member: " + MemberID);
            connection = DriverManager.getConnection(DatabaseURL);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT SleepTime, Mood, MoodTime, TimeCreatedMS FROM " + TableName + " WHERE MemberID =  (?)");
            preparedStatement.setString(1, MemberID);
            resultSet = preparedStatement.executeQuery();
            if (!resultSet.isClosed()) {
                result.put("SleepTime", resultSet.getString(1));
                result.put("Mood", resultSet.getString(2));
                result.put("MoodTime", resultSet.getString(3));
                result.put("TimeCreatedMS", resultSet.getString(4));
            }
        } catch (SQLException e) {
            log.error(e.toString());
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
        return result;
    }
    public boolean setTime(String MemberID, String Mood, String MoodTime,  String Sleep, String TimeCreatedMS) {
        Connection connection = null;
        boolean result = false;
        PreparedStatement preparedStatement = null;
        deleteEntry(MemberID);
        try {
            connection = DriverManager.getConnection(DatabaseURL);
            preparedStatement = connection.prepareStatement("INSERT INTO " + TableName + "(MenberID, Mood, MoodTime, SleepTime, TimeCreatedMS) VALUES (?,?,?,?,?)");
            preparedStatement.setString(1, MemberID);
            preparedStatement.setString(2, Mood);
            preparedStatement.setString(3, MoodTime);
            preparedStatement.setString(4, Sleep);
            preparedStatement.setString(4, TimeCreatedMS);
            result = preparedStatement.execute();

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
        return result;
    }

    public boolean deleteEntry(String MemberID){
        log.debug("SQL: Deleting Sleep King entry for Member: " + MemberID);

        try {
            Connection connection = DriverManager.getConnection(DatabaseURL);
            boolean result = connection.createStatement().execute("DELETE FROM "+ TableName +
                    " WHERE MemberID = " + MemberID + ";");
            connection.close();
            return result;
        } catch (SQLException e) {
            log.error(e.toString());

        }
        return false;
    }
}
