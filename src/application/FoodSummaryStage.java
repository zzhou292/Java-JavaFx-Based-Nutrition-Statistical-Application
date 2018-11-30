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
public class FoodSummaryStage extends Stage {
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



  public FoodSummaryStage(double fiber, double protein, double fat, double calories,
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
    this.setTitle("Another Window");
    this.setResizable(false);
    this.setScene(analysis);
    this.setWidth(300);
    this.setHeight(240);
    this.initModality(Modality.APPLICATION_MODAL);

  }

  private void setLabels() {
    Button ok = new Button("ok");
    Text fl = new Text("Fiber: ");
    Text pl = new Text("Protein: ");
    Text fal = new Text("Fat: ");
    Text cl = new Text("Calories: ");
    Text chl = new Text("Carbonhydrate: ");
    Text co = new Text("Total Item Count: ");
    fl.setFont(Font.font(null, FontWeight.BOLD, 25));
    pl.setFont(Font.font(null, FontWeight.BOLD, 25));
    fal.setFont(Font.font(null, FontWeight.BOLD, 25));
    cl.setFont(Font.font(null, FontWeight.BOLD, 25));
    chl.setFont(Font.font(null, FontWeight.BOLD, 25));
    co.setFont(Font.font(null, FontWeight.BOLD, 25));
    names.getChildren().addAll(fl, pl, fal, cl, chl, co, ok);
    VBox.setMargin(ok, new Insets(0, 0, 0, 130));
    handleOkButton(ok);

  }

  private void handleOkButton(Button ok) {
    ok.setOnAction(event -> {
      this.close();
    });

  }

  private void setTexts() {
    String fs = "" + fiber;
    String ps = "" + protein;
    String fas = "" + fat;
    String cs = "" + calories;
    String chs = "" + carbohydrate;
    String cos = "" + count;

    Text f = new Text(fs);
    Text p = new Text(ps);
    Text fa = new Text(fas);
    Text c = new Text(cs);
    Text ch = new Text(chs);
    Text co = new Text(cos);
    f.setFont(Font.font(null, FontWeight.BOLD, 25));
    p.setFont(Font.font(null, FontWeight.BOLD, 25));
    fa.setFont(Font.font(null, FontWeight.BOLD, 25));
    c.setFont(Font.font(null, FontWeight.BOLD, 25));
    ch.setFont(Font.font(null, FontWeight.BOLD, 25));
    co.setFont(Font.font(null, FontWeight.BOLD, 25));
    values.getChildren().addAll(f, p, fa, c, ch, co);
    data.getChildren().addAll(names, values);
  }



}
