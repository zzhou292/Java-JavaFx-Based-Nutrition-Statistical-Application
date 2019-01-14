/**
 * Filename: FoodList.java
 * 
 * Project: team project P5
 * 
 * Authors: Zhikang Meng, Jason ZHOU, Kejia Fan, James Higgins,YULU ZOU
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

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
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
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


/**
 * This is the main control of the food list. It handles the operation of the food list and displays
 * the food list under different operations.
 * 
 * @author Meng, Zhou, Zou, Fan, Higgins
 *
 */
public class FoodList extends VBox {
  // the menu bar and label of this food list
  private MenuBar menuBar;
  private Label label;
  private Stage primaryStage;// reference to the primary stage
  // the box and label displays totoal food items count
  private Label count;
  private HBox countBox;
  private Separator sp;// separate two counts
  // the box and label shows the on shown items count
  private Label onShownCount;
  private HBox onShownBox;
  private FoodQuery foodquery;// reference to the food query
  private MealList mealList;// reference to the meal list
  private FoodData foodData;// stores the food items info
  private ListView<HBox> currentFoodListView;// food item list views of the food items
  private List<FoodItem> currentFoodItemList; // stores the current food items in the food list
  private List<FoodItem> selectList;// stores the selected food items
  private List<FoodItem> queryFoodList;// stores the food items get filtered
  private boolean isFiltered;// check if this food list in currently being sorted
  private boolean openLoad;// check if user want to load a new file or not

  /**
   * This is the public constructor that crates the food list
   * 
   * @param primaryStage This represents the reference to the primary stage
   */
  public FoodList(Stage primaryStage) {
    // user not load and food item not filtered by default
    openLoad = false;
    isFiltered = false;
    // set up all list views, lists and data references
    this.currentFoodListView = new ListView<HBox>();
    this.foodData = new FoodData();
    this.menuBar = new MenuBar();
    this.foodquery = new FoodQuery(this);
    mealList = new MealList();
    selectList = new ArrayList<FoodItem>();
    queryFoodList = new ArrayList<FoodItem>();
    currentFoodItemList = new ArrayList<FoodItem>();
    this.label = new Label("Food Item List");
    // set up the counts
    this.count = new Label(String.valueOf(currentFoodItemList.size()));
    onShownCount = new Label(String.valueOf(currentFoodItemList.size()));
    sp = new Separator();
    this.primaryStage = primaryStage;
    createOnshownLabels();
    createCountLabels();
    createMenubar();
    displayAdjustment();

  }

  /**
   * This creates the box of on shown food count as a HBox
   */
  private void createOnshownLabels() {
    onShownBox = new HBox(5);
    Label countlb = new Label("Total on shown Items count: ");
    onShownBox.getChildren().addAll(countlb, onShownCount);
  }

  /**
   * This creates the box of total food count as a HBox
   */
  private void createCountLabels() {
    countBox = new HBox(5);
    Label countlb = new Label("Total Food Items count: ");
    countBox.getChildren().addAll(countlb, count);
  }

  /**
   * update the food item list from loading a new file
   * 
   * @param filePath This represents the file path of a new file to be read
   */
  private void updateList(String filePath) {
    // create a new food data instance to stores the new read info
    this.foodData = new FoodData();
    // load the food items from this new file
    foodData.loadFoodItems(filePath);
    // update the new food items info to the food query
    foodquery.updateFoodData(foodData);
    // update the food item list
    currentFoodItemList = foodData.getAllFoodItems();
    // clear the filtered list and update counts
    queryFoodList = new ArrayList<FoodItem>();
    count.setText(String.valueOf(currentFoodItemList.size()));
    onShownCount.setText(String.valueOf(currentFoodItemList.size()));
    sortCurrentFoodItemList();// sort food item list to maintain the order
    currentFoodListView.getItems().clear();// clean the list view
    selectList = new ArrayList<>();// clean the selected food item list
    // for each item in the food item list, add it to the list view
    for (FoodItem fooditem : currentFoodItemList) {
      FoodItemView current = new FoodItemView(fooditem);
      Button select = new Button("select");
      handleSelectButtonEvent(current, select, fooditem);
      currentFoodListView.getItems().add(current);
    }
    currentFoodListView.refresh();
  }

  /**
   * This sorts the current food item list
   */
  private void sortCurrentFoodItemList() {
    // sort this food item list by create a anonymous comparator class
    Collections.sort(this.currentFoodItemList, new Comparator<FoodItem>() {

      @Override
      public int compare(FoodItem o1, FoodItem o2) {
        // invoke the compare to method of a string since name is a string and case-insensitive
        return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
      }

    });
  }

  /**
   * This adjusts the lay out
   */
  private void displayAdjustment() {
    // add all components to the display
    this.getChildren().addAll(this.label, this.menuBar, this.countBox, this.sp, this.onShownBox,
        this.currentFoodListView);
    // set the label to the center
    VBox.setMargin(label, new Insets(0, 0, 0, 130));
    // set the width and background color
    this.setPrefWidth(360.0);
    this.setStyle("-fx-background-color:#BFEFFF");

  }

  /**
   * This sets up the menu bar
   */
  private void createMenubar() {
    // add load and save MenuItem to the file menu
    MenuItem load = new MenuItem("Load");
    MenuItem save = new MenuItem("Save");
    Menu file = new Menu("File", null, load, save);
    // add, apply, tip MenuItem added to the operation menu
    MenuItem add = new MenuItem("Add New Food");
    MenuItem apply = new MenuItem("Apply Selection");
    MenuItem tip = new MenuItem("Tip");
    Menu operation = new Menu("Operation", null, add, apply, tip);
    menuBar.getMenus().addAll(file, operation);// add these two menus to the bar
    // handle the click event of each MenuItem
    handleAddEvent(add);
    handleApplyButtonEvent(apply);
    handleTipEvent(tip);
    handleSaveLoadEvent(save, load);

  }

  /**
   * This handles the apply selection event
   * 
   * @param apply This represents the apply MenuItem
   */
  private void handleApplyButtonEvent(MenuItem apply) {
    apply.setOnAction(event -> {
      // sort the selected list
      Collections.sort(selectList, new Comparator<FoodItem>() {
        @Override
        public int compare(FoodItem o1, FoodItem o2) {
          // invoke the compare to method of a string since name is a string and case insensitive
          return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
        }
      });
      // open a apply selection window
      ApplySelectionStage appStage = new ApplySelectionStage(selectList, this, mealList);
      appStage.show();
    });
  }

  /**
   * handle the tip event
   * 
   * @param tip This represents the tip MenuItem
   */
  private void handleTipEvent(MenuItem tip) {
    tip.setOnAction(e1 -> {
      // display the tip info to the user
      String message = "Tip: Can't view the full name of a food Item?"
          + " Hover your mouse over it for few seconds and it will show up.";
      Alert alert = new Alert(AlertType.INFORMATION, message);
      alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
      alert.showAndWait().filter(response -> response == ButtonType.OK);
    });


  }

  /**
   * Handle the add event
   * 
   * @param add This represents the add MenuItem
   */
  private void handleAddEvent(MenuItem add) {
    add.setOnAction(e1 -> {
      // open a add food item window and process there
      AddFoodItemStage addFoodItemStage = new AddFoodItemStage(this, foodData);
      addFoodItemStage.show();
    });
  }



  /**
   * handle the button event of load and save
   * 
   * @param save This represents the save MenuItem
   * @param load This represents the load MenuItem
   */
  private void handleSaveLoadEvent(MenuItem save, MenuItem load) {
    // handle load event
    load.setOnAction(event -> {
      handlePreloadEvent();
      if (openLoad == false)
        return;// user cancel to load new file ,return
      // set up a new file chooser object
      FileChooser loadChooser = new FileChooser();
      // set the window title, default directory to current working dir, and file filter to .csv
      loadChooser.setTitle("Open Resource File");
      loadChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
      loadChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
      File loadFile = loadChooser.showOpenDialog(primaryStage);// show this choose window
      if (loadFile == null)
        return;// if user choose no file to load, just return
      else {
        // check if the chosen file can be read
        File trialLoadFile = new File(loadFile.getAbsolutePath());
        if (!trialLoadFile.canRead()) {
          // file can not read, display warning message to user and return
          String message0 = "Fail to load the file: " + loadFile.getAbsolutePath();
          Alert alert0 = new Alert(AlertType.INFORMATION, message0);
          alert0.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
          alert0.showAndWait().filter(response -> response == ButtonType.OK);
          return;
        }
        // for the readable file, update the food list
        updateList(loadFile.getAbsolutePath());
        isFiltered = false;// set the filtered status to false
        // display the successful load info to user
        String message = "Successfully load the file: " + loadFile.getAbsolutePath();
        Alert alert = new Alert(AlertType.INFORMATION, message);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait().filter(response -> response == ButtonType.OK);
      }
    });

    save.setOnAction(event -> {
      // set up a new file chooser object
      FileChooser SaveChooser = new FileChooser();
      // set the window title, default directory to current working dir, and file filter to .csv
      SaveChooser.setTitle("Save File");
      SaveChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
      SaveChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
      File saveFile = SaveChooser.showSaveDialog(primaryStage);// show this choose window
      if (saveFile == null)
        return;// if user choose no file to save, return
      else {
        // check if the chosen file can be write
        File trailSaveFile = new File(saveFile.getAbsolutePath());
        if (!trailSaveFile.canWrite()) {
          // if the file can not write, display warning message to the user
          String message2 =
              "Fail to save the current food list to the file: " + saveFile.getAbsolutePath();
          Alert alert2 = new Alert(AlertType.INFORMATION, message2);
          alert2.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
          alert2.showAndWait().filter(response -> response == ButtonType.OK);
          return;
        }
        // otherwise save the info to the file by calling FoodData.savesaveFoodItems
        foodData.saveFoodItems(saveFile.getAbsolutePath());
        // display the successful info to the user
        String message =
            "Successfully save the current food list to the file: " + saveFile.getAbsolutePath();
        Alert alert = new Alert(AlertType.INFORMATION, message);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait().filter(response -> response == ButtonType.OK);
      }
    });
  }

  /**
   * This displays the warning message before user load a new file
   * 
   * @return This returns true if the user agree to load a new file
   */
  private boolean handlePreloadEvent() {
    // display the warning message
    String message0 =
        "Confirm to load a new File.\nAttention: All unapplied selected food items will be clear"
            + " from the selected table.  All unsaved new added food items will be discarded";
    Alert alert0 = new Alert(AlertType.CONFIRMATION, message0);
    alert0.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
    alert0.showAndWait().filter(new Predicate<ButtonType>() {
      @Override
      public boolean test(ButtonType t) {
        if (t.getButtonData().isCancelButton()) {
          // user decide not to load a new file, set open load to false and return
          openLoad = false;
          return true;
        } else {
          // otherwise agree to load, set open load to true
          openLoad = true;
          return false;
        }
      }
    });
    return openLoad;
  }

  /**
   * This handles the event of selection button
   * 
   * @param current the current food item view to be added
   * @param select the select button
   * @param fooditem current added food item
   */
  private void handleSelectButtonEvent(FoodItemView current, Button select, FoodItem fooditem) {
    current.getChildren().add(select);// add this item to the view
    select.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        // if selected , add to select list and update the button
        if (select.getText().equals("select")) {
          select.setText("unselect");
          selectList.add(fooditem);
        } else {// otherwise remove from the selected list and update the button
          select.setText("select");
          selectList.remove(fooditem);
        }
      }
    });
  }

  /**
   * This sorts the filtered food list to maintain the order
   * 
   * @param queryFoodList This represents the sorted food list
   */
  private void sortQueryFoodList(List<FoodItem> queryFoodList) {
    // sort this food item list by create a anonymous comparator class
    Collections.sort(queryFoodList, new Comparator<FoodItem>() {

      @Override
      public int compare(FoodItem o1, FoodItem o2) {
        // invoke the compare to method of a string since name is a string and case insensitive
        return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
      }

    });

  }

  /**
   * This displays the filtered food item
   * 
   * @param queryFoodList This represents the filtered food item list after apply query
   */
  public void queryOnShown(List<FoodItem> queryFoodList) {
    isFiltered = true;// update the filtered status to true
    // clean the list view
    currentFoodListView.getItems().clear();
    this.queryFoodList = queryFoodList;// accept the new filtered list
    sortQueryFoodList(this.queryFoodList);// sort this list to maintain order
    onShownCount.setText(String.valueOf(this.queryFoodList.size()));// update on-shown count
    // for each items in the filtered food list, add it to the view
    for (FoodItem fooditem : this.queryFoodList) {
      FoodItemView current = new FoodItemView(fooditem);
      Button select = new Button();
      // maintain the selected status of each button, if being selected, set to "unselected"
      // otherwise set to selected
      if (selectList.contains(fooditem))
        select.setText("unselect");
      else
        select.setText("select");
      handleSelectButtonEvent(current, select, fooditem);
      currentFoodListView.getItems().add(current);
    }
    currentFoodListView.refresh();// refresh list view
  }

  /**
   * This displays the original food list and undo all filters
   */
  public void noFilterOnShown() {
    // clean the list view and clean the filtered food list
    currentFoodListView.getItems().clear();
    queryFoodList = new ArrayList<FoodItem>();
    sortCurrentFoodItemList();
    // for each items in the food list, add it to the view
    for (FoodItem fooditem : currentFoodItemList) {
      FoodItemView current = new FoodItemView(fooditem);
      Button select = new Button();
      // maintain the selected status of each button, if being selected, set to "unselected"
      // otherwise set to selected
      if (selectList.contains(fooditem))
        select.setText("unselect");
      else
        select.setText("select");
      handleSelectButtonEvent(current, select, fooditem);
      currentFoodListView.getItems().add(current);
    }
    currentFoodListView.refresh();
    onShownCount.setText(String.valueOf(currentFoodItemList.size()));// update on shown count
    isFiltered = false;// set the filtered status to false
  }

  /**
   * refresh the food list view after add an food item
   * 
   * @param FoodItem fi This represents the new food item being added to the food list
   */
  public void refreshAfterAdd(FoodItem fi) {
    if (isFiltered == false) {
      // if not currently being sorted, update the food items list
      currentFoodItemList = foodData.getAllFoodItems();
      // update the count and on shown count
      count.setText(String.valueOf(currentFoodItemList.size()));
      onShownCount.setText(String.valueOf(currentFoodItemList.size()));
      sortCurrentFoodItemList();// sort food list to maintain order
      currentFoodListView.getItems().clear();// clear the food list view
      // for each items in the food list, add it to the view
      for (FoodItem fooditem : currentFoodItemList) {
        FoodItemView current = new FoodItemView(fooditem);
        Button select = new Button();
        // maintain the selected status of each button, if being selected, set to "unselected"
        // otherwise set to selected
        if (selectList.contains(fooditem))
          select.setText("unselect");
        else
          select.setText("select");
        handleSelectButtonEvent(current, select, fooditem);// handle selection event
        currentFoodListView.getItems().add(current);
      }
      currentFoodListView.refresh();// refresh the list view
    } else {
      // otherwise being filtered list, still update food list and count number
      currentFoodItemList = foodData.getAllFoodItems();
      count.setText(String.valueOf(currentFoodItemList.size()));
      queryFoodList.add(fi);// add the new food item to the filtered list
      sortQueryFoodList(queryFoodList);// sort it to maintain the order
      onShownCount.setText(String.valueOf(queryFoodList.size()));// update on shown count
      currentFoodListView.getItems().clear();// clear the food list view
      // for each items in the filtered food list, add it to the view
      for (FoodItem fooditem : queryFoodList) {
        FoodItemView current = new FoodItemView(fooditem);
        Button select = new Button();
        // maintain the selected status of each button, if being selected, set to "unselected"
        // otherwise set to selected
        if (selectList.contains(fooditem))
          select.setText("unselect");
        else
          select.setText("select");
        // handle the selection event
        handleSelectButtonEvent(current, select, fooditem);
        currentFoodListView.getItems().add(current);
      }
      currentFoodListView.refresh();// refersh the list view after completion
    }

  }

  /**
   * reset all buttons to not being selected status after add items to the meal list
   */
  public void resetSelectButton() {
    if (isFiltered == false) {
      // when not currently being sorted by the query clean the list view and select list
      selectList = new ArrayList<FoodItem>();
      currentFoodListView.getItems().clear();
      // for each items in the food list, re-add them to the list view
      for (FoodItem fooditem : currentFoodItemList) {
        FoodItemView current = new FoodItemView(fooditem);
        // reset the select button and handle the select event
        Button select = new Button("select");
        handleSelectButtonEvent(current, select, fooditem);
        currentFoodListView.getItems().add(current);
      }
      // refresh list view and update the on shown count, no need to update total count
      currentFoodListView.refresh();
      onShownCount.setText(String.valueOf(currentFoodItemList.size()));
    } else {
      // otherwise being currently sorted by the query, clean the list view and select list
      selectList = new ArrayList<FoodItem>();
      currentFoodListView.getItems().clear();
      //// for each items in the filtered list, re-add them to the list view
      for (FoodItem fooditem : queryFoodList) {
        FoodItemView current = new FoodItemView(fooditem);
        // reset the select button and handle the select event
        Button select = new Button("select");
        handleSelectButtonEvent(current, select, fooditem);
        currentFoodListView.getItems().add(current);
      }
      currentFoodListView.refresh();// refresh the list view
    }

  }

  /**
   * return the reference of foodquery
   * 
   * @return the reference of foodquery
   */
  public FoodQuery getFoodquery() {
    return foodquery;
  }

  /**
   * return the reference of the meal list
   * 
   * @return the reference of the meal list
   */
  public MealList getMealList() {
    return mealList;
  }


}
