import javax.swing.JFrame;

public class SpaceInvaders {
	
	private static JFrame frame;
	private static GameBoard board;
	
	public static void main(String[] args) {
		
		createFrame();
		createGameBoard();
	}
	
	//Creo la finestra di gioco
	public static void createFrame() {
		System.out.println("[Main]: Creating Frame");
		frame = new JFrame("Space Invaders");
		
		frame.setBounds(250, 20, Reference.WINDOW_WIDTH, Reference.WINDOW_HEIGHT);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
	//Metodo tramite il quale aggiungo elementi alla finestra di gioco
	private static void createGameBoard() {
		System.out.println("[Main]: Creating Game Board");
		board = new GameBoard();
		frame.add(board);
		board.requestFocusInWindow();
	}
		
}