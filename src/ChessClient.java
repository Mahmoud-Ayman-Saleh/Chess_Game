// ChessClient.java
import javax.swing.*;
import java.io.*;
import java.net.*;

public class ChessClient extends Thread
{
    private ChessGUI gui;
    private String serverAddress;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public ChessClient(ChessGUI gui, String serverAddress)
    {
        this.gui = gui;
        this.serverAddress = serverAddress;
    }

    @Override
    public void run()
    {
        try
        {
            socket = new Socket(serverAddress, 5000);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(gui, "Connected to server!"));

            // Initialize the board after connection
            // SwingUtilities.invokeLater(() -> gui.initializeBoard());

            while (true)
            {
                Object obj = in.readObject();
                if (obj instanceof GameMessage)
                {
                    GameMessage msg = (GameMessage) obj;
                    switch (msg.getType())
                    {
                        case MOVE:
                            Position[] move = msg.getMove();
                            SwingUtilities.invokeLater(() -> gui.handleNetworkMove(move[0], move[1]));
                            break;
                        case GAME_OVER:
                            SwingUtilities.invokeLater(() -> {
                                String winner = msg.getWinner();
                                int response = JOptionPane.showConfirmDialog(gui,
                                        winner + " wins! Do you want to play again?", "Game Over",
                                        JOptionPane.YES_NO_OPTION);
                                if (response == JOptionPane.YES_OPTION)
                                {
                                    gui.resetGame();
                                }
                                else
                                {
                                    // Optionally, send a TERMINATE message back if desired.
                                    JOptionPane.showMessageDialog(gui, "Opponent has left the game. Exiting.");
                                    System.exit(0);
                                }
                            });
                            break;
                        case TERMINATE:
                            SwingUtilities.invokeLater(() ->
                            {
                                JOptionPane.showMessageDialog(gui, "Your opponent has left the game.");
                                System.exit(0);
                            });
                            break;
                        // You can also add RESTART and other cases if needed.
                    }
                }
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void sendMove(Position start, Position end)
    {
        try
        {
            GameMessage msg = new GameMessage(GameMessage.MessageType.MOVE, new Position[]{start, end});
            out.writeObject(msg);
            out.flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    public void sendGameOver(String winner)
    {
        try
        {
            GameMessage msg = new GameMessage(GameMessage.MessageType.GAME_OVER, winner);
            out.writeObject(msg);
            out.flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    public void sendTerminate()
    {
        try
        {
            // You can send a message with type TERMINATE and a simple message string.
            GameMessage msg = new GameMessage(GameMessage.MessageType.TERMINATE, "Player terminated");
            out.writeObject(msg);
            out.flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }





}