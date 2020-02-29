package neptune.storage.MySQL;

import neptune.Main;

import java.sql.*;

public class GetAuthToken {
    private String DatabaseURL = Main.DatabaseURL;
    private final String TableName = "AuthKeys";
    public String GetToken(String Service) {
        String token = null;
        Connection connection = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection(DatabaseURL);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT Token FROM AuthKeys WHERE Service = ?");
            preparedStatement.setString(1, Service);
            resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                throw new SQLException();
            }
            token = resultSet.getString(1);
            resultSet.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
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
        return token;
    }

}
