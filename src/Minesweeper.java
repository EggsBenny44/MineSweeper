import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.stage.Stage;

public class Minesweeper extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage theStage) throws Exception {
		theStage.setTitle("MINESWEEPER");
		
		FormPane formPane = new FormPane(1);
		MenuBar menuBar = new MenuBar();
		Menu game = new Menu("Game");
		menuBar.getMenus().addAll(game);

		MenuItem begginer = new MenuItem("Begginer");
		begginer.setOnAction(e -> {	
			formPane.changeLevel(1);
			theStage.setMinHeight(379.0);
			theStage.setMinWidth(279.0);
			theStage.setMaxHeight(379.0);
			theStage.setMaxWidth(279.0);
			theStage.centerOnScreen();
		});		
		MenuItem intermediate = new MenuItem("Intermediate");
		intermediate.setOnAction(e -> {		
			formPane.changeLevel(2);
			theStage.setMinHeight(627.0);
			theStage.setMinWidth(527.0);
			theStage.setMaxHeight(627.0);
			theStage.setMaxWidth(527.0);
			theStage.centerOnScreen();
		});	
		MenuItem expert = new MenuItem("Expert");
		expert.setOnAction(e -> {			
			formPane.changeLevel(3);
			theStage.setMinHeight(627.0);
			theStage.setMinWidth(1023.0);
			theStage.setMaxHeight(627.0);
			theStage.setMaxWidth(1023.0);
			theStage.centerOnScreen();
		});	
		MenuItem personalBest = new MenuItem("Personal Best Time");
		personalBest.setOnAction(e -> {			
			formPane.showHighScore();	
		});	
		MenuItem exit = new MenuItem("Exit");
		exit.setOnAction(e -> {			
			theStage.close();	
		});	
		game.getItems().addAll(begginer, intermediate, expert, new SeparatorMenuItem(), personalBest, new SeparatorMenuItem(), exit);
		formPane.getChildren().add(menuBar);
		formPane.setChildren();

		theStage.setTitle("MINESWEEPER");
		Scene scene = new Scene(formPane);
		theStage.setMinHeight(379.0);
		theStage.setMinWidth(279.0);
		theStage.setMaxHeight(379.0);
		theStage.setMaxWidth(279.0);
		theStage.centerOnScreen();
		theStage.setResizable(true);
		theStage.setScene(scene);
		theStage.show();	
	}

}
