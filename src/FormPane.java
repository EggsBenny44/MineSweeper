import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

class FormPane extends VBox {
	private HeaderPane headerPain;
	private GamePane gridPane;
	private boolean hasStarted;
	private int level = 0;
	private int colMaxCount;
	private int rowMaxCount;
	private int bombsCount;

	public FormPane(int level) {		
		super();
		this.hasStarted = false;
		this.level = level;
		setGridSize();
		setSize();
	}
	
	public void setChildren() {
		headerPain = new HeaderPane(rowMaxCount, colMaxCount, bombsCount);
		setMargin(headerPain,new Insets(10, 10, 10, 10));
		gridPane = new GamePane(rowMaxCount, colMaxCount, bombsCount);
		setMargin(gridPane,new Insets(5, 10, 10, 10));
		getChildren().addAll(headerPain, gridPane);
		setStyle("-fx-background-color: linear-gradient(#D5D5D5,#EEEEEE); -fx-border-width: 2;  -fx-border-insets: 1; -fx-border-color: white #B8B8B8 #B8B8B8 white; -fx-border-style: solid inside line-join miter;");		
	}
	public void changeLevel(int level) {
		this.level = level;
		hasStarted = false;
		getChildren().remove(2);
		getChildren().remove(1);
		setGridSize();
		setSize();
		setChildren();	
	}
	
	private void setGridSize() {
		switch (level) {
		case 1 : 
			this.colMaxCount = 8;
			this.rowMaxCount = 8;
			this.bombsCount = 10;
			break;
		case 2 : 
			this.colMaxCount = 16;
			this.rowMaxCount = 16;
			this.bombsCount = 40;
			break;
		case 3 : 
			this.colMaxCount = 16;
			this.rowMaxCount = 32;
			this.bombsCount = 99;
			break;
		}
	}
	private void setSize() {
		switch (level) {
		case 1 : 
			setMaxSize(357.0, 279.0);
			setMinSize(357.0, 279.0);
			setPrefSize(357.0, 279.0);
			break;
		case 2 : 
			setMinSize(527.0, 605.0);
			setMaxSize(527.0, 605.0);
			setPrefSize(527.0, 605.0);
			break;
		case 3 : 
			setMinSize(1023.0, 605.0);
			setMaxSize(1023.0, 605.0);
			setPrefSize(1023.0, 605.0);
			break;
		}		
	}
	public void play(int index) {
		if (	hasStarted) return;
		hasStarted = true;
		headerPain.play();
		gridPane.play(index);
	}

	public void play() {
		headerPain.play();
	}
	
	public void restart() {
		hasStarted = false;
		headerPain.restart();
		gridPane.restart();	

	}
	public void gameOver() {
		headerPain.dead();
		gridPane.gameOver();	
	}
	public void countUp() {
		headerPain.countUp();
		if (headerPain.hasWin()) {
			gridPane.win();
		}
	}
	public void showHighScore() {
		HighScores highScores = new HighScores();
		ArrayList<Record> list = highScores.getHighScoreList();
		ButtonType reset = new ButtonType("Reset Scores", ButtonData.OTHER);
		Alert alert = new Alert(AlertType.NONE, "", ButtonType.OK, reset);
		alert.setTitle("Fastest Mine Sweepers");
		alert.getDialogPane().setHeaderText("Personal Best Times");
		ImageView imageView = new ImageView(new Image("file:res/face-win.png"));
		imageView.setFitWidth(48.0);
		imageView.setFitHeight(48.0);
		alert.getDialogPane().setGraphic(imageView);
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(30);
		grid.setVgap(1);
		grid.setPadding(new Insets(10, 30, 10, 30));
		for (int i = 0; i <  list.size(); i++) {			
			grid.add(new Label(list.get(i).getLevelName()), 0, i);
			grid.add(new Label(list.get(i).getTime() + " seconds"), 1, i);
			grid.add(new Label(list.get(i).getName()), 2, i);
		}
		alert.getDialogPane().setContent(grid);
		alert.showAndWait()
	      .filter(response -> response == reset)
	      .ifPresent(response -> clearHighScore());

	}

	private void clearHighScore() {
		HighScores highScores = new HighScores();
		highScores.clearRecords();
	}
	private Record showConfirmDialog(int level, int time) {
		Record record = new Record(level, time, "");
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Fastest Mine Sweepers");
		dialog.getDialogPane()
				.setHeaderText("You have the fastest time for " + record.getLevelName() +" level.\nPlease enter your name.");
		ImageView imageView = new ImageView(new Image("file:res/face-smile.png"));
		imageView.setFitWidth(40.0);
		imageView.setFitHeight(40.0);
		dialog.getDialogPane().setGraphic(imageView);
		String result = dialog.showAndWait().orElse("");	 
		record.setName(result);
		return record;
	}
	public void setHighScore() {
		HighScores highScores = new HighScores();
		ArrayList<Record> list = highScores.getHighScoreList();
		Record newRecord = null;
		Record high = highScores.getHighScore(level);
		if (high == null || high.getTime() >= headerPain.getSeconds()) {
			newRecord = showConfirmDialog(level, headerPain.getSeconds());
			if (!"".equals(newRecord.getName())) {
				if (high == null) 
					list.add(newRecord);
				else 
					list.set(high.getIndex(), newRecord);
				
				highScores.save(list);
				showHighScore();		
			}				
		}
	}
	public boolean hasWin() {
		return headerPain.hasWin();
	}
	public void mousePress() {
		headerPain.mousePress();
	}

	public void mousReleasee() {
		headerPain.mouseRelease();
	}

	public boolean isGuess(int guessType) {
		if (guessType == MinesButton.GUESS_TYPE_SELECT) {
			if (headerPain.getGessCount() == bombsCount) return false;
			headerPain.countDownBoms();
		} else {
			if (headerPain.getGessCount() == 0) return false;
			headerPain.countUpBoms();
		}
		return true;
	}
}
