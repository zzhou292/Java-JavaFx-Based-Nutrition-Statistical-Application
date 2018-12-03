package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AddNutrientStage extends Stage {
  private AnchorPane anchorPane;
  private Scene scene;
  private TextField nutrientRuleText;

  public AddNutrientStage() {
    this.anchorPane = new AnchorPane();
    this.scene = new Scene(anchorPane, 600, 120);
    setLayOut();
    crateAddWindow();

  }

  private void crateAddWindow() {
    ObservableList<String> nutritions =
        FXCollections.observableArrayList("Fiber", "Protein", "Fat", "Calories", "Carbohydrate");
    final ComboBox comboBoxNutritions = new ComboBox(nutritions);

    ObservableList<String> signs = FXCollections.observableArrayList("=", ">=", "<=");
    final ComboBox comboBoxSigns = new ComboBox(signs);

    TextField input = new TextField();

    Button confirm = new Button("Add Rule");

    Button cancel = new Button("Cancel");
    Label nulabel = new Label("Nutrient                        comparator     value");
    anchorPane.getChildren().addAll(nulabel, comboBoxNutritions, comboBoxSigns, input, confirm,
        cancel);

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



    cancel.setOnAction(e1 -> {
      this.close();

    });

    confirm.setOnAction(e1 -> {
      String buffer = "";
      Double value = null;
      // exception handling
      if (comboBoxNutritions.getValue() == null || comboBoxSigns.getValue() == null
          || input.getText().equals("")) {
        String message = "Make sure to choose all component and input value, please try again!";
        Alert alert = new Alert(AlertType.INFORMATION, message);
        this.close();
        alert.showAndWait().filter(response -> response == ButtonType.OK);
        return;

      }
      try {
        value = Double.valueOf(input.getText().trim());
      } catch (Exception e) {
        String message = "The format of your value input is incorrect, please try again!";
        Alert alert = new Alert(AlertType.INFORMATION, message);
        this.close();
        alert.showAndWait().filter(response -> response == ButtonType.OK);
        return;
      }
      if (value < 0.0) {
        String message = "The input of the nutrient can not be negative, please try again!";
        Alert alert = new Alert(AlertType.INFORMATION, message);
        this.close();
        alert.showAndWait().filter(response -> response == ButtonType.OK);
        return;
      }



      // processing input
      buffer = buffer + comboBoxNutritions.getValue();
      buffer = buffer + comboBoxSigns.getValue();
      buffer = buffer + input.getText().trim();
      System.out.println(buffer);
      this.close();

    });

    anchorPane.getChildren().addAll();

  }

  private void setLayOut() {
    this.setTitle("Add Nutrient Rule");
    this.setScene(scene);

    this.setResizable(false);
    this.initModality(Modality.APPLICATION_MODAL);

  }

}
