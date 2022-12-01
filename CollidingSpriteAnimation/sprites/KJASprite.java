import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class KJASprite implements DisplayableSprite, MovableSprite, CollidingSprite {

private static Image image;	
private double centerX = 25;
private double centerY = 25;
private double width = 50;
private double height = 50;
private boolean dispose = false;	
private double velocityX = 0;
private double velocityY = 0;

public KJASprite(double centerX, double centerY) {
	
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

private boolean checkCollisionWithBarrier(ArrayList<DisplayableSprite> sprites, double deltaX, double deltaY) {
	boolean colliding = false;

	for (DisplayableSprite sprite : sprites) {
		if (sprite instanceof BarrierSprite) {
			if (CollisionDetection.overlaps(this.getMinX() + deltaX, this.getMinY() + deltaY, 
					this.getMaxX()  + deltaX, this.getMaxY() + deltaY, 
					sprite.getMinX(),sprite.getMinY(), 
					sprite.getMaxX(), sprite.getMaxY())) {
				colliding = true;
				break;					
			}
		}
	}		
	return colliding;		
}


@Override
public void update(Universe universe, KeyboardInput keyboard, long actual_delta_time) {
	double deltaX = actual_delta_time * 0.001 * velocityX;
	double deltaY = actual_delta_time * 0.001 * velocityY;
	
	boolean collidingBarrierX = checkCollisionWithBarrier(universe.getSprites(), deltaX, 0);
	boolean collidingBarrierY = checkCollisionWithBarrier(universe.getSprites(), 0, deltaY);
	
	if (collidingBarrierX = false) {
	this.centerX += deltaX;
	}
	if (collidingBarrierY = false) {
	this.centerY += deltaY;
	}
	
	
}

@Override
public long getScore() {
	// TODO Auto-generated method stub
	return 0;
}

@Override
public String getProximityMessage() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public boolean getIsAtExit() {
	// TODO Auto-generated method stub
	return false;
}
}

