
public class Highscore {
	String name;
	int score;
	
	public Highscore(String name, int score) {
		this.name = name;
		this.score = score;
	}
	
	public String toString() {
		return name +" - "+score;
	}
}
