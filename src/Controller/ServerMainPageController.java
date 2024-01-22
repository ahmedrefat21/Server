
package Controller;

import Model.Server;
import Model.ServerHandler;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.image.ImageView;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.NumberAxis;

/**
 * FXML Controller class
 *
 * @author Kimo Store
 */
public class ServerMainPageController implements Initializable {
    ServerHandler server;
    ResultSet chartData;
    public static boolean serverState ;
    int trueCount, falseCount;
    @FXML
     private Button startBtn;
    @FXML
     private CategoryAxis xAxis;
    @FXML
     private NumberAxis yAxis;
    @FXML
     private Button startChart;
    @FXML
     private ImageView img;
    @FXML
     private BarChart<String, Number> barChart;
    @FXML
     private ImageView serverStateImage;
    @FXML
     private Button startBtn1;
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        serverState = false;
        server = ServerHandler.getServer();
    }    

    @FXML
    private void startServer(ActionEvent event) throws InterruptedException{
        
        serverState = !serverState;
        if(serverState){
            try {
              server.startConnections();
              System.out.println("The server is UP");
               StartChart();              
            }catch(SQLException e){
                System.out.println("wrong in the connection");
                System.out.println(e);
                serverState = !serverState;
            }

        }
        }

      @FXML
         private void shutDown(ActionEvent event) throws InterruptedException{
        
        serverState = !serverState;
        if(!serverState) 
            try {

                System.out.println("The server is down");
                stopChart();
            }

            finally{
                server.endConnections();
            }
        }

         
     
   private void StartChart() {
    try {

        ResultSet rsTrue = server.getActivePlayersChart();
         trueCount = 0;
           if (rsTrue.next()) {
            trueCount = rsTrue.getInt("true_count");
            }

        ResultSet rsFalse = server.getOfflinePlayers();
         falseCount = 0;
           if (rsFalse.next()) {
            falseCount = rsFalse.getInt("false_count");
            }
    } catch (SQLException ex) {
        System.out.println(ex);
    }

   }
         
         
         
    @FXML
    private void mouseEntered(MouseEvent event) {
    }
    
}
