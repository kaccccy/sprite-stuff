import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class KJASprite implements DisplayableSprite, MovableSprite, CollidingSprite {

	private static Image image;	
	private double centerX = 25;
	private double centerY = 25;
	private double width = 24;
	private double height = 50;
	private boolean dispose = false;	
	private double velocityX = 0;
	private double velocityY = 0;
	private final double VELOCITY = 200;
	private final double PROXIMITY = 100;
	private long score = 0;
	private boolean isAtExit = false;
	private String proximityMessage = "";


	public KJASprite(double centerX, double centerY) {

		this.centerX = centerX;
		this.centerY = centerY;

		if (image == null) {
			try {
				image = ImageIO.read(new File("res/KJA/crab.png"));
			}
			catch (IOException e) {
				System.out.println(e.toString());
			}		
		}		
	}

	public KJASprite(double centerX, double centerY,  double height, double width) {
		this(centerX, centerY);

		this.height = height;
		this.width = width;
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

		double velocityX = 0;
		double velocityY = 0;

		if (keyboard.keyDown(37)) {
			velocityX = -VELOCITY;
		}
		//UP
		if (keyboard.keyDown(38)) {
			velocityY = -VELOCITY;			
		}
		// RIGHT
		if (keyboard.keyDown(39)) {
			velocityX += VELOCITY;
		}
		// DOWN
		if (keyboard.keyDown(40)) {
			velocityY += VELOCITY;			
		}
		//LEFT	

		double deltaX = actual_delta_time * 0.001 * velocityX;
		double deltaY = actual_delta_time * 0.001 * velocityY;

		boolean collidingWithKJA = checkCollisionWithKJA(universe.getSprites(), deltaX, deltaY);
		boolean collidingBarrierX = checkCollisionWithBarrier(universe.getSprites(), deltaX, 0);
		boolean collidingBarrierY = checkCollisionWithBarrier(universe.getSprites(), 0, deltaY);
		boolean checkProximity = checkProximity(universe.getSprites());
		
		if (collidingBarrierX == false || collidingWithKJA == false) {
			this.centerX += deltaX;
		}
		if (collidingBarrierY == false || collidingWithKJA == false) {
			this.centerY += deltaY;
		}
		
		if (checkProximity == false) {
			proximityMessage = "";
		}
		
		checkCoversCoin(universe.getSprites(), deltaX, deltaY);
		checkInExit(universe.getSprites(), deltaX, deltaY);
		checkProximity(universe.getSprites());
		
	}



	private boolean checkCollisionWithBarrier(ArrayList<DisplayableSprite> sprites, double deltaX, double deltaY) {
		boolean colliding = false;

		for (DisplayableSprite sprite : sprites) {
			if (sprite instanceof BarrierSprite || sprite instanceof OtherSprite) {
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

	private boolean checkCollisionWithKJA(ArrayList<DisplayableSprite> sprites, double deltaX, double deltaY) {

		//deltaX and deltaY represent the potential change in position
		boolean colliding = false;

		for (DisplayableSprite sprite : sprites) {
			if (sprite instanceof KJASprite) {


				if (CollisionDetection.pixelBasedOverlaps(this, sprite, deltaX, deltaY)) {
					colliding = true;
					break;					
				}
			}
		}		
		return colliding;		
	}

	private void checkCoversCoin(ArrayList<DisplayableSprite> sprites, double deltaX, double deltaY) {
	
		for (DisplayableSprite sprite : sprites) {
			if (sprite instanceof CoinSprite) {				
				if (CollisionDetection.overlaps(this, sprite)) {
					sprite.setDispose(true);
					score += 100;
					
					break;					
				}
			}
		}		
			
	}
	
	private void checkInExit(ArrayList<DisplayableSprite> sprites, double deltaX, double deltaY) {
		for (DisplayableSprite sprite : sprites) {
			if (sprite instanceof ExitSprite) {				
				if (CollisionDetection.inside(this, sprite) && score >= 1000) {
					isAtExit = true;
					break;					
				}
			}
		}
			
	}

	private boolean checkProximity(ArrayList<DisplayableSprite> sprites) {
		boolean inProximity = false;
		for (DisplayableSprite sprite : sprites) {
			if (sprite.getClass().toString().contains("OtherSprite")) {	
				if (withinProximity(this, sprite) == true) {
					proximityMessage = "1000 points to pass!";
					inProximity = true;
					break;
				}
			}
		}
		return inProximity;
	}
	
	private boolean withinProximity(DisplayableSprite spriteA, DisplayableSprite spriteB) {
		boolean inProximity = false;
		
		double a = spriteB.getCenterX() - spriteA.getCenterX();
		double b = spriteB.getCenterY() - spriteA.getCenterY();
		double c = Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
		
		if (c <= PROXIMITY) {
		inProximity = true;
		}
		return inProximity;
	}
	
	
	@Override
	public long getScore() {
		return score;
	}

	@Override
	public String getProximityMessage() {
		return proximityMessage;
	}

	@Override
	public boolean getIsAtExit() {
		return isAtExit;
	} 
}

