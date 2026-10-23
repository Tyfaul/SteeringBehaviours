package agent;

public class Arrow {

    // Configurações iniciais
    private final double InitialPositionX = 500;
    private final double InitialPositionY = 350;
    private final double InitialVelocityX = 150;
    private final double InitialVelocityY = 0;

    // Estado atual da flecha
    private double PositionX = InitialPositionX;
    private double PositionY = InitialPositionY;
    private double VelocityX = InitialVelocityX;
    private double VelocityY = InitialVelocityY;

    public Arrow() {
    }
    public void SetVelocity(double VelocityX, double VelocityY){
        this.VelocityX = VelocityX;
        this.VelocityY = VelocityY;
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