import java.io.Serializable;

public class GameMessage implements Serializable
{
    private static final long serialVersionUID = 1L;

    public enum MessageType
    {
        MOVE,         // for normal moves
        GAME_OVER,    // indicates game over (someone won)
        RESTART,      // indicates a restart request
        TERMINATE     // indicates the game should close
    }

    private MessageType type;
    private Position[] move;  // Only used if type == MOVE
    private String winner;    // Only used if type == GAME_OVER

    // Constructors for different message types
    public GameMessage(MessageType type, Position[] move)
    {
        this.type = type;
        this.move = move;
    }

    public GameMessage(MessageType type, String winner)
    {
        this.type = type;
        this.winner = winner;
    }

    public MessageType getType()
    {
        return type;
    }

    public Position[] getMove()
    {
        return move;
    }

    public String getWinner()
    {
        return winner;
    }
}
