package application;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AddNutrientStage extends Stage {
  private AnchorPane anchorPane;
  private Scene scene;
  private TextField nutrientRuleText;

  public AddNutrientStage() {
    this.anchorPane = new AnchorPane();
    this.scene = new Scene(anchorPane, 300, 80);
    setLayOut();
    crateAddWindow();

  }

  private void crateAddWindow() {
    this.nutrientRuleText = new TextField();
    AnchorPane.setLeftAnchor(nutrientRuleText, 35.0);
    AnchorPane.setTopAnchor(nutrientRuleText, 25.0);
    Button addNutrientRuleButton = new Button("Add Nutrient Rule");
    AnchorPane.setLeftAnchor(addNutrientRuleButton, 35.0);
    AnchorPane.setTopAnchor(addNutrientRuleButton, 55.0);
    addNutrientRuleButton.setOnAction(e1 -> {
      String ruleinput = nutrientRuleText.getText();
      // get Text
      if (ruleinput.equals("") == false) {
        System.out.println(ruleinput);
        // do sth
        this.close();
      } else {
        this.close();
      }
    });

    anchorPane.getChildren().addAll(nutrientRuleText, addNutrientRuleButton);

  }

  private void setLayOut() {
    this.setTitle("Add Nutrient Rule");
    this.setScene(scene);
    
    this.setResizable(false);
    this.initModality(Modality.APPLICATION_MODAL);

  }

}
