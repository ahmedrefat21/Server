/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Server;
import Model.ServerHandler;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

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
//    @FXML
//    private Button startBtn;
//    @FXML
//    private BarChart<?, ?> barChart;
//    @FXML
//    private ImageView serverStateImage;
//    @FXML
//    private Button startBtn1;
//    @FXML
//    private Button startChart;
//    @FXML
//    private ImageView img;
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

    @FXML
    private void StartChart(ActionEvent event) {
    }

    @FXML
    private void mouseEntered(MouseEvent event) {
    }
    
}
