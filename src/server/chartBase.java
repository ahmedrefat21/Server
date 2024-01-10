package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import org.apache.derby.jdbc.ClientDriver;

public  class chartBase extends AnchorPane {

    protected final CategoryAxis categoryAxis;
    protected final NumberAxis numberAxis;
    protected final BarChart barChart;
    protected final Button startBtn;
    protected final Button stopBtn;
    
    Connection con;
    private boolean isChartPopulated = false;


    public chartBase() {

        categoryAxis = new CategoryAxis();
        numberAxis = new NumberAxis();
        barChart = new BarChart(categoryAxis, numberAxis);
        startBtn = new Button();
        stopBtn = new Button();

        setId("AnchorPane");
        setPrefHeight(480.0);
        setPrefWidth(740.0);
        getStylesheets().add("/server/Style.css");

        categoryAxis.setSide(javafx.geometry.Side.BOTTOM);

        numberAxis.setSide(javafx.geometry.Side.LEFT);
        barChart.setLayoutX(271.0);
        barChart.setLayoutY(54.0);
        barChart.setPrefHeight(373.0);
        barChart.setPrefWidth(427.0);

        startBtn.setLayoutX(86.0);
        startBtn.setLayoutY(155.0);
        startBtn.setMnemonicParsing(false);
        startBtn.setPrefHeight(68.0);
        startBtn.setPrefWidth(143.0);
        startBtn.setText("Start");

        stopBtn.setLayoutX(86.0);
        stopBtn.setLayoutY(304.0);
        stopBtn.setMnemonicParsing(false);
        stopBtn.setPrefHeight(68.0);
        stopBtn.setPrefWidth(143.0);
        stopBtn.setText("Stop");

        getChildren().add(barChart);
        getChildren().add(startBtn);
        getChildren().add(stopBtn);

        startBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
        if (!isChartPopulated) {
            connectDB();
            StartChart();
            isChartPopulated = true;
        }
    }
});
        
        
            stopBtn.setOnAction(event -> {
                if (isChartPopulated) {
                    disconnectDB();
                    clearChart();
                    isChartPopulated = false;
                }
            });
        
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

        barChart.getData().add(series);
        //adjusting labels to the two colms
        CategoryAxis xAxis = (CategoryAxis) barChart.getXAxis();
        xAxis.setCategories(FXCollections.observableArrayList("Online", "Offline"));
    } catch (SQLException ex) {
        System.out.println(ex);
    }
}
    
    
//    private void StartChart() {
//    try {
//        String queryTrue = "SELECT COUNT(*) AS true_count FROM PLAYER WHERE ISONLINE = true";
//        String queryFalse = "SELECT COUNT(*) AS false_count FROM PLAYER WHERE ISONLINE = false";
//
//        // Database connection
//        con = connectDB();
//        Statement statement = con.createStatement();
//
//        ResultSet rsTrue = statement.executeQuery(queryTrue);
//        int trueCount = 0;
//        if (rsTrue.next()) {
//            trueCount = rsTrue.getInt("true_count");
//        }
//
//        ResultSet rsFalse = statement.executeQuery(queryFalse);
//        int falseCount = 0;
//        if (rsFalse.next()) {
//            falseCount = rsFalse.getInt("false_count");
//        }
//
//        XYChart.Series<String, Double> series = new XYChart.Series<>();
//        series.getData().add(new XYChart.Data<>("Online", (double) trueCount));
//        series.getData().add(new XYChart.Data<>("Offline", (double) falseCount));
//
//        barChart.getData().add(series);
//    } catch (SQLException ex) {
//        System.out.println(ex);
//    }
//}
   

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
   
        private void disconnectDB() {
         try {
             // close the database connection
             if (con != null) {
                 con.close();
                 System.out.println("Database connection closed");
             }
         } catch (SQLException e) {
             System.out.println("Error closing the database connection: " + e.getMessage());
         }
}

        private void clearChart() {
            // clear the contents of the chart
            barChart.getData().clear();
            System.out.println("BarChart cleared");
        }
   
   
}
