package application;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SetNameRuleStage extends Stage {

  private TextField nameRuleField;
  private AnchorPane ap1;
  private Scene sc;
  private TextField textField;

  public SetNameRuleStage(TextField nameRuleField) {

    this.nameRuleField = nameRuleField;
    this.ap1 = new AnchorPane();
    this.sc = new Scene(ap1, 300, 80);
    createSetNameWindow();
    setLayout();
  }

  private void createSetNameWindow() {
    this.textField = new TextField();
    AnchorPane.setLeftAnchor(textField, 35.0);
    AnchorPane.setTopAnchor(textField, 25.0);
    textField.setPrefWidth(200.0);
    Button setNameRule = new Button("Set Name Rule");
    AnchorPane.setLeftAnchor(setNameRule, 35.0);
    AnchorPane.setTopAnchor(setNameRule, 55.0);
    Button cancel = new Button("cancel");
    AnchorPane.setLeftAnchor(cancel, 240.0);
    AnchorPane.setTopAnchor(cancel, 55.0);
    handleButtonEvent(setNameRule, cancel);

    ap1.getChildren().addAll(textField, setNameRule, cancel);

  }

  private void handleButtonEvent(Button setNameRule, Button cancel) {
    setNameRule.setOnAction(e1 -> {
      String searchinput = textField.getText().trim();
      // get Text
      if (searchinput.equals("") == false) {

        nameRuleField.setText(searchinput);
        this.close();
      } else {
        String message = "The name rule you entered is empty, please try again!";
        Alert alert = new Alert(AlertType.INFORMATION, message);
        alert.showAndWait().filter(response -> response == ButtonType.OK);
        this.close();
      }

    });

    cancel.setOnAction(e1 -> {
      this.close();
    });

  }

  private void setLayout() {
    this.setTitle("Set Name Rule");
    this.setScene(sc);
    this.setResizable(false);
    this.initModality(Modality.APPLICATION_MODAL);

  }


}
