/**
 * Filename: Main.java
 * 
 * Project: team project P5
 * 
 * Authors: Debra Deppeler, Zhikang Meng, Jason ZHOU, Kejia Fan, James Higgins,YULU ZOU
 *
 * Semester: Fall 2018
 * 
 * Course: CS400
 * 
 * Lecture: 002
 * 
 * Due Date: Before 10pm on December 12, 2018 Version: 1.0
 * 
 * Credits: NONE
 * 
 * Bugs: no known bugs
 */
package application;



import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Main class drives the whole GUI program
 * 
 * @author Meng, Zhou, Zou, Fan, Higgins
 *
 */
public class Main extends Application {

  /**
   * Start the whole GUI program
   * 
   * @param args command line arguments
   */
  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    AnchorPane ap = new AnchorPane();
    FoodList flp = new FoodList(primaryStage);


    ap.getChildren().addAll(flp, flp.getMealList(), flp.getFoodquery());



    AnchorPane.setLeftAnchor(flp.getMealList(), 370.0);
    AnchorPane.setLeftAnchor(flp.getFoodquery(), 750.0);
    Scene sc = new Scene(ap, 1110, 465);
    primaryStage.setTitle("HomePage");
    primaryStage.setScene(sc);
    primaryStage.sizeToScene();
    primaryStage.show();
    primaryStage.setResizable(false);



  }

}
