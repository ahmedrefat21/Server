/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author ahmed
 */
public class PlayerHandler {
    
    
    
    
    
    private void sendInvitation(){ 
        String player2Mail = token.nextToken(); 
        String player1Data = token.nextToken(""); 
        for(PlayerHandler i : activeUsers){
            System.out.println("Data of enemy sent");
            if(i.email.equals(player2Mail)){
              i.ps.println("requestPlaying");
              i.ps.println(player1Data);
            }
        }      
    }
    
   
     private void acceptGame(){
  
        String player2 = token.nextToken(); 
        String player2Name = token.nextToken();
        String player1 = token.nextToken();
        server.makeBusy(player1, player2);
        System.out.println("Two Players playing now");
        PlayerHandler p1 = null;
        PlayerHandler p2 = null;
        for(PlayerHandler i : activeUsers){
            if(i.email.equals(player1)){
                p1 = i;
            }else if(i.email.equals(player2)){
                p2 = i;
            }
        }
        if(p1 == null || p2 == null){
        System.out.print("Error!, please try again");
        System.out.println("Not Found");
        }else{
            game.put(player1, p2);
            game.put(player2, p1);
            p1.ps.println("gameOn");
            p1.ps.println(player2Name);
            p1.ps.println(p2.server.retriveScore(p2.email));
        }  
             private void withdraw(){
        PlayerHandler player = null;
        player = game.get(this.email);
        if(player != null){
            player.ps.println("withdraw");
            game.remove(this.email);
            game.remove(player.email);
        }
    }
   
    }   
    
    
    
    
    
    
    
    
}
