
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
    
    
    
    
    public synchronized void SignUp(String username , String email, String password) throws SQLException{
        DriverManager.registerDriver(new ClientDriver());
        connection = DriverManager.getConnection("jdbc:derby://localhost:1527/TicTacToy", "root", "root");
        PreparedStatement ps = connection.prepareStatement("insert into player(username,email,password) values(?,?,?)");
        ps.setString(1, username);
        ps.setString(2, email);
        ps.setString(3, password);
        ps.executeUpdate(); 
        login(email,password);
    }
    
    
    public synchronized String checkIsalreadysignedup(String username, String email) {
        try {
            DriverManager.registerDriver(new ClientDriver());
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/TicTacToy", "root", "root");
            PreparedStatement pst = connection.prepareStatement("select * from player where username = ? and email = ?");
            pst.setString(1, username);
            pst.setString(2, email);
            result = pst.executeQuery();
            if (result.next()) {
                return "signed-up before";
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return "Registered Successfully";
    }
    
}


