
package dao;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
    
    
    
    
    
    public synchronized void updateScore(String mail, int score){
        try {
            DriverManager.registerDriver(new ClientDriver());
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/TicTacToy", "root", "root");
            PreparedStatement ps = connection.prepareStatement("update player set score = ?  where email = ?",ResultSet.TYPE_SCROLL_SENSITIVE ,ResultSet.CONCUR_UPDATABLE  );
            ps.setInt(1, score);
            ps.setString(2, mail);
            ps.executeUpdate();
            ResultSet();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    
    
    public synchronized String getUserName(String email){
        try {
            DriverManager.registerDriver(new ClientDriver());
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/TicTacToy", "root", "root");
            PreparedStatement ps= connection.prepareStatement("select * from player where email = ?");
            ps.setString(1, email);
            result = ps.executeQuery();
            result.next();
            String userName = result.getString(2);
            return userName;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
    
    
    
    
    public synchronized boolean checkBusy(String player){
        try {
            DriverManager.registerDriver(new ClientDriver());
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/TicTacToy", "root", "root");
            PreparedStatement ps= connection.prepareStatement("select * from player where username = ?");
            ps.setString(1, player);
            result = ps.executeQuery();
            result.next();
            boolean available = result.getBoolean(4);
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;

    }
}


