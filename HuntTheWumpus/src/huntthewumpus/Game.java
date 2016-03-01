package huntthewumpus;

import java.io.BufferedReader;
import java.io.Console;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Random;
/**
 * This is the main game logic. This will set up the rooms and start the game.
 * 
 * @author Ivan Samuelson
 */
public class Game {
    private Room[] rooms;       // Hold the "map" of the rooms.
    private int arrowsLeft = 3; // Fill that quiver up with 3 arrows.
    
    /**
     * Default constructor that sets up the game to play.
     * 
     */
    public Game() {
        // Set up the game.
        setupRooms();
    }

    /**
     * Sets up the rooms by reading in the information from a text file.
     * 
     */
    private void setupRooms() {
        // Read in the file information. Let's first get the number of rooms to read in.
        try (Reader ir = new InputStreamReader(new FileInputStream("rooms.txt"))) {
            BufferedReader in = new BufferedReader(ir);
            
            // Read the first line in which is the # number of rooms
            String line = in.readLine();
            
            int numRooms = Integer.parseInt(line);
            
            // Make sure we have at LEAST 6 rooms or the game won't work the way it's set up.
            if (numRooms >= 6 ) {
                // We know the number of rooms, so create an array list of room objects to store the room info
                rooms = new Room[numRooms];
                
                // Next read in 
                for (int i = 0; i < numRooms; i++) {
                    // Read the list of rooms
                    line = in.readLine();
                    String roomNums[] = line.split(" ");

                    // Next, read in the room description
                    line = in.readLine();
                    
                    rooms[i] = new Room(
                            Integer.parseInt(roomNums[0]),
                            Integer.parseInt(roomNums[1]),
                            Integer.parseInt(roomNums[2]),
                            Integer.parseInt(roomNums[3]),
                            line);
                }
                // Place a wumpus in a room.
                placeDanger(numRooms, Room.Danger.WUMPUS, 1);

                // Place spiders in 2 different rooms.
                placeDanger(numRooms, Room.Danger.SPIDER, 2);

                // Place pits in 2 different rooms.
                placeDanger(numRooms, Room.Danger.PIT, 2);
            } else {
                // not enough rooms. Exit out of the game.
                System.out.printf("Only %d rooms were defined. Need at least 6.\n", numRooms);
            }
        } catch (Exception e) {
            System.out.printf("Encountered the following exception while reading file: %s\n", e.getMessage());
        }
    }
    
    /**
     * This starts the game. It will start the player in room 1 (index array 0).
     * 
     */
    public void playGame() {
        boolean gameOver = false;
        int playerRoom = 0; // This will be the room number-1 of where the player is at. Starting in room 1 (1 - 0).
        int adjRoom;        // This is used to determine an adjacent room the user is moving or shooting into.
        
        // Grab the system console for reading from.
        Console console = System.console();
        
        // Loop until the game is over. The game ends as follows:
        //    1. The user shoots the Wumpus. WIN!
        //    2. The player runs out of arrows and gets eaten by the Wumpus. LOSE!
        //    3. The player moves into a room with a pit. LOSE!
        //    4. The player moves into a room with a spider. LOSE!
        //    5. The player moves into a room with the Wumpus. LOSE!
        while(!gameOver) {
            // Tell the player about the current room they are in
            // and what may lay ahead. GASP!!!!
            describeRoom(playerRoom);
            
            // Now tell the the valid commands and wait for input from player.
            String command = console.readLine("(M)ove or (S)hoot?");
            
            // Determine what was entered
            switch (command.toUpperCase()) {
                case "M":   // User wants to move
                    // Get which room number they wish to move to.
                    command = console.readLine("Which room?");
                    
                    // Parse out the room number and catch an exceptions where
                    // they may have entered a non-number.
                    try {
                        adjRoom = Integer.parseInt(command);
                    } catch (NumberFormatException e) {
                        // Slap their wrists for being bad!
                        System.out.printf("You entered an invalid number. Please try again.\n");
                        
                        break;  // Break out of the switch and go back to the top.
                    }
                    
                    // Verify if it's a valid room to move to.
                    if (rooms[playerRoom].validRoom(adjRoom)) {
                        // It is, so set them to that room, but remember, the
                        // room number is also our undex into the array, so
                        // subtract one from it.
                        playerRoom = adjRoom-1;
                        
                        // This will check if the player died and print the
                        // appropriate message and then set gameOver to true 
                        // to exit the loop, effectively ending the game.
                        gameOver = playerDead(playerRoom);
                    } else {
                        // User entered an invalid room number. Slap their wrists again.
                        System.out.printf("\nYou think you can teleport? Try again...\n");
                    }
                    break;
                    
                case "S":   // User wants to shoot
                    // Make sure the player has enough arrows first
                    if (arrowsLeft > 0) {
                        // Get which room number they wish to shoot into.
                        command = console.readLine("Which room?");

                        // Parse out the room number and catch an exceptions where
                        // they may have entered a non-number.
                        try {
                            adjRoom = Integer.parseInt(command);
                        } catch (NumberFormatException e) {
                            // Slap their wrists for being bad!
                            System.out.printf("You entered an invalid number. Please try again.\n");
                            
                            break;  // Break out of the switch and go back to the top.
                        }               

                        // Verify if it's a valid room to move to.
                        if (rooms[playerRoom].validRoom(adjRoom)) {
                            // Decreate the arrow count since the shot an arrow 
                            // into a valid adjacent room.
                            arrowsLeft--;
                            
                            // Check if that room has the Wumpus.
                            // gameOver will be true if they did.
                            gameOver = shootRoom(adjRoom-1);

                            // Check if the game is over.
                            if (gameOver) {
                                // THEY KILLED THE WUMPUS! THEY WIN!!!
                                System.out.printf("You shot the Wumpus!   ** You Win! **\n");
                                System.out.printf("\nEnjoy your fame!");
                            } else {
                                // They shot into an empty room. Np dead Wumpus.
                                // Check if arrow count is 0. If so, game is over and they lose!
                                if (arrowsLeft == 0) {
                                    // Wumpus has a free lunch as the user is out of arrows.
                                    System.out.printf("You ran out of arrows and the Wumpus found you and had you for lunch.\n");
                                    System.out.printf("Better luck next time!\n");
                                    
                                    // Game is over.
                                    gameOver = true;
                                }
                            }
                        } else {
                            // Slap their wrists again. They entered a non-adjacent room number. Tsk, tsk!
                            System.out.printf("Hey. You can't shoot into that room. Try again...\n");
                        }
                    }
                    break;
                    
                default:    
                    // Uh oh. User enter an invalid command. Admonish the user for being stupid.
                    System.out.printf("\nWrong command buddy! Can't you read directions? Try again.\n");
            }
        }
    }
    
    /**
     * Describes the room based on the room number given.
     * 
     * @param room The room number whose description needs printing.
     */
    private void describeRoom(int room) {
        // Print out the room description
        System.out.printf("You are in room %d\n", room+1);  // Need to add 1 since this is zero-based.
        System.out.printf("You have %d arrows left.\n", arrowsLeft);    // print number of arrows left.
        System.out.printf("%s\n", rooms[room].getRoomDesc());   // Print out the room description next.
        
        // Now print out what adjacent rooms are off the current room.
        System.out.printf("There are tunnels to rooms %d, %d, and %d.\n", rooms[room].getAdjRoom1(), rooms[room].getAdjRoom2(), 
                rooms[room].getAdjRoom3());

        // Check to see if there are any dangers present in the other three rooms.
        printDanger(room);
    }

    // This is called when the player wants to shoot into a room.
    // It receives the room number to shoot into.
    /**
     * Determines if the user can shoot into this room. If they can,
     * it will also look to see if the user shoots into the same room
     * as the Wumpus. If that is the case, then the game is won.
     * 
     * @param room The room number the player wishes to shoot into
     * @return Returns whether the Wumpus is dead or alive
     */
    private boolean shootRoom(int room) {
        boolean wumpusDead; // Did the player kill the Wumpus?
        
        // See what is in the room they shot into.
        Room.Danger danger = rooms[room].getDanger();
        
        // Check to see what danger, if any, is in the adjacent room.
        switch (danger) {
            case WUMPUS:
                // WOOT!!! The player killed the Wumpus. They WIN!
                System.out.printf("Your arrow goes down the tunnel and finds its mark!\n");
                wumpusDead = true;
                break;
                
            default:
                // Well, they guessed wrong. The Wumpus wasn't in that room. Arrow is lost.
                System.out.printf("Your arrow goes down the tunnel and is lost. You missed.\n");
                wumpusDead = false;
        }
        
        // Return whether the Wumpus is dead (true) or alive (false).
        return wumpusDead;
    }
    
    /**
     * This checks to see if the player died while moving into a room. This can
     * happen if:
     * 
     *      1. The player moves into the same room as the Wumpus
     *      2. The player moves into the same room as a Spider
     *      3. The player moves into the same room as a Pit.
     * 
     * This will print out the appropriate message based on whether they live
     * or die.
     * 
     * @param room The room number the player is moving into.
     * @return True if the player has died, false if they are still alive.
     */
    private boolean playerDead(int room) { 
        boolean isDead; // Is player is dead or alive?
        
        // Get the danger, if any, that exists in the room they
        // moved into.
        Room.Danger danger = rooms[room].getDanger();
        
        // Let's see what danger, if any, is here.
        switch (danger) {
            case NONE:
                // Whew! No danger in this room. The player is safe.
                isDead = false;
                break;
                
            case PIT:
                // AHHHHH! They fell down a pit! Player died. Game over.
                System.out.printf("You fell into a bottomless pit! AHHHHHH!\n");
                isDead = true;
                break;
                
            case WUMPUS:
                // Yummy! The Wumpus had lunch. Player died. Game over.
                System.out.printf("Munch, munch, munch! You made a tasty snack for the Wumpus.\n");
                isDead = true;
                break;
                
            case SPIDER:
                // YUCK! They ran into a spider's web and will become lunch for them.
                // Player died. Game over.
                System.out.printf("The spiders spin a web around you.\n");
                isDead = true;
                break;
                
            default:
                // If we get here, it means there was a programming mistake and we
                // have an extra Danger enum. This is what we call a feature, not a bug. ;)
                System.out.printf("WHOA! The programmer made a mistake. Contact them at once!\n");
                isDead = false;
        }
        
        // Return whether the player is dead (true) or alive (false).
        return isDead;
    }
    
    /**
     * This will give the player clues as to any dangers that lie ahead in any
     * of the adjacent rooms. It will only print one warning for each unique
     * danger that is one room away.
     * 
     * @param room The room number that the player is current in.
     */
    private void printDanger(int room) {
        // Create an array of the adjacent rooms from the given room. This makes
        // it easier to use one set of code to loop through instead of repeating
        // for each adjacent room. This also leaves room for expansion if we wish
        // to add more than 3 adjacent rooms (although we'd still have to change
        // the following initialization of the array).
        int[] adjRooms = { rooms[room].getAdjRoom1(), rooms[room].getAdjRoom2(), rooms[room].getAdjRoom3() };
        
        int pitCount = 0;       // How many pits are in the adjacent rooms.
        int spiderCount = 0;    // How many spiders are in the adjacent rooms.
        int wumpusCount = 0;    // Is there a wumpus in one of the adjacent rooms?

        // Loop through all three adjacent rooms.
        for (int i = 0; i < adjRooms.length; i++) {
            // Get the current adjacent room number from the current room.
            int adjRoom = adjRooms[i];
            
            // Check to see what danger exists in the adjacent room
            switch (rooms[adjRoom-1].getDanger()) {
                case PIT:
                    // There is a pit in one of the rooms. Increase the pit count by one.
                    pitCount++;
                    break;

                case SPIDER:
                    // There is a spider in one of the rooms. Increase the spider count by one.
                    spiderCount++;
                    break;

                case WUMPUS:
                    // Uh oh. The Wumpus is in one of the adjacent rooms. Increase the count.
                    wumpusCount++;
                    break;
            }
        }
        
        // Now, check over each of the counts to see if we need to print out a
        // warning of danger lurking in one or more of the adjacent rooms.
        if (pitCount > 0) {
            // One or more pits are in the adjacent rooms.
            System.out.printf("You smell a dank odor.\n");
        }
        
        if (spiderCount > 0) {
            // One or more spiders are in the adjacent rooms.
            System.out.printf("You hear a faint clicking noise.\n");
        }
        
        if (wumpusCount > 0) {
            // The wumpus is in one of the adjacent rooms. Proceed with caution.
            // Might I suggest the user shoots an arrow?
            System.out.printf("You smell some nasty Wumpus!\n");
        }
        
        
    }
    
    // This is used to randomlly place a danger in a room.
    // numRooms is the number of potential rooms to place a danger in
    // danger is the type of danger to place
    // dangerCount is the number of that danger to place.
    /**
     * This is used to randomly place a danger into the room.
     * 
     * @param numRooms The number of potential rooms to place a danger in
     * @param danger The type of danger to place
     * @param dangerCount The number of the danger type to place
     */
    private void placeDanger(int numRooms, Room.Danger danger, int dangerCount) {
        int room;   // This will hold a random room number for the danger.
        int dangerPlaced = 0;   // Count how many dangers we've placed.
        
        // Loop until we've placed "dangerCount" dangers.
        while(dangerPlaced != dangerCount) {
            room = randomRoom(numRooms);
            
            // Check to see if the room doesn't already have danger in it.
            if (rooms[room-1].getDanger() == Room.Danger.NONE) {
                // It's empty. Place the danger!
                rooms[room-1].setDanger(danger);
                
                // Increase the count of danger's placed
                dangerPlaced++;
            }
        }
    }
    
    // This will generate a random room, ignoring room 1 since we don't place
    // dangers in the starting room. That would be a little unfair, right?
    /**
     * Generate a random room, excluding the room #1 since that is the starting point
     * 
     * @param numRooms The number of rooms to random generate (from 1 to numRooms)
     * @return Returns a randomly chosen room number from 1 to numRooms
     */
    private int randomRoom(int numRooms) {
        // Get a random number generator
        Random randRoom = new Random();
        int room;   // This will hold the random room number
        
        do {
            // Generate a random number from 0 to numRooms-1.
            // We add one to it so it's from 1 to numRooms.
            room = randRoom.nextInt(numRooms) + 1;
        } while (room == 1);    // Keep looping until room doesn't equal 1.
        
        // Return the room #.
        return room;
    }
}
