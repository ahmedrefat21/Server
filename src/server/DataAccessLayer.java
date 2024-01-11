/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.derby.jdbc.ClientDriver;

/**
 *
 * @author ahmed
 */
public class DataAccessLayer {
    
    private static DataAccessLayer DataRefaerence;
    private Connection con;
    private ResultSet rs ;
    private PreparedStatement pst;
    
    public synchronized ResultSet getResultSet(){
        return rs;
    }
    
    private  DataAccessLayer() throws SQLException{
         DriverManager.registerDriver(new ClientDriver());
         con = DriverManager.getConnection("jdbc:derby://localhost:1527/TicTacToe","root","root");
    }

    public synchronized static DataAccessLayer startDataBase() throws SQLException {
        if(DataRefaerence == null){
            DataRefaerence = new DataAccessLayer();
        }
        return DataRefaerence;
    }
    public synchronized void login(PlayerDTO player) throws SQLException{
                //take email and password
        pst = con.prepareStatement("update player set ISONLINE = ?  where email = ? and password = ? ",ResultSet.TYPE_SCROLL_SENSITIVE ,ResultSet.CONCUR_UPDATABLE  );
        pst.setString(1, "true");
        pst.setString(2, player.getEmail());
        pst.setString(3, player.getPassword());
        pst.executeUpdate(); 
        updateResultSet();    
       
    }
    //username ,email, pass
    public synchronized void SignUp(PlayerDTO player) throws SQLException{
                //take username - email - password
        pst = con.prepareStatement("insert into player(username,email,password) values(?,?,?)");
        pst.setString(1, player.getUsername());
        pst.setString(2, player.getEmail());
        pst.setString(3, player.getPassword());
        pst.executeUpdate();
        login(player); ///it takes username + password

    }
    

    public synchronized void changeOnline(){
        try {
            pst = con.prepareStatement("update player set ISONLINE = ? ",ResultSet.TYPE_SCROLL_SENSITIVE ,ResultSet.CONCUR_UPDATABLE  );
            pst.setString(1, "false"); //not online
            pst.executeUpdate(); 
            updateResultSet();
            } catch (SQLException ex) {
            System.out.println("Changed user status to offline");
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
        
    public synchronized void changeToNotAvailable(){
        try {
            pst = con.prepareStatement("update player set AVALIBLE = ? ",ResultSet.TYPE_SCROLL_SENSITIVE ,ResultSet.CONCUR_UPDATABLE  );
            pst.setString(1, "false"); //he is playing now
            //mafrood a7ot playe.setAvailabe("false")???????
            pst.executeUpdate();
            updateResultSet();
        } catch (SQLException ex) {
            System.out.println("Changed user status to NOT AVALIBLE");
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
       
    public synchronized void disableConnection() throws SQLException{
        changeOnline();
        changeToNotAvailable();

        con.close();
        rs.close();
        pst.close();
        DataRefaerence = null;
    }

        
    
    


    public synchronized String getUserName(PlayerDTO player){
        String userName;
        ResultSet result;
        PreparedStatement pstCheck;
        try {
            pstCheck = con.prepareStatement("select * from player where email = ?");
            pstCheck.setString(1,player.getEmail());
            result = pstCheck.executeQuery();
            result.next();
            userName = result.getString(2);
            return userName;
        } catch (SQLException ex) {
            System.out.println("Invalod Email address");
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public synchronized void isPlaying(String player1, String player2){
        try {
            pst = con.prepareStatement("update player set avalible = true  where email = ?",ResultSet.TYPE_SCROLL_SENSITIVE ,ResultSet.CONCUR_UPDATABLE  );
            pst.setString(1, player1);
            pst.executeUpdate(); 
            pst = con.prepareStatement("update player set avalible = true  where email = ?",ResultSet.TYPE_SCROLL_SENSITIVE ,ResultSet.CONCUR_UPDATABLE  );
            pst.setString(1, player2);
            pst.executeUpdate();
            updateResultSet();
        } catch (SQLException ex) {
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Boolean checkIsOnline(PlayerDTO player){
        ResultSet checkRs;
        PreparedStatement pstCheck;
        Boolean isActive ;
        try {
            pstCheck = con.prepareStatement("select isonline from player where email = ?");
            pstCheck.setString(1, player.getEmail());
            checkRs = pstCheck.executeQuery();
            checkRs.next();
            System.out.println("checkIsActive true ");
            isActive = checkRs.getBoolean("isactive");
            System.out.println("checkIsActive " +isActive);
            return isActive ;
        } catch (SQLException ex) {
            System.out.println("Invalod Email address");
            //Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false; 
    }
    
    public synchronized boolean checkPlaying(String player){

        boolean available;
        ResultSet result;
        PreparedStatement pstCheckAv;
        try {
            pstCheckAv = con.prepareStatement("select * from player where username = ?");
            pstCheckAv.setString(1, player);
            result = pstCheckAv.executeQuery();
            result.next();
            available = result.getBoolean(4);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public synchronized String getData(PlayerDTO player){
        String email;
        ResultSet result;
        PreparedStatement pstCheck;
        try {
            pstCheck = con.prepareStatement("select * from player where email = ?");
            pstCheck.setString(1, player.getUsername());
            result = pstCheck.executeQuery();
            result.next();
            email = result.getString(3);
            return email;
        } catch (SQLException ex) {
            System.out.println("Invalod Email address");
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    
    
    public synchronized void updateResultSet(){
    
        try {
            this.pst= con.prepareStatement("SELECT * FROM PLAYER",ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            this.rs=pst.executeQuery();
        } catch (SQLException ex) {
           ex.printStackTrace();
        }
 
    }
    
    public synchronized int countOfflineUsers(){
    
        try {
            this.pst=con.prepareStatement("SELECT COUNT(*) FROM PLAYER WHERE ISONLINE = false",ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            ResultSet result=pst.executeQuery();
            return result.next() ? result.getInt(1):result.getInt(-1);
        } catch (SQLException ex) {
             ex.printStackTrace();
        }
        return -1;
    }
    
    public synchronized void setOnline(PlayerDTO player){
    
        try {
            pst=con.prepareStatement("UPDATE PLAYER SET ISONLINE = ? WHERE EMAIL = ?");
            pst.setString(1,"true");
            pst.setString(2, player.getEmail());
            System.out.println("email:"+player.getEmail()+"is online now");
            pst.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    
    public synchronized void setNotAvalible(PlayerDTO player){
      
        try {
            pst=con.prepareStatement("UPDATE PLAYER SET AVALIBLE = false WHERE EMAIL = ?");
            pst.setString(1,player.getEmail());
            pst.executeUpdate();
            updateResultSet();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    
    }
    
    
    public synchronized ResultSet  getOnlinePlayers(){
    
        try {
            this.pst=con.prepareStatement("SELECT * FROM PLAYER WHERE ISONLINE =TRUE",ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            return pst.executeQuery();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
    public synchronized String checkRegister(String username , String email){
        ResultSet checkRs;
        PreparedStatement pstCheck;
        
        try {
            String queryString= new String("select USERNAME from PLAYER where USERNAME = ?");
            pstCheck = con.prepareStatement("select * from PLAYER where USERNAME = ? and EMAIL = ?");
            pstCheck.setString(1, username);
            pstCheck.setString(2, email);
            checkRs = pstCheck.executeQuery();
            if(checkRs.next()){
                return "already signed-up";
            }
        } catch (SQLException ex) {
            System.out.println("here ");
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Registered Successfully";
    } 
    
    public synchronized String checkSignIn(PlayerDTO player){
        ResultSet checkRs;
        PreparedStatement pstCheck;
        String check;  
        System.out.println("checkSignIn " +checkIsOnline(player.getEmail()));
        if(!checkIsOnline(player.getEmail())){
            System.out.println(" checkSignIn: " +checkIsOnline(player.getEmail()));
                try { 
            pstCheck = con.prepareStatement("select * from PLAYER where EMAIL = ? ");
            pstCheck.setString(1, player.getEmail());
            checkRs = pstCheck.executeQuery();
            if(checkRs.next()){
                if(player.getPassword().equals(checkRs.getString(4))){
                    return "Logged in successfully";
                }
                return "Password is incorrect";
            }
            return "Email is incorrect";
          } catch (SQLException ex) {
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
            return "Connection issue, please try again later";
             }
        }else{
            System.out.println("This Email alreay sign-in " + checkIsOnline(email));
           return "This Email is alreay sign-in";  
        }
              
    }
    
     public synchronized int getScore(String email){
        int score;
        ResultSet checkRs;
        PreparedStatement pstCheck;
 
        try {
            pstCheck = con.prepareStatement("select * from PLAYER where EMAIL = ?");
            pstCheck.setString(1, email);
            checkRs = pstCheck.executeQuery();
            checkRs.next();
            score = checkRs.getInt(5);
            return score;
        } catch (SQLException ex) {
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    } 
    public synchronized void updateScore(String mail, int score){
        try {
            pst = con.prepareStatement("update PLAYER set SCORE = ?  where EMAIL = ?",ResultSet.TYPE_SCROLL_SENSITIVE ,ResultSet.CONCUR_UPDATABLE  );
            pst.setInt(1, score);
            pst.setString(2, mail);
            pst.executeUpdate();
            updateResultSet();
        } catch (SQLException ex) {
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   
}