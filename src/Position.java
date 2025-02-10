import java.io.Serializable;

public class Position implements Serializable
{
    private static final long serialVersionUID = 1L; // Ensures compatibility during deserialization

    private int y;
    private int x;

    public Position(int y, int x)
    {
        this.y = y;
        this.x = x;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }
}
