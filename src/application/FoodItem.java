/**
 * Filename: FoodItem.java
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class represents a food item with all its properties.
 * 
 * @author aka, Meng, Zhou, Zou, Fan, Higgins
 */
@SuppressWarnings("unused")
public class FoodItem {
  // The name of the food item.
  private String name;

  // The id of the food item.
  private String id;

  // Map of nutrients and value.
  private HashMap<String, Double> nutrients;

  /**
   * Constructor
   * 
   * @param name name of the food item
   * @param id unique id of the food item
   */
  public FoodItem(String id, String name) {
    this.name = name;
    this.id = id;
    this.nutrients = new HashMap<String, Double>();
  }

  /**
   * Gets the name of the food item
   * 
   * @return name of the food item
   */
  public String getName() {
    return this.name;
  }

  /**
   * Gets the unique id of the food item
   * 
   * @return id of the food item
   */
  public String getID() {

    return this.id;
  }

  /**
   * Gets the nutrients of the food item
   * 
   * @return nutrients of the food item
   */
  public HashMap<String, Double> getNutrients() {

    return this.nutrients;
  }

  /**
   * Adds a nutrient and its value to this food. If nutrient already exists, updates its value.
   */
  public void addNutrient(String name, double value) {
    this.nutrients.put(name, value);
  }

  /**
   * Returns the value of the given nutrient for this food item. If not present, then returns 0.
   */
  public double getNutrientValue(String name) {
    return this.nutrients.get(name);
  }



}
