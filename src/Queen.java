// Queen.java
public class Queen extends Piece
{

    public Queen(PieceColor color, Position position)
    {
        super(color, position);
    }

    @Override
    public boolean isValidMove(Position newPosition, Piece[][] board)
    {
        if (newPosition.equals(this.position))
        {
            return false;
        }

        int rowDiff = Math.abs(newPosition.getY() - this.position.getY());
        int colDiff = Math.abs(newPosition.getX() - this.position.getX());

        boolean straightLine = this.position.getY() == newPosition.getY()
                || this.position.getX() == newPosition.getX();

        boolean diagonal = rowDiff == colDiff;

        if (!straightLine && !diagonal)
        {
            return false;
        }

        int rowDirection = Integer.compare(newPosition.getY(), this.position.getY());
        int colDirection = Integer.compare(newPosition.getX(), this.position.getX());

        int currentRow = this.position.getY() + rowDirection;
        int currentCol = this.position.getX() + colDirection;
        while (currentRow != newPosition.getY() || currentCol != newPosition.getX())
        {
            if (board[currentRow][currentCol] != null)
            {
                return false;
            }
            currentRow += rowDirection;
            currentCol += colDirection;
        }

        Piece destinationPiece = board[newPosition.getY()][newPosition.getX()];
        return destinationPiece == null || destinationPiece.getColor() != this.getColor();
    }
}
