/**
 * Filename: FoodItemView.java
 * 
 * Project: team project P5
 * 
 * Authors: Debra Deppeler, Zhikang Meng, Jason ZHOU, Kejia Fan, James Higgins,YULU ZOU
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


import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;

/**
 * This represents a view of food item to be added into the list view
 * 
 * @author Meng, Zhou, Zou, Fan, Higgins
 *
 */
public class FoodItemView extends HBox {
  private FoodItem fooditem;// this represents the reference of the food item

  /**
   * public constructor
   * 
   * @param fooditem
   */
  public FoodItemView(FoodItem fooditem) {
    this.fooditem = fooditem;
    this.setSpacing(5.0);
    // add a text filed to show the name of the food
    TextField foodItemName = new TextField(fooditem.getName());
    Tooltip tip = new Tooltip();
    tip.setText(fooditem.getName());// a tool tip the show the full name of the food item
    foodItemName.setTooltip(tip);
    foodItemName.setPrefWidth(250.0);
    foodItemName.setEditable(false);// can't edit the display of food item
    this.getChildren().add(foodItemName);
  }

  /**
   * returns this food view instance
   * 
   * @return the reference of the HBox
   */
  public HBox getView() {
    return this;
  }



  /**
   * return the fooditem of this view instance
   * 
   * @return the fooditem of this view instance
   */
  public FoodItem getFooditem() {
    return fooditem;
  }


}
