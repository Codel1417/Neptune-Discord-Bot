package neptune.storage.MySQL;

import neptune.Main;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class GreatSleepKingStorage {
    private String DatabaseURL = Main.DatabaseURL;
    private final String TableName = "GreatSleepKing";

    public Map<String,String> getSleepInfo(String MemberID){
        HashMap<String, String> result = new HashMap();
        Connection connection;
        try{
            connection = DriverManager.getConnection(DatabaseURL);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT SleepTime, Mood, MoodTime, TimeCreatedMS FROM " + TableName + " WHERE MemberID =  (?)");
            preparedStatement.setString(1,MemberID);
            ResultSet resultSet  = preparedStatement.executeQuery();
            if (!resultSet.isClosed()){
                result.put("SleepTime",resultSet.getString(1));
                result.put("Mood",resultSet.getString(2));
                result.put("MoodTime",resultSet.getString(3));
                result.put("TimeCreatedMS",resultSet.getString(4));
                resultSet.close();
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    public boolean setTime(String MemberID, String Mood, String MoodTime, String TimeCreatedMS){
        Connection connection;
        boolean result = false;
        try{
            connection = DriverManager.getConnection(DatabaseURL);
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO " +  TableName + "(MenberID, Mood, MoodTime, TimeCreatedMS) VALUES (?,?,?,?)");
            preparedStatement.setString(1,MemberID);
            preparedStatement.setString(2,Mood);
            preparedStatement.setString(3,MoodTime);
            preparedStatement.setString(4,TimeCreatedMS);
            result = preparedStatement.execute();
            connection.close();
            preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    public void updateTime(String MemberID, String Mood, String MoodTime, String TimeCreatedMS){

    }
}
