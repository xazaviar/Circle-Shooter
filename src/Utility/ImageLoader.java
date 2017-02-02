package Utility;

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
}
