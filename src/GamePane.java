import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;

class GamePane extends GridPane {	
	private int colMaxCount = 0;
	private int rowMaxCount = 0;
	private int bombsCount = 0;
	private ArrayList<Integer> bombs; 

	public GamePane(int colIndex, int rowIndex, int bomsCount) {
		super();
		this.colMaxCount = colIndex;
		this.rowMaxCount = rowIndex;
		this.bombsCount = bomsCount;
		setPane();
	}
	
	public void play(int index) {
		bombs = setBoms(index);
		setMines();
	}
	public void restart() {
		getChildren().clear();
		setButtons();
	}
	
	private void setButtons() {
		MinesButton[][] buttons = new MinesButton[rowMaxCount][colMaxCount];
		for (int i = 0; i < rowMaxCount; i++) {
			for (int j = 0; j < colMaxCount; j++) {
				buttons[i][j] = new MinesButton();
				MinesButton minesButton = buttons[i][j];
				setMouseAction(minesButton);
				add(minesButton, j, i);
			}
		}
	}
	
	private void setMouseAction(MinesButton minesButton) {
		minesButton.setOnMousePressed(event -> {
			FormPane grandParent = (FormPane) minesButton.getParent().getParent();
			grandParent.mousePress();
		});
		minesButton.setOnMouseClicked(event -> {
			MouseButton button = event.getButton();						
			GamePane parent = (GamePane) minesButton.getParent();
			FormPane grandParent = (FormPane) parent.getParent();
			if (button == MouseButton.PRIMARY) {
				// left Click
				if (minesButton.getGraphic().equals(minesButton.imageView[2])) {
					grandParent.mousReleasee();
					return;
				}
				grandParent.play(getChildren().indexOf(minesButton));
				if (minesButton.buttonType == -1) {
					minesButton.setGraphic(minesButton.imageView[5]);
					grandParent.gameOver();
				} else {
					if (minesButton.isOpen() && minesButton.getButtonType() >= 1) {
						boolean hasBomb = parent.expandFlagArea(getChildren().indexOf(minesButton), minesButton.getButtonType());
						if (hasBomb) {
							grandParent.gameOver();
							return;
						}						
					} else {
						minesButton.open();
						if (minesButton.buttonType == 0) {
							parent.expand(getChildren().indexOf(minesButton));
						}
					}
					if (!grandParent.hasWin()) 
						grandParent.mousReleasee();
				}
			} else if (button == MouseButton.SECONDARY) {
				// Right Click
				grandParent.play();
				if (!minesButton.isOpen() || minesButton.isGuessed()) {
					if (minesButton.getGuessType() == MinesButton.GUESS_TYPE_NONE) {
						if (grandParent.isGuess(MinesButton.GUESS_TYPE_SELECT)) {
							minesButton.setGraphic(minesButton.imageView[2]);
							minesButton.setGuessType(MinesButton.GUESS_TYPE_SELECT);						
						}
					} else {
						if (grandParent.isGuess(MinesButton.GUESS_TYPE_NONE)) {
							minesButton.setGraphic(minesButton.imageView[0]);
							minesButton.setGuessType(MinesButton.GUESS_TYPE_NONE);		
						}
					}
				}
				grandParent.mousReleasee();
			}
		});
	}
	
	
	public void expand(int index) {
		openButton(index);
	}
	
	public boolean expandFlagArea(int index, int buttonType) {
		ArrayList<Integer> list = getCheckArea(index);
		list.sort(null);
		list.remove((Object)index);
		boolean hasBomb = false;
		int flagCount = 0;
		for (int i = 0; i < list.size(); i ++) {
			MinesButton b = (MinesButton) getChildren().get(list.get(i)); 
			if (b.getGraphic().equals(b.imageView[2])) {
				flagCount++;
			}
		}
		if (flagCount == buttonType) {
			for (int i = 0; i < list.size(); i ++) {
				if (i != index) {
					MinesButton b = (MinesButton) getChildren().get(list.get(i)); 
					if (!b.isOpen()) {
						if (b.buttonType == -1) {
							hasBomb = true;
							b.setGraphic(b.imageView[5]);
						} else {
							if (b.getButtonType() == 0) {
								openButton(index);
							} else {
								b.open();								
							}
						}
					}
				}
			}
		}
		return hasBomb;
	}

	private ArrayList<Integer> getCheckArea(int index) {
		ArrayList<Integer> buttonIndex = new ArrayList<Integer>();
		buttonIndex.add(index);
		if (index % colMaxCount != 0) {
			if (!(index >= 0 && index < colMaxCount)) {
				
				buttonIndex.add(index - colMaxCount - 1);
			}
			buttonIndex.add(index - 1);
			if (!(index >= rowMaxCount * colMaxCount - colMaxCount && index < rowMaxCount * colMaxCount)) {
				buttonIndex.add(index + colMaxCount - 1);
			}
		}
		if (index % colMaxCount != colMaxCount - 1) {
			if (!(index >= 0 && index < colMaxCount)) {
				buttonIndex.add(index - colMaxCount + 1);
			}
			buttonIndex.add(index + 1);
			if (!(index >= rowMaxCount * colMaxCount - colMaxCount && index < rowMaxCount * colMaxCount)) {
				buttonIndex.add(index + colMaxCount + 1);	
			}
		}
		if (!(index >= 0 && index < colMaxCount)) {
			buttonIndex.add(index - colMaxCount);
		}
		if (!(index >= rowMaxCount * colMaxCount - colMaxCount && index < rowMaxCount * colMaxCount)) {
			buttonIndex.add(index + colMaxCount);
		}
		return buttonIndex;		
	}
	
	private void openButton(int index) {
		ArrayList<Integer> buttonIndex = getCheckArea(index);
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		if (buttonIndex == null || buttonIndex.size() == 0)
			return;

		for (int i : buttonIndex) {
			if (i < 0 || rowMaxCount * colMaxCount > i) {
				MinesButton b = (MinesButton) getChildren().get(i);
				if (!b.isOpen()) {
					b.open();
					if (b.getButtonType() == 0) {
						indexes.add(i);
					}
				}
			}
		}
		if (indexes == null || indexes.size() == 0) {
			return;
		}
		for (int j = 0; j < indexes.size(); j++) {
			openButton(indexes.get(j));
		}
	}
	
	public void gameOver() {
		for (int bombIdx = 0; bombIdx < bombs.size(); bombIdx++) {
			MinesButton button = (MinesButton) getChildren().get(bombs.get(bombIdx));
			button.gameOver();
		}
		
		for (int i = 0; i < colMaxCount * rowMaxCount; i++) {
			MinesButton button = (MinesButton) getChildren().get(i);	
			button.miss();
			button.setOnMouseClicked(null);
			button.setOnMousePressed(null);
		}
	}
	
	public void win() {
		for (int bombIdx = 0; bombIdx < bombs.size(); bombIdx++) {
			MinesButton button = (MinesButton) getChildren().get(bombs.get(bombIdx));
			button.win();
		}
		for (int i = 0; i < colMaxCount * rowMaxCount; i++) {
			MinesButton button = (MinesButton) getChildren().get(i);	
			button.setOnMouseClicked(null);
			button.setOnMousePressed(null);
		}
	}
	private void setPane() {
		setAlignment(Pos.CENTER);
		setHgap(1);
		setVgap(1);
		setPadding(new Insets(1, 1, 1, 1));
		setButtons();
		autosize();
		setStyle("-fx-background-color: #BEBEBE;-fx-border-width: 2; -fx-border-color: #B8B8B8 white white #B8B8B8; -fx-border-style: solid inside line-join miter;");
		updateBounds();
	}
	
	private ArrayList<Integer> setBoms(int index) {
		ArrayList<Integer> bombsNum = new ArrayList<Integer>();
		ArrayList<Integer> avoidArea = getCheckArea(index);
		int i = 0;
		while (i < bombsCount) {
			int bomb = (int) (Math.random() * ((colMaxCount * rowMaxCount) - 1));
			if (!(bombsNum.contains(bomb) || avoidArea.contains(bomb))) {
				bombsNum.add(bomb);
				i++;
			}
		}
		bombsNum.sort(null);
		return bombsNum;
	}

	private void setMines() {
		for (int index : bombs) {
			MinesButton button = (MinesButton) getChildren().get(index);
			button.setButtonType(-1);
			if (index % colMaxCount != 0) {
				if (!(index >= 0 && index < colMaxCount)) {
					button = (MinesButton) getChildren().get(index - colMaxCount - 1);
					if (button.getButtonType() != -1) button.setButtonType(button.getButtonType() + 1);
				}
				button = (MinesButton) getChildren().get(index - 1);
				if (button.getButtonType() != -1) button.setButtonType(button.getButtonType() + 1);
				if (!(index >= rowMaxCount * colMaxCount - colMaxCount && index < rowMaxCount * colMaxCount)) {
					button = (MinesButton) getChildren().get(index + colMaxCount - 1);
					if (button.getButtonType() != -1) button.setButtonType(button.getButtonType() + 1);
				}
			}
			if (index % colMaxCount != colMaxCount - 1) {
				if (!(index >= 0 && index < colMaxCount)) {
					button = (MinesButton) getChildren().get(index - colMaxCount + 1);
					if (button.getButtonType() != -1) button.setButtonType(button.getButtonType() + 1);
				}
				button = (MinesButton) getChildren().get(index + 1);
				if (button.getButtonType() != -1) button.setButtonType(button.getButtonType() + 1);
				if (!(index >= rowMaxCount * colMaxCount - colMaxCount && index < rowMaxCount * colMaxCount)) {
					button = (MinesButton) getChildren().get(index + colMaxCount + 1);
					if (button.getButtonType() != -1) button.setButtonType(button.getButtonType() + 1);
				}
			}
			if (!(index >= 0 && index < colMaxCount)) {
				button = (MinesButton) getChildren().get(index - colMaxCount);
				if (button.getButtonType() != -1) button.setButtonType(button.getButtonType() + 1);
			}
			if (!(index >= rowMaxCount * colMaxCount - colMaxCount && index < rowMaxCount * colMaxCount)) {
				button = (MinesButton) getChildren().get(index + colMaxCount);
				if (button.getButtonType() != -1) button.setButtonType(button.getButtonType() + 1);
			}
		}
// ********* debug  ***************
		ObservableList<Node> list = getChildren();
		for (int i = 0; i < list.size(); i++) {
			MinesButton button = (MinesButton) getChildren().get(i);
			System.out.print(button.getButtonType() + "\t");
			if (i % colMaxCount == colMaxCount -1) {				
				System.out.println();
			}
		}
		System.out.println();

// ********* debug  ***************
	}
	

	// ******** debug ************
	public void test() {
		for (int bombIdx = 0; bombIdx < bombs.size(); bombIdx++) {
			MinesButton button = (MinesButton) getChildren().get(bombs.get(bombIdx));
			button.setGraphic(button.imageView[2]);
			button.setGuessType(MinesButton.GUESS_TYPE_SELECT);
		}
		for (int i = 0; i < colMaxCount * rowMaxCount; i++) {
			MinesButton button = (MinesButton) getChildren().get(i);
			if(!button.isOpen() && !button.getGraphic().equals(button.imageView[2])) {
				button.open();
			}
		}
	}
	// ******** debug ************
}
