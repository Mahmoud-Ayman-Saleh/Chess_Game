// ChessGUI.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

// Class representing the GUI for the chess game
public class ChessGUI extends JFrame
{
    private final ChessSquareComponent[][] squares = new ChessSquareComponent[8][8];
    private final ChessGame game = new ChessGame();
    private ChessServer chessServer;
    private ChessClient chessClient;
    private Position selectedPosition;

    private final Map<Class<? extends Piece>, String> pieceUnicodeMap = new HashMap<>()
    {
        {
            put(Pawn.class, "\u265F");
            put(Rook.class, "\u265C");
            put(Knight.class, "\u265E");
            put(Bishop.class, "\u265D");
            put(Queen.class, "\u265B");
            put(King.class, "\u265A");
        }
    };

    // Constructor to initialize the GUI and game board
    public ChessGUI()
    {
        setTitle("Chess Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(8, 8));

        // Initialize the board
        initializeBoard();

        // Add game mode selection dialog
        String[] options = {"Opponent (Local)", "Server (Network)"};
        int choice = JOptionPane.showOptionDialog(this, "Choose game mode:", "Chess Game",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (choice == 0)
        {
            // Local game
            // No additional setup needed
        }
        else if (choice == 1)
        {
            // Network game
            startNetworkGame();
        }

        addGameResetOption();
        pack();
        setVisible(true);
    }


    private void startNetworkGame()
    {
        String[] options = {"Host Game", "Join Game"};
        int choice = JOptionPane.showOptionDialog(this, "Host or join a game?", "Network Game",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (choice == 0)
        {
            // Host game: store the instance in the chessServer field
            chessServer = new ChessServer(this);
            chessServer.start();
        }
        else if (choice == 1)
        {
            // Join game: store the instance in the chessClient field
            String serverAddress = JOptionPane.showInputDialog(this, "Enter server IP address:");
            if (serverAddress != null && !serverAddress.isEmpty())
            {
                chessClient = new ChessClient(this, serverAddress);
                chessClient.start();
            }
        }
    }


    // Method to initialize the chess board GUI

    void initializeBoard()
    {
        System.out.println("Initializing board...");
        getContentPane().removeAll(); // Clear any existing components
        for (int row = 0; row < squares.length; row++)
        {
            for (int col = 0; col < squares[row].length; col++)
            {
                final int finalRow = row;
                final int finalCol = col;
                ChessSquareComponent square = new ChessSquareComponent(row, col);
                square.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        handleSquareClick(finalRow, finalCol);
                    }
                });
                add(square);
                squares[row][col] = square;
                System.out.println("Added square at (" + row + ", " + col + ")");
            }
        }
        refreshBoard();
        revalidate();
        repaint();
    }



    private void refreshBoard()
    {
        GameBoard board = game.getBoard();
        for (int row = 0; row < 8; row++)
        {
            for (int col = 0; col < 8; col++)
            {
                Piece piece = board.getPiece(row, col);
                if (piece != null)
                {
                    String symbol = pieceUnicodeMap.get(piece.getClass());
                    Color color = (piece.getColor() == PieceColor.WHITE) ? Color.WHITE : Color.BLACK;
                    squares[row][col].setPieceSymbol(symbol, color);
                }
                else
                {
                    squares[row][col].clearPieceSymbol();
                }
            }
        }
        revalidate(); // Refresh the GUI layout
        repaint(); // Repaint the GUI
    }

    // Method to handle cell clicks on the board


    private void handleSquareClick(int row, int col)
    {
        // If no piece is selected, try to select a piece
        if (selectedPosition == null)
        {
            Piece selectedPiece = game.getBoard().getPiece(row, col);
            if (selectedPiece != null && selectedPiece.getColor() == game.getCurrentPlayerColor())
            {
                selectedPosition = new Position(row, col); // Store the selected piece's position
                highlightLegalMoves(selectedPosition); // Highlight legal moves for the selected piece
            }
        }
        else
        {
            // If a piece is already selected, try to move it
            boolean moveResult = game.makeMove(selectedPosition, new Position(row, col));
            if (moveResult)
            {
                // If the move is successful, refresh the board and check game state
                refreshBoard();
                checkGameState();
                checkGameOver();

                // Send move over the network (if in network mode)
                if (chessServer != null)
                {
                    chessServer.sendMove(selectedPosition, new Position(row, col));
                }
                else if (chessClient != null)
                {
                    chessClient.sendMove(selectedPosition, new Position(row, col));
                }
            }
            // Clear the selected position and highlights
            selectedPosition = null;
            clearHighlights();
        }
        refreshBoard();
    }

    private void checkGameState()
    {
        PieceColor currentPlayer = game.getCurrentPlayerColor(); // This method should return the current player's color
        boolean inCheck = game.isInCheck(currentPlayer);

        if (inCheck)
        {
            JOptionPane.showMessageDialog(this, currentPlayer + " is in check!");
        }
    }

    // ChessGUI.java (updated)
    private void highlightLegalMoves(Position position)
    {
        List<Position> legalMoves = game.getLegalMovesForPieceAt(position);
        for (Position move : legalMoves)
        {
            squares[move.getY()][move.getX()].setBackground(Color.GREEN);
        }
    }

    private void clearHighlights()
    {
        for (int row = 0; row < 8; row++)
        {
            for (int col = 0; col < 8; col++)
            {
                squares[row][col].setBackground((row + col) % 2 == 0 ? new Color(245, 229, 203) : new Color(112, 74, 46));
            }
        }
    }

    private void addGameResetOption()
    {
        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");
        JMenuItem resetItem = new JMenuItem("Reset");
        resetItem.addActionListener(e -> resetGame());
        gameMenu.add(resetItem);
        menuBar.add(gameMenu);
        setJMenuBar(menuBar);
    }

    void resetGame()
    {
        game.resetGame();
        refreshBoard();
    }

    private void checkGameOver()
    {
        if (game.isCheckmate(game.getCurrentPlayerColor()))
        {
            String winner = (game.getCurrentPlayerColor() == PieceColor.WHITE) ? "Black" : "White";
            int response = JOptionPane.showConfirmDialog(this,
                    "Checkmate! " + winner + " wins! Would you like to play again?", "Game Over",
                    JOptionPane.YES_NO_OPTION);

            if (response == JOptionPane.YES_OPTION)
            {
                // Notify the opponent about the game over (optional: you could send a RESTART message instead)
                if (chessServer != null)
                {
                    chessServer.sendGameOver(winner);
                }
                else if (chessClient != null)
                {
                    chessClient.sendGameOver(winner);
                }
                resetGame();
            }
            else
            {
                // The local player chooses not to play again.
                // Send a TERMINATE message to notify the opponent.
                if (chessServer != null)
                {
                    chessServer.sendTerminate();
                }
                else if (chessClient != null)
                {
                    chessClient.sendTerminate();
                }
                // Inform the local user and exit.
                JOptionPane.showMessageDialog(this, "Opponent has left the game. Exiting.");
                System.exit(0);
            }
        }
    }



    public void handleNetworkMove(Position start, Position end)
    {
        game.makeMove(start, end);
        refreshBoard(); // Refresh the board after applying the move
    }

    // Main method to start the game

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() -> {
            ChessGUI gui = new ChessGUI(); // Only one instance is created
        });
    }
}
