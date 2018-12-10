/**
 * 
 */
package application;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
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
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;


/**
 * @author admin
 *
 */
public class FoodQuery extends VBox {

  private ListView<HBox> queryRuleListView;
  private List<String> rules;
  private MenuBar menuBar;
  private Label label;
  private FoodData foodData;// can get value from it, but do not change it !!
  private List<FoodItem> queryFoodList;// do update it when appropriate
  private HBox nameRuleBox;
  private TextField nameRuleField;
  private FoodList foodList;
  private Label workingFilter;
  private Separator sp;

  public FoodQuery(FoodList foodList) {
    this.label = new Label("FoodQuery Rules");
    this.foodList = foodList;
    sp = new Separator();
    this.workingFilter = new Label("No Filter is currently working!");
    workingFilter.setFont(Font.font(null, FontWeight.BOLD, 25));
    nameRuleField = new TextField();
    this.rules = new ArrayList<String>();
    this.menuBar = new MenuBar();
    this.foodData = new FoodData();
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
      String message = "Confirm to clear this name rule\n"
          + "Attention: the food list will not automatically return to"
          + " the original food list. To do so please click on 'unDo All Filters' button";
      Alert alert = new Alert(AlertType.CONFIRMATION, message);
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

    this.getChildren().addAll(this.label, this.menuBar, this.workingFilter, sp, this.nameRuleBox,
        this.queryRuleListView);
    VBox.setMargin(label, new Insets(0, 0, 0, 130));
    this.setPrefWidth(400.0);
    this.setStyle("-fx-background-color:#BFEFFF");

  }

  private void createMenubar() {
    MenuItem addNutrientRule = new MenuItem("Add Nutrient Rule");
    MenuItem setNameRule = new MenuItem("Set Name Rule");
    MenuItem clear = new MenuItem("Clear All Rules");
    MenuItem filterByName = new MenuItem("Filter by Name");
    MenuItem filterByNutrient = new MenuItem("Filter By Nutrient");
    MenuItem filterByAllRules = new MenuItem("Filter by All Rules");
    MenuItem unDoFilters = new MenuItem("unDo All Filters");
    Menu operation = new Menu("Operation", null, addNutrientRule, setNameRule, filterByName,
        filterByNutrient, filterByAllRules, unDoFilters, clear);
    menuBar.getMenus().addAll(operation);
    handleClearEvent(clear);
    handleAddRuleEvent(addNutrientRule, setNameRule);
    handleNameFilterEvent(filterByName);
    handleUndoEvent(unDoFilters);
    handleNutrientEvent(filterByNutrient);
    handleApplyAllRuleEvent(filterByAllRules);
  }

  private void handleApplyAllRuleEvent(MenuItem filterByAllRules) {
    filterByAllRules.setOnAction(rule -> {
      if (rules.size() == 0 || nameRuleField.getText().equals("")) {
        String message = "Please Add a Nutrient rule and set a name rule before apply it!";
        Alert alert = new Alert(AlertType.INFORMATION, message);
        alert.showAndWait().filter(response -> response == ButtonType.OK);
      } else {
        /**
         * uncomment after implement BPTree
         */
        List<FoodItem> NameFiltered = foodData.filterByName(nameRuleField.getText());
        List<FoodItem> NutriFiltered = foodData.filterByNutrients(rules);
        List<FoodItem> interSection = getIntersection(NameFiltered, NutriFiltered);
        foodList.queryOnShown(interSection);
        workingFilter.setText("Both filters are now working!");
      }
    });

  }


  private List<FoodItem> getIntersection(List<FoodItem> nameFiltered,
      List<FoodItem> nutriFiltered) {
    List<FoodItem> interset = new ArrayList<FoodItem>();
    for (FoodItem fi : nameFiltered) {
      if (nutriFiltered.contains(fi)) {
        interset.add(fi);
      }
    }
    return interset;
  }


  private void handleNutrientEvent(MenuItem filterByNutrient) {
    filterByNutrient.setOnAction(rule -> {
      if (rules.size() == 0) {
        String message = "Please Add a Nutrient rule before apply it!";
        Alert alert = new Alert(AlertType.INFORMATION, message);
        alert.showAndWait().filter(response -> response == ButtonType.OK);
      } else {

        /**
         * uncomment after implement BPTree
         */
        queryFoodList = foodData.filterByNutrients(rules);
        foodList.queryOnShown(queryFoodList);

        workingFilter.setText("Nutrient filter now working!");
      }
    });

  }


  private void handleUndoEvent(MenuItem unDoFilters) {
    // TODO Auto-generated method stub
    unDoFilters.setOnAction(rule -> {
      foodList.noFilterOnShown();
    });
  }


  private void handleNameFilterEvent(MenuItem filterByName) {
    filterByName.setOnAction(rule -> {
      if (nameRuleField.getText().equals("")) {
        String message = "Please Set a name rule before apply it!";
        Alert alert = new Alert(AlertType.INFORMATION, message);
        alert.showAndWait().filter(response -> response == ButtonType.OK);
      } else {
        queryFoodList = foodData.filterByName(nameRuleField.getText());
        foodList.queryOnShown(queryFoodList);
        workingFilter.setText("Name Filter is now Working!");
      }
    });

  }


  private void handleAddRuleEvent(MenuItem addNutrientRule, MenuItem setNameRule) {
    addNutrientRule.setOnAction(rule -> {
      AddNutrientStage ans = new AddNutrientStage(queryRuleListView, rules);
      ans.show();
    });
    setNameRule.setOnAction(searchnames -> {
      SetNameRuleStage setNameRuleStage = new SetNameRuleStage(nameRuleField);
      setNameRuleStage.show();
    });
  }


  private void handleClearEvent(MenuItem clear) {
    clear.setOnAction(e1 -> {
      String message = "Confirm to clear all query rules!\n"
          + "Attention: the food list will not automatically return to"
          + " the original food list. To do so please click on 'unDo All Filters' button";
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


  public void updateFoodData(FoodData foodData) {
    this.foodData = foodData;
  }


  /**
   * @return the workingFilter
   */
  public Label getWorkingFilter() {
    return workingFilter;
  }



}


