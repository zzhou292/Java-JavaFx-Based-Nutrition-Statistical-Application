package application;

import java.util.List;
import java.util.function.Predicate;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AddNutrientStage extends Stage {
  private AnchorPane anchorPane;
  private Scene scene;
  private ListView<HBox> queryRuleListView;
  private List<String> rules;
  private ComboBox<String> comboBoxNutritions;
  private ComboBox<String> comboBoxSigns;
  private TextField input;

  public AddNutrientStage(ListView<HBox> queryRuleList, List<String> rules) {
    this.anchorPane = new AnchorPane();
    this.scene = new Scene(anchorPane, 600, 120);
    this.queryRuleListView = queryRuleList;
    this.rules = rules;
    setStageLayOut();
    crateAddWindow();

  }


  private void crateAddWindow() {

    this.comboBoxNutritions = new ComboBox<String>();
    comboBoxNutritions.getItems().addAll("calories", "fat", "carbohydrate", "fiber", "protein");
    this.comboBoxSigns = new ComboBox<String>();
    comboBoxSigns.getItems().addAll("==", ">=", "<=");
    this.input = new TextField();
    Button confirm = new Button("Add Rule");
    Button cancel = new Button("Cancel");
    Label nulabel = new Label("Nutrient                        comparator     value");
    anchorPane.getChildren().addAll(nulabel, comboBoxNutritions, comboBoxSigns, input, confirm,
        cancel);
    setComponentLayout(nulabel, input, confirm, cancel);
    handleCancelConfirmEvent(cancel, confirm);
  }


  private void handleCancelConfirmEvent(Button cancel, Button confirm) {


    cancel.setOnAction(e1 -> {
      this.close();

    });

    confirm.setOnAction(e1 -> {
      String buffer = "";
      boolean valid = checkInputValidity();
      if (valid == false)
        return;
      else {
        // processing input
        buffer = buffer + comboBoxNutritions.getValue() + " ";
        buffer = buffer + comboBoxSigns.getValue() + " ";
        buffer = buffer + input.getText().trim();
        addNewRule(buffer);
        this.close();
      }

    });

  }


  private boolean checkInputValidity() {
    Double value = null;
    // exception handling
    if (comboBoxNutritions.getValue() == null || comboBoxSigns.getValue() == null
        || input.getText().equals("")) {
      String message = "Make sure to choose all component and enter the value, please try again!";
      Alert alert = new Alert(AlertType.INFORMATION, message);
      this.close();
      alert.showAndWait().filter(response -> response == ButtonType.OK);
      return false;

    }
    try {
      value = Double.valueOf(input.getText().trim());
    } catch (Exception e) {
      String message = "The format of your value input is incorrect, please try again!";
      Alert alert = new Alert(AlertType.INFORMATION, message);
      this.close();
      alert.showAndWait().filter(response -> response == ButtonType.OK);
      return false;
    }
    if (value < 0.0) {
      String message = "The input of the nutrient can not be negative, please try again!";
      Alert alert = new Alert(AlertType.INFORMATION, message);
      this.close();
      alert.showAndWait().filter(response -> response == ButtonType.OK);
      return false;
    }

    return true;
  }

  private void addNewRule(String buffer) {
    HBox newRuleBox = new HBox();
    Button remove = new Button("remove");
    TextField newRule = new TextField(buffer);
    Tooltip tip = new Tooltip();
    tip.setText(buffer);
    newRule.setTooltip(tip);
    newRule.setPrefWidth(200.0);
    newRule.setEditable(false);
    newRuleBox.getChildren().addAll(newRule, remove);

    rules.add(buffer);
    queryRuleListView.getItems().add(newRuleBox);
    queryRuleListView.refresh();

    remove.setOnAction(event -> {
      String message = "Confirm to remove nutrient Rule: " + buffer;
      message += "Attention: the food list will not automatically return to"
          + " the original food list. To do so please click on 'unDo All Filters' button";
      Alert alert = new Alert(AlertType.CONFIRMATION, message);
      alert.showAndWait().filter(new Predicate<ButtonType>() {
        @Override
        public boolean test(ButtonType t) {
          if (t.getButtonData().isCancelButton())
            return true;
          else {
            rules.remove(buffer);
            queryRuleListView.getItems().remove(newRuleBox);
            queryRuleListView.refresh();
            return false;
          }
        }
      });

    });
  }


  private void setComponentLayout(Label nulabel, TextField input, Button confirm, Button cancel) {
    AnchorPane.setLeftAnchor(comboBoxNutritions, 20.0);
    AnchorPane.setLeftAnchor(comboBoxSigns, 160.0);
    AnchorPane.setLeftAnchor(input, 240.0);
    AnchorPane.setLeftAnchor(confirm, 420.0);
    AnchorPane.setLeftAnchor(cancel, 500.0);
    AnchorPane.setLeftAnchor(nulabel, 20.0);

    AnchorPane.setTopAnchor(comboBoxNutritions, 60.0);
    AnchorPane.setTopAnchor(comboBoxSigns, 60.0);
    AnchorPane.setTopAnchor(input, 60.0);
    AnchorPane.setTopAnchor(confirm, 60.0);
    AnchorPane.setTopAnchor(cancel, 60.0);
    AnchorPane.setTopAnchor(nulabel, 40.0);

  }

  private void setStageLayOut() {
    this.setTitle("Add Nutrient Rule");
    this.setScene(scene);

    this.setResizable(false);
    this.initModality(Modality.APPLICATION_MODAL);

  }

}
