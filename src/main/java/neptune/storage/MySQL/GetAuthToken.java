package neptune.storage.MySQL;

import neptune.Main;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class GetAuthToken {
    private String DatabaseURL = Main.DatabaseURL;
    private final String TableName = "AuthKeys";
    protected static final Logger log = LogManager.getLogger();
    public String GetToken(String Service) {
        String token = null;
        Connection connection = null;
        ResultSet resultSet = null;
        try {
            log.debug("Getting auth token: " + Service);
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
            log.fatal(e.toString());
            System.exit(1);
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
        return token;
    }

}
