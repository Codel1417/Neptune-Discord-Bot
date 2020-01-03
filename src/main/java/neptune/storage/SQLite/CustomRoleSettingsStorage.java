package neptune.storage.SQLite;

import java.sql.*;

public class CustomRoleSettingsStorage {
    private String DatabaseURL = Database.DatabaseURL;
    private final String TableName = "CustomRoleSettings";

    public CustomRoleSettingsStorage(){
        Connection connection;
        try {
            connection = DriverManager.getConnection(DatabaseURL);
            connection.createStatement().execute("CREATE TABLE IF NOT EXISTS `"+ TableName +"` (\n" +
                    "  `GuildID` INT NOT NULL,\n" +
                    "  `Enabled` VARCHAR(45) NOT NULL,\n" +
                    "  PRIMARY KEY ('GuildID'),\n" +
                    "  CONSTRAINT `GuildID`\n" +
                    "    FOREIGN KEY (`GuildID`)\n" +
                    "    REFERENCES `Guilds` (`GuildID`)\n" +
                    "    ON DELETE CASCADE\n" +
                    "    ON UPDATE CASCADE)");
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean getEnabled(String GuildID){
        addGuild(GuildID);
        System.out.println("SQL: Retrieving Settings for Guild: " + GuildID);
        Connection connection;
        try {
            connection = DriverManager.getConnection(DatabaseURL);
            ResultSet resultSet = connection.prepareStatement("SELECT Enabled FROM "+ TableName +" Where GuildID = " + GuildID).executeQuery();
            String result;
            result = resultSet.getString(1);
            resultSet.close();
            connection.close();
            if (result.equalsIgnoreCase("enabled")){
                return true;
            }
            else return false;

        } catch (SQLException e) {
            System.out.println("SQL: Error Code= "+ e.getErrorCode());
            System.out.println("SQL: Role entry not found");
            e.printStackTrace();
            return false;
        }
    }
    public boolean setEnabled(String GuildID, String enabled){
        try {
            Connection connection = DriverManager.getConnection(DatabaseURL);
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE "+TableName+" SET Enabled = ? WHERE GuildID = " + GuildID);
            preparedStatement.setString(1,enabled);
            boolean result =  preparedStatement.execute();
            connection.close();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean addGuild(String GuildID){
        System.out.println("SQL: Adding Role Guild " + GuildID);
        try {
            Connection connection;
            connection = DriverManager.getConnection(DatabaseURL);
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO "+ TableName +"(GuildID, Enabled) VALUES(?,?)");
            preparedStatement.setString(1, GuildID);
            preparedStatement.setString(2,"disabled");
            preparedStatement.execute();
            connection.close();
            return true;
        } catch (SQLException e) {
            if(e.getErrorCode() == 19){
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
}
