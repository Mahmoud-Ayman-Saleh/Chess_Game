// Pawn.java
// Class representing a Pawn piece, inherits from Piece
public class Pawn extends Piece
{

    // Constructor to initialize the Pawn's color
    public Pawn(PieceColor color, Position position)
    {
        super(color, position);
    }

    // Method to check if the move for the Pawn is valid

    @Override
    public boolean isValidMove(Position newPosition, Piece[][] board)
    {
        int forwardDirection = color == PieceColor.WHITE ? -1 : 1;
        int rowDiff = (newPosition.getY() - position.getY()) * forwardDirection;
        int colDiff = newPosition.getX() - position.getX();

        if (colDiff == 0 && rowDiff == 1 && board[newPosition.getY()][newPosition.getX()] == null)
        {
            return true;
        }

        boolean isStartingPosition = (color == PieceColor.WHITE && position.getY() == 6) ||
                (color == PieceColor.BLACK && position.getY() == 1);
        if (colDiff == 0 && rowDiff == 2 && isStartingPosition && board[newPosition.getY()][newPosition.getX()] == null)
        {
            int middleRow = position.getY() + forwardDirection;
            if (board[middleRow][position.getX()] == null)
            {
                return true;
            }
        }

        if (Math.abs(colDiff) == 1 && rowDiff == 1 && board[newPosition.getY()][newPosition.getX()] != null &&
                board[newPosition.getY()][newPosition.getX()].color != this.color)
        {
            return true;
        }

        return false;
    }
}
