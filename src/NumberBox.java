import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

class NumberBox extends HBox {
	ImageView[] imageView = null;
	Image[] image = null;
	public NumberBox(Pos pos, int initNumber) {		
		super();
		setStyle("-fx-background-color: #000000;-fx-border-width: 1; -fx-border-color: #676767 white white #676767; -fx-border-style: solid inside line-join miter;");
		setAlignment(pos);
		setMaxHeight(30);
		setMinHeight(30);
		setSpacing(10);
		InnerShadow innerShadow = new InnerShadow();
		innerShadow.setOffsetX(2); 
	    innerShadow.setOffsetY(2); 
	    innerShadow.setBlurType(BlurType.GAUSSIAN);
	    innerShadow.setColor(Color.valueOf("#000000"));
	    
		imageView = new ImageView[10];
		image = new Image[10];
		setSpacing(0);
		setNumber(initNumber);
	}

	public void setNumber(int num) {
		if (num > 999) return;
		String number = String.format("%03d", num);
		getChildren().clear();
		for (int i = 0; i < number.length(); i++) {
			int n = Integer.valueOf(number.substring(i, i + 1));
			image[i] = new Image("file:res/digits/" + n + ".png", 13.0, 25.0, false, false);
			imageView[i] = new ImageView(image[i]);
			setMargin(imageView[i], new Insets(2,3,2,3));
			getChildren().add(imageView[i]);
		}
	}
}
