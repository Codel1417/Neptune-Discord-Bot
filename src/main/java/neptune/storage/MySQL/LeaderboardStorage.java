package neptune.storage.MySQL;

import neptune.Main;

import java.sql.*;

public class LeaderboardStorage {
    private String DatabaseURL = Main.DatabaseURL;
    private final String TableName = "Leaderboard";

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
                System.out.println("    Error Code= " + e.getErrorCode());
                e.printStackTrace();
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
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
