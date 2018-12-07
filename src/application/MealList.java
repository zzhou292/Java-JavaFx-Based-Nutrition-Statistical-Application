/**
 * 
 */
package application;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


/**
 * @author admin
 *
 */
public class MealList extends VBox {

  /**
   * @return the count
   */
  public Label getCount() {
    return count;
  }

  private ListView<HBox> currentMealListView;
  private MenuBar menuBar;
  private Label label;
  private List<FoodItem> currentMealList;
  private Label count;
  private HBox countBox;


  /**
   * @return the currentMealList
   */
  public List<FoodItem> getCurrentMealList() {
    return currentMealList;
  }

  public MealList() {
    this.currentMealListView = new ListView<HBox>();
    currentMealList = new ArrayList<FoodItem>();
    this.menuBar = new MenuBar();
    this.label = new Label("Meal List");
    this.count = new Label(String.valueOf(currentMealList.size()));
    countBox = new HBox(5);
    createCountLabels();
    createMenubar();
    boxAdjustment();
  }

  private void createCountLabels() {
    Label countlb = new Label("Total Meal Items count: ");
    countBox.getChildren().addAll(countlb, count);
  }

  /**
   * @return the foodlist
   */
  public ListView<HBox> getcurrentMealListView() {
    return currentMealListView;
  }

  private void boxAdjustment() {

    this.getChildren().addAll(this.label, this.menuBar, countBox, this.currentMealListView);
    VBox.setMargin(label, new Insets(0, 0, 0, 130));
    this.setPrefWidth(370.0);
    this.setStyle("-fx-background-color:#BFEFFF");

  }

  private void createMenubar() {
    MenuItem clear = new MenuItem("Clear");
    MenuItem analyze = new MenuItem("Analyze");
    handleOperations(clear, analyze);
    Menu operation = new Menu("Operation", null, clear, analyze);
    menuBar.getMenus().add(operation);

  }

  private void handleOperations(MenuItem clear, MenuItem analyze) {
    analyze.setOnAction(event -> {
      double proteintotal = 0.0;
      double fibertotal = 0.0;
      double fattotal = 0.0;
      double caloriestotal = 0.0;
      double carbohydratetotal = 0.0;
      int count = currentMealList.size();
      // print meal info, use to debug
      for (int i = 0; i < currentMealList.size(); i++) {
        FoodItem fi = currentMealList.get(i);
        proteintotal = proteintotal + fi.getNutrientValue("protein");
        fibertotal = fibertotal + fi.getNutrientValue("fiber");
        fattotal = fattotal + fi.getNutrientValue("fat");
        caloriestotal = caloriestotal + fi.getNutrientValue("calories");
        carbohydratetotal = carbohydratetotal + fi.getNutrientValue("carbohydrate");
      }
     

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
            count.setText(String.valueOf(currentMealList.size()));
            return false;
          }
        }
      });
    });

  }

  private void displaySummary(double fiber, double protein, double fat, double calories,
      double carbohydrate, int count) {
    Stage foodSummaryStage =
        new MealSummaryStage(fiber, protein, fat, calories, carbohydrate, count);
    foodSummaryStage.show();
  }

  public void addFoodItems(List<FoodItem> selectList) {
    for (FoodItem foodItem : selectList) {
      currentMealList.add(foodItem);
    }
    sortCurrentMealItemList();
    currentMealListView.getItems().clear();
    for (FoodItem foodItem : currentMealList) {
      FoodItemView current = new FoodItemView(foodItem);
      currentMealListView.getItems().add(current);
      handleMealRemoveEvent(current, foodItem);
    }
    currentMealListView.refresh();
  }

  private void sortCurrentMealItemList() {
    // sort this food item list by create a anonymous comparator class
    Collections.sort(this.currentMealList, new Comparator<FoodItem>() {

      @Override
      public int compare(FoodItem o1, FoodItem o2) {
        // invoke the compare to method of a string since name is a string
        return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
      }

    });
  }



  private void handleMealRemoveEvent(FoodItemView current, FoodItem foodItem) {
    Button remove = new Button("remove");
    current.getChildren().add(remove);
    remove.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        String message = "Confirm to remove item: " + foodItem.getName();
        Alert alert = new Alert(AlertType.CONFIRMATION, message);
        alert.showAndWait().filter(new Predicate<ButtonType>() {
          @Override
          public boolean test(ButtonType t) {
            if (t.getButtonData().isCancelButton())
              return true;
            else {
              currentMealList.remove(foodItem);
              currentMealListView.getItems().remove(current);
              count.setText(String.valueOf(currentMealList.size()));
              currentMealListView.refresh();
              return false;
            }
          }
        });
      }
    });
  }



}
