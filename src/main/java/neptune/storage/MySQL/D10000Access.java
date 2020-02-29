package neptune.storage.MySQL;

import neptune.Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class D10000Access {
    String DatabaseURL = Main.DatabaseURL;
    String TableName = "D10000";
    public String getResult(int Result) {
        System.out.println("SQL: Retrieving D10K Result #: " + Result);
        Connection connection = null;
        String results = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection(DatabaseURL);
            resultSet = connection.prepareStatement("SELECT Result FROM " + TableName + " Where ID = " + Result).executeQuery();
            if (resultSet.isClosed()) {
                System.out.println("SQL: Result does not Exist in database!");
            }
            if (!resultSet.next()) {
                connection.close();
                throw new SQLException();
            }
            results = resultSet.getString(1);
            resultSet.close();
            connection.close();

        } catch (SQLException e) {
            System.out.println("    Error Code= " + e.getErrorCode());
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
        return results;

    }
}
