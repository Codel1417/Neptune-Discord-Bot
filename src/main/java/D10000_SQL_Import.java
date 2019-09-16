import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class D10000_SQL_Import {


    public static void main(String[] args){
        String DatabaseURL = "jdbc:sqlite:NepDB.db";
        String TableName = "D10000";
        try {
            Connection connection = DriverManager.getConnection(DatabaseURL);
            connection.createStatement().execute("CREATE TABLE IF NOT EXISTS `" + TableName + "` (\n" +
                    "  `ID` INT NOT NULL,\n" +
                    "  `Result` VARCHAR(45) NOT NULL,\n" +
                    "  PRIMARY KEY (ID))");
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            Scanner scanner = new Scanner(new File("d10000_random_result.txt"));
            while (scanner.hasNext()){
                String line = scanner.nextLine();
                int ID = Integer.parseInt(line.substring(0,4).trim());
                String Content = line.substring(5).trim();
                System.out.println("ID: " + ID + " Content: " + Content);
                try {
                    Connection connection;
                    connection = DriverManager.getConnection(DatabaseURL);
                    PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO "+ TableName +"(ID, Result) VALUES(?,?)");
                    preparedStatement.setInt(1, ID);
                    preparedStatement.setString(2,Content);
                    preparedStatement.execute();
                    connection.close();
                } catch (SQLException e) {
                   // e.printStackTrace();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }
}
