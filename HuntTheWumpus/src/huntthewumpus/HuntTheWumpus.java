package huntthewumpus;

/**
 * Plays a rendition of the classic 1976 game, Hunt the Wumpus.
 * 
 * @author Ivan Samuelson
 */
public class HuntTheWumpus {

    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // We need to read in the file information first
        Game game = new Game();
        game.playGame();
    }
    
}
