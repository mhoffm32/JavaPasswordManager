package Package1;

import java.sql.*;
import java.util.HashMap;

public class PassAdapter {

    Connection connection;

    public PassAdapter(Connection conn, Boolean reset) throws SQLException {
        connection = conn;
        if (reset) {
            Statement stmt = connection.createStatement();
            try {
                // Remove tables if database tables have been created.
                // This will throw an exception if the tables do not exist
                stmt.execute("DROP TABLE Passwords");

            } catch (SQLException ex) {

            } finally {
                // Create the table of Matches
                stmt.execute("CREATE TABLE Passwords ("
                        +"ID INT NOT NULL PRIMARY KEY,"
                        + "Application CHAR(15) NOT NULL, "
                        + "Password CHAR(15) NOT NULL)");
                populateSamples();
            }
        }
    }

    private void populateSamples() throws SQLException{
        // Create a listing of the matches to be played
        this.insertPass("Google", "Hi1234");
    }

    public int getMax() throws SQLException {
        int num = 0;

        Statement stmt = connection.createStatement();
        String query = "SELECT Max(MatchNumber) FROM Matches";
        ResultSet result = stmt.executeQuery(query);

        if(result.next()){
            num = result.getInt(1)+1;
        }
        return num;
    }

    public void insertPass(String app, String pass) throws SQLException {
        Statement stmt = connection.createStatement();
        int index = this.getMax();
        stmt.executeUpdate("INSERT INTO Matches (ID,Application,Password) " + "VALUES (" + index + " , '" + app + "','" + pass+"')");
    }
    public void deletePass(String app) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("DELETE FROM Passwords WHERE Application = '" + app + "'");
    }

    public void changePass(String oldPass, String newPass) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("UPDATE Passwords SET Password = '" + newPass + "' WHERE Password = '" + oldPass + "'");
    }


    public HashMap<String,String> getPassMap() throws SQLException {
       HashMap<String,String> map = new HashMap<>();
        ResultSet rs;
        Statement stmt = connection.createStatement();

        rs = stmt.executeQuery("SELECT * FROM Passwords");

        while(rs.next()){
            map.put(rs.getString("Application"),rs.getString("Password"));
        }

        return map;
    }





}


