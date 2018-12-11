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
 * This stage provides a window for the user to confirm their selections the selections of the food
 * will be copied from the foodlist to the meallist
 */
public class ApplySelectionStage extends Stage {

  // select food list and list view of it
  private List<FoodItem> selectList;
  private ListView<HBox> currentselectList;
  private VBox vbox;// vertical layout control
  private Scene selectScene;// scene control
  // ok and cancel button
  private Button ok;
  private Button cancel;
  private HBox hbox;
  // count the number of food items to be added to the meal list
  private Label count;
  private HBox countBox;
  // reference of the food list and meal list
  private FoodList foodList;
  private MealList mealList;

  /**
   * public Constructor
   * 
   * @param selectList the list of selected food items
   * @param foodList the food list object
   * @param mealList the meal list object
   */
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

  /**
   * The method sets the action of the ok and cancel buttons
   */
  private void handleOKCancelEvent(Button ok, Button cancel) {
    // if the cancel button is pressed, close the apply selection stage
    cancel.setOnAction(event -> {
      this.close();
    });
    // if the ok button is pressed, copied the selected fooditems from the foodlist to the meallist
    ok.setOnAction(event -> {
      mealList.addFoodItems(selectList);
      // display total count
      mealList.getCount().setText(String.valueOf(mealList.getCurrentMealList().size()));
      this.close();
      this.foodList.resetSelectButton();// reset the select button status in the food list
    });

  }

  /**
   * create default count label
   */
  private void createCountLabels() {
    // add the count message and the count number info to the label
    Label countlb = new Label("Total selected food Items count: ");
    countBox.getChildren().addAll(countlb, count);
  }

  /**
   * create default selectList view
   */
  private void setSelectList() {
    // add every selected fooditems from the selected list to the current list view
    for (FoodItem fooditem : selectList) {
      FoodItemView current = new FoodItemView(fooditem);
      currentselectList.getItems().add(current);
    }

  }

  /**
   * set layout of the default message and display
   */
  private void setLayout() {
    hbox.getChildren().addAll(ok, cancel);
    // add the ok and cancel buttons to the hbox layout
    Label label = new Label("Items shown below will be added to meal list");
    label.setFont(Font.font(null, FontWeight.BOLD, 25));
    vbox.getChildren().addAll(label, countBox, currentselectList, hbox);
    VBox.setMargin(hbox, new Insets(0, 0, 0, 130));


  }

  /**
   * set default properties of the stage
   */
  private void setStageDisplay() {
    // create the stage based on the scene
    selectScene = new Scene(vbox);
    this.setTitle("Selection summary"); // set the title of the stage
    // fix the position of the stage
    this.setResizable(false);
    this.setScene(selectScene);
    this.sizeToScene();
    // protects user from accidentally click other session
    this.initModality(Modality.APPLICATION_MODAL);

  }



}
