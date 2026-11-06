package behaviours;

import static java.lang.Math.sqrt;
import static java.lang.Math.pow;
import agent.Arrow;

public class Seek {

    private Arrow Arrow;
    public Seek (Arrow Arrow){
        this.Arrow = Arrow;
        System.out.println(Arrow == null);
    }

    private boolean IsActive = false;

    //Positions
    private double PositionX;
    private double PositionY;
    private double TargetX;
    private double TargetY;

    //Velocity
    private double maxSpeed = 200;
    private double currentVelocityX;
    private double currentVelocityY;

    //Direction
    private double DirectionX;
    private double DirectionY;

    private double InfluenceRadius = 300;

    public double CalculateDirectionX(double PositionX, double TargetX){
        return TargetX - PositionX;
    }
    public double CalculateDirectionY(double PositionY, double TargetY){
        return TargetY - PositionY;
    }

    private double CalculateVectorLength() {
        return sqrt(pow(CalculateDirectionX(PositionX, TargetX), 2) + pow(CalculateDirectionY(PositionY, TargetY), 2));
    }

    private double CalculateNormalizedX(){
        if (DirectionX != 0){
            return DirectionX / CalculateVectorLength();
        }
        else {return 0;}
    }
    private double CalculateNormalizedY(){
        if (DirectionY != 0){
            return DirectionY / CalculateVectorLength();
        }
        else {return 0;}
    }

    private double CalculateDesiredVelocityX(){
        return CalculateNormalizedX() * maxSpeed;
    }
    private double CalculateDesiredVelocityY(){
        return CalculateNormalizedY() * maxSpeed;
    }

    private double CalculateSteeringX(){
        return CalculateDesiredVelocityX() - currentVelocityX;
    }
    private double CalculateSteeringY(){
        return CalculateDesiredVelocityY() - currentVelocityY;
    }

    public void SetTarget(double TargetX, double TargetY){
        this.TargetX = TargetX;
        this.TargetY = TargetY;
        SetActive(true);
    }

    public void Update(double DeltaTime) {
        PositionX = Arrow.GetPositionX();
        PositionY = Arrow.GetPositionY();

        DirectionX = CalculateDirectionX(PositionX, TargetX);
        DirectionY = CalculateDirectionY(PositionY, TargetY);

        currentVelocityX = Arrow.GetVelocityX();
        currentVelocityY = Arrow.GetVelocityY();

        CalculateSteeringX();
        CalculateSteeringY();

        double NewVelocityX = currentVelocityX + CalculateSteeringX() * DeltaTime;
        double NewVelocityY = currentVelocityY + CalculateSteeringY() * DeltaTime;

        Arrow.SetVelocity(NewVelocityX, NewVelocityY);
    }

    public void SetActive(boolean IsActive){
        this.IsActive = IsActive;
    }
    public boolean GetIsActive(){
        return IsActive;
    }

    //Raio de Influencia
    public boolean IsInsideRadius() {
        PositionX = Arrow.GetPositionX();
        PositionY = Arrow.GetPositionY();

        double DistanceX = TargetX - PositionX;
        double DistanceY = TargetY - PositionY;

        double Distance = sqrt(pow(DistanceX, 2) + pow(DistanceY, 2));

        return Distance <= InfluenceRadius;
    }

    //Limpar os pontos ao reiniciar
    public void Reset() {
        TargetX = 0;
        TargetY = 0;
        SetActive(false);
    }
}