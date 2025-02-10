public class Rook extends Piece
{

    public Rook(PieceColor color, Position position)
    {
        super(color, position);
    }

    @Override
    public boolean isValidMove(Position newPosition, Piece[][] board)
    {
        // if you move the rook in Straight horizontal direction
        if (position.getY() == newPosition.getY())
        {
            // here we want to check if there is any opponent's piece between position & newPosition
            int columnStart = Math.min(position.getX(), newPosition.getX()) + 1;
            int columnEnd = Math.max(position.getX(), newPosition.getX());

            for (int i = columnStart; i < columnEnd; i++)
            {
                if (board[position.getY()][i] != null)
                {
                    return false;
                }
            }
        }

        else if (position.getX() == newPosition.getX())
        {
            // if you move the rook in Straight vertical direction
            // here we want to check if there is any opponent's piece between position & newPosition
            int rowStart = Math.min(position.getY(), newPosition.getY()) + 1;
            int rowEnd = Math.max(position.getY(), newPosition.getY());

            for (int i = rowStart; i < rowEnd; i++)
            {
                if (board[i][position.getX()] != null)
                {
                    return false;
                }
            }
        }

        else
        {
            return false;
        }

        Piece destinationPiece = board[newPosition.getY()][newPosition.getX()];

        if (destinationPiece == null) return true;
        else if (destinationPiece.getColor() != this.color) return true;
        return false;
    }
}
