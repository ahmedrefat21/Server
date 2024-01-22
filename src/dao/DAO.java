
package dao;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.derby.jdbc.ClientDriver;


public class DAO {
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public synchronized void setNotBusy (String email){
        try {
            DriverManager.registerDriver(new ClientDriver());
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/TicTacToy", "root", "root");
            PreparedStatement ps = connection.prepareStatement("update player set isPlaying = false where email = ?");
            ps.setString(1, email);
            ps.executeUpdate();
            ResultSet();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}


