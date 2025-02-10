// Bishop.java
public class Bishop extends Piece
{

    public Bishop(PieceColor color, Position position)
    {
        super(color, position);
    }

    @Override
    public boolean isValidMove(Position newPosition, Piece[][] board)
    {
        int rowDiff = Math.abs(position.getY() - newPosition.getY());
        int colDiff = Math.abs(position.getX() - newPosition.getX());

        if (rowDiff != colDiff)
        {
            return false;
        }

        int rowStep = newPosition.getY() > position.getY() ? 1 : -1;
        int colStep = newPosition.getX() > position.getX() ? 1 : -1;
        int steps = rowDiff - 1;

        for (int i = 1; i <= steps; i++)
        {
            if (board[position.getY() + i * rowStep][position.getX() + i * colStep] != null)
            {
                return false;
            }
        }

        Piece destinationPiece = board[newPosition.getY()][newPosition.getX()];
        if (destinationPiece == null)
        {
            return true;
        }
        else if (destinationPiece.getColor() != this.getColor())
        {
            return true;
        }

        return false;
    }
}
