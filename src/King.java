// King.java
public class King extends Piece
{

    public King(PieceColor color, Position position)
    {
        super(color, position);
    }

    @Override
    public boolean isValidMove(Position newPosition, Piece[][] board)
    {
        int rowDiff = Math.abs(position.getY() - newPosition.getY());
        int colDiff = Math.abs(position.getX() - newPosition.getX());

        boolean isOneSquareMove = rowDiff <= 1 && colDiff <= 1 && !(rowDiff == 0 && colDiff == 0);

        if (!isOneSquareMove)
        {
            return false;
        }

        Piece destinationPiece = board[newPosition.getY()][newPosition.getX()];
        return destinationPiece == null || destinationPiece.getColor() != this.getColor();
    }
}
