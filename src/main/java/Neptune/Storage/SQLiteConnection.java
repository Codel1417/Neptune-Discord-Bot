package Neptune.Storage;



import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class SQLiteConnection implements StorageCommands {
    private String DatabaseURL = "jdbc:sqlite:NepDB.db";
    private final String TableName = "Neptune";
    SQLiteConnection(){
        try {
            DriverManager.getConnection(DatabaseURL).createStatement().execute("CREATE TABLE IF NOT EXISTS " + TableName + "(" +
                    " ID TEXT PRIMARY KEY," +
                    " Data TEXT NOT NULL," +
                    " UNIQUE(ID)" +
                    ")");
        } catch (SQLException e) {
                System.out.println("Error Code= "+ e.getErrorCode());
                e.printStackTrace();
            }
    }
    @Override
    public boolean addData(String Id, String Data) throws MissingDataException {
        try {
            if (Objects.equals(Data, "")) throw new MissingDataException("No Data to add to database");

            PreparedStatement preparedStatement = DriverManager.getConnection(DatabaseURL).prepareStatement("INSERT INTO "+ TableName +"(ID, Data) VALUES(?,?)");
            preparedStatement.setString(1, Id);
            preparedStatement.setString(2,Data);
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            if(e.getErrorCode() == 19){
                System.out.println("Data Exists :)");
                return true;
            }
            else{
                System.out.println("Error Code= "+ e.getErrorCode());
                e.printStackTrace();
            }
            return false;
        }
    }


    @Override
    public String getData(String Id){
        ResultSet resultSet;
        try {
            resultSet = DriverManager.getConnection(DatabaseURL).prepareStatement("SELECT ID, Data FROM "+ TableName +" Where ID = " + Id).executeQuery();
            return resultSet.getString("Data");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean updateData(String Id, String Data) throws MissingDataException {
        if (Data == "") throw new MissingDataException("No Data to add to database");
        try {
            PreparedStatement preparedStatement = DriverManager.getConnection(DatabaseURL).prepareStatement("UPDATE Guilds SET Data = ? WHERE id = " + Id);
            preparedStatement.setString(1,Data);
            preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }



}
