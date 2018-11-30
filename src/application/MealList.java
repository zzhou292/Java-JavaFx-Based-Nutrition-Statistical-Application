/**
 * 
 */
package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;


/**
 * @author admin
 *
 */
public class MealList extends VBox {

  private ListView<HBox> currentMealListView;
  private FoodData foodData;
  private MenuBar menuBar;
  private Label label;
  private List<FoodItem> currentMealList;


  /**
   * @return the currentMealList
   */
  public List<FoodItem> getCurrentMealList() {
    return currentMealList;
  }

  public MealList() {
    this.currentMealListView = new ListView<HBox>();
    currentMealList = new ArrayList<FoodItem>();
    this.foodData = new FoodData();
    this.menuBar = new MenuBar();
    this.label = new Label("Meal List");

    createMenubar();
    boxAdjustment();
  }

  /**
   * @return the foodlist
   */
  public ListView<HBox> getcurrentMealListView() {
    return currentMealListView;
  }

  private void boxAdjustment() {

    this.getChildren().addAll(this.label, this.menuBar, this.currentMealListView);
    VBox.setMargin(label, new Insets(0, 0, 0, 130));
    this.setPrefWidth(370.0);
    this.setStyle("-fx-background-color:#BFEFFF");

  }

  private void createMenubar() {
    MenuItem load = new MenuItem("Load");
    MenuItem save = new MenuItem("Save");
    Menu file = new Menu("File", null, load, save);
    MenuItem clear = new MenuItem("Clear");
    MenuItem analyze = new MenuItem("Analyze");
    handleOperations(clear, analyze);

    Menu operation = new Menu("Operation", null, clear, analyze);
    menuBar.getMenus().addAll(file, operation);

  }

  private void handleOperations(MenuItem clear, MenuItem analyze) {
    analyze.setOnAction(event -> {
      double proteintotal = 0.0;
      double fibertotal = 0.0;
      double fattotal = 0.0;
      double caloriestotal = 0.0;
      double carbohydratetotal = 0.0;
      int count = currentMealList.size();
      for (int i = 0; i < currentMealList.size(); i++) {
        FoodItem fi = currentMealList.get(i);
        proteintotal = proteintotal + fi.getNutrientValue("protein");
        fibertotal = fibertotal + fi.getNutrientValue("fiber");
        fattotal = fattotal + fi.getNutrientValue("fat");
        caloriestotal = caloriestotal + fi.getNutrientValue("calories");
        carbohydratetotal = carbohydratetotal + fi.getNutrientValue("carbohydrate");
        System.out.println(fi.getName() + fi.getNutrients());
      }
      System.out.println("------------");
      System.out.println("Nutrition Summary:");
      System.out.println("Fiber: " + fibertotal);
      System.out.println("Protein: " + proteintotal);
      System.out.println("Fat: " + fattotal);
      System.out.println("Calories: " + caloriestotal);
      System.out.println("Carbohydrate: " + carbohydratetotal);

      this.displaySummary(proteintotal, fibertotal, fattotal, caloriestotal, carbohydratetotal,
          count);



    });
    clear.setOnAction(event -> {
      Alert alert =
          new Alert(AlertType.CONFIRMATION, "Confirm to clear all items in the meal list");
      alert.showAndWait().filter(new Predicate<ButtonType>() {
        @Override
        public boolean test(ButtonType t) {
          if (t.getButtonData().isCancelButton())
            return true;
          else {
            currentMealList = new ArrayList<FoodItem>();
            currentMealListView.getItems().clear();
            currentMealListView.refresh();
            return false;
          }
        }
      });
    });

  }

  private void displaySummary(double fiber, double protein, double fat, double calories,
      double carbohydrate, int count) {
    Stage foodSummaryStage = new FoodSummaryStage(fiber, protein, fat, calories, carbohydrate,count);
    foodSummaryStage.show();
  }



}
