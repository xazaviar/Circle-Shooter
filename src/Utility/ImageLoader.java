package Utility;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Class of static helper methods for image importing and manipulation
 * 
 * @author Mathonwy Dean-Hall
 *
 */
public class ImageLoader {

	/*
	 * Loads an image file from the path str
	 * Used code found at
	 * https://docs.oracle.com/javase/tutorial/2d/images/loadimage.html
	 */
	/**
	 * Loads an image file from the specified path.
	 * Based on code found at
	 * https://docs.oracle.com/javase/tutorial/2d/images/loadimage.html
	 * 
	 * @param str
	 * 		String file path of the image
	 * @return
	 * 		BufferedImage object of the imported image
	 */
	static public BufferedImage loadImage(String str){
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(str));
		} catch (IOException e) {
			System.out.println("Failed to load" + str);
		}
		return img;
	}
	
	/**
	 * Manipulates an image to rotate it based on an angle.
	 * 
	 * @param theta
	 * 		double angle to rotate the object in radians, effected with an additional quarter turn clockwise.
	 * @param img
	 * 		BufferedImage image to be rotated
	 * @return
	 * 		AffineTransformOP object with the rotation operation for the image
	 */
	static public AffineTransformOp getRotation(double theta, BufferedImage img){
		double rotationRequired = theta - Math.toRadians(90);
		double locationX = img.getWidth() / 2;
		double locationY = img.getHeight() / 2;
		AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		return op;
	}
}
