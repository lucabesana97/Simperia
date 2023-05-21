package sounds;

import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class Sound {
	public Clip clip;
	URL soundURL[] = new URL[30];
	
	public Sound() {
		soundURL[0] = getClass().getResource("/audio/Victory.wav");
		soundURL[1] = getClass().getResource("/audio/Loss.wav");
		soundURL[2] = getClass().getResource("/audio/Background_Loop.wav");
		soundURL[3] = getClass().getResource("/audio/Coin.wav");
		soundURL[4] = getClass().getResource("/audio/Bang.wav");
		soundURL[5] = getClass().getResource("/audio/Monster_Death.wav");
	}
	
	public void setFile(int i) {
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
			clip = AudioSystem.getClip();
			clip.open(ais);
		} catch(Exception e) {
			System.out.println("fuck");
		}
	}
	
	public void play() {
		clip.start();
	}
	
	public void loop() {
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	public void stop() {
		clip.stop();
		clip.setFramePosition(0);
	}
	
	public void changeVolume(float volume) {
		// Get the gain control from the clip
		FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

		// Set the desired volume level in decibels (-80.0 to 6.0)
		gainControl.setValue(volume);
	}
	
	public void playMusic(int i) {
		setFile(i);
		play();
		loop();
	}

	public void stopMusic() {
		try {
			stop();
		} catch(Exception e) {
			System.out.println(e.getCause());
		}
	}

	public void playSoundEffect(int i) {
		setFile(i);
		play();
	}
}
