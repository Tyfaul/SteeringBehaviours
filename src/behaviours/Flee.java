package behaviours;

import agent.Arrow;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Flee {
    private Arrow Arrow;

    public Flee(Arrow Arrow) {
        this.Arrow = Arrow;
    }

        private boolean IsActive = false;

        // Positions
        private double PositionX;
        private double PositionY;
        private double TargetX;
        private double TargetY;

        // Velocity
        private double maxSpeed = 200;
        private double currentVelocityX;
        private double currentVelocityY;

        // Direction
        private double DirectionX;
        private double DirectionY;

    private double InfluenceRadius = 150;

    public void SetTarget(double TargetX, double TargetY) {
        this.TargetX = TargetX;
        this.TargetY = TargetY;
        SetActive(true);
    }

    //Mesma base de Seek, apenas invertendo Position e Target (✿◠‿◠)
    public double CalculateDirectionX(double PositionX, double TargetX){
        return PositionX - TargetX;
    }
    public double CalculateDirectionY(double PositionY, double TargetY){
        return PositionY - TargetY;
    }

    private double CalculateVectorLength() {
        return sqrt(pow(CalculateDirectionX(PositionX, TargetX), 2) + pow(CalculateDirectionY(PositionY, TargetY), 2));
    }

    private double CalculateNormalizedX() { //Evitar divisão por Zero
        double Length = CalculateVectorLength();
        if (Length > 0) {
            return DirectionX / Length;
        }
        return 0;
    }
    private double CalculateNormalizedY() { //Evitar divisão por Zero
        double Length = CalculateVectorLength();
        if (Length > 0) {
            return DirectionY / Length;
        }
        return 0;
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

    public void Update(double DeltaTime) {
        PositionX = Arrow.GetPositionX();
        PositionY = Arrow.GetPositionY();

        DirectionX = CalculateDirectionX(PositionX, TargetX);
        DirectionY = CalculateDirectionY(PositionY, TargetY);

        currentVelocityX = Arrow.GetVelocityX();
        currentVelocityY = Arrow.GetVelocityY();

        double NewVelocityX = currentVelocityX + CalculateSteeringX() * DeltaTime;
        double NewVelocityY = currentVelocityY + CalculateSteeringY() * DeltaTime;

        Arrow.SetVelocity(NewVelocityX, NewVelocityY);
    }

    public void SetActive(boolean IsActive) {
        this.IsActive = IsActive;
    }

    public boolean GetIsActive() {
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

    public void Reset() {
        TargetX = 0;
        TargetY = 0;
        SetActive(false);
    }
}