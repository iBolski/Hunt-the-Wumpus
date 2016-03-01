package huntthewumpus;

/**
 * This defines a room in the Hunt the Wumpus game.
 * 
 * @author Ivan Samuelson
 */
public final class Room {
    // Enumerate the different kinds of danger the player can experience
    public enum Danger { NONE, WUMPUS, SPIDER, PIT; }
    
    private int roomNo; // This is the current room number
    private int adjRoom1, adjRoom2, adjRoom3;   // There are three adjacent rooms from this room
    private String roomDesc;    // Description of the current room.
    private Danger danger;    // This will hold what danger is in the room.
                              // null = None, Wumpus, Pit, Spider - based off of Danger enum.
    
    // Default constructor. Doesn't do anything except set the danger to none, otherwise
    // it will be null and we don't want that.
    public Room() {
        setDanger(Danger.NONE);
    };
    
    /**
     * Overloaded constructor that will create a room with supplied values except for danger.
     * 
     * @param roomNo What room number is this room
     * @param adjRoom1 The first room that is adjacent to this room
     * @param adjRoom2 The second room that is adjacent to this room
     * @param adjRoom3 The third room that is adjacent to this room
     * @param roomDesc The description of this room
     */
    public Room(int roomNo, int adjRoom1, int adjRoom2, int adjRoom3, String roomDesc) {
        this(roomNo, adjRoom1, adjRoom2, adjRoom3, roomDesc, Danger.NONE);
    }

    
    /**
     * Another overloaded constructor. This assigns all the values, including Danger.
     * 
     * @param roomNo What room number is this room
     * @param adjRoom1 The first room that is adjacent to this room
     * @param adjRoom2 The second room that is adjacent to this room
     * @param adjRoom3 The third room that is adjacent to this room
     * @param roomDesc The description of this room
     * @param danger What danger lies in this room.
     */
    public Room(int roomNo, int adjRoom1, int adjRoom2, int adjRoom3, String roomDesc, Danger danger) {
        setRoom(roomNo);
        setAdjRoom1(adjRoom1);
        setAdjRoom2(adjRoom2);
        setAdjRoom3(adjRoom3);
        setRoomDesc(roomDesc);
        setDanger(danger);
    }
    
    /**
     * Check to see if the room given to move or shoot into is valid from the
     * current room the player is in.
     * 
     * @param room The room the player is moving/shooting into to
     * @return True if the room is adjacent to the current room, false otherwise.
     */
    public boolean validRoom(int room) {
        return (room == adjRoom1 || room == adjRoom2 || room == adjRoom3);
    }
    
    // Public getters and setters
    
    /**
     * Sets the rooms danger
     * 
     * @param danger What danger to set this room to.
     */
    public void setDanger(Danger danger) {
        this.danger = danger;
    }
    
    /**
     * Gets the rooms danger
     * 
     * @return Danger that is assigned to the room
     */
    public Danger getDanger() {
        return this.danger;
    }
    
    /**
     * Sets this rooms number.
     * 
     * @param roomNum What room number is this room
     */
    public void setRoom(int roomNum) {
        this.roomNo = roomNum;
    }
    
   /**
     * Gets the rooms number
     * 
     * @return The rooms number
     */
    public int getRoom() {
        return this.roomNo;
    }
    
    /**
     * Sets the first adjacent room value from this room
     * 
     * @param adjRoom The room number of the adjacent room
     */
    public void setAdjRoom1(int adjRoom) {
        this.adjRoom1 = adjRoom;
    }

    /**
     * Sets the second adjacent room value from this room
     * 
     * @param adjRoom The room number of the adjacent room
     */
    public void setAdjRoom2(int adjRoom) {
        this.adjRoom2 = adjRoom;
    }

    /**
     * Sets the third adjacent room value from this room
     * 
     * @param adjRoom The room number of the adjacent room
     */
    public void setAdjRoom3(int adjRoom) {
        this.adjRoom3 = adjRoom;
    }

    /**
     * Gets the first adjacent room value from this room
     * 
     * @return The room number of the first adjacent room
     */
    public int getAdjRoom1() {
        return this.adjRoom1;
    }

    /**
     * Gets the second adjacent room value from this room
     * 
     * @return The room number of the second adjacent room
     */
    public int getAdjRoom2() {
        return this.adjRoom2;
    }

    /**
     * Gets the third adjacent room value from this room
     * 
     * @return The room number of the third adjacent room
     */
    public int getAdjRoom3() {
        return this.adjRoom3;
    }
    
    /**
     * Sets the rooms description
     * 
     * @param roomDesc The description of the room
     */
    public void setRoomDesc(String roomDesc) {
        this.roomDesc = roomDesc;
    }
    
    /**
     * Gets the rooms description
     * 
     * @return The description of the room
     */
    public String getRoomDesc() {
        return this.roomDesc;
    }
}
