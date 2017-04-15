package Audio;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;


/**
 * This class starts the PCMFilePlayer Class and allows
 * the ability to choose what songs are selected to play
 * and loop said songs
 * 
 * @author Xazaviar
 */
public class Music extends Object implements LineListener{
    
    private File soundFile;
    private PCMFilePlayer player;
    private boolean loop;
        
    /**
     * The default constructor (not in use)
     */
    public Music(){}  
    
    /**
     * The primary constructor. This implements the 
     * chosen sound file as the first song to play
     * and loads the PCMFilePlayer Class
     * 
     * @param f
     *          The sound file
     * @throws LineUnavailableException
     * @throws IOException
     * @throws UnsupportedAudioFileException 
     */
    public Music(String f, boolean loop){
        //Sets the sound file
		soundFile = new File(f); 
		this.loop = loop;
    }
    
    
    public void play() throws IOException, LineUnavailableException, UnsupportedAudioFileException{
    	this.loop = true;
    	player = new PCMFilePlayer (soundFile);
		player.getLine().addLineListener (this);
		player.start();
    }
    
    public void play2(){
    	new javafx.embed.swing.JFXPanel();
        String uriString = soundFile.toURI().toString();
        new MediaPlayer(new Media(uriString)).play();
    }
    
    public void stop(){
    	this.loop = false;
    	this.player.stop();
    }
    
    /**
     * This is the required method to implement
     * when implementing LineListener. This updates
     * the status of the sound file playing when an action 
     * occurs
     * @param le 
     *          The current event happening with the music
     */
     public void update (LineEvent le){
		LineEvent.Type type = le.getType();
		if (type == LineEvent.Type.OPEN) {
		} else if (type == LineEvent.Type.CLOSE) {
		} else if (type == LineEvent.Type.START) {
		} else if (type == LineEvent.Type.STOP) {
	            
            //Updates the file that should be looped
			if(loop){
	            try {
	            	this.play();
	                //player = new PCMFilePlayer(soundFile);
	            } catch (IOException ex) {
	                //Logger.getLogger(this.class.getName()).log(Level.SEVERE, null, ex);
	            } catch (UnsupportedAudioFileException ex) {
	                //Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
	            } catch (LineUnavailableException ex) {
	                //Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
	            }
		            
		            
//		        try {
//					Main s = new Main (f);
//		        } catch (Exception e) {
//					e.printStackTrace();
//		        }
			}
		}
  }
}
