package Neptune.Storage.SQLite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class D10000Access {
    String DatabaseURL = "jdbc:sqlite:NepDB.db";
    String TableName = "D10000";
    public String getResult(int Result){
        System.out.println("SQL: Retrieving D10K Result #: " + Result);
        Connection connection;
        try {
            connection = DriverManager.getConnection(DatabaseURL);
            ResultSet resultSet = connection.prepareStatement("SELECT Result FROM " + TableName + " Where ID = " + Result).executeQuery();
            String results = resultSet.getString(1);
            if (resultSet.isClosed()){
                System.out.println("SQL: Result does not Exist in database!");
                return null;
            }
            resultSet.close();
            connection.close();
            return results;

        } catch (SQLException e) {
            System.out.println("    Error Code= "+ e.getErrorCode());
            e.printStackTrace();
            return null;
        }
    }
}
