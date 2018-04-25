import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

class HeaderPane extends BorderPane {
	private SmileButton smileButton;
	private NumberBox bomsCountBox;
	private NumberBox timeBox;
	private int safeCount;
	private int rowMaxCount;
	private int colMaxCount;
	private int bombsCount;
	private Timeline timer;
	private int seconds;
	private boolean hasWin;

private int gessCount;
	public HeaderPane(int colIndex, int rowIndex, int bomsCount) {
		super();
		timer = null;
		this.colMaxCount = colIndex;
		this.rowMaxCount = rowIndex;
		this.bombsCount = bomsCount;
		hasWin = false;
		safeCount = 0;
		seconds = 0;
		gessCount = 0;
		setStyle("-fx-background-color: #BEBEBE;-fx-border-width: 2; -fx-border-color: #676767 white white #676767; -fx-border-style: solid inside line-join miter;");
		InnerShadow innerShadow = new InnerShadow();
		innerShadow.setOffsetX(-1); 
	    innerShadow.setOffsetY(-1); 
	    innerShadow.setBlurType(BlurType.GAUSSIAN);
	    innerShadow.setColor(Color.valueOf("#B6B6B6"));
	    	setEffect(innerShadow);
	    
		smileButton = new SmileButton();
		smileButton.setOnAction (e -> {
			FormPane parent = (FormPane) smileButton.getParent().getParent();
			parent.restart();
		});
		bomsCountBox = new NumberBox (Pos.CENTER_LEFT, bomsCount);
		timeBox = new NumberBox (Pos.CENTER_RIGHT, 0);
		timer = new Timeline(new KeyFrame(Duration.millis(1000), event -> {
			seconds++;
			timeBox.setNumber(seconds);
		}));
		timer.setCycleCount(Timeline.INDEFINITE);
		setCenter(smileButton);		
		setLeft(bomsCountBox);
		setRight(timeBox);
	}
	
	public void restart() {
		timer.stop();
		safeCount = 0;
		seconds = 0;
		gessCount = 0;
		hasWin = false;
		smileButton.smile();
		bomsCountBox.setNumber(bombsCount);
		timeBox.setNumber(0);
	}
	
	public void dead() {
		timer.stop();
		smileButton.dead();
	}

	public void play() {
		if (seconds != 0) return;
		timer.play();
	}

	public void mousePress() {
		smileButton.press();
	}
	
	public void mouseRelease() {
		smileButton.smile();
	}
	public void countUpBoms() {
		gessCount--;
		bomsCountBox.setNumber(bombsCount - gessCount);
	}
	public void countDownBoms() {
		gessCount++;
		bomsCountBox.setNumber(bombsCount - gessCount);
	}
	public void countUp() {
		safeCount++;
		if (safeCount == (colMaxCount * rowMaxCount) - bombsCount) {
			timer.stop();
			hasWin = true;
			smileButton.win();	
		}
	}

	public boolean hasWin() {
		return hasWin;
	}


	public int getGessCount() {
		return gessCount;
	}

	public int getSeconds() {
		return seconds;
	}
	

}
