/**
 * Filename: SetNameRuleStage.java
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

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;


/**
 * This stage provides a window to allow user to add a name search rule
 * 
 * @author Meng, Zhou, Zou, Fan, Higgins
 */
public class SetNameRuleStage extends Stage {

  private TextField nameRuleField;
  private AnchorPane ap1;
  private Scene sc;
  private TextField textField;

  /**
   * The constructor creates the default stage layout, size, and is in charge of calling private
   * helper method
   * 
   * @param nameRuleField
   */
  public SetNameRuleStage(TextField nameRuleField) {

    this.nameRuleField = nameRuleField;
    this.ap1 = new AnchorPane();
    this.sc = new Scene(ap1, 300, 80);
    createSetNameWindow(); // call helper method to set properties of the window
    setLayout(); // call helper method to set layout of the stage
  }

  /**
   * The method creates required elements and add elements to the layout for display
   */
  private void createSetNameWindow() {
    this.textField = new TextField(); // the input field for user to input the name of the target
                                      // food
    AnchorPane.setLeftAnchor(textField, 35.0); // fix the element
    AnchorPane.setTopAnchor(textField, 25.0);
    textField.setPrefWidth(200.0);
    Button setNameRule = new Button("Set Name Rule"); // create a confirm button to submit search
                                                      // requirement
    AnchorPane.setLeftAnchor(setNameRule, 35.0); // fix the element
    AnchorPane.setTopAnchor(setNameRule, 55.0);
    Button cancel = new Button("cancel"); // create a cancel button to allow the user to cancel the
                                          // input
    AnchorPane.setLeftAnchor(cancel, 240.0); // fix the element
    AnchorPane.setTopAnchor(cancel, 55.0);
    handleButtonEvent(setNameRule, cancel); // call private helper method to set actions of buttons

    ap1.getChildren().addAll(textField, setNameRule, cancel);// add all elements to the layout

  }


  /**
   * This method sets actions which will be executed if setNameRule or cancel button is pressed
   */
  private void handleButtonEvent(Button setNameRule, Button cancel) {
    setNameRule.setOnAction(e1 -> {
      String searchinput = textField.getText().trim();
      // get Text
      // exception handling - if there's no input detected
      if (searchinput.equals("") == false) {
        nameRuleField.setText(searchinput);
        Tooltip tip = new Tooltip(searchinput);
        nameRuleField.setTooltip(tip);
        // confirm the input
        this.close();
        // close the stage
      } else {
        // the input is empty, send an alert message and an alert window to the user
        String message = "The name rule you entered is empty, please try again!";
        Alert alert = new Alert(AlertType.INFORMATION, message);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait().filter(response -> response == ButtonType.OK);
        // wait for the response of ok button
        this.close();
        // close the stage
      }

    });

    cancel.setOnAction(e1 -> {
      this.close();
      // if cancel button is pressed, close the window
    });

  }

  /**
   * The method sets properties of the stage
   */
  private void setLayout() {
    this.setTitle("Set Name Rule"); // set title
    this.setScene(sc); // set Scene
    this.setResizable(false); // fix the size of the stage
    this.initModality(Modality.APPLICATION_MODAL); // set modality of the stage

  }


}
