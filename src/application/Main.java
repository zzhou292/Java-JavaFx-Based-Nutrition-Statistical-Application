/**
 * 
 */
package application;



import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * @author admin
 *
 */
public class Main extends Application {

  /**
   * @param args
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
    Scene sc = new Scene(ap, 1110, 450);
    primaryStage.setTitle("HomePage");
    primaryStage.setScene(sc);
    primaryStage.sizeToScene();
    primaryStage.show();
    primaryStage.setResizable(false);



  }

}
