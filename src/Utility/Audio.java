package Utility;

import java.io.*;
import javax.sound.sampled.*;

/**
 * Based mostly on Joe's old code, but also
 * the tutorial found here
 * https://docs.oracle.com/javase/tutorial/sound/converters.html
 * 
 * @author Matt and smol Joe
 *
 */
public class Audio implements Runnable{

	AudioInputStream audioStream;
	int totalFramesRead;
	int bytesPerFrame;
	int numBytes;
	byte[] buffer;
	SourceDataLine line;
	Thread audioThread;
	boolean playing;
	boolean notEOF;

	public Audio(String filename){
		File in = new File(filename);

		try{
			audioStream = AudioSystem.getAudioInputStream(in);
			AudioFormat format = audioStream.getFormat();
			bytesPerFrame = format.getFrameSize();

			AudioFormat.Encoding formatEncoding = format.getEncoding();
			if (! (formatEncoding.equals (AudioFormat.Encoding.PCM_SIGNED) || formatEncoding.equals (AudioFormat.Encoding.PCM_UNSIGNED))){
				throw new UnsupportedAudioFileException (in.getName() + " is not PCM audio");
			}

			if (bytesPerFrame == AudioSystem.NOT_SPECIFIED) {
				bytesPerFrame = 1;
			}
			numBytes = 1024 * bytesPerFrame;
			buffer = new byte[numBytes];

			DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
			line = (SourceDataLine)AudioSystem.getLine(info);
			line.open();
			audioThread = new Thread(this);
			playing = false;
			notEOF = true;
			audioThread.start();

		} catch (Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		int numBytesRead = 0;
		int numFramesRead = 0;

		try{
			while (notEOF) {
				if (playing) {
					numBytesRead = audioStream.read (buffer, numFramesRead, buffer.length - numFramesRead);

					if (numBytesRead == -1) {			 
						notEOF = false;
						break;
					}
					// how many frames did we get,
					// and how many are left over?
					int leftover = numBytesRead % bytesPerFrame;
					// send to line
					line.write (buffer, numFramesRead, numBytesRead - leftover);
					// save the leftover bytes
					System.arraycopy (buffer, numBytesRead, buffer, 0, leftover);
					numFramesRead = leftover;


				}else{
					// if not playing
					Thread.yield();
					try { Thread.sleep (10);}
					catch (InterruptedException ie) {}
				}
			}
			playing = false;
			System.out.println("EOF reached");
		} catch (Exception e){
			e.printStackTrace();
		}

	}
	
	public void start() {
		playing = true;
		if (!audioThread.isAlive()){
			audioThread.start();                
		}
		line.start();
	}
	
	public void stop() {
		playing = false;
		line.stop();
		line.close();
	}
	
	public SourceDataLine getLine() {
		return line;
	}
	
	public boolean getPlaying(){
		return playing;
	}
	
	public boolean notEOF(){
		return notEOF;
	}
	
}

