package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class test {

  public static void main(String[] args) {

    FoodData fd = new FoodData();
    fd.loadFoodItems("foodItems.csv");
    fd.saveFoodItems("results.csv");
    fd.print();
  }

}
