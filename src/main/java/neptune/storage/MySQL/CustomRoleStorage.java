package neptune.storage.MySQL;

import neptune.Main;

import java.sql.*;

public class CustomRoleStorage {
    private String DatabaseURL = Main.DatabaseURL;
    private final String TableName = "CustomRole";

    public boolean addRole(String MemberID, String GuildID, String RoleID) {
        System.out.println("SQL: Adding new Custom Role for guild: " + GuildID + " Author ID: " + MemberID + " Role ID: " + RoleID);
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean result = false;
        try {
            connection = DriverManager.getConnection(DatabaseURL);
            preparedStatement = connection.prepareStatement("INSERT INTO " + TableName + "(GuildID, MemberID, RoleID) VALUES(?,?,?)");
            preparedStatement.setString(1, GuildID);
            preparedStatement.setString(2, MemberID);
            preparedStatement.setString(3, RoleID);
            preparedStatement.execute();
            result = true;
        } catch (SQLException e) {
            System.out.println("SQL: Error Code= " + e.getErrorCode());
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
    public boolean removeRole(String RoleID) {
        System.out.println("SQL: Deleting Role; " + RoleID);
        boolean result = false;
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DatabaseURL);
            result = connection.createStatement().execute("DELETE FROM " + TableName +
                    " WHERE RoleID = " + RoleID + ";");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    public String getRoleID(String GuildID, String MemberID) {
        System.out.println("SQL: Retrieving role for Member: " + MemberID);
        Connection connection = null;
        String result = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection(DatabaseURL);
            resultSet = connection.prepareStatement("SELECT RoleID FROM " + TableName + " Where MemberID = " + MemberID + " AND GuildID = " + GuildID).executeQuery();
            result = resultSet.getString(1);
        } catch (SQLException e) {
            System.out.println("SQL: Error Code= " + e.getErrorCode());
            System.out.println("SQL: Role entry not found");
            //e.printStackTrace();
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
