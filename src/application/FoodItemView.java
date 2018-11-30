package application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class FoodItemView extends HBox {


  private Button remove;
  private FoodItem fooditem;

  public FoodItemView(FoodItem fooditem) {
    this.fooditem = fooditem;
    this.setSpacing(5.0);
    TextField foodItemName = new TextField(fooditem.getName());
    foodItemName.setPrefWidth(200.0);
    foodItemName.setEditable(false);
    remove = new Button("remove");
    this.getChildren().addAll(foodItemName, remove);
  }

  public HBox getView() {
    return this;
  }



  /**
   * @return the remove
   */
  public Button getRemove() {
    return remove;
  }

  /**
   * @return the fooditem
   */
  public FoodItem getFooditem() {
    return fooditem;
  }


}
