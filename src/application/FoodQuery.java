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

  private ListView<HBox> queryRuleList;
  private List<String> rules;
  private MenuBar menuBar;
  private Label label;
  private List<FoodItem> currentFoodItemList;// can get value from it, but do not change it !!
  private ListView<HBox> currentFoodListView;// do update it when appropriate
  private List<FoodItem> queryFoodList;// do update it when appropriate
  private HBox nameRuleBox;
  private String nameRule;
  private TextField nameRuleField;

  public FoodQuery(ListView<HBox> currentFoodListView, List<FoodItem> currentFoodItemList) {
    this.label = new Label("FoodQuery Rules");
    nameRule = "";
    nameRuleField = new TextField();
    this.rules = new ArrayList<String>();
    this.menuBar = new MenuBar();
    this.currentFoodItemList = currentFoodItemList;
    this.currentFoodListView = currentFoodListView;
    queryFoodList = new ArrayList<FoodItem>();
    this.queryRuleList = new ListView<HBox>();
    nameRuleBox = new HBox();
    crateNameRuleBox();
    createMenubar();
    boxAdjustment();

  }


  private void crateNameRuleBox() {
    Label nameRuleLabel = new Label("Name Rule: ");
    Button remove = new Button("remove");
    nameRuleField.setEditable(false);
    nameRuleBox.getChildren().addAll(nameRuleLabel, nameRuleField, remove);
    handleNameRuleRemove(remove);
  }


  private void handleNameRuleRemove(Button remove) {
    remove.setOnAction(e1 -> {
      Alert alert = new Alert(AlertType.CONFIRMATION, "Confirm to remove this name rule");
      alert.showAndWait().filter(new Predicate<ButtonType>() {
        @Override
        public boolean test(ButtonType t) {
          if (t.getButtonData().isCancelButton())
            return true;
          else {
            nameRule = "";
            nameRuleField.setText(nameRule);
            return false;
          }
        }
      });
    });

  }


  private void boxAdjustment() {

    this.getChildren().addAll(this.label, this.menuBar, this.nameRuleBox, this.queryRuleList);
    VBox.setMargin(label, new Insets(0, 0, 0, 130));
    this.setPrefWidth(370.0);
    this.setStyle("-fx-background-color:#BFEFFF");

  }

  private void createMenubar() {
    MenuItem addNutrientRule = new MenuItem("Add Nutrient Rule");
    addNutrientRule.setOnAction(rule -> {
      AddNutrientStage ans = new AddNutrientStage();
     ans.show();
    });

    MenuItem setNameRule = new MenuItem("Set Name Rule");
    setNameRule.setOnAction(searchnames -> {
      SetNameRuleStage setNameRuleStage = new SetNameRuleStage(nameRule, nameRuleField);
      setNameRuleStage.show();
    });


    MenuItem clear = new MenuItem("Clear Rules");
    MenuItem applyName = new MenuItem("Apply Name Rules");
    MenuItem applyNutrientRule = new MenuItem("Apply Nutrient Rules");
    MenuItem applyallRule = new MenuItem("Apply All Rules");
    Menu operation = new Menu("Operation", null, addNutrientRule, setNameRule, applyName,
        applyNutrientRule, applyallRule, clear);
    menuBar.getMenus().addAll(operation);

  }

  public void updateList(String filePath) {

  }
}


