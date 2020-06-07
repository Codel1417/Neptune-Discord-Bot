package neptune.storage.MySQL;

import neptune.Main;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class LeaderboardStorage {
    private String DatabaseURL = Main.DatabaseURL;
    private final String TableName = "Leaderboard";
    protected static final Logger log = LogManager.getLogger();

    public boolean addMember(String GuildID, String MemberId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean result = false;
        try {
            connection = DriverManager.getConnection(DatabaseURL);
            preparedStatement = connection.prepareStatement("INSERT INTO " + TableName + "(GuildID, MemberID) VALUES(?,?)");
            preparedStatement.setString(1, GuildID);
            preparedStatement.setString(2, MemberId);

            preparedStatement.execute();
            connection.close();
            result = true;
        } catch (SQLException e) {
            //Duplicate Data, Do nothing
            if (e.getErrorCode() == 1062) {
                //System.out.println("    Data Exists :)");
                result = true;
            } else {
                log.error("Error Code= " + e.getErrorCode());
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
        return result;
    }
    public boolean incrementPoints(String GuildID, String MemberID) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean result = false;
        try {
            connection = DriverManager.getConnection(DatabaseURL);
            preparedStatement = connection.prepareStatement("UPDATE " + TableName + " SET Points = Points + 1 WHERE GuildID = " + GuildID + " AND MemberID = " + MemberID);
            if (preparedStatement.execute()) {
                connection.close();
                result = true;
            } else {
                connection.close();
                addMember(GuildID, MemberID);
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
                preparedStatement.close();
            } catch (SQLException e) {
                log.error(e.toString());
            }
        }
        return result;
    }
    public int getPoints(String GuildID, String MemberID) {
        Connection connection = null;
        int result = 0;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection(DatabaseURL);
            resultSet = connection.prepareStatement("Select Points FROM " + TableName + " WHERE GuildID = " + GuildID + " AND  MemberID = " + MemberID).executeQuery();
            connection.close();
            result = resultSet.getInt(1);
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
    public Map getTopUsers(String GuildID){
        Map results = new HashMap();
        Connection connection = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection(DatabaseURL);
            resultSet = connection.prepareStatement("SELECT MemberID, Points FROM neptune.leaderboard WHERE GuildID = " +GuildID +" ORDER BY points DESC LIMIT 10;").executeQuery();
            if (resultSet.isClosed()){
                return results;
            }
            resultSet.next();
            while (!resultSet.isAfterLast()) {
                results.put(resultSet.getString(1), resultSet.getString(2));
                resultSet.next();
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
            return results;
        }
    }
}
