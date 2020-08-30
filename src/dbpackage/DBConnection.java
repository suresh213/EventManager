
package dbpackage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    
    // connection object
    Connection con;
    public DBConnection() {
        try {
            // get connection
            Class.forName("com.mysql.jdbc.Driver");  
            this.con = DriverManager.getConnection("jdbc:mysql://localhost:3306/eventmanager","suresh","1234");
        } catch(SQLException | ClassNotFoundException e) {
           System.out.println("Error " + e );
       }
    }
    
    public Connection getConnection() {
        return con;
    }
    
}
