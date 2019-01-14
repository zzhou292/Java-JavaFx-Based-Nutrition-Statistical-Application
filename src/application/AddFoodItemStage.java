/**
 * Filename: AddFoodItemStage.java
 * 
 * Project: team project P5
 * 
 * Authors: Zhikang Meng, Jason ZHOU, Kejia Fan, James Higgins,YULU Zou
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

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * This stage provides a window for the user to add information for a new food
 * 
 * @author Meng, Zhou, Zou, Fan, Higgins
 */
public class AddFoodItemStage extends Stage {

  // reference to the food list and food data
  private FoodList foodList;
  private FoodData foodData;// store the food items info
  // pane and scene control the layout
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

  /**
   * The constructor of the addfood stage
   */
  public AddFoodItemStage(FoodList foodList, FoodData foodData) {
    // initialize the foodList and foodData fields
    this.foodList = foodList;
    this.foodData = foodData;
    // create a new pane
    this.addFoodPane = new AnchorPane();
    createTextFieldLabels();
    handleConfirmEvent();
    handleCancelEvent();
    componentPositionAdjustment();
    StageAdjustment();
  }

  /**
   * Adjustment of position of the stage will be applied if the method has been called
   */
  private void StageAdjustment() {
    addFoodPane.getChildren().addAll(fiberLabel, caloriesLabel, fatLabel, carbohydrateLabel,
        proteinLabel, name, nameLabel, fiber, calories, fat, carbohydrate, protein, confirm, cancel,
        id, idLabel);
    // create a new scene with the size 300x350 based on addFoodPane
    this.addFoodScene = new Scene(addFoodPane, 300, 350);
    this.setScene(addFoodScene);
    this.setTitle("Add Food");// set the title of the stage
    this.setResizable(false);// fix the size of the stage
    // protects user from accidentally click other session
    this.initModality(Modality.APPLICATION_MODAL);
  }

  /**
   * Adjustment of the position of each element - including textfields, labels, and buttons
   */
  private void componentPositionAdjustment() {
    // set the y position of each element
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
    // set the x position of each element
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

  /**
   * The method defines the action will be executed if cancel button has been pressed
   */
  private void handleCancelEvent() {
    // if the cancel button has been pressed, the stage will automatically be closed
    this.cancel = new Button("Cancel");
    cancel.setOnAction(e2 -> {
      this.close(); // close the addfood stage
    });

  }


  /**
   * The method defines the action will be executed if the Add food button has been pressed
   */
  private void handleConfirmEvent() {
    this.confirm = new Button("Add Food");
    confirm.setOnAction(e2 -> {
      boolean valid = checkInputValidity();
      // if the inputs failed the validation test
      // return without calling refresh method to add a new food data
      if (valid == false)
        return;
      else
        addNewFoodItemRefresh(); // call the method to add a food item
    });

  }

  /**
   * Add a new food item to the food database
   */
  private void addNewFoodItemRefresh() {
    // create a foodItem instance with input information
    FoodItem foodItem = new FoodItem(id.getText(), name.getText());
    foodItem.addNutrient("calories", Double.valueOf(calories.getText()));
    foodItem.addNutrient("fat", Double.valueOf(fat.getText()));
    foodItem.addNutrient("carbohydrate", Double.valueOf(carbohydrate.getText()));
    foodItem.addNutrient("fiber", Double.valueOf(fiber.getText()));
    foodItem.addNutrient("protein", Double.valueOf(protein.getText()));
    this.foodData.addFoodItem(foodItem);// add it to the food data to store
    this.close();// close the addfooditem stage
    this.foodList.refreshAfterAdd(foodItem);// refresh the foodList

  }

  /**
   * This method checks whether the user put in the correct data type returns true if every input is
   * the correct type, false otherwise
   */
  private boolean checkInputValidity() {
    // if any of the input field is empty, return false directly
    if (name.getText().equals("") || id.getText().equals("") || fiber.getText().equals("")
        || protein.getText().equals("") || fat.getText().equals("") || calories.getText().equals("")
        || carbohydrate.getText().equals("")) {
      String message = "Make sure enter the value of all nutrient components, please try again!";
      // display the warning windows with the assigned warning message
      Alert alert = new Alert(AlertType.INFORMATION, message);
      alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
      this.close();// close the add food stage
      alert.showAndWait().filter(response -> response == ButtonType.OK);
      return false;
    }
    // if any nutrition info input is not a number value or is negative, return false directly
    try {
      Double fibervalue = null;
      Double proteinvalue = null;
      Double fatvalue = null;
      Double caloriesvalue = null;
      Double carbohydratevalue = null;
      // trim the input to exact numeric value
      fibervalue = Double.valueOf(fiber.getText().trim());
      proteinvalue = Double.valueOf(protein.getText().trim());
      fatvalue = Double.valueOf(fat.getText().trim());
      caloriesvalue = Double.valueOf(calories.getText().trim());
      carbohydratevalue = Double.valueOf(carbohydrate.getText().trim());
      // nutrition input is suppose to be positive numbers
      // if any of the numbers is negative, return false diretcly
      if (fibervalue < 0.0 || proteinvalue < 0.0 || fatvalue < 0.0 || caloriesvalue < 0.0
          || carbohydratevalue < 0.0) {
        String message = "The input of the nutrient can not be negative, please try again!";
        Alert alert = new Alert(AlertType.INFORMATION, message);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        this.close();
        alert.showAndWait().filter(response -> response == ButtonType.OK);
        return false;
      }
      // if any input of the nutrition info is not a double value, catch the exception and return
      // false
    } catch (Exception e) {
      String message =
          "At least one nutrition value input is invalid, please type a number in nutrient textbox!";
      // display the warning windows with the assigned warning message
      Alert alert = new Alert(AlertType.INFORMATION, message);
      alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
      this.close(); // close the addfood stage
      // wait for response from ok button
      alert.showAndWait().filter(response -> response == ButtonType.OK);
      return false;
    }
    return true;
  }

  /**
   * The method initializes all fields
   */
  private void createTextFieldLabels() {
    // initialize empty textfield
    this.name = new TextField();
    this.id = new TextField();
    this.fiber = new TextField();
    this.calories = new TextField();
    this.fat = new TextField();
    this.carbohydrate = new TextField();
    this.protein = new TextField();
    // initialize labels
    this.nameLabel = new Label("Name:");
    this.idLabel = new Label("ID:");
    this.fiberLabel = new Label("Fiber:");
    this.caloriesLabel = new Label("Calories:");
    this.fatLabel = new Label("Fat:");
    this.carbohydrateLabel = new Label("Carbohydrate:");
    this.proteinLabel = new Label("Protein:");
  }



}
