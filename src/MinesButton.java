import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

class MinesButton extends Button  {
	int imageCount = 5;
	ImageView[] imageView = null;
	static final double BUTTON_SIZE = 30;
	public static final int GUESS_TYPE_NONE = 0;
	public static final int GUESS_TYPE_SELECT = 1;
	int buttonType;
	int guessType = 0;

	public MinesButton() {
		super();		
		this.buttonType = 0;
		this.guessType = 0;
		setMinWidth(BUTTON_SIZE);
		setMaxWidth(BUTTON_SIZE);
		setMinHeight(BUTTON_SIZE);
		setMaxHeight(BUTTON_SIZE);
		setFocused(false);
		setFocusTraversable(false);
		imageView = new ImageView[6];
		imageView[0] = new ImageView(new Image("file:res/blank.gif"));
		imageView[0].setFitWidth(BUTTON_SIZE);
		imageView[0].setFitHeight(BUTTON_SIZE);
		imageView[1] = new ImageView(new Image("file:res/0.png"));
		imageView[2] = new ImageView(new Image("file:res/flag.png"));	
		imageView[3] = new ImageView(new Image("file:res/mine-misflagged.png"));				
		imageView[4] = new ImageView(new Image("file:res/mine-grey.png"));	
		imageView[5] = new ImageView(new Image("file:res/mine-red.png"));	
		setGraphic(imageView[0]);
	}
	
	public void gameOver() {
		ImageView image = (ImageView) getGraphic(); 
		if (image != null && image.equals(imageView[0])) {
			setGraphic(imageView[4]);
		}
	}
	public void miss() {
		if (guessType == GUESS_TYPE_SELECT && buttonType != -1) {
			setGraphic(imageView[3]);
		}
	}
	
	public void win() {
		setGraphic(imageView[2]);
	}
	public void open() {
		FormPane grandParent = (FormPane) getParent().getParent();
		if (!isOpen()) grandParent.countUp();			
		setGraphic(imageView[1]);
		if (grandParent.hasWin()) grandParent.setHighScore();

	}
	public boolean isGuessed() {
		return getGraphic().equals(imageView[2]);
	}
	public boolean isOpen() {
		return !(getGraphic().equals(imageView[0]));
	}
	public boolean isEmpty() {
		return !(getGraphic().equals(imageView[1]));
	}
	public int getButtonType() {
		return buttonType;
	}
	public void setButtonType(int buttonType) {
		this.buttonType = buttonType;
		if (buttonType >= 1 && buttonType <=9) 
			imageView[1] = new ImageView(new Image("file:res/" + buttonType + ".png"));
	}

	public int getGuessType() {
		return guessType;
	}

	public void setGuessType(int guessType) {
		this.guessType = guessType;
	}
	
}
