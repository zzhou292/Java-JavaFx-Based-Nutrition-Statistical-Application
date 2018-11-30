/**
 * 
 */
package application;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


/**
 * @author admin
 *
 */
public class FoodListBox extends VBox {

  private ListView<HBox> currentFoodListView;
  private FoodData foodData;
  private MenuBar menuBar;
  private Label label;
  private List<FoodItem> currentFoodItemList;
  private List<FoodItem> selectList;
  private MealList mealList;

  public FoodListBox() {
    this("foodItems.csv");
  }

  public FoodListBox(String filePath) {
    this.currentFoodListView = new ListView<HBox>();
    this.foodData = new FoodData();
    this.menuBar = new MenuBar();
    mealList = new MealList();
    selectList = new ArrayList<FoodItem>();
    currentFoodItemList = new ArrayList<FoodItem>();
    this.label = new Label("Food Item List");
    foodData.loadFoodItems("foodItems.csv");
    foodData.saveFoodItems("sorted.csv");
    updateList("sorted.csv");
    createMenubar();
    boxAdjustment();

  }

  /**
   * @return the ml
   */
  public MealList getMl() {
    return mealList;
  }

  private void boxAdjustment() {

    this.getChildren().addAll(this.label, this.menuBar, this.currentFoodListView);
    VBox.setMargin(label, new Insets(0, 0, 0, 130));
    this.setPrefWidth(360.0);
    this.setStyle("-fx-background-color:#BFEFFF");

  }

  private void createMenubar() {
    MenuItem load = new MenuItem("Load");
    MenuItem save = new MenuItem("Save");
    Menu file = new Menu("File", null, load, save);
    MenuItem add = new MenuItem("Add");
    MenuItem clear = new MenuItem("Clear");
    MenuItem apply = new MenuItem("Apply Selection");
    MenuItem undo = new MenuItem("undoAll Selection");
    Menu operation = new Menu("Operation", null, add, clear, apply, undo);
    menuBar.getMenus().addAll(file, operation);
    handleApplyButtonEvent(apply);
    handleUndoEvent(undo);
    handleClearEvent(clear);

  }

  private void handleClearEvent(MenuItem clear) {
    clear.setOnAction(event -> {
      Alert alert =
          new Alert(AlertType.CONFIRMATION, "Confirm to clear all items in the food list");
      alert.showAndWait().filter(new Predicate<ButtonType>() {
        @Override
        public boolean test(ButtonType t) {
          if (t.getButtonData().isCancelButton())
            return true;
          else {
            currentFoodItemList = new ArrayList<FoodItem>();
            selectList = new ArrayList<FoodItem>();
            currentFoodListView.getItems().clear();
            currentFoodListView.refresh();
            return false;
          }
        }
      });

    });


  }

  private void handleUndoEvent(MenuItem undo) {
    undo.setOnAction(event -> {
      Alert alert =
          new Alert(AlertType.CONFIRMATION, "Confirm to undo all item selection");
      alert.showAndWait().filter(new Predicate<ButtonType>() {
        @Override
        public boolean test(ButtonType t) {
          if (t.getButtonData().isCancelButton())
            return true;
          else {
            selectList = new ArrayList<FoodItem>();
            currentFoodListView.getItems().clear();
            for (FoodItem fooditem : currentFoodItemList) {
              FoodItemView current = new FoodItemView(fooditem);
              Button select = new Button("select");
              handleSelectRemoveButtonEvent(current, select, fooditem);
              currentFoodListView.getItems().add(current);
            }
            currentFoodListView.refresh();
            return false;
          }
        }
      });
    });
  }

  private void handleApplyButtonEvent(MenuItem apply) {
    apply.setOnAction(event -> {
      for (FoodItem foodItem : selectList) {
        FoodItemView current = new FoodItemView(foodItem);
        if (!mealList.getCurrentMealList().contains(foodItem)) {
          mealList.getCurrentMealList().add(foodItem);
          mealList.getcurrentMealListView().getItems().add(current);
          handleMealRemoveEvent(current, foodItem);
        }
      }
    });


  }

  private void handleMealRemoveEvent(FoodItemView current, FoodItem foodItem) {
    current.getRemove().setOnMouseClicked(new EventHandler<MouseEvent>() {
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
              mealList.getCurrentMealList().remove(foodItem);
              mealList.getcurrentMealListView().getItems().remove(current);
              mealList.getcurrentMealListView().refresh();
              return false;
            }
          }
        });
      }
    });
  }

  public void updateList(String filePath) {
    foodData.loadFoodItems(filePath);
    currentFoodItemList = foodData.getAllFoodItems();
    for (FoodItem fooditem : currentFoodItemList) {
      FoodItemView current = new FoodItemView(fooditem);
      Button select = new Button("select");
      handleSelectRemoveButtonEvent(current, select, fooditem);
      currentFoodListView.getItems().add(current);
    }
  }

  private void handleSelectRemoveButtonEvent(FoodItemView current, Button select,
      FoodItem fooditem) {
    current.getChildren().add(select);
    current.getRemove().setOnMouseClicked(new EventHandler<MouseEvent>() {

      @Override
      public void handle(MouseEvent event) {
        String message = "Confirm to remove item: " + fooditem.getName();
        Alert alert = new Alert(AlertType.CONFIRMATION, message);
        alert.showAndWait().filter(new Predicate<ButtonType>() {
          @Override
          public boolean test(ButtonType t) {
            if (t.getButtonData().isCancelButton())
              return true;
            else {
              currentFoodItemList.remove(fooditem);
              selectList.remove(fooditem);
              currentFoodListView.getItems().remove(current);
              currentFoodListView.refresh();
              return false;
            }
          }
        });
      }
    });
    select.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        if (select.getText().equals("select")) {
          select.setText("unselect");
          selectList.add(fooditem);
        } else {
          select.setText("select");
          selectList.remove(fooditem);
        }
      }
    });
  }


}
