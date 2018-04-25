import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;

class SmileButton extends Button {
	private ImageView[] imageView;
	private String[] images = {"face-smile.png", "face-dead.png", "face-win.png", "face-O.png"};
	static final double BUTTON_SIZE = 30;

	public SmileButton() {
		imageView = new ImageView[images.length];
		for (int i = 0; i < images.length; i++) {
			imageView[i] = new ImageView(new Image("file:res/" + images[i]));
			imageView[i].setFitWidth(BUTTON_SIZE);
			imageView[i].setFitHeight(BUTTON_SIZE);
			imageView[i].setCursor(null);
			imageView[i].setFocusTraversable(false);
			imageView[i].setFitHeight(BUTTON_SIZE);
		}
		setMinSize(BUTTON_SIZE, BUTTON_SIZE);
		setMaxSize(BUTTON_SIZE, BUTTON_SIZE);
		setBorder(Border.EMPTY);
		setGraphic(imageView[0]);
		setFocused(false);
		setFocusTraversable(false);
	}
//	public void restart() {
//		setGraphic(imageView[0]);
//	}
	public void win() {
		setGraphic(imageView[2]);
	}
	public void dead() {
		setGraphic(imageView[1]);
	}
	public void press() {
		setGraphic(imageView[3]);
	}
	
	public void smile() {
		setGraphic(imageView[0]);
	}
}
