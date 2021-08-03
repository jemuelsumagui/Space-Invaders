
import javax.swing.JOptionPane;

public class EndGame {
	public static String playerName;
	public static Boolean newHighscore = false;
	
	public EndGame() {
		System.out.println("[EndGame]:Game Over");
		playerName =(String)JOptionPane.showInputDialog(null, "Insert Name: ");
		Highscore newPlayer = new Highscore(playerName, GameBoard.score);
		System.out.println(newPlayer);
		
		if(playerName != null) {
			sortHighscore(newPlayer);
		} else {
			System.out.println("[EndGame]:You choose to not update the highscore");
		}
		
		Resources.updateHighscore();
	}
	
	public static void sortHighscore(Highscore h) {
		int length = Resources.highscore.size();
		
		if(h.name.compareTo("") == 0) {
			System.out.println("[EndGame]: Name is null");
			h.name = "Default";
		}
		
		for(int i=0; i < Resources.highscore.size();i++){
			if(h.score > Resources.highscore.get(i).score) {
				Resources.highscore.add(i, h);
				if(i == 0) {
					newHighscore = true;
				}
				break;
			}
				if(h.score == Resources.highscore.get(i).score) {
					Resources.highscore.add(i+1, h);
					break;
				}
				
				if(h.score <= Resources.highscore.get(length-1).score) {
					Resources.highscore.add(h);
					break;
				}
	}
		
		if(Resources.highscore.size() == 0) {
			Resources.highscore.add(h);
			newHighscore = true;
		}
		
	}
	

}
