
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class Resources{
	
	public static final BufferedImage player = readImage("player.png");
	public static final BufferedImage alien0 = readImage("alien0.png");
	public static final BufferedImage alien0_1 = readImage("alien0_1.png");
	public static final BufferedImage alien1 = readImage("alien1.png");
	public static final BufferedImage alien1_1 = readImage("alien1_1.png");
	public static final BufferedImage alien2 = readImage("alien2.png");
	public static final BufferedImage explosion = readImage("explosion.png");
	public static final BufferedImage deadplayer = readImage("deadPlayer.png");
	public static ArrayList<Highscore> highscore;


	
	
	//lettura immagine file
	private static BufferedImage readImage(String fileName) {
		System.out.println("[Resources]: Loading "+fileName);
		BufferedImage img = null;
		
		try {
			img = ImageIO.read(new File("src/resources/"+fileName));
		} catch (IOException e) {
			System.out.println("[Resources] : Exception when loading src/resources/"+fileName+ ".png");
		}
		return img;
	}
	
	public static void getHighscore() {
		File file = new File("src/resources/files/highscore.txt");
		
		if(!file.exists()) {
			
			try {
				file.createNewFile();
			} catch(IOException e) {
				System.out.println("[Resources]: Error when creatin a new file");
			}
		}
		try {
			Scanner scanF = new Scanner(file);
			highscore = new ArrayList<Highscore>();
			
			while(scanF.hasNext()) {
				
				String record = scanF.nextLine();
				Scanner scanRec = new Scanner(record);
				
				scanRec.useDelimiter("-");
				
				String player;
				int score;
				
				player = scanRec.next();
				score = scanRec.nextInt();
				
				highscore.add(new Highscore(player,score));
				
				scanRec.close();
			}
			scanF.close();
		} catch(FileNotFoundException e) {
			System.out.println("[Resources]: File not found");
		}
	}
	
	public static void updateHighscore() {
		try {
			PrintWriter writer = new PrintWriter("src/resources/files/highscore.txt");
			
			for(int i=0; i < highscore.size();i++) {
				writer.println(highscore.get(i).name+"-"+highscore.get(i).score);
			}
			writer.close();
		} catch(FileNotFoundException e) {
			System.out.println("[Resources]: File not found");
		}
 
	}
	
	
	

	
}
	