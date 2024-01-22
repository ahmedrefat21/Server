package dao;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.derby.jdbc.ClientDriver;



public class DAO {
    
    private static DAO db;
    private Connection connection;
    private ResultSet result ;
    private PreparedStatement pst;
    
    public synchronized ResultSet getResultSet(){
        return result;
    }
       
    private  DAO() throws SQLException{
         DriverManager.registerDriver(new ClientDriver());
         connection = DriverManager.getConnection("jdbc:derby://localhost:1527/TicTacToe","root","root");
    }
  
    public synchronized void ResultSet(){
        try {
            DriverManager.registerDriver(new ClientDriver());
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/TicTacToy","root","root");
            PreparedStatement ps=connection.prepareStatement("Select * from player",ResultSet.TYPE_SCROLL_SENSITIVE ,ResultSet.CONCUR_READ_ONLY  );
            result= ps.executeQuery(); 
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
  
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
    
    public synchronized int retriveScore(String email){
        try {
            DriverManager.registerDriver(new ClientDriver());
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/TicTacToy", "root", "root");
            PreparedStatement pst = connection.prepareStatement("select * from player where email = ?");
            pst.setString(1, email);
            result = pst.executeQuery();
            result.next();
            int score = result.getInt(5);
            return score;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    } 
    
    public synchronized String checkisalreadyloginIn(String email, String password){
        if(!checkIsOnline(email)){
            try { 
                PreparedStatement pst = connection.prepareStatement("select * from player where email = ? ");
                pst.setString(1, email);
                result = pst.executeQuery();
                if(result.next()){
                    if(password.equals(result.getString(4))){
                        return "Logged in successfully";
                    }
                    return "Password is incorrect";
                }
                return "Email is incorrect";
            } catch (SQLException ex) {
                ex.printStackTrace();
                return "Connection issue, please try again later";
            }
        }else{
            return "This Email is alreay sign-in";  
        }        
    }
    
    public Boolean checkIsOnline(String email){
         try {
            DriverManager.registerDriver(new ClientDriver());
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/TicTacToy", "root", "root");
            PreparedStatement ps= connection.prepareStatement("select isactive from player where email = ?");
            ps.setString(1, email);
            result = ps.executeQuery();
            result.next();
            Boolean isOnline = result.getBoolean("isactive");
            return isOnline ;
         } catch (SQLException ex) {
               ex.printStackTrace();
         }
         return false;
         
    }
    
    public synchronized void login(String email,String password) throws SQLException{
        DriverManager.registerDriver(new ClientDriver());
        connection = DriverManager.getConnection("jdbc:derby://localhost:1527/TicTacToy", "root", "root");
        PreparedStatement ps = connection.prepareStatement("update player set isActive = ?  where email = ? and password = ? ",ResultSet.TYPE_SCROLL_SENSITIVE ,ResultSet.CONCUR_UPDATABLE  );
        ps.setString(1, "true");
        ps.setString(2, email);
        ps.setString(3, password);
        ps.executeUpdate(); 
        ResultSet();          
    }
    
    public synchronized void  setOnline(boolean state , String email){
        try {
            DriverManager.registerDriver(new ClientDriver());
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/TicTacToy", "root", "root");
            PreparedStatement ps = connection.prepareStatement("update player set isActive = ? where email = ?");
            ps.setString(1, state+"");
            ps.setString(2, email);
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
    
    public synchronized String getEmail(String username){
        try {
            DriverManager.registerDriver(new ClientDriver());
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/TicTacToy", "root", "root");
            PreparedStatement ps= connection.prepareStatement("select * from player where username = ?");
            ps.setString(1, username);
            result = ps.executeQuery();
            result.next();
            String email = result.getString(3);
            return email;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
        
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
   
    public synchronized void changeToNotBusy(){
         try {
            DriverManager.registerDriver(new ClientDriver());
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/TicTacToy", "root", "root");
            PreparedStatement ps = connection.prepareStatement("update player set isPlaying = ? ",ResultSet.TYPE_SCROLL_SENSITIVE ,ResultSet.CONCUR_UPDATABLE  );
            ps.setString(1, "false");
            ps.executeUpdate(); 
            ResultSet();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
     
    public synchronized void changeToOffline(){
        try {
            DriverManager.registerDriver(new ClientDriver());
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/TicTacToy", "root", "root");
            PreparedStatement ps = connection.prepareStatement("update player set isActive = ? ",ResultSet.TYPE_SCROLL_SENSITIVE ,ResultSet.CONCUR_UPDATABLE  );
            ps.setString(1, "false");
            ps.executeUpdate(); 
            ResultSet();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
    }
    
    public synchronized void make2PlayersBusy(String player1, String player2){
        try {
            DriverManager.registerDriver(new ClientDriver());
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/TicTacToy", "root", "root");
            PreparedStatement ps = connection.prepareStatement("update player set isPlaying = true  where email = ?",ResultSet.TYPE_SCROLL_SENSITIVE ,ResultSet.CONCUR_UPDATABLE  );
            ps.setString(1, player1);
            ps.executeUpdate();
            ps = connection.prepareStatement("update player set isPlaying = true  where email = ?",ResultSet.TYPE_SCROLL_SENSITIVE ,ResultSet.CONCUR_UPDATABLE  );
            ps.setString(1, player2);
            ps.executeUpdate();
            ResultSet();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public synchronized static DAO getDataBase() throws SQLException {
        if(db == null){
            db = new DAO();
        }
        return db;
    }
    
          public synchronized ResultSet getActivePlayersChart(){
        try {
            this.pst =connection.prepareStatement("SELECT COUNT(*) AS true_count FROM PLAYER WHERE isactive = true",ResultSet.TYPE_SCROLL_SENSITIVE ,ResultSet.CONCUR_READ_ONLY  );
            return pst.executeQuery(); 
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("catch getactive");
            return null;
        }
        
    }
          

        
    public synchronized ResultSet getOfflinePlayers( ){   
        try {
            this.pst =connection.prepareStatement("SELECT COUNT(*) AS false_count FROM PLAYER WHERE isactive = false",ResultSet.TYPE_SCROLL_SENSITIVE ,ResultSet.CONCUR_READ_ONLY  );
            return pst.executeQuery(); 
        } catch (SQLException ex) {
            System.out.println(ex);
            return null;
        }
        
    }
    
    
        public synchronized ResultSet getOnlinePlayers( ){
        
        try {
            DriverManager.registerDriver(new ClientDriver());
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/TicTacToy", "root", "root");
            PreparedStatement ps =connection.prepareStatement("Select * from player where isactive = true ",ResultSet.TYPE_SCROLL_SENSITIVE ,ResultSet.CONCUR_READ_ONLY  );
            return ps.executeQuery(); 
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
        
    }
    
        public synchronized int getOfflineUsers() {
        try {
            DriverManager.registerDriver(new ClientDriver());
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/TicTacToy", "root", "root");
            PreparedStatement ps = connection.prepareStatement("select count(*) from player where isactive = false", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet r = ps.executeQuery();
            if (r.next()) {
                return r.getInt(1);
            } else {
                return -1;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }
        
        public synchronized void disConnection() {
        try {
            changeToOffline();
            changeToNotBusy();
            result.close();
            pst.close();
            connection.close();
            db = null;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}




