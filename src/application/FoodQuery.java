/**
 * 
 */
package application;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


/**
 * @author admin
 *
 */
public class FoodQuery extends VBox {

  private ListView<HBox> foodlist;
  private List<String> rules;
  private MenuBar menuBar;
  private Label label;

  public FoodQuery() {
	    this.label = new Label("FoodQuery Rules");
	    this.rules = new ArrayList<String>();
	    this.menuBar = new MenuBar();

	    this.foodlist = new ListView<HBox>();
	    createMenubar();
	    boxAdjustment();

  }

  public FoodQuery(String filePath) {
    this.foodlist = new ListView<HBox>();
   
    this.menuBar = new MenuBar();
    this.label = new Label("FoodQuery Rules");
    this.rules = new ArrayList<String>();
    createMenubar();
    boxAdjustment();
    
  }

  private void boxAdjustment() {

    this.getChildren().addAll(this.label, this.menuBar, this.foodlist);
    VBox.setMargin(label, new Insets(0, 0, 0, 130));
    this.setPrefWidth(370.0);
    this.setStyle("-fx-background-color:#BFEFFF");

  }

  private void createMenubar() {
	MenuItem add = new MenuItem("Add Rule");
	MenuItem search = new MenuItem("Search");
	MenuItem clear = new MenuItem("Clear Rules");
	MenuItem apply = new MenuItem("Apply Rules");
    Menu operation = new Menu("Operation",null,add,search,clear,apply);
    menuBar.getMenus().addAll(operation);

  }

  public void updateList(String filePath) {
    
    }
  }


