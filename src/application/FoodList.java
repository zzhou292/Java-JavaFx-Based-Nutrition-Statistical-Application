/**
 * 
 */
package application;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;


/**
 * @author admin
 *
 */
public class FoodList extends VBox {

  private ListView<HBox> currentFoodListView;
  private FoodData foodData;
  private MenuBar menuBar;
  private Label label;
  private List<FoodItem> currentFoodItemList;
  private List<FoodItem> selectList;
  private MealList mealList;
  private Stage primaryStage;
  private FoodQuery foodquery;

  public FoodList(Stage primaryStage) {
    this.currentFoodListView = new ListView<HBox>();
    this.foodData = new FoodData();
    this.menuBar = new MenuBar();
    this.foodquery = new FoodQuery(this.currentFoodListView,this.currentFoodItemList);
    mealList = new MealList();
    selectList = new ArrayList<FoodItem>();
    currentFoodItemList = new ArrayList<FoodItem>();
    this.label = new Label("Food Item List");
    this.primaryStage = primaryStage;
    createMenubar();
    boxAdjustment();

  }


  /**
   * @return the foodquery
   */
  public FoodQuery getFoodquery() {
    return foodquery;
  }


  public void updateList(String filePath) {
    this.foodData = new FoodData();
    foodData.loadFoodItems(filePath);
    currentFoodItemList = foodData.getAllFoodItems();
    sortCurrentFoodItemList();
    currentFoodListView.getItems().clear();
    for (FoodItem fooditem : currentFoodItemList) {
      FoodItemView current = new FoodItemView(fooditem);
      Button select = new Button("select");
      handleSelectButtonEvent(current, select, fooditem);
      currentFoodListView.getItems().add(current);
    }
    currentFoodListView.refresh();
  }

  private void sortCurrentFoodItemList() {
    // sort this food item list by create a anonymous comparator class
    Collections.sort(this.currentFoodItemList, new Comparator<FoodItem>() {

      @Override
      public int compare(FoodItem o1, FoodItem o2) {
        // invoke the compare to method of a string since name is a string
        return o1.getName().compareTo(o2.getName());
      }

    });
  }


  /**
   * @return the ml
   */
  public MealList getMealList() {
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
    handleSaveLoadEvent(save, load);

  }

  private void handleSaveLoadEvent(MenuItem save, MenuItem load) {
    // TODO Auto-generated method stub
    load.setOnAction(event -> {
      FileChooser loadChooser = new FileChooser();
      loadChooser.setTitle("Open Resource File");
      loadChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
      loadChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
      File loadFile = loadChooser.showOpenDialog(primaryStage);
      if (loadFile == null)
        return;
      else
        updateList(loadFile.getAbsolutePath());
    });

    save.setOnAction(event -> {
      FileChooser SaveChooser = new FileChooser();
      SaveChooser.setTitle("Save File");
      SaveChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
      SaveChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
      File saveFile = SaveChooser.showSaveDialog(primaryStage);
      if (saveFile == null)
        return;
      else
        foodData.saveFoodItems(saveFile.getAbsolutePath());
    });
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
            foodData = new FoodData();
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
      Alert alert = new Alert(AlertType.CONFIRMATION, "Confirm to undo all item selection");
      alert.showAndWait().filter(new Predicate<ButtonType>() {
        @Override
        public boolean test(ButtonType t) {
          if (t.getButtonData().isCancelButton())
            return true;
          else {
            resetSelectButton();
            return false;
          }
        }
      });
    });
  }

  private void resetSelectButton() {
    selectList = new ArrayList<FoodItem>();
    currentFoodListView.getItems().clear();
    for (FoodItem fooditem : currentFoodItemList) {
      FoodItemView current = new FoodItemView(fooditem);
      Button select = new Button("select");
      handleSelectButtonEvent(current, select, fooditem);
      currentFoodListView.getItems().add(current);
    }
    currentFoodListView.refresh();
  }

  private void handleApplyButtonEvent(MenuItem apply) {
    apply.setOnAction(event -> {
      ApplySelectionStage appStage = new ApplySelectionStage(selectList);
      handleOKCancelEvent(appStage.getCancel(), appStage.getOk(), appStage);
      appStage.show();
    });


  }


  private void handleOKCancelEvent(Button cancel, Button ok, ApplySelectionStage appStage) {
    cancel.setOnAction(event -> {
      appStage.close();
    });
    ok.setOnAction(event -> {
      mealList.addFoodItems(selectList);
      mealList.getCount().setText(String.valueOf(mealList.getCurrentMealList().size()));
      appStage.close();
      resetSelectButton();
    });
  }



  private void handleSelectButtonEvent(FoodItemView current, Button select, FoodItem fooditem) {
    current.getChildren().add(select);
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
