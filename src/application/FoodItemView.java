package application;


import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class FoodItemView extends HBox {



  private FoodItem fooditem;

  public FoodItemView(FoodItem fooditem) {
    this.fooditem = fooditem;
    this.setSpacing(5.0);
    TextField foodItemName = new TextField(fooditem.getName());
    foodItemName.setPrefWidth(200.0);
    foodItemName.setEditable(false);
    this.getChildren().add(foodItemName);
  }

  public HBox getView() {
    return this;
  }



  /**
   * @return the fooditem
   */
  public FoodItem getFooditem() {
    return fooditem;
  }


}
