
package server;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

 
public class ConnectedPlayer {
     private ResultSet result;
     private Server server;
     private DataInputStream dis;
     private PrintStream ps;
     private Socket currentSocket;
     private String clientData,query;
     private String username,email;
     private Boolean loggedin;
     private StringTokenizer token;
     private PlayerDTO player; 
     private DataAccessLayer databaseInstance;
     static ArrayList<ConnectedPlayer> activeUsers = new ArrayList();
     static HashMap<String,ConnectedPlayer> game = new HashMap();
     
     
     
     public ConnectedPlayer(Socket socket){
     
     result = server.getResultSet();
  //   server = ServerHadler.getServer();
       try {
            dis = new DataInputStream(socket.getInputStream());
            ps = new PrintStream(socket.getOutputStream());
            currentSocket = socket;
         //   this.start();
       }catch (IOException ex) {
           System.out.println("1");
            ex.printStackTrace();
            // alert 
           try {
               socket.close();
           } catch (IOException ex1) {
               Logger.getLogger(ConnectedPlayer.class.getName()).log(Level.SEVERE, null, ex1);
           }
       }
     
     }
      public void run() throws SQLException{
       while(currentSocket.isConnected()){
           try {
               clientData = dis.readLine();
               System.out.println(clientData);
               if(clientData != null){
                   token = new StringTokenizer(clientData,"####");
                   query = token.nextToken();
                   switch(query){
                      case "SignUp":
                            signUp();
                            break;            
                    
                   }}
                   
                
               }  
        catch (IOException ex) {
               Logger.getLogger(ConnectedPlayer.class.getName()).log(Level.SEVERE, null, ex);
           }
           
      
      try {
                    currentSocket.close();
                } catch (IOException ex1) {
                    Logger.getLogger(ConnectedPlayer.class.getName()).log(Level.SEVERE, null, ex1);
                }
               // this.stop();
       if(currentSocket.isClosed()){
            System.out.println("3");
            System.out.println("close");
            server.getActivePlayers();
        }
      
      
      }
      }
      
       private void signUp() throws SQLException{
       String username = token.nextToken();
       String email = token.nextToken();
       String password = token.nextToken();
       email= player.getEmail();
       password =player.getPassword();
            
      String check= server.checkRegister(player);
       ps.println(check);
            if(check.equals("Registered Successfully")){
                ps.println(username+"###"+email); // send data to registerController
                server.SignUp(player);
                System.out.println("User is registered now , check database");   
                activeUsers.add(this);
            }else if (check.equals("already signed-up")){
                ps.println("already signed-up"+"###");
            }
       }
    
    private void signIn(){
        email = token.nextToken();
        String password = token.nextToken();
        String check;
        int score;
        System.out.println(email+" "+password);
        try{
            check = server.checkSignIn(player);
            if(check.equals("Logged in successfully")){
                score = server.getScore(player);
                username = server.getUserName(player);
                server.login(player);
                ps.println(check +"###" + score);
                ps.println(username+"###"+email+"###"+score); // send data to registerController
                loggedin = true;
                activeUsers.add(this);
            }else if(check.equals("This Email is alreay sign-in")){
                System.out.println("alread in connected");
                ps.println(check +"###");
            }else if(check.equals("Email is incorrect")){
                ps.println(check +"###");
            }else if(check.equals("Password is incorrect")){
                ps.println(check +"###");
            }else if(check.equals("Connection issue, please try again later")){
                ps.println(check +"###");
            }
        }catch(SQLException ex){
            System.out.println("Connection Issues");
        }
        token = null;
   }
    
}
