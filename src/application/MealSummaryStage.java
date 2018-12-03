/**
 * 
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
 * @author admin
 *
 */
public class MealSummaryStage extends Stage {
  private VBox names;
  private VBox values;
  private HBox data;
  private double fiber;
  private double protein;
  private double fat;
  private double calories;
  private double carbohydrate;
  private int count;
  private Scene analysis;



  public MealSummaryStage(double fiber, double protein, double fat, double calories,
      double carbohydrate, int count) {
    names = new VBox();
    values = new VBox();
    data = new HBox();
    analysis = new Scene(data);
    this.count = count;
    this.fiber = fiber;
    this.protein = protein;
    this.fat = fat;
    this.calories = calories;
    this.carbohydrate = carbohydrate;
    setLabels();
    setTexts();
    setStageDisplay();
  }

  private void setStageDisplay() {
    this.setTitle("Meal Summary");
    this.setResizable(false);
    this.setScene(analysis);
    this.setWidth(300);
    this.setHeight(265);
    this.initModality(Modality.APPLICATION_MODAL);

  }

  private void setLabels() {
    Button ok = new Button("ok");
    Text fiber = new Text("Fiber: ");
    Text protein = new Text("Protein: ");
    Text fat = new Text("Fat: ");
    Text calory = new Text("Calories: ");
    Text carbon = new Text("Carbonhydrate: ");
    Text count = new Text("Total Item Count: ");
    fiber.setFont(Font.font(null, FontWeight.BOLD, 25));
    protein.setFont(Font.font(null, FontWeight.BOLD, 25));
    fat.setFont(Font.font(null, FontWeight.BOLD, 25));
    calory.setFont(Font.font(null, FontWeight.BOLD, 25));
    carbon.setFont(Font.font(null, FontWeight.BOLD, 25));
    count.setFont(Font.font(null, FontWeight.BOLD, 25));
    names.getChildren().addAll(fiber, protein, fat, calory, carbon, count, ok);
    VBox.setMargin(ok, new Insets(0, 0, 0, 130));
    handleOkButton(ok);

  }

  private void handleOkButton(Button ok) {
    ok.setOnAction(event -> {
      this.close();
    });

  }

  private void setTexts() {
    String fiberStr = "" + fiber;
    String proteinStr = "" + protein;
    String fatStr = "" + fat;
    String caloriesStr = "" + calories;
    String carbohydrateStr = "" + carbohydrate;
    String countStr = "" + count;

    Text fiberTx = new Text(fiberStr);
    Text proteinTx = new Text(proteinStr);
    Text fatTx = new Text(fatStr);
    Text caloriesTx = new Text(caloriesStr);
    Text carbohydrateTx = new Text(carbohydrateStr);
    Text countTx = new Text(countStr);
    fiberTx.setFont(Font.font(null, FontWeight.BOLD, 25));
    proteinTx.setFont(Font.font(null, FontWeight.BOLD, 25));
    fatTx.setFont(Font.font(null, FontWeight.BOLD, 25));
    caloriesTx.setFont(Font.font(null, FontWeight.BOLD, 25));
    carbohydrateTx.setFont(Font.font(null, FontWeight.BOLD, 25));
    countTx.setFont(Font.font(null, FontWeight.BOLD, 25));
    values.getChildren().addAll(fiberTx, proteinTx, fatTx, caloriesTx, carbohydrateTx, countTx);
    data.getChildren().addAll(names, values);
  }



}
