package Neptune.Storage;



import java.sql.*;
import java.util.Objects;

public class SQLiteConnection implements StorageCommands {
    private String DatabaseURL = "jdbc:sqlite:NepDB.db";
    private final String TableName = "Neptune";
    SQLiteConnection(){
        System.out.println("Init SQLite");
        try {
            Connection connection = DriverManager.getConnection(DatabaseURL);
            connection.createStatement().execute("CREATE TABLE IF NOT EXISTS " + TableName + "(" +
                    " ID TEXT PRIMARY KEY," +
                    " Data TEXT NOT NULL," +
                    " UNIQUE(ID)" +
                    ")");
            connection.close();
        } catch (SQLException e) {
                System.out.println("Error Code= "+ e.getErrorCode());
                e.printStackTrace();
            }
    }
    @Override
    public boolean addData(String Id, String Data) throws MissingDataException {
        try {
            if (Objects.equals(Data, "")) throw new MissingDataException("No Data to add to database");
            System.out.println(
                    "Adding New Item to Database \n" +
                    "   ID: " + Id +"\n" +
                    "   Data: " + Data + "\n" +
                    "   Table: " + TableName);

            Connection connection = DriverManager.getConnection(DatabaseURL);
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO "+ TableName +"(ID, Data) VALUES(?,?)");
            preparedStatement.setString(1, Id);
            preparedStatement.setString(2,Data);
            preparedStatement.execute();
            connection.close();
            return true;
        } catch (SQLException e) {
            if(e.getErrorCode() == 19){
                System.out.println("    Data Exists :)");
                return true;
            }
            else{
                System.out.println("    Error Code= "+ e.getErrorCode());
                e.printStackTrace();
            }
            return false;
        }
    }

    @Override
    public String getData(String Id){
        ResultSet resultSet;
        try {
            System.out.println(
                    "Getting Item from Database \n" +
                            "   ID: " + Id +"\n" +
                            "   Table: " + TableName);
            Connection connection = DriverManager.getConnection(DatabaseURL);
            resultSet = connection.prepareStatement("SELECT ID, Data FROM "+ TableName +" Where ID = " + Id).executeQuery();
            String result = resultSet.getString("Data");
            resultSet.close();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean updateData(String Id, String Data) throws MissingDataException {
        if (Data == "") throw new MissingDataException("No Data to add to database");
        try {
            System.out.println(
                    "Updating Item In Database \n" +
                            "   ID: " + Id +"\n" +
                            "   Data: " + Data + "\n" +
                            "   Table: " + TableName);

            Connection connection = DriverManager.getConnection(DatabaseURL);
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE "+TableName+" SET Data = ? WHERE id = " + Id);
            preparedStatement.setString(1,Data);
            boolean result =  preparedStatement.execute();
            connection.close();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }



}
