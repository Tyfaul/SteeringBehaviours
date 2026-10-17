package agent;

public class Arrow {
    private double PositionX; //Posição horizontal
    private double PositionY; //Posição vertical
    private double VelocityX; //Velocidade horizontal
    private double VelocityY; //Velocidade vertical

    public Arrow(double PositionX, double PositionY, double VelocityX, double VelocityY){ //Armazenamento de Instância
        this.PositionX = PositionX;
        this.PositionY = PositionY;
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

        //Limita Movimentação em X e Y, parando na borda
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
}
