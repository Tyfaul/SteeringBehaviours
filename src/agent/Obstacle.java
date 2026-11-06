package agent;

public class Obstacle {

    private final double PositionX;
    private final double PositionY;
    private final int Size;

    public Obstacle(double PositionX, double PositionY, int Size) {
        this.PositionX = PositionX;
        this.PositionY = PositionY;
        this.Size = Math.max(1, Size);
    }

    public double GetPositionX() {
        return PositionX;
    }

    public double GetPositionY() {
        return PositionY;
    }

    public int GetSize() {
        return Size;
    }
}