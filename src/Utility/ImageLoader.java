package Utility;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageLoader {

	/*
	 * Loads an image file from the path str
	 * Used code found at
	 * https://docs.oracle.com/javase/tutorial/2d/images/loadimage.html
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
	
	static public AffineTransformOp getRotation(double theta, BufferedImage img){
		double rotationRequired = theta - Math.toRadians(90);
		double locationX = img.getWidth() / 2;
		double locationY = img.getHeight() / 2;
		AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		return op;
	}
}
