import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class KJASprite implements DisplayableSprite, MovableSprite {

private static Image image;	
private double centerX = 25;
private double centerY = 25;
private double width = 50;
private double height = 50;
private boolean dispose = false;	
private double velocityX = 0;
private double velocityY = 0;

public KJASprite() {
	
	if (image == null) {
		try {
			image = ImageIO.read(new File("res/KJA/crab.png"));
		}
		catch (IOException e) {
			System.out.println(e.toString());
		}		
	}		
}

public Image getImage() {
	return image;
}

@Override
public void setCenterX(double centerX) {
	this.centerX=centerX;
}

@Override
public void setCenterY(double centerY) {
	this.centerY=centerY;
}

@Override
public void setVelocityX(double pixelsPerSecond) {
	this.velocityX=pixelsPerSecond;
}

@Override
public void setVelocityY(double pixelsPerSecond) {
	this.velocityY=pixelsPerSecond;
}

@Override
public boolean getVisible() {
	return true;
}

@Override
public double getMinX() {
	return centerX - (width / 2);
}

@Override
public double getMaxX() {
	return centerX + (width / 2);
}

@Override
public double getMinY() {
	return centerY - (height / 2);
}

@Override
public double getMaxY() {
	return centerY + (height / 2);
}

@Override
public double getHeight() {
	return height;
}

@Override
public double getWidth() {
	return width;
}

@Override
public double getCenterX() {
	return centerX;
}

@Override
public double getCenterY() {
	return centerY;
}

@Override
public boolean getDispose() {
	return dispose;
}

@Override
public void setDispose(boolean dispose) {
	this.dispose=dispose;
}

@Override
public void update(Universe universe, KeyboardInput keyboard, long actual_delta_time) {
	this.centerX += actual_delta_time * 0.001 * velocityX;
	this.centerY += actual_delta_time * 0.001 * velocityY;
}
}
