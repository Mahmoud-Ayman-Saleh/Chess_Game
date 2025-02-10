// ChessSquareComponent.java
import javax.swing.*;
import java.awt.*;

// ChessSquareComponent.java (updated)
public class ChessSquareComponent extends JButton
{
    private int row;
    private int col;

    public ChessSquareComponent(int row, int col)
    {
        this.row = row;
        this.col = col;
        initButton();
    }

    private void initButton()
    {
        setPreferredSize(new Dimension(64, 64));
        if ((row + col) % 2 == 0)
        {
            setBackground(new Color(245, 229, 203)); // Light color
        }
        else
        {
            setBackground(new Color(112, 74, 46)); // Dark color
        }
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
        setFont(new Font("Serif", Font.BOLD, 29));
    }

    public void setPieceSymbol(String symbol, Color color)
    {
        this.setText(symbol);
        this.setForeground(color);
    }

    public void clearPieceSymbol()
    {
        this.setText("");
    }
}