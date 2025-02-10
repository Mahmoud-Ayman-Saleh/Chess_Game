// GameBoard.java
// Class representing the chess game board
public class GameBoard
{
    // 2D array representing the chess board with pieces
    private Piece[][] board;

    // Constructor to initialize the game board
    public GameBoard()
    {
        board = new Piece[8][8];
        setupPieces();
    }

    public Piece[][] getBoard()
    {
        return board;
    }


    public Piece getPiece(int y, int x)
    {
        return board[y][x];
    }

    public void setPiece(int y, int x, Piece piece)
    {
        board[y][x] = piece;
        if (piece != null)
        {
            piece.setPosition(new Position(y, x));
        }
    }

    // Method to set up the initial chess board configuration with pieces
    private void setupPieces()
    {
        for (int i = 0; i < 8; i++)
        {
            board[1][i] = new Pawn(PieceColor.BLACK, new Position(1, i));
            board[6][i] = new Pawn(PieceColor.WHITE, new Position(6, i));
        }

        // Place Rooks
        board[0][0] = new Rook(PieceColor.BLACK, new Position(0, 0));
        board[0][7] = new Rook(PieceColor.BLACK, new Position(0, 7));
        board[7][0] = new Rook(PieceColor.WHITE, new Position(7, 0));
        board[7][7] = new Rook(PieceColor.WHITE, new Position(7, 7));
        // Place Knights
        board[0][1] = new Knight(PieceColor.BLACK, new Position(0, 1));
        board[0][6] = new Knight(PieceColor.BLACK, new Position(0, 6));
        board[7][1] = new Knight(PieceColor.WHITE, new Position(7, 1));
        board[7][6] = new Knight(PieceColor.WHITE, new Position(7, 6));
        // Place Bishops
        board[0][2] = new Bishop(PieceColor.BLACK, new Position(0, 2));
        board[0][5] = new Bishop(PieceColor.BLACK, new Position(0, 5));
        board[7][2] = new Bishop(PieceColor.WHITE, new Position(7, 2));
        board[7][5] = new Bishop(PieceColor.WHITE, new Position(7, 5));
        // Place Queens
        board[0][3] = new Queen(PieceColor.BLACK, new Position(0, 3));
        board[7][3] = new Queen(PieceColor.WHITE, new Position(7, 3));
        // Place Kings
        board[0][4] = new King(PieceColor.BLACK, new Position(0, 4));
        board[7][4] = new King(PieceColor.WHITE, new Position(7, 4));
    }


    // Method to move a piece from one position to another
    public void movePiece(Position start, Position end)
    {
        if (board[start.getY()][start.getX()] != null && board[start.getY()][start.getX()].isValidMove(end, board))
        {
            // perform the move
            board[end.getY()][end.getX()] = board[start.getY()][start.getX()];

            // Update the piece's position
            board[end.getY()][end.getX()].setPosition(end);

            // clear the start position
            board[start.getY()][start.getX()] = null;
        }
    }

}
