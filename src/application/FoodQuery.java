/**
 * 
 */
package application;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
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
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


/**
 * @author admin
 *
 */
public class FoodQuery extends VBox {

  private ListView<HBox> queryRuleListView;
  private List<String> rules;
  private MenuBar menuBar;
  private Label label;
  private List<FoodItem> currentFoodItemList;// can get value from it, but do not change it !!
  private ListView<HBox> currentFoodListView;// do update it when appropriate
  private List<FoodItem> queryFoodList;// do update it when appropriate
  private HBox nameRuleBox;
  private TextField nameRuleField;

  public FoodQuery(ListView<HBox> currentFoodListView, List<FoodItem> currentFoodItemList) {
    this.label = new Label("FoodQuery Rules");
    nameRuleField = new TextField();
    this.rules = new ArrayList<String>();
    this.menuBar = new MenuBar();
    this.currentFoodItemList = currentFoodItemList;
    this.currentFoodListView = currentFoodListView;
    queryFoodList = new ArrayList<FoodItem>();
    this.queryRuleListView = new ListView<HBox>();
    nameRuleBox = new HBox();
    crateNameRuleBox();
    createMenubar();
    boxAdjustment();

  }


  private void crateNameRuleBox() {
    Label nameRuleLabel = new Label("Name Rule: ");
    Button clear = new Button("clear");
    nameRuleField.setEditable(false);
    nameRuleBox.getChildren().addAll(nameRuleLabel, nameRuleField, clear);
    handleNameRuleClear(clear);
  }


  private void handleNameRuleClear(Button clear) {
    clear.setOnAction(e1 -> {
      Alert alert = new Alert(AlertType.CONFIRMATION, "Confirm to clear this name rule");
      alert.showAndWait().filter(new Predicate<ButtonType>() {
        @Override
        public boolean test(ButtonType t) {
          if (t.getButtonData().isCancelButton())
            return true;
          else {
            nameRuleField.setText("");
            return false;
          }
        }
      });
    });

  }


  private void boxAdjustment() {

    this.getChildren().addAll(this.label, this.menuBar, this.nameRuleBox, this.queryRuleListView);
    VBox.setMargin(label, new Insets(0, 0, 0, 130));
    this.setPrefWidth(370.0);
    this.setStyle("-fx-background-color:#BFEFFF");

  }

  private void createMenubar() {
    MenuItem addNutrientRule = new MenuItem("Add Nutrient Rule");
    addNutrientRule.setOnAction(rule -> {
      AddNutrientStage ans = new AddNutrientStage(queryRuleListView, rules);
      ans.show();
    });

    MenuItem setNameRule = new MenuItem("Set Name Rule");
    setNameRule.setOnAction(searchnames -> {
      SetNameRuleStage setNameRuleStage = new SetNameRuleStage(nameRuleField);
      setNameRuleStage.show();
    });


    MenuItem clear = new MenuItem("Clear All Rules");
    MenuItem filterByName = new MenuItem("Filter by Name");
    MenuItem filterByNutrient = new MenuItem("Filter By Nutrient");
    MenuItem filterByAllRules = new MenuItem("Filter by All Rules");
    MenuItem unDoFilters = new MenuItem("unDo All Filters");
    Menu operation = new Menu("Operation", null, addNutrientRule, setNameRule, filterByName,
        filterByNutrient, filterByAllRules, unDoFilters, clear);
    menuBar.getMenus().addAll(operation);
    handleClearEvent(clear);
  }

  private void handleClearEvent(MenuItem clear) {
    clear.setOnAction(e1 -> {
      String message = "Confirm to clear all query rules!";
      Alert alert = new Alert(AlertType.CONFIRMATION, message);
      alert.showAndWait().filter(new Predicate<ButtonType>() {
        @Override
        public boolean test(ButtonType t) {
          if (t.getButtonData().isCancelButton())
            return true;
          else {
            queryRuleListView.getItems().clear();
            rules = new ArrayList<String>();
            nameRuleField.setText("");
            return false;
          }
        }
      });    
    });
  }


  public void updateList(String filePath) {

  }
}


