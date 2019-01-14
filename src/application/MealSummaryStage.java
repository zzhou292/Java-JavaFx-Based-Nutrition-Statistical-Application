/**
 * Filename: MealSummaryStage.java
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

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;


/**
 * This stage provides a window to show the user the summary of nutrition info of food in meallist
 * each nutrient number will be added to calculate total
 * 
 * @author Meng, Zhou, Zou, Fan, Higgins
 */
public class MealSummaryStage extends Stage {
  // the Vboxs use to display the names labels and nutrient values
  private VBox names;
  private VBox values;
  private HBox data;
  // the values of each nutrient components
  private double fiber;
  private double protein;
  private double fat;
  private double calories;
  private double carbohydrate;
  // the count of total meal items
  private int count;
  // the sence control this window
  private Scene analysis;


  /**
   * The constructor of the FoodSummaryStage The SummaryStage will accept total nutrition info and
   * display
   */
  public MealSummaryStage(double fiber, double protein, double fat, double calories,
      double carbohydrate, int count) {
    names = new VBox();
    values = new VBox();
    data = new HBox();
    analysis = new Scene(data); // set Scene
    this.count = count;
    this.fiber = fiber;
    this.protein = protein;
    this.fat = fat;
    this.calories = calories;
    this.carbohydrate = carbohydrate; // set field values
    setLabels(); // call helper methods to create layout and other components
    setTexts();
    setStageDisplay();
  }

  /**
   * The method sets the properties of the stage
   */
  private void setStageDisplay() {
    this.setTitle("Meal Summary");// set title of the stage
    this.setScene(analysis); // set Scene
    this.setWidth(400); // set Width
    this.setHeight(265); // set Height
    this.initModality(Modality.APPLICATION_MODAL); // set Stage modality

  }

  /**
   * This methods creates label and button components and sets the textfield inside
   */
  private void setLabels() {
    Button ok = new Button("ok"); // initiate button item
    Text fiber = new Text("Fiber: "); // initiate text labels (text objects)
    Text protein = new Text("Protein: ");
    Text fat = new Text("Fat: ");
    Text calory = new Text("Calories: ");
    Text carbon = new Text("Carbonhydrate: ");
    Text count = new Text("Total Item Count: ");
    fiber.setFont(Font.font(null, FontWeight.BOLD, 25));//// set font
    protein.setFont(Font.font(null, FontWeight.BOLD, 25));
    fat.setFont(Font.font(null, FontWeight.BOLD, 25));
    calory.setFont(Font.font(null, FontWeight.BOLD, 25));
    carbon.setFont(Font.font(null, FontWeight.BOLD, 25));
    count.setFont(Font.font(null, FontWeight.BOLD, 25));
    // add all elements to the VBox layout
    names.getChildren().addAll(fiber, protein, fat, calory, carbon, count, ok);
    VBox.setMargin(ok, new Insets(0, 0, 0, 130));
    // set margin
    handleOkButton(ok);
    // set the action and operation for the ok button
  }

  /**
   * The method sets the action will be executed if ok button has been clicked
   */
  private void handleOkButton(Button ok) {
    ok.setOnAction(event -> {
      this.close();
      // if ok button is clicked
      // close the stage
    });
  }

  /**
   * The method sets nutrition value display by creating Text objects
   */
  private void setTexts() {
    // transform double to String instances
    String fiberStr = "" + fiber;
    String proteinStr = "" + protein;
    String fatStr = "" + fat;
    String caloriesStr = "" + calories;
    String carbohydrateStr = "" + carbohydrate;
    String countStr = "" + count;

    // create Text objects and set text inside
    Text fiberTx = new Text(fiberStr);
    Text proteinTx = new Text(proteinStr);
    Text fatTx = new Text(fatStr);
    Text caloriesTx = new Text(caloriesStr);
    Text carbohydrateTx = new Text(carbohydrateStr);
    Text countTx = new Text(countStr);
    // set font of each nutrition display
    fiberTx.setFont(Font.font(null, FontWeight.BOLD, 25));
    proteinTx.setFont(Font.font(null, FontWeight.BOLD, 25));
    fatTx.setFont(Font.font(null, FontWeight.BOLD, 25));
    caloriesTx.setFont(Font.font(null, FontWeight.BOLD, 25));
    carbohydrateTx.setFont(Font.font(null, FontWeight.BOLD, 25));
    countTx.setFont(Font.font(null, FontWeight.BOLD, 25));
    values.getChildren().addAll(fiberTx, proteinTx, fatTx, caloriesTx, carbohydrateTx, countTx);
    data.getChildren().addAll(names, values);
    // add all elements to the layout
  }

}
