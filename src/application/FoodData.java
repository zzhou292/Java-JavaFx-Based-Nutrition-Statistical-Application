/**
 * Filename: FoodData.java
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * This class represents the backend for managing all the operations associated with FoodItems
 * 
 * @author sapan (sapan@cs.wisc.edu), Meng, Zhou, Zou, Fan, Higgins
 */
public class FoodData implements FoodDataADT<FoodItem> {

  // List of all the food items.
  private List<FoodItem> foodItemList;

  // Map of nutrients and their corresponding index
  private HashMap<String, BPTree<Double, FoodItem>> indexes;


  /**
   * Public constructor
   */
  public FoodData() {
    this.foodItemList = new ArrayList<FoodItem>();
    this.indexes = new HashMap<String, BPTree<Double, FoodItem>>();
    // create BP trees for each nutrients
    BPTree<Double, FoodItem> calories = new BPTree<>(3);
    BPTree<Double, FoodItem> fat = new BPTree<>(3);
    BPTree<Double, FoodItem> carbohydrate = new BPTree<>(3);
    BPTree<Double, FoodItem> fiber = new BPTree<>(3);
    BPTree<Double, FoodItem> protein = new BPTree<>(3);
    // push nutrient BP Tress into the indexes hash table
    indexes.put("calories", calories);
    indexes.put("fat", fat);
    indexes.put("carbohydrate", carbohydrate);
    indexes.put("fiber", fiber);
    indexes.put("protein", protein);
  }



  /**
   * Loads the data in the .csv file
   * 
   * file format: <id1>,<name>,<nutrient1>,<value1>,<nutrient2>,<value2>,...
   * <id2>,<name>,<nutrient1>,<value1>,<nutrient2>,<value2>,...
   * 
   * Example:
   * 556540ff5d613c9d5f5935a9,Stewarts_PremiumDarkChocolatewithMintCookieCrunch,calories,280,fat,18,carbohydrate,34,fiber,3,protein,3
   * 
   * Note: 1. All the rows are in valid format. 2. All IDs are unique. 3. Names can be duplicate. 4.
   * All columns are strictly alphanumeric (a-zA-Z0-9_). 5. All food items will strictly contain 5
   * nutrients in the given order: calories,fat,carbohydrate,fiber,protein 6. Nutrients are
   * CASE-INSENSITIVE.
   * 
   * @param filePath path of the food item data file (e.g. folder1/subfolder1/.../foodItems.csv)
   */
  @Override
  public void loadFoodItems(String filePath) {
    // cratee a file and scanner object to extract info from file
    File file = new File(filePath);
    try {
      Scanner input = new Scanner(file);
      // keep iterating the file when there still has contents
      while (input.hasNextLine()) {
        String current = input.nextLine();
        String[] currentFood = current.split(",");// split current line by comma
        if (currentFood.length == 0)// no more food item to read, stop reading
          break;
        // create a food item instance to store current food
        // the first and second in the String array represents ID and name
        FoodItem foodItem = new FoodItem(currentFood[0], currentFood[1]);
        // iterating the nutrition info of current food item and store it
        for (int i = 2; i < currentFood.length; i = i + 2)
          foodItem.addNutrient(currentFood[i], Double.valueOf(currentFood[i + 1]));
        // add current item in the food list
        foodItemList.add(foodItem);
        // insert this food item into all of its nutrient trees
        HashMap<String, Double> nuTri = foodItem.getNutrients();
        indexes.get("calories").insert(nuTri.get("calories"), foodItem);
        indexes.get("fat").insert(nuTri.get("fat"), foodItem);
        indexes.get("carbohydrate").insert(nuTri.get("carbohydrate"), foodItem);
        indexes.get("fiber").insert(nuTri.get("fiber"), foodItem);
        indexes.get("protein").insert(nuTri.get("protein"), foodItem);
      }
      input.close();// close file
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }


  }

  /**
   * Gets all the food items that have name containing the substring.
   * 
   * Example: All FoodItem
   * 51c38f5d97c3e6d3d972f08a,Similac_FormulaSoyforDiarrheaReadytoFeed,calories,100,fat,0,carbohydrate,0,fiber,0,protein,3
   * 556540ff5d613c9d5f5935a9,Stewarts_PremiumDarkChocolatewithMintCookieCrunch,calories,280,fat,18,carbohydrate,34,fiber,3,protein,3
   * Substring: soy Filtered FoodItem
   * 51c38f5d97c3e6d3d972f08a,Similac_FormulaSoyforDiarrheaReadytoFeed,calories,100,fat,0,carbohydrate,0,fiber,0,protein,3
   * 
   * Note: 1. Matching should be CASE-INSENSITIVE. 2. The whole substring should be present in the
   * name of FoodItem object. 3. substring will be strictly alphanumeric (a-zA-Z0-9_)
   * 
   * @param substring substring to be searched
   * @return list of filtered food items; if no food item matched, return empty list
   */
  @Override
  public List<FoodItem> filterByName(String substring) {
    List<FoodItem> result = new ArrayList<FoodItem>();// list of filtered food items
    // for each item in this.fooditemlist
    // exams if its name contains this substring add it to the list
    for (FoodItem foodItem : foodItemList) {
      // to lower case and then compare to make it case in-sensitive
      if (foodItem.getName().toLowerCase().contains(substring.toLowerCase()))
        result.add(foodItem);

    }
    return result;
  }

  /**
   * Gets all the food items that fulfill ALL the provided rules
   *
   * Format of a rule: "<nutrient> <comparator> <value>"
   * 
   * Definition of a rule: A rule is a string which has three parts separated by a space: 1.
   * <nutrient>: Name of one of the 5 nutrients [CASE-INSENSITIVE] 2. <comparator>: One of the
   * following comparison operators: <=, >=, == 3. <value>: a double value
   * 
   * Note: 1. Multiple rules can contain the same nutrient. E.g. ["calories >= 50.0", "calories <=
   * 200.0", "fiber == 2.5"] 2. A FoodItemADT object MUST satisfy ALL the provided rules i to be
   * returned in the filtered list.
   *
   * @param rules list of rules
   * @return list of filtered food items; if no food item matched, return empty list
   */
  @Override
  public List<FoodItem> filterByNutrients(List<String> rules) {
    // if the rules is null or contains no rules, just return this foodItemList
    if (rules == null)
      return foodItemList;
    if (rules.size() == 0)
      return foodItemList;
    // if rules contains only one rule, apply it by performing a single range search
    if (rules.size() == 1) {
      String[] firstRule = rules.get(0).split(" ");
      return indexes.get(firstRule[0]).rangeSearch(Double.valueOf(firstRule[2]), firstRule[1]);
    }
    // have more than two rules
    // performing the first rule by range search
    List<FoodItem> result = new ArrayList<FoodItem>();
    String[] firstRule = rules.get(0).split(" ");
    result = indexes.get(firstRule[0]).rangeSearch(Double.valueOf(firstRule[2]), firstRule[1]);
    if (result.size() == 0)
      return result;// it the first rule gives empty list, return it directly
    // otherwise get the intersection of each rules
    for (int i = 1; i < rules.size(); i++) {
      // get the ith rule
      String[] currentRule = rules.get(i).split(" ");
      // get the intersection of this ith rule and its previous rule as the result
      List<FoodItem> currentList =
          indexes.get(currentRule[0]).rangeSearch(Double.valueOf(currentRule[2]), currentRule[1]);
      result = getIntersection(result, currentList);
      if (result.size() == 0)
        return result;// intersection gives a empty list, return directly
    }
    return result; // return the intersection of all rules

  }

  /**
   * This returns the intersection of two lists
   * 
   * @param result the result of the list
   * @param currentList a current read list to be intersected
   * @return This returns the intersection of two list
   */
  private List<FoodItem> getIntersection(List<FoodItem> result, List<FoodItem> currentList) {
    List<FoodItem> interset = new ArrayList<FoodItem>();// list to record the intersection
    // iterate through all food items in the result list
    for (FoodItem fi : result) {
      if (currentList.contains(fi)) {
        // if both lists contains the same food element, add it to the intersection list
        interset.add(fi);
      }
    }
    return interset;
  }



  /**
   * Adds a food item to the loaded data.
   * 
   * @param foodItem the food item instance to be added
   */
  @Override
  public void addFoodItem(FoodItem foodItem) {
    this.foodItemList.add(foodItem);
    // add the food item to all of its nutrient BP trees
    HashMap<String, Double> nuTri = foodItem.getNutrients();
    indexes.get("calories").insert(nuTri.get("calories"), foodItem);
    indexes.get("fat").insert(nuTri.get("fat"), foodItem);
    indexes.get("carbohydrate").insert(nuTri.get("carbohydrate"), foodItem);
    indexes.get("fiber").insert(nuTri.get("fiber"), foodItem);
    indexes.get("protein").insert(nuTri.get("protein"), foodItem);
  }

  /**
   * Gets the list of all food items.
   * 
   * @return list of FoodItem
   */
  @Override
  public List<FoodItem> getAllFoodItems() {

    return this.foodItemList;
  }

  /**
   * Save the list of food items in ascending order by name
   * 
   * @param filename name of the file where the data needs to be saved
   */
  @Override
  public void saveFoodItems(String filename) {
    // sort this food item list by create a anonymous comparator class
    Collections.sort(this.foodItemList, new Comparator<FoodItem>() {

      @Override
      public int compare(FoodItem o1, FoodItem o2) {
        // invoke the compare to method of a string since name is a string
        return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
      }

    });
    File file = new File(filename);
    try {
      // print the sorted list to new file by using print writer instance
      PrintWriter output = new PrintWriter(file);
      // for each item in the food item list
      // print them to the file
      // each component is still separated by the comma
      for (FoodItem foodItem : this.foodItemList) {
        output.print(foodItem.getID() + ",");
        output.print(foodItem.getName() + ",");
        // iterate the hash map of all nutritions
        HashMap<String, Double> nutrients = foodItem.getNutrients();
        for (String nutrientsName : nutrients.keySet()) {
          output.print(nutrientsName + ",");
          output.print(nutrients.get(nutrientsName) + ",");
        }
        output.println();
      }
      output.close();// close the file
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }



}
