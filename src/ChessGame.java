import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

// ChessGame.java
public class ChessGame
{
    private GameBoard board;
    private boolean whiteTurn = true;

    public ChessGame()
    {
        this.board = new GameBoard();
    }

    public boolean makeMove(Position start, Position end)
    {
            Piece movingPiece = board.getPiece(start.getY(), start.getX());
        if (movingPiece == null || movingPiece.getColor() != (whiteTurn ? PieceColor.WHITE : PieceColor.BLACK))
            {
                return false;
            }

            if (movingPiece.isValidMove(end, board.getBoard()))
            {
                board.movePiece(start, end);
                // switch turns
                whiteTurn = !whiteTurn;
                return true;
            }

            return false;
    }

    public boolean isInCheck(PieceColor kingColor)
    {
        Position kingPosition = findKingPosition(kingColor);
        for (int row = 0; row < board.getBoard().length; row++)
        {
            for (int col = 0; col < board.getBoard()[row].length; col++)
            {
                Piece piece = board.getPiece(row, col);
                if (piece != null && piece.getColor() != kingColor)
                {
                    if (piece.isValidMove(kingPosition, board.getBoard()))
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public Position findKingPosition(PieceColor kingColor)
    {
        for (int row = 0; row < board.getBoard().length; row++)
        {
            for (int col = 0; col < board.getBoard()[row].length; col++)
            {
                Piece piece = board.getPiece(row, col);
                if (piece instanceof King && piece.getColor() == kingColor)
                {
                    return new Position(row, col);
                }
            }
        }

        throw new RuntimeException("King not found");
    }

    public boolean isCheckmate(PieceColor kingColor)
    {
        if (!isInCheck(kingColor))
        {
            return false;
        }
        Position kingPosition = findKingPosition(kingColor);
        King king = (King) board.getPiece(kingPosition.getY(), kingPosition.getX());
        for (int i = -1; i <= 1; i++)
        {
            for (int j = -1; j <= 1; j++)
            {
                if (i == 0 && j == 0)
                {
                    continue; // skip current position of king
                }
                Position newPosition= new Position(kingPosition.getY() + i, kingPosition.getX() + j);
                if (isPositionOnBoard(newPosition) && king.isValidMove(newPosition, board.getBoard())
                        && !wouldBeInCheckAfterMove(kingColor, kingPosition, newPosition))
                {
                    return false;
                }
            }
        }

        return true;

    }

    private boolean isPositionOnBoard(Position position)
    {
        if (position.getY() >= 0 && board.getBoard().length > position.getY() &&
        position.getX() >= 0 && board.getBoard()[0].length > position.getX())
        {
            return true;
        }

        return false;
    }

    private boolean wouldBeInCheckAfterMove(PieceColor kingColor, Position from, Position to)
    {
        // Simulate the move temporarily
        Piece temp = board.getPiece(to.getY(), to.getX());
        board.setPiece(to.getY(), to.getX(), board.getPiece(from.getY(), from.getX()));
        board.setPiece(from.getY(), from.getX(), null);

        boolean inCheck = isInCheck(kingColor);

        // Undo the move
        board.setPiece(from.getY(), from.getX(), board.getPiece(to.getY(), to.getX()));
        board.setPiece(to.getY(), to.getX(), temp);

        return inCheck;

    }

    public GameBoard getBoard()
    {
        return this.board;
    }

    public void resetGame()
    {
        this.board = new GameBoard(); // Re-initialize the board
        this.whiteTurn = true; // Reset turn to white
    }

    public PieceColor getCurrentPlayerColor()
    {
        return whiteTurn ? PieceColor.WHITE : PieceColor.BLACK;
    }

    private Position selectedPosition; // Tracks the currently selected piece's position

    public boolean isPieceSelected()
    {
        return selectedPosition != null;
    }

    public boolean handleSquareSelection(int row, int col)
    {
        // If selectedPosition is null, it means no piece has been selected yet.
        if (selectedPosition == null)
        {
            // Attempt to select a piece
            Piece selectedPiece = board.getPiece(row, col);
            if (selectedPiece != null
                    && selectedPiece.getColor() == (whiteTurn ? PieceColor.WHITE : PieceColor.BLACK)) {
                selectedPosition = new Position(row, col);
                return false; // Indicate a piece was selected but not moved
            }
        }
        else
        {
            // Attempt to move the selected piece
            boolean moveMade = makeMove(selectedPosition, new Position(row, col));
            selectedPosition = null; // Reset selection regardless of move success
            return moveMade; // Return true if a move was successfully made
        }
        return false; // Return false if no piece was selected or move was not made
    }

    public List<Position> getLegalMovesForPieceAt(Position position)
    {
        Piece selectedPiece = board.getPiece(position.getY(), position.getX());
        if (selectedPiece == null)
            return new ArrayList<>();

        List<Position> legalMoves = new ArrayList<>();
        switch (selectedPiece.getClass().getSimpleName())
        {
            case "Pawn":
                addPawnMoves(position, selectedPiece.getColor(), legalMoves);
                break;
            case "Rook":
                addLineMoves(position, new int[][] { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } }, legalMoves);
                break;
            case "Knight":
                addSingleMoves(position, new int[][] { { 2, 1 }, { 2, -1 }, { -2, 1 }, { -2, -1 }, { 1, 2 }, { -1, 2 },
                        { 1, -2 }, { -1, -2 } }, legalMoves);
                break;
            case "Bishop":
                addLineMoves(position, new int[][] { { 1, 1 }, { -1, -1 }, { 1, -1 }, { -1, 1 } }, legalMoves);
                break;
            case "Queen":
                addLineMoves(position, new int[][] { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 }, { 1, 1 }, { -1, -1 },
                        { 1, -1 }, { -1, 1 } }, legalMoves);
                break;
            case "King":
                addSingleMoves(position, new int[][] { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 }, { 1, 1 }, { -1, -1 },
                        { 1, -1 }, { -1, 1 } }, legalMoves);
                break;
        }
        return legalMoves;
    }

    private void addLineMoves(Position position, int[][] directions, List<Position> legalMoves)
    {
        for (int[] d : directions) {
            Position newPos = new Position(position.getY() + d[0], position.getX() + d[1]);
            while (isPositionOnBoard(newPos)) {
                if (board.getPiece(newPos.getY(), newPos.getX()) == null) {
                    legalMoves.add(new Position(newPos.getY(), newPos.getX()));
                    newPos = new Position(newPos.getY() + d[0], newPos.getX() + d[1]);
                } else {
                    if (board.getPiece(newPos.getY(), newPos.getX()).getColor() != board
                            .getPiece(position.getY(), position.getX()).getColor()) {
                        legalMoves.add(newPos);
                    }
                    break;
                }
            }
        }
    }

    private void addSingleMoves(Position position, int[][] moves, List<Position> legalMoves) {
        for (int[] move : moves) {
            Position newPos = new Position(position.getY() + move[0], position.getX() + move[1]);
            if (isPositionOnBoard(newPos) && (board.getPiece(newPos.getY(), newPos.getX()) == null ||
                    board.getPiece(newPos.getY(), newPos.getX()).getColor() != board
                            .getPiece(position.getY(), position.getX()).getColor())) {
                legalMoves.add(newPos);
            }
        }
    }

    private void addPawnMoves(Position position, PieceColor color, List<Position> legalMoves) {
        int direction = color == PieceColor.WHITE ? -1 : 1;
        Position newPos = new Position(position.getY() + direction, position.getX());
        if (isPositionOnBoard(newPos) && board.getPiece(newPos.getY(), newPos.getX()) == null) {
            legalMoves.add(newPos);
        }

        if ((color == PieceColor.WHITE && position.getY() == 6)
                || (color == PieceColor.BLACK && position.getY() == 1)) {
            newPos = new Position(position.getY() + 2 * direction, position.getX());
            Position intermediatePos = new Position(position.getY() + direction, position.getX());
            if (isPositionOnBoard(newPos) && board.getPiece(newPos.getY(), newPos.getX()) == null
                    && board.getPiece(intermediatePos.getY(), intermediatePos.getX()) == null) {
                legalMoves.add(newPos);
            }
        }

        int[] captureCols = { position.getX() - 1, position.getX() + 1 };
        for (int col : captureCols) {
            newPos = new Position(position.getY() + direction, col);
            if (isPositionOnBoard(newPos) && board.getPiece(newPos.getY(), newPos.getX()) != null &&
                    board.getPiece(newPos.getY(), newPos.getX()).getColor() != color) {
                legalMoves.add(newPos);
            }
        }
    }


}
