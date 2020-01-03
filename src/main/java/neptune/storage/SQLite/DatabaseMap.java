package neptune.storage.SQLite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseMap {
    private String DatabaseURL = Database.DatabaseURL;
    private final String TableName = "DB_Info";

    public DatabaseMap(){
        Connection connection;
        try {
            connection = DriverManager.getConnection(DatabaseURL);
            connection.createStatement().execute("CREATE TABLE IF NOT EXISTS `"+ TableName +"` (\n" +
                    "  `Key` VARCHAR(45) NOT NULL,\n" +
                    "  `Value` VARCHAR(45) NOT NULL,\n" +
                    "  PRIMARY KEY (`Key`))");
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
