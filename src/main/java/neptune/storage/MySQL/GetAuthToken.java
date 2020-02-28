package neptune.storage.MySQL;

import neptune.Main;

import java.sql.*;

public class GetAuthToken {
    private String DatabaseURL = Main.DatabaseURL;
    private final String TableName = "AuthKeys";
    public String GetToken(String Service){
        try {
            Connection connection;
            connection = DriverManager.getConnection(DatabaseURL);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT Token FROM AuthKeys WHERE Service = ?");
            preparedStatement.setString(1,Service);
            ResultSet resultSet  = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                throw new SQLException();
            }
                String token = resultSet.getString(1);
            resultSet.close();
            connection.close();
            return token;

        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

}
