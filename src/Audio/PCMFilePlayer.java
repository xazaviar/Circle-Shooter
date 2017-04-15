package Audio;

import javax.sound.sampled.*;
import java.io.*;

/**
 * This class runs plays the audio file provided.
 * @author Xazaviar
 */
public class PCMFilePlayer implements Runnable {
	File file;
	AudioInputStream in;
	SourceDataLine line;
	int frameSize;
	byte[] buffer = new byte [32 * 1024]; // 32k is arbitrary
	Thread playThread;
	boolean playing;
	boolean notYetEOF;
        
    /**
     * The default constructor (not in use)
     */
    public PCMFilePlayer(){}
    
    /**
     * This is the primary constructor. It implements
     * the song file and runs the audio stream.
     * @param f
     *          The audio file
     * @throws IOException
     * @throws UnsupportedAudioFileException
     * @throws LineUnavailableException 
     */
	public PCMFilePlayer (File f) throws IOException, UnsupportedAudioFileException, LineUnavailableException {

        file = f;
        in = AudioSystem.getAudioInputStream (f);
        AudioFormat format = in.getFormat();
        AudioFormat.Encoding formatEncoding = format.getEncoding();

        if (! (formatEncoding.equals (AudioFormat.Encoding.PCM_SIGNED) || formatEncoding.equals (AudioFormat.Encoding.PCM_UNSIGNED))){
            throw new UnsupportedAudioFileException (file.getName() + " is not PCM audio");
        }
                          
        //System.out.println ("got PCM format");
        frameSize = format.getFrameSize();
        DataLine.Info info = new DataLine.Info (SourceDataLine.class, format);
	   
        //System.out.println ("got info");
        line = (SourceDataLine) AudioSystem.getLine (info);
        //System.out.println ("got line");
        line.open();
        //System.out.println ("opened line");
        playThread = new Thread (this);
        playing = false;
        notYetEOF = true;
        playThread.start();
    }
        
    /**
     * Runs the data and thread so the audio
     * can run in conjuction with another program
     */
	public void run() {
        int readPoint = 0;
        int bytesRead = 0;

        try {
            while (notYetEOF) {
                if (playing) {
                    bytesRead = in.read (buffer, readPoint, buffer.length - readPoint);

                    if (bytesRead == -1) {			 
                        notYetEOF = false;
                        break;
                    }
                    // how many frames did we get,
                    // and how many are left over?
//                   int frames = bytesRead / frameSize;
                    int leftover = bytesRead % frameSize;
                    // send to line
                    line.write (buffer, readPoint, bytesRead-leftover);
                    // save the leftover bytes
                    System.arraycopy (buffer, bytesRead, buffer, 0, leftover);
                    readPoint = leftover;


                }else{
                    // if not playing
                    Thread.yield();
                    try { Thread.sleep (10);}
                    catch (InterruptedException ie) {}
                }
            }
            
            //System.out.println ("reached eof");
            line.drain();
            line.stop();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            line.close();
        }
	}

    /**
     * Starts the thread that plays the song
     */
	public void start() {
        playing = true;
        if (!playThread.isAlive()){
            playThread.start();                
        }
        line.start();
	}

    /**
     * Stops the song
     */
	public void stop() {
        playing = false;
        line.stop();
        line.close();
	}

    /**
     * Returns the SourceDataLine
     * @return 
     *          The current sourceDataLine
     */
	public SourceDataLine getLine() {
        return line;
	}

    /**
     * Returns the audio file currently playing
     * @return 
     *          The current file
     */
	public File getFile() {
        return file;
	}

    /**
     * Returns if the song is playing
     * @return 
     *          If the song is playing
     */
    public boolean loop(){
        return playing;
    }
}

