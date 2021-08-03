import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;


public class GameBoard extends JPanel implements ActionListener, KeyListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Timer timer;
	private int timerDelay = 50;
	public static int score = 0;
	private static int level = 1;

	
	private int playerTimeDelay = 0;
	private int playerDeathDelay = 15;
	private int alienTimeDelay = 0;
	private int aliensKilled = 0;
	
	private GameState state;
	private static Player player;
	private static int generator[][];
	private static Alien aliens[][];
	private static ArrayList<Bullets> bullets1;
	public static ArrayList<Bullets> bullets2;
	public static EndGame gameover;
	
	//costruttore GameBoard
	public GameBoard() {
		addKeyListener(this);
		this.setFocusable(true);
		
		
		//inzializzo giocatore
		player = new Player();
		
		//inizializzo mappa
		generator = new GeneratorMap().initMap();

		//inizializzo alieni
		aliens = new Alien[Reference.ALIENS_COL][Reference.ALIENS_ROW];
		for(int i=0;i<generator.length;i++) {
			for(int j=0;j<generator[i].length;j++) {
				aliens[i][j] = new Alien (200+i*45,70+j*45);
			}
		}
		
		//inizializzo proiettili
		bullets1 = new ArrayList<Bullets>();	
		bullets2 = new ArrayList<Bullets>();
		
		state = GameState.MAIN_MENU;
		
		Resources.getHighscore();
		
		//inizializzo timer
		timer = new Timer(timerDelay, this);
		timer.start();
		
	}
	
	public static void addScore(int value) {
		score = score + value;
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		
		//background
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, Reference.WINDOW_WIDTH, Reference.WINDOW_HEIGHT);
		
		if(state == GameState.RUNNING) {
			graphicRunGame(g);
		}
		if(state == GameState.MAIN_MENU) {
			graphicMenu(g);
		}
		if(state == GameState.GAME_OVER) {
			graphicGameOver(g);
		}
		if(state == GameState.HIGHSCORES) {
			graphicHighscore(g);
		}
		

	}
	
	
	
	@Override
	public void keyTyped(KeyEvent e) {}
	
	@Override
	public void keyPressed(KeyEvent e) {
		
		if(state == GameState.RUNNING) {
			if(!player.isDead()) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_LEFT:
					if(player.getPosX()>10) {
						player.move(-10);
					} break;
				case KeyEvent.VK_RIGHT:
					if(player.getPosX()<Reference.WINDOW_WIDTH-90) {
						player.move(10);
					} break;
				case KeyEvent.VK_SPACE:
					if(playerTimeDelay>player.shotDelay) {
						bullets1.add(new Bullets(player.getPosX()+25, player.getPosY()-10,10));
						playerTimeDelay = 0;
					} break;
				} 
			}
		}
		
		if(state == GameState.MAIN_MENU) {
			switch(e.getKeyChar()) {
			case KeyEvent.VK_ENTER:
				state = GameState.RUNNING;
				break;
			case KeyEvent.VK_SPACE:
				state = GameState.HIGHSCORES;
				break;
			}
		} else if(state == GameState.GAME_OVER) {
			state = GameState.HIGHSCORES;
			gameover = new EndGame();
			score = 0;
			restart();
			
		} else if(state == GameState.HIGHSCORES) {
			state = GameState.MAIN_MENU;
		}
		
		repaint();
		revalidate();
	}
	
	@Override
	public void keyReleased(KeyEvent e) {}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(state == GameState.RUNNING) {
		playerTimeDelay++;
		alienTimeDelay++;

				
		if(alienTimeDelay == Alien.getMotionDelay()) {
		//movimento laterale alieni
		for(int i=0;i<generator.length;i++) {
			for(int j=0;j<generator[i].length;j++) {
				aliens[i][j].lateralMove();
			}
		}
			Alien.changeFrame();
		}

		
		//movimento in basso degli alieni quando si raggiunge limite finestra
		if(aliens[0][0].getDir()>0) {
			if(aliens[Reference.ALIENS_COL-1][Reference.ALIENS_ROW-1].checkDir()) {
				for(int i=0;i<Reference.ALIENS_COL;i++) {
					for(int j=0;j<Reference.ALIENS_ROW;j++) {
						
						try {
							aliens[i][j].downMove();
						} catch(NullPointerException d) {
							continue;
						}
					}
				}
			} 
		} else {
			if(aliens[0][0].checkDir()) {
				for(int i=0;i<Reference.ALIENS_COL;i++) {
					for(int j=0;j<Reference.ALIENS_ROW;j++) {
						
						try {
							aliens[i][j].downMove();
						} catch(NullPointerException d) {
							continue;
						}
					}
				}
				
			}
		}
		
		
		
		/*controllo la direzione degli alieni*/
		if(aliens[0][0].getDir()>0) {
			aliens[Reference.ALIENS_COL-1][Reference.ALIENS_ROW-1].checkDir();
		} else {
			aliens[0][0].checkDir();
		}
		
		
		/*
		 * controllo se gli alieni sono arrivati sulla Terra
		 * */
		for(int i=0;i<aliens.length;i++) {
			for(int j=0;j<aliens[i].length;j++) {
				if(aliens[i][j].getPosY()>Reference.WINDOW_HEIGHT-80 && !aliens[i][j].isDead()) {
					state = GameState.GAME_OVER;
					System.out.println("[GameBoard]: Aliens are landed");
				}
			}
		}
		

		
		/*movimento dei proiettili sparati dal giocatore*/
		for(int i=0;i<bullets1.size();i++) {
			bullets1.get(i).moveUp();;
			
			/*controllo se il proiettile esce dallo schermo, se sì lo cancello*/
			if(bullets1.get(i).getPosY() < 25) {
				bullets1.remove(i);
			}
		}
		
		/*movimento dei proiettili sparati dagli alieni*/
		for(int i=0;i<bullets2.size();i++) {
			bullets2.get(i).moveDown();
			
			/*controllo se il proiettile esce dallo schermo, se sì lo cancello*/
			if(bullets2.get(i).getPosY() > Reference.WINDOW_HEIGHT-55) {
				bullets2.remove(i);
			}
		}
		
		
		/*spari degli alieni*/
		Random rand = new Random();
		
		int col = rand.nextInt(Reference.ALIENS_COL);
		int row = rand.nextInt(Reference.ALIENS_ROW);
		
		if(alienTimeDelay == Alien.getShootDelay()) {
			if(!aliens[col][row].isDead()) {
			bullets2.add(new Bullets(aliens[col][row].getPosX()+15,aliens[col][row].getPosY()+25,2));
			alienTimeDelay=0;
			}
		}
		
		if(alienTimeDelay >= Alien.getShootDelay()) {
			alienTimeDelay = 0;
		}

		
		/*
		 * scontro alieno-proiettile: controllo se il proiettile colpisce l'alieno
		 */
		for(int i=0;i<Reference.ALIENS_COL;i++) {
			for(int j=0;j<Reference.ALIENS_ROW;j++) {
				for(int k=0;k<bullets1.size();k++) {
					if(bullets1.get(k).getBoundingBox().intersects(aliens[i][j].getBoundingBox()) && !aliens[i][j].isDead()) {
						aliens[i][j].setDead(true);
						aliensKilled++;
						bullets1.remove(k);
						
						if(generator[i][j]==0) {addScore(10);}
						if(generator[i][j]==1) {addScore(20);}
						if(generator[i][j]==2) {addScore(30);}
					}
				}
			}
		}
		
		/*
		 * scontro proiettile-giocatore: controllo se il proiettile colpisce il giocatore
		 */
		for(int i=0;i<bullets2.size();i++) {
			if(bullets2.get(i).getBoundingBox().intersects(player.getBoundingBox())) {
				player.setDead(true);
				bullets2.remove(i);
				player.takeLives();
			}
		}
		
		/*quando il giocatore muore*/
		if(player.isDead()) {
			playerDeathDelay--;
			bullets1 = new ArrayList<Bullets>();
			bullets2 = new ArrayList<Bullets>();

			if(playerDeathDelay == 0) {	
				playerDeathDelay = 15;
				if(player.getLives()>0) {
					player.setDead(false);
				} else {
					state = GameState.GAME_OVER;
				}
			}
		}
		
		/*quando vengono uccisi tutti gli alieni*/
		if(aliensKilled == Reference.ALIENS_COL*Reference.ALIENS_ROW) {
			aliensKilled = 0;
			
			/*re-inizializzo gli alieni*/
			{
				aliens = new Alien[Reference.ALIENS_COL][Reference.ALIENS_ROW];
				for(int i=0;i<generator.length;i++) {
					for(int j=0;j<generator[i].length;j++) {
						aliens[i][j] = new Alien (200+i*45,45+j*45);
					}
				}
			}
			Alien.decreaseMotionDelay();
			level++;
		}
		

		}
		repaint();
		revalidate();
	}
	
private static void graphicRunGame(Graphics g) {
	//lives
	g.setFont(new Font("Arial", Font.BOLD,15));
	g.setColor(Color.WHITE);
	g.drawString("Lives", 5, 20);
	
	for(int i=0;i<player.getLives();i++) {
		g.drawImage(Resources.player, i*40+50, 10, Resources.player.getWidth()*2, Resources.player.getHeight()*2, null);
	}
	
	
	//level
	g.setFont(new Font("Arial", Font.BOLD,15));
	g.setColor(Color.WHITE);
	g.drawString("Level", 400, 20);
	g.drawString(""+level, 450 , 20);
	
	//score
	g.setFont(new Font("Arial", Font.BOLD,15));
	g.setColor(Color.WHITE);
	g.drawString("Score", 780, 20);
	g.drawString(""+score, 850, 20);
	
	
	
	//ground
	g.setColor(Color.GREEN);
	g.fillRect(2, 553, 880, 5);
	
	//sky (decidere se mettere o meno)
	//g.setColor(Color.LIGHT_GRAY);
	//g.fillRect(2, 25 , 880, 2);
	
	// draw player
	if(!player.isDead()) {
	g.drawImage(Resources.player, player.getPosX(), player.getPosY(), Resources.player.getWidth()*Reference.PLAYER_WIDTH, Resources.player.getHeight()*Reference.PLAYER_HEIGHT, null);
	} else {
		g.drawImage(Resources.deadplayer, player.getPosX(), player.getPosY(), Resources.player.getWidth()*Reference.PLAYER_WIDTH, Resources.player.getHeight()*Reference.PLAYER_HEIGHT, null);
	}
	
	//draw aliens
	for(int i=0;i<generator.length;i++) {
		for(int j=0;j<generator[i].length;j++) {
			if(!aliens[i][j].isDead()) {
			switch(generator[i][j]) {
			case 0: //e' il caso degli alieni delle prime due file della matrice
				if(Alien.getFrame() == 0) {
					g.drawImage(Resources.alien0, aliens[i][j].getPosX(), aliens[i][j].getPosY(), Resources.alien0.getWidth()*Reference.ALIEN_WIDTH, Resources.alien0.getHeight()*Reference.ALIEN_HEIGHT, null);
				} else {
					g.drawImage(Resources.alien0_1, aliens[i][j].getPosX(), aliens[i][j].getPosY(), Resources.alien0_1.getWidth()*Reference.ALIEN_WIDTH, Resources.alien0_1.getHeight()*Reference.ALIEN_HEIGHT, null);
				}
			break;
			
			case 1://e' il caso degli alieni della terza e quarta fila
				if(Alien.getFrame() == 0) {
					g.drawImage(Resources.alien1, aliens[i][j].getPosX(), aliens[i][j].getPosY(), Resources.alien1.getWidth()*Reference.ALIEN_WIDTH, Resources.alien1.getHeight()*Reference.ALIEN_HEIGHT, null);		
				} else {
					g.drawImage(Resources.alien1_1, aliens[i][j].getPosX(), aliens[i][j].getPosY(), Resources.alien1_1.getWidth()*Reference.ALIEN_WIDTH, Resources.alien1_1.getHeight()*Reference.ALIEN_HEIGHT, null);		
				}
				break;
				
			case 2://è il caso dell'ultima fila degli alieni
				g.drawImage(Resources.alien2, aliens[i][j].getPosX(), aliens[i][j].getPosY(), Resources.alien2.getWidth()*Reference.ALIEN_WIDTH, Resources.alien2.getHeight()*Reference.ALIEN_HEIGHT, null);
				break;
				}
			
			} else {
				if(aliens[i][j].getDeathDelay()>0) {
					g.drawImage(Resources.explosion, aliens[i][j].getPosX(), aliens[i][j].getPosY(), Resources.explosion.getWidth()*Reference.ALIEN_EXPL_WIDTH, Resources.explosion.getHeight()*Reference.ALIEN_EXPL_HEIGHT, null);
					aliens[i][j].decreaseDeathDelay();
				}
			}
		}
	}
	
	//draw bullets
	g.setColor(Color.WHITE);
	for(int i=0;i<bullets1.size();i++) {
		g.fillRect(bullets1.get(i).getPosX(), bullets1.get(i).getPosY(), 3, 10);
	}
	for(int i=0;i<bullets2.size();i++) {
		g.fillRect(bullets2.get(i).getPosX(), bullets2.get(i).getPosY(), 3, 10);
	}
}

private static void graphicMenu (Graphics g) {
	
	//titolo
	g.setColor(Color.WHITE);
	g.setFont(new Font("Arial", Font.BOLD, 70));
	g.drawString("SPACE", 140, 180);
	g.setColor(Color.GREEN);
	g.drawString("INVADERS", 400, 180);
	
	//legenda
	g.drawImage(Resources.alien0, 330, 250, Resources.alien0.getWidth()*Reference.ALIEN_WIDTH, Resources.alien0.getHeight()*Reference.ALIEN_HEIGHT, null);
	g.drawImage(Resources.alien1, 330, 300, Resources.alien1.getWidth()*Reference.ALIEN_WIDTH, Resources.alien1.getHeight()*Reference.ALIEN_HEIGHT, null);
	g.drawImage(Resources.alien2, 335, 350, Resources.alien2.getWidth()*Reference.ALIEN_WIDTH, Resources.alien2.getHeight()*Reference.ALIEN_HEIGHT, null);
	
	g.setColor(Color.WHITE);
	g.setFont(new Font("Arial", Font.PLAIN, 20));
	g.drawString(":      10 Points", 400, 270);
	g.drawString(":      20 Points", 400, 320);
	g.drawString(":      30 Points", 400, 370);
	
	//scelta pulsanti
	g.setColor(Color.WHITE);
	g.setFont(new Font("Arial", Font.PLAIN, 20));
	g.drawString("[Enter] Start Game", 350, 500);
	g.drawString("[Space] Highscores", 350, 530);
	
}

private static void graphicGameOver(Graphics g) {
	g.setColor(Color.RED);
	g.setFont(new Font("Arial", Font.BOLD, 80));
	g.drawString("GAME OVER", 200, 250);
	
	g.setColor(Color.WHITE);
	g.setFont(new Font("Arial", Font.PLAIN,20));
	g.drawString("Score:"+" "+score, 400, 350);
	

	g.setColor(Color.WHITE);
	g.setFont(new Font("Arial", Font.PLAIN,17));
	g.drawString("[Press any button]", 380, 530);
}

private static void graphicHighscore(Graphics g) {
	
	g.setColor(Color.WHITE);
	g.setFont(new Font("Arial", Font.BOLD,50));
	g.drawString("Highscore", 320, 90);
	g.setColor(Color.GREEN);
	g.setFont(new Font("Arial", Font.BOLD,20));
	g.drawString("#", 150, 150);
	g.drawString("Player", 400,150);
	g.drawString("Score", 650, 150);
	
	g.setColor(Color.WHITE);
	g.setFont(new Font("Arial", Font.PLAIN,15));
	//disegno la classifica, max 10 posti
	for(int i=0;i<Resources.highscore.size() && i!= 10;i++) {
		g.drawString(""+(i+1), 150,180+(i*30));
		g.drawString(""+Resources.highscore.get(i).name, 400, 180+(i*30));
		g.drawString(""+Resources.highscore.get(i).score, 665, 180+(i*30));
	}
	
	if(EndGame.newHighscore) {
		g.setColor(Color.YELLOW);
		g.setFont(new Font("Arial", Font.PLAIN, 13));
		g.drawString("New Highscore", 690, 170);
	}
	
	g.setColor(Color.WHITE);
	g.setFont(new Font("Arial", Font.PLAIN,15));
	g.drawString("[Press any button to return to the menu]", 330, 530);
	
	
}

public static void restart() {
	
	//re-inizializzo elementi di gioco
	
	player = new Player();

	aliens = new Alien[Reference.ALIENS_COL][Reference.ALIENS_ROW];
	for(int i=0;i<generator.length;i++) {
		for(int j=0;j<generator[i].length;j++) {
			aliens[i][j] = new Alien (200+i*45,45+j*45);
		}
	}

	bullets1 = new ArrayList<Bullets>();	
	bullets2 = new ArrayList<Bullets>();
	
}
	
}
