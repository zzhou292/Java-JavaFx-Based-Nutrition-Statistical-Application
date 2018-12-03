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

  public FoodList(Stage primaryStage) {
    this.currentFoodListView = new ListView<HBox>();
    this.foodData = new FoodData();
    this.menuBar = new MenuBar();
    this.foodquery = new FoodQuery(this.currentFoodListView, this.currentFoodItemList);
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
    add.setOnAction(e1 -> {
      Stage addFoodStage = new Stage();

      AnchorPane addFoodPane = new AnchorPane();

      TextField name = new TextField();
      TextField id = new TextField();
      TextField fiber = new TextField();
      TextField calories = new TextField();
      TextField fat = new TextField();
      TextField carbohydrate = new TextField();
      TextField protein = new TextField();

      Label nameLabel = new Label("Name:");
      Label idLabel = new Label("ID:");
      Label fiberLabel = new Label("Fiber:");
      Label caloriesLabel = new Label("Calories:");
      Label fatLabel = new Label("Fat:");
      Label carbohydrateLabel = new Label("Carbohydrate:");
      Label proteinLabel = new Label("Protein:");

      Button confirm = new Button("Add Food");
      confirm.setOnAction(e2 -> {
        String buffer = "";
        buffer = buffer + "Name:";
        buffer = buffer + name.getText();
        buffer = buffer + ";  ";

        buffer = buffer + "ID:";
        buffer = buffer + id.getText();
        buffer = buffer + ";  ";

        buffer = buffer + "Fiber:";
        buffer = buffer + fiber.getText();
        buffer = buffer + ";  ";

        buffer = buffer + "Protein:";
        buffer = buffer + protein.getText();
        buffer = buffer + ";  ";

        buffer = buffer + "Fat:";
        buffer = buffer + fat.getText();
        buffer = buffer + ";  ";

        buffer = buffer + "Calories:";
        buffer = buffer + calories.getText();
        buffer = buffer + ";  ";

        buffer = buffer + "Carbohydrate:";
        buffer = buffer + carbohydrate.getText();
        buffer = buffer + ";  ";

        System.out.println(buffer);

        addFoodStage.close();

      });


      Button cancel = new Button("Cancel");
      cancel.setOnAction(e2 -> {
        addFoodStage.close();

      });

      AnchorPane.setTopAnchor(nameLabel, 30.0);
      AnchorPane.setTopAnchor(name, 30.0);
      AnchorPane.setTopAnchor(fiberLabel, 70.0);
      AnchorPane.setTopAnchor(proteinLabel, 110.0);
      AnchorPane.setTopAnchor(fatLabel, 150.0);
      AnchorPane.setTopAnchor(caloriesLabel, 190.0);
      AnchorPane.setTopAnchor(carbohydrateLabel, 230.0);
      AnchorPane.setTopAnchor(fiber, 70.0);
      AnchorPane.setTopAnchor(calories, 190.0);
      AnchorPane.setTopAnchor(fat, 150.0);
      AnchorPane.setTopAnchor(carbohydrate, 230.0);
      AnchorPane.setTopAnchor(protein, 110.0);
      AnchorPane.setTopAnchor(id, 270.0);
      AnchorPane.setTopAnchor(idLabel, 270.0);
      AnchorPane.setTopAnchor(confirm, 310.0);
      AnchorPane.setTopAnchor(cancel, 310.0);

      AnchorPane.setLeftAnchor(fiberLabel, 20.0);
      AnchorPane.setLeftAnchor(proteinLabel, 20.0);
      AnchorPane.setLeftAnchor(fatLabel, 20.0);
      AnchorPane.setLeftAnchor(caloriesLabel, 20.0);
      AnchorPane.setLeftAnchor(carbohydrateLabel, 20.0);
      AnchorPane.setLeftAnchor(nameLabel, 20.0);
      AnchorPane.setLeftAnchor(idLabel, 20.0);
      AnchorPane.setLeftAnchor(name, 120.0);
      AnchorPane.setLeftAnchor(fiber, 120.0);
      AnchorPane.setLeftAnchor(calories, 120.0);
      AnchorPane.setLeftAnchor(fat, 120.0);
      AnchorPane.setLeftAnchor(id, 120.0);
      AnchorPane.setLeftAnchor(carbohydrate, 120.0);
      AnchorPane.setLeftAnchor(protein, 120.0);
      AnchorPane.setLeftAnchor(confirm, 50.0);
      AnchorPane.setLeftAnchor(cancel, 200.0);

      addFoodPane.getChildren().addAll(fiberLabel, caloriesLabel, fatLabel, carbohydrateLabel,
          proteinLabel, name, nameLabel, fiber, calories, fat, carbohydrate, protein, confirm,
          cancel, id, idLabel);

      Scene addFoodScene = new Scene(addFoodPane, 300, 350);
      addFoodStage.setScene(addFoodScene);
      addFoodStage.setTitle("Add Food");
      addFoodStage.show();
      addFoodStage.setResizable(false);

    });
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
