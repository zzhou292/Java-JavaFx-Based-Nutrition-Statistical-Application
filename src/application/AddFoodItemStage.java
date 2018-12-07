package application;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AddFoodItemStage extends Stage {

  private FoodList foodList;
  private FoodData foodData;
  private AnchorPane addFoodPane;
  private Scene addFoodScene;
  // text fields
  private TextField name;
  private TextField id;
  private TextField fiber;
  private TextField calories;
  private TextField fat;
  private TextField carbohydrate;
  private TextField protein;
  // labels
  private Label nameLabel;
  private Label idLabel;
  private Label fiberLabel;
  private Label caloriesLabel;
  private Label fatLabel;
  private Label carbohydrateLabel;
  private Label proteinLabel;
  private Button confirm;
  private Button cancel;

  public AddFoodItemStage(FoodList foodList, FoodData foodData) {

    this.foodList = foodList;
    this.foodData = foodData;
    this.addFoodPane = new AnchorPane();
    createTextFieldLabels();
    handleConfirmEvent();
    handleCancelEvent();
    componentPositionAdjustment();
    StageAdjustment();
  }

  private void StageAdjustment() {
    addFoodPane.getChildren().addAll(fiberLabel, caloriesLabel, fatLabel, carbohydrateLabel,
        proteinLabel, name, nameLabel, fiber, calories, fat, carbohydrate, protein, confirm, cancel,
        id, idLabel);

    this.addFoodScene = new Scene(addFoodPane, 300, 350);
    this.setScene(addFoodScene);
    this.setTitle("Add Food");
    this.setResizable(false);
    this.initModality(Modality.APPLICATION_MODAL);
  }

  private void componentPositionAdjustment() {
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
  }

  private void handleCancelEvent() {
    this.cancel = new Button("Cancel");
    cancel.setOnAction(e2 -> {
      this.close();
    });

  }

  private void handleConfirmEvent() {
    this.confirm = new Button("Add Food");
    confirm.setOnAction(e2 -> {
      boolean valid = checkInputValidity();
      if (valid == false)
        return;
      else
        addNewFoodItemRefresh();
    });

  }

  private void addNewFoodItemRefresh() {
    FoodItem foodItem = new FoodItem(id.getText(), name.getText());
    foodItem.addNutrient("calories", Double.valueOf(calories.getText()));
    foodItem.addNutrient("fat", Double.valueOf(fat.getText()));
    foodItem.addNutrient("carbohydrate", Double.valueOf(carbohydrate.getText()));
    foodItem.addNutrient("fiber", Double.valueOf(fiber.getText()));
    foodItem.addNutrient("protein", Double.valueOf(protein.getText()));
    this.foodData.addFoodItem(foodItem);

    this.close();
    this.foodList.refreshAfterAdd();

  }

  private boolean checkInputValidity() {
    if (name.getText().equals("") || id.getText().equals("") || fiber.getText().equals("")
        || protein.getText().equals("") || fat.getText().equals("") || calories.getText().equals("")
        || carbohydrate.getText().equals("")) {
      String message = "Make sure to choose all component and enter the value, please try again!";
      Alert alert = new Alert(AlertType.INFORMATION, message);
      this.close();
      alert.showAndWait().filter(response -> response == ButtonType.OK);
      return false;
    }

    try {
      Double fibervalue = null;
      Double proteinvalue = null;
      Double fatvalue = null;
      Double caloriesvalue = null;
      Double carbohydratevalue = null;
      fibervalue = Double.valueOf(fiber.getText().trim());
      proteinvalue = Double.valueOf(protein.getText().trim());
      fatvalue = Double.valueOf(fat.getText().trim());
      caloriesvalue = Double.valueOf(calories.getText().trim());
      carbohydratevalue = Double.valueOf(carbohydrate.getText().trim());

      if (fibervalue < 0.0 || proteinvalue < 0.0 || fatvalue < 0.0 || caloriesvalue < 0.0
          || carbohydratevalue < 0.0) {
        String message = "The input of the nutrient can not be negative, please try again!";
        Alert alert = new Alert(AlertType.INFORMATION, message);
        this.close();
        alert.showAndWait().filter(response -> response == ButtonType.OK);
        return false;
      }
    } catch (Exception e) {
      String message =
          "At least one nutrition value input is invalid, please type a number in nutrient textbox!";
      Alert alert = new Alert(AlertType.INFORMATION, message);
      this.close();
      alert.showAndWait().filter(response -> response == ButtonType.OK);
      return false;
    }
    return true;
  }

  private void createTextFieldLabels() {
    this.name = new TextField();
    this.id = new TextField();
    this.fiber = new TextField();
    this.calories = new TextField();
    this.fat = new TextField();
    this.carbohydrate = new TextField();
    this.protein = new TextField();

    this.nameLabel = new Label("Name:");
    this.idLabel = new Label("ID:");
    this.fiberLabel = new Label("Fiber:");
    this.caloriesLabel = new Label("Calories:");
    this.fatLabel = new Label("Fat:");
    this.carbohydrateLabel = new Label("Carbohydrate:");
    this.proteinLabel = new Label("Protein:");
  }



}
