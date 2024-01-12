/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.image.Image;

/**
 * FXML Controller class
 *
 * @author Kimo Store
 */
public class ChartController implements Initializable {

    Server server;
    ResultSet chartData;
    Connection con;
    private boolean isChartPopulated = false;
    public static boolean serverState ;
    @FXML
    private BarChart<?, ?> barChart;
    @FXML
    private Button startBtn;
    @FXML
    private Button stopBtn;
 
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         serverState = false;
            server = Server.getServer();        
        
    }    
    @FXML
    private void toggleServer(ActionEvent event) throws InterruptedException{
              
        serverState = !serverState;
        if(serverState){
            try {
                System.out.println("start server");
                server.enableConnections();
                StartChart();

            
            }catch(SQLException e){
                System.out.println("Connection Issues, Try again later");
                serverState = !serverState;
            }
//            catch (FileNotFoundException ex) {
//                Logger.getLogger(ServerMainPageController.class.getName()).log(Level.SEVERE, null, ex);
//            }
        }else{ // state is true needed to be deactivate
            try {

                //Platform.exit();
            }
//            catch (FileNotFoundException ex) {
//                System.out.println("No Img");
//            }
            finally{
                server.disableConnections();
               
            }
        }
            
        
    }
    private void StartChart() {
    try {
        String queryTrue = "SELECT COUNT(*) AS true_count FROM PLAYER WHERE ISONLINE = true";
        String queryFalse = "SELECT COUNT(*) AS false_count FROM PLAYER WHERE ISONLINE = false";

        // Database connection
        con = connectDB();
        Statement statement = con.createStatement();

        ResultSet rsTrue = statement.executeQuery(queryTrue);
        int trueCount = 0;
        if (rsTrue.next()) {
            trueCount = rsTrue.getInt("true_count");
        }

        ResultSet rsFalse = statement.executeQuery(queryFalse);
        int falseCount = 0;
        if (rsFalse.next()) {
            falseCount = rsFalse.getInt("false_count");
        }

        XYChart.Series<String, Double> series = new XYChart.Series<>();

        XYChart.Data<String, Double> onlineData = new XYChart.Data<>("Online", (double) trueCount);
        XYChart.Data<String, Double> offlineData = new XYChart.Data<>("Offline", (double) falseCount);

        series.getData().add(onlineData);
        series.getData().add(offlineData);
        onlineData.nodeProperty().addListener((ov, oldNode, newNode) -> {
            if (newNode != null) {
                newNode.setStyle("-fx-bar-fill: #A4C991;");
            }
        });
        offlineData.nodeProperty().addListener((ov, oldNode, newNode) -> {
            if (newNode != null) {
                newNode.setStyle("-fx-bar-fill: #ea6e6e;");
            }
        });

        
        ///?????barChart.getData().add(series);
        //adjusting labels to the two colms
        CategoryAxis xAxis = (CategoryAxis) barChart.getXAxis();
        xAxis.setCategories(FXCollections.observableArrayList("Online", "Offline"));
    } catch (SQLException ex) {
        System.out.println(ex);
    }
}
    
     private Connection connectDB() {
    try {
        String url = "jdbc:derby://localhost:1527/TicTacToe"; 
        String username = "root"; 
        String password = "root"; 

        Connection con = DriverManager.getConnection(url, username, password);
        System.out.println("Connected to the database");
        return con;
    } catch (SQLException ex) {
        System.out.println(ex);
    }
    return null;
}
}
