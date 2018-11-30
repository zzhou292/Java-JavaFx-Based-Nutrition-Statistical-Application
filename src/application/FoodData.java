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
 * @author sapan (sapan@cs.wisc.edu)
 */
public class FoodData implements FoodDataADT<FoodItem> {

  // List of all the food items.
  private List<FoodItem> foodItemList;

  // Map of nutrients and their corresponding index
  @SuppressWarnings("unused")
  private HashMap<String, BPTree<Double, FoodItem>> indexes;


  /**
   * Public constructor
   */
  public FoodData() {
    this.foodItemList = new ArrayList<FoodItem>();
    this.indexes = new HashMap<String, BPTree<Double, FoodItem>>();
  }

  /**
   * Public constructor
   */
  public FoodData(List<FoodItem> foodItemList) {
    this.foodItemList = new ArrayList<FoodItem>();
    this.indexes = new HashMap<String, BPTree<Double, FoodItem>>();
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
      if (foodItem.getName().contains(substring))
        result.add(foodItem);
    }
    return result;
  }

  /*
   * (non-Javadoc)
   * 
   * @see skeleton.FoodDataADT#filterByNutrients(java.util.List)
   */
  @Override
  public List<FoodItem> filterByNutrients(List<String> rules) {
    // TODO : Complete
    return null;
  }

  /**
   * Adds a food item to the loaded data.
   * 
   * @param foodItem the food item instance to be added
   */
  @Override
  public void addFoodItem(FoodItem foodItem) {
    this.foodItemList.add(foodItem);
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
        return o1.getName().compareTo(o2.getName());
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

  public void print() {
    Collections.sort(this.foodItemList, new Comparator<FoodItem>() {

      @Override
      public int compare(FoodItem o1, FoodItem o2) {

        return o1.getName().compareTo(o2.getName());
      }

    });
    int count = 1;
    for (FoodItem ft : this.foodItemList) {
      System.out.print(count++ + ": ");
      System.out.print(ft.getID() + ",");
      System.out.print(ft.getName() + ",");
      HashMap<String, Double> nu = ft.getNutrients();
      for (String s : nu.keySet()) {
        System.out.print(s + ",");
        System.out.print(nu.get(s) + ",");
      }
      System.out.println();
    }

    System.out.println("end of file");
  }


}
