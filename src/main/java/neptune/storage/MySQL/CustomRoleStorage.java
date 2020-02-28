package neptune.storage.MySQL;

import neptune.Main;

import java.sql.*;

public class CustomRoleStorage {
    private String DatabaseURL = Main.DatabaseURL;
    private final String TableName = "CustomRole";

    public boolean addRole(String MemberID, String GuildID, String RoleID){
        System.out.println("SQL: Adding new Custom Role for guild: " + GuildID +  " Author ID: " + MemberID + " Role ID: " + RoleID);
        try {
            Connection connection;
            connection = DriverManager.getConnection(DatabaseURL);
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO "+ TableName +"(GuildID, MemberID, RoleID) VALUES(?,?,?)");
            preparedStatement.setString(1,GuildID);
            preparedStatement.setString(2,MemberID);
            preparedStatement.setString(3,RoleID);
            preparedStatement.execute();
            connection.close();
            return true;
        } catch (SQLException e) {
            System.out.println("SQL: Error Code= "+ e.getErrorCode());
            e.printStackTrace();
        }
        return false;
    }
    public boolean removeRole(String RoleID){
        System.out.println("SQL: Deleting Role; " + RoleID);
        try {
            Connection connection = DriverManager.getConnection(DatabaseURL);
            boolean result = connection.createStatement().execute("DELETE FROM "+ TableName +
                    " WHERE RoleID = " + RoleID + ";");
            connection.close();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public String getRoleID(String GuildID, String MemberID){
        System.out.println("SQL: Retrieving role for Member: " + MemberID);
        Connection connection;
        try {
            connection = DriverManager.getConnection(DatabaseURL);
            ResultSet resultSet = connection.prepareStatement("SELECT RoleID FROM "+ TableName +" Where MemberID = " + MemberID + " AND GuildID = " + GuildID).executeQuery();
            String result;
            result = resultSet.getString(1);
            resultSet.close();
            connection.close();
            return result;

        } catch (SQLException e) {
            System.out.println("SQL: Error Code= "+ e.getErrorCode());
            System.out.println("SQL: Role entry not found");
            //e.printStackTrace();
            return null;
        }
    }
}
