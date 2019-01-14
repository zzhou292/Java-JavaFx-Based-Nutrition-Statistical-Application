/**
 * Filename: FoodQuery.java
 * 
 * Project: team project P5
 * 
 * Authors: Zhikang Meng, Jason ZHOU, Kejia Fan, James Higgins,YULU Zou
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
import java.util.List;
import java.util.function.Predicate;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * This is the food query class. This displays the food query rules. And it
 * handles the operations to filter foods by the name rules and add new rules to
 * this query. It also supports the operation to return to original food list
 * and clear rules.
 * 
 * @author Meng, Zhou, Zou, Fan, Higgins
 *
 */
public class FoodQuery extends VBox {

	private ListView<HBox> queryRuleListView;// list view of query rules
	private List<String> rules;// stores query rules
	// menu button and label
	private MenuBar menuBar;
	private Label label;
	private FoodData foodData;// reference to the food data the stores food info
	private List<FoodItem> queryFoodList;// filtered food list
	// name rule boxes and fields
	private HBox nameRuleBox;
	private TextField nameRuleField;
	private FoodList foodList;// reference to the food list

	/**
	 * This represents the public constructor of the food query
	 * 
	 * @param foodList
	 *            reference to the food list
	 */
	public FoodQuery(FoodList foodList) {
		this.label = new Label("FoodQuery Rules");
		this.foodList = foodList;
		nameRuleField = new TextField();
		this.rules = new ArrayList<String>();
		this.menuBar = new MenuBar();
		this.foodData = new FoodData();
		queryFoodList = new ArrayList<FoodItem>();
		this.queryRuleListView = new ListView<HBox>();
		nameRuleBox = new HBox();
		crateNameRuleBox();
		createMenubar();
		boxAdjustment();

	}

	/**
	 * This create the name rule HBox
	 */
	private void crateNameRuleBox() {
		// set up the label and clear button
		Label nameRuleLabel = new Label("Name Rule: ");
		Button clear = new Button("clear");
		nameRuleField.setEditable(false);
		nameRuleBox.getChildren().addAll(nameRuleLabel, nameRuleField, clear);
		handleNameRuleClear(clear);
	}

	/**
	 * This handle the clear button event of name rule box
	 * 
	 * @param clear
	 *            This represents the clear button
	 */
	private void handleNameRuleClear(Button clear) {
		clear.setOnAction(e1 -> {
			// display message to let user confirm to clear this name rule
			String message = "Confirm to clear this name rule\n";
			Alert alert = new Alert(AlertType.CONFIRMATION, message);
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.showAndWait().filter(new Predicate<ButtonType>() {
				@Override
				public boolean test(ButtonType t) {
					if (t.getButtonData().isCancelButton())
						return true;// user choose to cancel, just return
					else {
						// otherwise clear this name rule by setting its text to empty
						nameRuleField.setText("");
						Tooltip tip = new Tooltip("");
						nameRuleField.setTooltip(tip);
						return false;
					}
				}
			});
		});
	}

	/**
	 * This adjusts the lay out of this food query box
	 */
	private void boxAdjustment() {
		// add all components to the box
		this.getChildren().addAll(this.label, this.menuBar, this.nameRuleBox, this.queryRuleListView);
		VBox.setMargin(label, new Insets(0, 0, 0, 130));// set label to the center
		// set the width of the box and the back ground color
		this.setPrefWidth(400.0);
		this.setStyle("-fx-background-color:#BFEFFF");
	}

	/**
	 * This creates the menu bar
	 */
	private void createMenubar() {
		// crates menu items of all functional components
		MenuItem addNutrientRule = new MenuItem("Add Nutrient Rule");
		MenuItem setNameRule = new MenuItem("Set Name Rule");
		MenuItem clear = new MenuItem("Clear All Rules");
		MenuItem filterByName = new MenuItem("Filter Only by Name");
		MenuItem filterByNutrient = new MenuItem("Filter Only By Nutrient");
		MenuItem filterByAllRules = new MenuItem("Filter by All Rules");
		MenuItem unDoFilters = new MenuItem("Return Original FoodList");
		MenuItem tips = new MenuItem("Tip");
		// add them to the operation menu and menu bar
		Menu operation = new Menu("Operation", null, addNutrientRule, setNameRule, filterByName, filterByNutrient,
				filterByAllRules, unDoFilters, clear, tips);
		menuBar.getMenus().addAll(operation);
		// handle the event of each components
		handleClearEvent(clear);
		handleAddRuleEvent(addNutrientRule, setNameRule);
		handleNameFilterEvent(filterByName);
		handleUndoEvent(unDoFilters);
		handleNutrientEvent(filterByNutrient);
		handleApplyAllRuleEvent(filterByAllRules);
		handleTipEvent(tips);
	}

	/**
	 * Handle the tips event
	 * 
	 * @param tips
	 *            This represents tips MenuItem
	 */
	private void handleTipEvent(MenuItem tips) {
		tips.setOnAction(e1 -> {
			// display the tip info to the user
			String message = "Tip: Can't view the full info of name rules or nutrients rules?"
					+ " Hover your mouse over it for few seconds and it will show up.";
			Alert alert = new Alert(AlertType.INFORMATION, message);
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.showAndWait().filter(response -> response == ButtonType.OK);
		});

	}

	/**
	 * Handle the filter by all rules event
	 * 
	 * @param filterByAllRules
	 *            This represents filterByAllRules MenuItem
	 */
	private void handleApplyAllRuleEvent(MenuItem filterByAllRules) {
		filterByAllRules.setOnAction(rule -> {
			// if there is either no name rule or nutrient rule
			if (rules.size() == 0 || nameRuleField.getText().equals("")) {
				// display the warning message
				String message = "Please Add a Nutrient rule and set a name rule before apply it!";
				Alert alert = new Alert(AlertType.INFORMATION, message);
				alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
				alert.showAndWait().filter(response -> response == ButtonType.OK);
			} else {
				// otherwise filter the food list by both name rule and nutrient rules
				List<FoodItem> NameFiltered = foodData.filterByName(nameRuleField.getText());
				List<FoodItem> NutriFiltered = foodData.filterByNutrients(rules);
				// get the intersection of food lists applying both rules and refresh food list
				// view
				List<FoodItem> interSection = getIntersection(NameFiltered, NutriFiltered);
				foodList.queryOnShown(interSection);
			}
		});

	}

	/**
	 * This returns the intersection of two lists
	 * 
	 * @param nameFiltered
	 *            Food list of applying name rule
	 * @param nutriFiltered
	 *            Food list of applying nutrient rules
	 * @return This returns the intersection of two list
	 */
	private List<FoodItem> getIntersection(List<FoodItem> nameFiltered, List<FoodItem> nutriFiltered) {
		List<FoodItem> interset = new ArrayList<FoodItem>();// list to record the intersection
		for (FoodItem fi : nameFiltered) {
			// if both lists contains the same food element, add it to the intersection list
			if (nutriFiltered.contains(fi)) {
				interset.add(fi);
			}
		}
		return interset;
	}

	/**
	 * This handles the filterByNutrient event
	 * 
	 * @param filterByNutrient
	 *            MenuItem of filterByNutrient)
	 */
	private void handleNutrientEvent(MenuItem filterByNutrient) {
		filterByNutrient.setOnAction(rule -> {
			if (rules.size() == 0) {
				// if the nutrient rules is empty, display the warning message
				String message = "Please Add a Nutrient rule before apply it!";
				Alert alert = new Alert(AlertType.INFORMATION, message);
				alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
				alert.showAndWait().filter(response -> response == ButtonType.OK);
			} else {
				// otherwise filter the food items by these rules and refresh food list view
				queryFoodList = foodData.filterByNutrients(rules);
				foodList.queryOnShown(queryFoodList);
			}
		});

	}

	/**
	 * This handles the unDoFilters event
	 * 
	 * @param unDoFilters
	 *            the MenuItem of unDoFilters
	 */
	private void handleUndoEvent(MenuItem unDoFilters) {
		unDoFilters.setOnAction(rule -> {
			// this returns the food list to original food list
			foodList.noFilterOnShown();
		});
	}

	/**
	 * This handles the filter by name event
	 * 
	 * @param filterByName
	 *            the MenuItem filterByName)
	 */
	private void handleNameFilterEvent(MenuItem filterByName) {
		filterByName.setOnAction(rule -> {
			if (nameRuleField.getText().equals("")) {
				// if the name rule is empty, display warning message
				String message = "Please Set a name rule before apply it!";
				Alert alert = new Alert(AlertType.INFORMATION, message);
				alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
				alert.showAndWait().filter(response -> response == ButtonType.OK);
			} else {
				// otherwise filter the food list by this name rule and refresh the list view
				queryFoodList = foodData.filterByName(nameRuleField.getText());
				foodList.queryOnShown(queryFoodList);
			}
		});

	}

	/**
	 * This handles the events to add new rules
	 * 
	 * @param addNutrientRule
	 *            the MenuItem addNutrientRule
	 * @param setNameRule
	 *            the MenuItem setNameRule
	 */
	private void handleAddRuleEvent(MenuItem addNutrientRule, MenuItem setNameRule) {
		addNutrientRule.setOnAction(rule -> {
			// show the add nutrient rule window to process
			AddNutrientStage ans = new AddNutrientStage(queryRuleListView, rules);
			ans.show();
		});
		setNameRule.setOnAction(searchnames -> {
			// show the add name rule window to process
			SetNameRuleStage setNameRuleStage = new SetNameRuleStage(nameRuleField);
			setNameRuleStage.show();
		});
	}

	/**
	 * This handles the clear event
	 * 
	 * @param clear
	 *            this represents the clear menu item
	 */
	private void handleClearEvent(MenuItem clear) {
		clear.setOnAction(e1 -> {
			// display the confirm to clear rule message
			String message = "Confirm to clear all query rules!\n";
			Alert alert = new Alert(AlertType.CONFIRMATION, message);
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.showAndWait().filter(new Predicate<ButtonType>() {
				@Override
				public boolean test(ButtonType t) {
					if (t.getButtonData().isCancelButton())
						return true;// user choose to cancel, no performance and return
					else {// otherwise clear all nutrient rules and the name rule
						queryRuleListView.getItems().clear();
						rules = new ArrayList<String>();
						nameRuleField.setText("");
						Tooltip tip = new Tooltip("");
						nameRuleField.setTooltip(tip);
						return false;
					}
				}
			});
		});
	}

	/**
	 * This updates the food Data of this food query
	 * 
	 * @param foodData
	 *            This represents a food Data instance stores the food items info
	 */
	public void updateFoodData(FoodData foodData) {
		this.foodData = foodData;
	}
}
