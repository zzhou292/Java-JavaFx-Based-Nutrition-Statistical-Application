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
import javafx.scene.Scene;
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
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
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
  private boolean openLoad;
  private Label count;
  private HBox countBox;

  public FoodList(Stage primaryStage) {
    openLoad = false;
    this.currentFoodListView = new ListView<HBox>();
    this.foodData = new FoodData();
    this.menuBar = new MenuBar();
    this.foodquery = new FoodQuery(this);
    mealList = new MealList();
    selectList = new ArrayList<FoodItem>();
    currentFoodItemList = new ArrayList<FoodItem>();
    this.label = new Label("Food Item List");
    this.count = new Label(String.valueOf(currentFoodItemList.size()));
    this.primaryStage = primaryStage;
    createCountLabels();
    createMenubar();
    boxAdjustment();

  }

  public void refreshAfterAdd() {
    currentFoodItemList = foodData.getAllFoodItems();
    count.setText(String.valueOf(currentFoodItemList.size()));
    sortCurrentFoodItemList();
    currentFoodListView.getItems().clear();
    for (FoodItem fooditem : currentFoodItemList) {
      FoodItemView current = new FoodItemView(fooditem);
      Button select = new Button();
      if (selectList.contains(fooditem))
        select.setText("unselect");
      else
        select.setText("select");
      handleSelectButtonEvent(current, select, fooditem);
      currentFoodListView.getItems().add(current);
    }
    currentFoodListView.refresh();
    foodquery.getWorkingFilter().setText("No Filter is currently working!");
  }

  public void resetSelectButton() {
    selectList = new ArrayList<FoodItem>();
    currentFoodListView.getItems().clear();
    for (FoodItem fooditem : currentFoodItemList) {
      FoodItemView current = new FoodItemView(fooditem);
      Button select = new Button("select");
      handleSelectButtonEvent(current, select, fooditem);
      currentFoodListView.getItems().add(current);
    }
    currentFoodListView.refresh();
    foodquery.getWorkingFilter().setText("No Filter is currently working!");
  }

  private void handleApplyButtonEvent(MenuItem apply) {
    apply.setOnAction(event -> {
      ApplySelectionStage appStage = new ApplySelectionStage(selectList, this, mealList);
      appStage.show();
    });
  }

  /**
   * @return the foodquery
   */
  public FoodQuery getFoodquery() {
    return foodquery;
  }

  /**
   * @return the ml
   */
  public MealList getMealList() {
    return mealList;
  }

  private void createCountLabels() {

    countBox = new HBox(5);
    Label countlb = new Label("Total Food Items count: ");
    countBox.getChildren().addAll(countlb, count);
  }

  private void updateList(String filePath) {
    this.foodData = new FoodData();
    foodData.loadFoodItems(filePath);
    foodquery.updateFoodData(foodData);
    currentFoodItemList = foodData.getAllFoodItems();
    count.setText(String.valueOf(currentFoodItemList.size()));
    sortCurrentFoodItemList();
    currentFoodListView.getItems().clear();
    selectList = new ArrayList<>();
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
        return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
      }

    });
  }



  private void boxAdjustment() {

    this.getChildren().addAll(this.label, this.menuBar, this.countBox, this.currentFoodListView);
    VBox.setMargin(label, new Insets(0, 0, 0, 130));
    this.setPrefWidth(360.0);
    this.setStyle("-fx-background-color:#BFEFFF");

  }

  private void createMenubar() {
    MenuItem load = new MenuItem("Load");
    MenuItem save = new MenuItem("Save");
    Menu file = new Menu("File", null, load, save);
    MenuItem add = new MenuItem("Add New Food");
    MenuItem apply = new MenuItem("Apply Selection");
    MenuItem undo = new MenuItem("undoAll Selection");
    Menu operation = new Menu("Operation", null, add, apply, undo);
    menuBar.getMenus().addAll(file, operation);
    handleAddEvent(add);
    handleApplyButtonEvent(apply);
    handleUndoEvent(undo);

    handleSaveLoadEvent(save, load);

  }

  private void handleAddEvent(MenuItem add) {
    add.setOnAction(e1 -> {

      AddFoodItemStage afis = new AddFoodItemStage(this, foodData);
      afis.show();

    });
  }



  private void handleSaveLoadEvent(MenuItem save, MenuItem load) {
    // TODO Auto-generated method stub
    load.setOnAction(event -> {
      handlePreloadEvent();
      if (openLoad == false)
        return;
      FileChooser loadChooser = new FileChooser();
      loadChooser.setTitle("Open Resource File");
      loadChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
      loadChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
      File loadFile = loadChooser.showOpenDialog(primaryStage);
      if (loadFile == null)
        return;
      else {
        // check if the chosen file can be read, if not just return without any warning
        File trialLoadFile = new File(loadFile.getAbsolutePath());
        if (!trialLoadFile.canRead())
          return;
        // for the readable file, update the food list
        updateList(loadFile.getAbsolutePath());
        foodquery.getWorkingFilter().setText("No Filter is currently working!");
        String message = "Successfully load the file: " + loadFile.getAbsolutePath();
        Alert alert = new Alert(AlertType.INFORMATION, message);
        alert.showAndWait().filter(response -> response == ButtonType.OK);
      }
    });

    save.setOnAction(event -> {
      FileChooser SaveChooser = new FileChooser();
      SaveChooser.setTitle("Save File");
      SaveChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
      SaveChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
      File saveFile = SaveChooser.showSaveDialog(primaryStage);
      if (saveFile == null)
        return;
      else {
        // check if the chosen file can be write, if not just return without any warning
        File trailSaveFile = new File(saveFile.getAbsolutePath());
        if (!trailSaveFile.canWrite())
          return;
        foodData.saveFoodItems(saveFile.getAbsolutePath());
        String message =
            "Successfully save the current food list to the file: " + saveFile.getAbsolutePath();
        Alert alert = new Alert(AlertType.INFORMATION, message);
        alert.showAndWait().filter(response -> response == ButtonType.OK);
      }
    });
  }


  private boolean handlePreloadEvent() {
    String message0 =
        "Confirm to load a new File.\nAttention: All unapplied selected food items will be clear"
            + " from the selected table.  All unsaved new added food items will be discarded";
    Alert alert0 = new Alert(AlertType.CONFIRMATION, message0);
    alert0.showAndWait().filter(new Predicate<ButtonType>() {
      @Override
      public boolean test(ButtonType t) {
        if (t.getButtonData().isCancelButton()) {
          openLoad = false;
          return true;
        } else {
          openLoad = true;
          return false;
        }
      }
    });
    return openLoad;
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

  public void queryOnShown(List<FoodItem> queryFoodList) {
    currentFoodListView.getItems().clear();
    sortQueryFoodList(queryFoodList);
    for (FoodItem fooditem : queryFoodList) {
      FoodItemView current = new FoodItemView(fooditem);
      Button select = new Button();
      if (selectList.contains(fooditem))
        select.setText("unselect");
      else
        select.setText("select");
      handleSelectButtonEvent(current, select, fooditem);
      currentFoodListView.getItems().add(current);
    }
    currentFoodListView.refresh();
  }

  private void sortQueryFoodList(List<FoodItem> queryFoodList) {
    // sort this food item list by create a anonymous comparator class
    Collections.sort(queryFoodList, new Comparator<FoodItem>() {

      @Override
      public int compare(FoodItem o1, FoodItem o2) {
        // invoke the compare to method of a string since name is a string
        return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
      }

    });

  }

  public void noFilterOnShown() {
    currentFoodListView.getItems().clear();
    for (FoodItem fooditem : currentFoodItemList) {
      FoodItemView current = new FoodItemView(fooditem);
      Button select = new Button();
      if (selectList.contains(fooditem))
        select.setText("unselect");
      else
        select.setText("select");
      handleSelectButtonEvent(current, select, fooditem);
      currentFoodListView.getItems().add(current);
    }
    currentFoodListView.refresh();
    foodquery.getWorkingFilter().setText("No Filter is currently working!");
  }


}
