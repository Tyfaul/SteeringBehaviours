
package agent;

public class Arrow {

    private double PositionX;
    private double PositionY;
    private double VelocityX;
    private double VelocityY;

    private final double InitialPositionX;
    private final double InitialPositionY;
    private final double InitialVelocityX;
    private final double InitialVelocityY;

    public Arrow(double PositionX, double PositionY, double VelocityX, double VelocityY) {
        this.PositionX = PositionX;
        this.PositionY = PositionY;
        this.VelocityX = VelocityX;
        this.VelocityY = VelocityY;

        InitialPositionX = PositionX;
        InitialPositionY = PositionY;
        InitialVelocityX = VelocityX;
        InitialVelocityY = VelocityY;
    }

    public double GetPositionX() {
        return PositionX;
    }

    public double GetPositionY() {
        return PositionY;
    }

    public double GetVelocityX() {
        return VelocityX;
    }

    public double GetVelocityY() {
        return VelocityY;
    }

    public void Update(double DeltaTime, double MaxPositionX, double MaxPositionY) {
        PositionX += VelocityX * DeltaTime;
        PositionY += VelocityY * DeltaTime;

        //Cria bordas de limitação em X e Y
        if (PositionX >= MaxPositionX) {
            PositionX = MaxPositionX;
            VelocityX = 0;
        }

        if (PositionX < 0) {
            PositionX = 0;
            VelocityX = 0;
        }

        if (PositionY >= MaxPositionY) {
            PositionY = MaxPositionY;
            VelocityY = 0;
        }

        if (PositionY < 0) {
            PositionY = 0;
            VelocityY = 0;
        }
    }

    public void Reset() {
        PositionX = InitialPositionX;
        PositionY = InitialPositionY;
        VelocityX = InitialVelocityX;
        VelocityY = InitialVelocityY;
    }
}