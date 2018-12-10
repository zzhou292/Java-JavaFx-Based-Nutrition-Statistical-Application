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

/**
 * This stage provides a window for the user to add a nutrition rule 
 */
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

/**
 * Initialize each private field of the class
 */
  private void crateAddWindow() {
	  
	  //initialize comboboxes
    this.comboBoxNutritions = new ComboBox<String>();
    comboBoxNutritions.getItems().addAll("calories", "fat", "carbohydrate", "fiber", "protein");
    this.comboBoxSigns = new ComboBox<String>();
    comboBoxSigns.getItems().addAll("==", ">=", "<=");
    //initialize texfield
    this.input = new TextField();
    //initialize buttons
    Button confirm = new Button("Add Rule");
    Button cancel = new Button("Cancel");
    //initialize label
    Label nulabel = new Label("Nutrient                        comparator     value");
    //add all elements to the pane
    anchorPane.getChildren().addAll(nulabel, comboBoxNutritions, comboBoxSigns, input, confirm,
        cancel);
    //set the layout of the component
    setComponentLayout(nulabel, input, confirm, cancel);
    //set the actions of the buttons
    handleCancelConfirmEvent(cancel, confirm);
  }

/**
 * The method initializes the action of each button
 */
  private void handleCancelConfirmEvent(Button cancel, Button confirm) {

	  //if the cancel button is pressed, close the add nutrient stage
    cancel.setOnAction(e1 -> {
      this.close();
    });

    //if the confirm button is pressed, check validity of the inputs and add the nutrition rule to the query
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
        //close the stage
      }

    });

  }

/**
 * The method check whether all the inputs are valid
 */
  private boolean checkInputValidity() {
    Double value = null;
    
    //if any of the combobox is empty, an alert window will be displayed
    if (comboBoxNutritions.getValue() == null || comboBoxSigns.getValue() == null
        || input.getText().equals("")) {
      String message = "Make sure to choose all component and enter the value, please try again!";
      Alert alert = new Alert(AlertType.INFORMATION, message);
      this.close();
      alert.showAndWait().filter(response -> response == ButtonType.OK);
      //wait for the response of the ok button
      return false;

    }
    try {
      value = Double.valueOf(input.getText().trim());
    } 
    //the nutrition value is supposed to a valid numerical value
    //if the input can't be converted to a double value
    //catch the exception
    catch (Exception e) {
      String message = "The format of your value input is incorrect, please try again!";
      Alert alert = new Alert(AlertType.INFORMATION, message);
      //display an alert button to the user
      this.close();
      //close the addnutrient stage
      alert.showAndWait().filter(response -> response == ButtonType.OK);
      //wait for the response of the ok button
      return false;
      //return false
    }
    
    //the nutrient value should be positive
    //if a negative value has been detected
    //directly return false
    if (value < 0.0) {
      String message = "The input of the nutrient can not be negative, please try again!";
      Alert alert = new Alert(AlertType.INFORMATION, message);
      //display an alert window to the user
      this.close();
      //close the addnutrirnt stage
      alert.showAndWait().filter(response -> response == ButtonType.OK);
      //wait for the response of the ok button
      return false;
      //return false
    }

    	//if all tests are passes, return true
    return true;
  }

  /**
   * The method creates a new nutrition rule to the query list
   */
  private void addNewRule(String buffer) {
    HBox newRuleBox = new HBox();	//create a new default HBox layout
    Button remove = new Button("remove");	
    TextField newRule = new TextField(buffer);	
    Tooltip tip = new Tooltip();
    tip.setText(buffer);
    //set the properties of the rule display frame
    newRule.setTooltip(tip);
    newRule.setPrefWidth(200.0);
    newRule.setEditable(false);
    newRuleBox.getChildren().addAll(newRule, remove);

    //add the query rule to the data list
    rules.add(buffer);
    queryRuleListView.getItems().add(newRuleBox);
    queryRuleListView.refresh();

    //if the remove button has been pressed, the 
    remove.setOnAction(event -> {
      String message = "Confirm to remove nutrient Rule: " + buffer;
      message += "Attention: the food list will not automatically return to"
          + " the original food list. To do so please click on 'unDo All Filters' button";
      Alert alert = new Alert(AlertType.CONFIRMATION, message);
      //an alert window will be displayed
      alert.showAndWait().filter(new Predicate<ButtonType>() {
        @Override
        public boolean test(ButtonType t) {
          if (t.getButtonData().isCancelButton())
            return true;
          else {
        	  //remove the rule from the query and update datalist
            rules.remove(buffer);
            queryRuleListView.getItems().remove(newRuleBox);
            queryRuleListView.refresh();
            return false;
            //return false indicator
          }
        }
      });

    });
  }

  /**
   * The method sets the position of each element
   */
  private void setComponentLayout(Label nulabel, TextField input, Button confirm, Button cancel) {
	  //set the x position of each element
    AnchorPane.setLeftAnchor(comboBoxNutritions, 20.0);
    AnchorPane.setLeftAnchor(comboBoxSigns, 160.0);
    AnchorPane.setLeftAnchor(input, 240.0);
    AnchorPane.setLeftAnchor(confirm, 420.0);
    AnchorPane.setLeftAnchor(cancel, 500.0);
    AnchorPane.setLeftAnchor(nulabel, 20.0);

    //set the y position of each element
    AnchorPane.setTopAnchor(comboBoxNutritions, 60.0);
    AnchorPane.setTopAnchor(comboBoxSigns, 60.0);
    AnchorPane.setTopAnchor(input, 60.0);
    AnchorPane.setTopAnchor(confirm, 60.0);
    AnchorPane.setTopAnchor(cancel, 60.0);
    AnchorPane.setTopAnchor(nulabel, 40.0);

  }

  /**
   * The methods set the properties of the stage
   */
  private void setStageLayOut() {
	  //set the title of the stage
    this.setTitle("Add Nutrient Rule");
    //set the scene
    this.setScene(scene);

    this.setResizable(false);
    //fix the size of the stage
    this.initModality(Modality.APPLICATION_MODAL);
    //set the modality

  }

}
