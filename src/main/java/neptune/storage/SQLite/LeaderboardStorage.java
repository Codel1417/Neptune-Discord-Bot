package neptune.storage.SQLite;

import neptune.Main;

import java.sql.*;

public class LeaderboardStorage {
    private String DatabaseURL = Main.DatabaseURL;
    private final String TableName = "Leaderboard";

    public boolean addMember(String GuildID, String MemberId){
        try {
            Connection connection;
            connection = DriverManager.getConnection(DatabaseURL);
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO "+ TableName +"(GuildID, MemberID) VALUES(?,?)");
            preparedStatement.setString(1, GuildID);
            preparedStatement.setString(2, MemberId);

            preparedStatement.execute();
            connection.close();
            return true;
        } catch (SQLException e) {
            //Duplicate Data, Do nothing
            if(e.getErrorCode() == 1062){
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
    public boolean incrementPoints(String GuildID, String MemberID){
        try {
            Connection connection;
            connection = DriverManager.getConnection(DatabaseURL);
            PreparedStatement preparedStatement =  connection.prepareStatement("UPDATE " + TableName + " SET Points = Points + 1 WHERE GuildID = " + GuildID + " AND MemberID = " + MemberID);
            if(preparedStatement.execute()){
                connection.close();
                return true;
            }
            else{
                connection.close();
                addMember(GuildID,MemberID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public int getPoints(String GuildID, String MemberID){
        try {
            Connection connection;
            connection = DriverManager.getConnection(DatabaseURL);
            ResultSet resultSet = connection.prepareStatement("Select Points FROM " + TableName + " WHERE GuildID = " + GuildID + " AND  MemberID = " + MemberID).executeQuery();
            connection.close();
            return resultSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
