/**
 * Filename: MealList.java
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
 * This is the meal list. It displays the list of food items to be make a meal. It can display the
 * food analyze of the meal and clear and remove items in the meal list.
 * 
 * @author Meng, Zhou, Zou, Fan, Higgins
 *
 */
public class MealList extends VBox {

  private ListView<HBox> currentMealListView;// list view of food items in meal
  private MenuBar menuBar;
  private Label label;// label of meal list
  private List<FoodItem> currentMealList;// list of food items in the meal
  // count box and label of the meals
  private Label count;
  private HBox countBox;

  /**
   * Public constructor that creates a meal list
   */
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

  /**
   * This creates a count label box that counts number of meal items
   */
  private void createCountLabels() {
    Label countlb = new Label("Total Meal Items count: ");
    countBox.getChildren().addAll(countlb, count);
  }


  /**
   * This adjusts the box lay out
   */
  private void boxAdjustment() {
    // adds all components to the box
    this.getChildren().addAll(this.label, this.menuBar, countBox, this.currentMealListView);
    VBox.setMargin(label, new Insets(0, 0, 0, 130));// set the label to the center
    // set the width and background color
    this.setPrefWidth(370.0);
    this.setStyle("-fx-background-color:#BFEFFF");

  }

  /**
   * This creates the menu of operation
   */
  private void createMenubar() {
    // creates the clear and analyze menu item and add them to the menu bar
    MenuItem clear = new MenuItem("Clear");
    MenuItem analyze = new MenuItem("Analyze");
    handleOperations(clear, analyze);
    Menu operation = new Menu("Operation", null, clear, analyze);
    menuBar.getMenus().add(operation);

  }

  /**
   * This handle the clear and analyze events
   * 
   * @param clear MenuItem clear
   * @param analyze MenuItem analyze
   */
  private void handleOperations(MenuItem clear, MenuItem analyze) {
    analyze.setOnAction(event -> {
      // creates the total sum of each nutrient components
      double proteintotal = 0.0;
      double fibertotal = 0.0;
      double fattotal = 0.0;
      double caloriestotal = 0.0;
      double carbohydratetotal = 0.0;
      int count = currentMealList.size();
      // for each items in the meal list
      // Calculate their total summation of each nutrients component
      for (FoodItem fi : currentMealList) {
        proteintotal = proteintotal + fi.getNutrientValue("protein");
        fibertotal = fibertotal + fi.getNutrientValue("fiber");
        fattotal = fattotal + fi.getNutrientValue("fat");
        caloriestotal = caloriestotal + fi.getNutrientValue("calories");
        carbohydratetotal = carbohydratetotal + fi.getNutrientValue("carbohydrate");
      }
      // display the summary table
      this.displaySummary(fibertotal, proteintotal, fattotal, caloriestotal, carbohydratetotal,
          count);
    });
    clear.setOnAction(event -> {
      // display the confirm info to ask user to confirm cancel
      Alert alert =
          new Alert(AlertType.CONFIRMATION, "Confirm to clear all items in the meal list");
      alert.showAndWait().filter(new Predicate<ButtonType>() {
        @Override
        public boolean test(ButtonType t) {
          if (t.getButtonData().isCancelButton())
            return true;// user cancel, return
          else {
            // otherwise clear the meal list and list view
            currentMealList = new ArrayList<FoodItem>();
            currentMealListView.getItems().clear();
            currentMealListView.refresh();
            // update the count
            count.setText(String.valueOf(currentMealList.size()));
            return false;
          }
        }
      });
    });
  }

  /**
   * This displays the meal summary table
   * 
   * @param fiber total fiber value
   * @param protein total protein value
   * @param fat total fat value
   * @param calories total calories value
   * @param carbohydrate total carbohydrate value
   * @param count total items count
   */
  private void displaySummary(double fiber, double protein, double fat, double calories,
      double carbohydrate, int count) {
    // open the meal summary table
    Stage foodSummaryStage =
        new MealSummaryStage(fiber, protein, fat, calories, carbohydrate, count);
    foodSummaryStage.show();
  }

  /**
   * This adds the selected food items to the meal list
   * 
   * @param selectList the list of selected food items from the food list
   */
  public void addFoodItems(List<FoodItem> selectList) {
    for (FoodItem foodItem : selectList) {
      currentMealList.add(foodItem);
    } // add every selected items into the meal list
    sortCurrentMealItemList();// sort the meal list to maintain the order
    currentMealListView.getItems().clear();// clear the list view
    // for each items in the meal list, add it to the list view
    for (FoodItem foodItem : currentMealList) {
      FoodItemView current = new FoodItemView(foodItem);
      currentMealListView.getItems().add(current);
      handleMealRemoveEvent(current, foodItem);// handle the remove button event
    }
    currentMealListView.refresh();// refresh the meal list view
  }

  /**
   * This sorts the meal list food items to maintain the order
   */
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

  /**
   * This handle the remove button of a meal food item
   * 
   * @param current the FoodItemView current
   * @param foodItem the FoodItem foodItem
   */
  private void handleMealRemoveEvent(FoodItemView current, FoodItem foodItem) {
    Button remove = new Button("remove");
    current.getChildren().add(remove);// add remove button to the meal item view
    remove.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        // display the info to user to confirm removal
        String message = "Confirm to remove item: " + foodItem.getName();
        Alert alert = new Alert(AlertType.CONFIRMATION, message);
        alert.showAndWait().filter(new Predicate<ButtonType>() {
          @Override
          public boolean test(ButtonType t) {
            if (t.getButtonData().isCancelButton())
              return true;// user cancel, remove nothing
            else {
              // otherwise remove this meal item from the list and list view
              currentMealList.remove(foodItem);
              currentMealListView.getItems().remove(current);
              // update the meal item count
              count.setText(String.valueOf(currentMealList.size()));
              currentMealListView.refresh();// refresh list view
              return false;
            }
          }
        });
      }
    });
  }

  /**
   * This returns the count label of the meal list
   * 
   * @return the count label
   */
  public Label getCount() {
    return count;
  }

  /**
   * this returns the list of food items in the meal
   * 
   * @return the currentMealList in the meal
   */
  public List<FoodItem> getCurrentMealList() {
    return currentMealList;
  }

  /**
   * This returns the current meal list view
   * 
   * @return the foodlist view in the meal list
   */
  public ListView<HBox> getcurrentMealListView() {
    return currentMealListView;
  }

}
