package xxx.xxx;

import static spark.Spark.get;
import static spark.Spark.port;

import java.io.File;
import java.util.Date;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

public class App {
    
	public static void main(String[] args) throws Exception {

		port(8080);
		get("/", (req, res) -> {

			System.out.println("Request : "+req.ip());
			App.playSound();
			
			return "Running : "+new Date();
			
		});
    }
	
	public static void playSound() throws Exception {
		
		Thread x = new Thread(new Runnable() {
			
			public void run() {
			
				try {
					
					File file = new File(getClass().getResource("/sound.wav").getFile());
					
					AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
					AudioFormat format = audioInputStream.getFormat();
					
					DataLine.Info line = new DataLine.Info(Clip.class, format);
					
					Clip clip = (Clip)AudioSystem.getLine(line);
					
					//Adding Line Listener to block until done playing;
					clip.addLineListener(new LineListener() {
						
						@Override
						public void update(LineEvent event) {
						
							if(event.getType() == LineEvent.Type.STOP)
								event.getLine().close();
						}
					});
					
					clip.open(audioInputStream);
					clip.start();
				}
				catch(Exception e) {}
			}
		});
		
		x.setName("Sound-Thread");
		x.start();
	}
}