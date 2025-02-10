// piece.java
// Abstract base class representing a generic chess piece
public abstract class Piece
{
    protected Position position;
    protected PieceColor color;

    // Constructor to initialize the piece's color
    public Piece(PieceColor color, Position position)
    {
        this.color = color;
        this.position = position;
    }

    public PieceColor getColor()
    {
        return color;
    }

    public void setPosition(Position position)
    {
        this.position = position;
    }

    public Position getPosition()
    {
        return position;
    }

    // Abstract method to be implemented by each specific piece to check valid moves
    public abstract boolean isValidMove(Position newPosition, Piece[][] board);
}
