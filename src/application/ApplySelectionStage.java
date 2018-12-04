/**
 * 
 */
package application;

import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * @author admin
 *
 */
public class ApplySelectionStage extends Stage {

  private List<FoodItem> selectList;
  private ListView<HBox> currentselectList;
  private VBox vbox;
  private Scene selectScene;
  private Button ok;
  private Button cancel;
  private HBox hbox;
  private Label count;
  private HBox countBox;
  private FoodList foodList;
  private MealList mealList;

  public ApplySelectionStage(List<FoodItem> selectList, FoodList foodList, MealList mealList) {
    this.mealList = mealList;
    this.foodList = foodList;
    this.selectList = selectList;
    currentselectList = new ListView<HBox>();
    vbox = new VBox();
    ok = new Button("OK");
    cancel = new Button("cancel");
    hbox = new HBox(130);
    this.count = new Label(String.valueOf(selectList.size()));
    countBox = new HBox(5);
    countBox.setStyle("-fx-background-color:#BFEFFF");
    handleOKCancelEvent(ok, cancel);
    createCountLabels();
    setSelectList();
    setLayout();
    setStageDisplay();


  }

  private void handleOKCancelEvent(Button ok, Button cancel) {
    cancel.setOnAction(event -> {
      this.close();
    });
    ok.setOnAction(event -> {
      mealList.addFoodItems(selectList);
      mealList.getCount().setText(String.valueOf(mealList.getCurrentMealList().size()));
      this.close();
      this.foodList.resetSelectButton();
    });

  }

  private void createCountLabels() {
    Label countlb = new Label("Total selected food Items count: ");
    countBox.getChildren().addAll(countlb, count);
  }

  private void setSelectList() {
    for (FoodItem fooditem : selectList) {
      FoodItemView current = new FoodItemView(fooditem);
      currentselectList.getItems().add(current);
    }

  }

  private void setLayout() {
    hbox.getChildren().addAll(ok, cancel);
    Label label = new Label("Items shown below will be added to meal list");
    label.setFont(Font.font(null, FontWeight.BOLD, 25));
    vbox.getChildren().addAll(label, countBox, currentselectList, hbox);
    VBox.setMargin(hbox, new Insets(0, 0, 0, 130));


  }


  private void setStageDisplay() {
    selectScene = new Scene(vbox);
    this.setTitle("Selection summary");
    this.setResizable(false);
    this.setScene(selectScene);
    this.sizeToScene();
    this.initModality(Modality.APPLICATION_MODAL);

  }





}
