package behaviours;

import agent.Arrow;
import java.awt.geom.Point2D;

public class SteeringManager {

    // Pesos dos comportamentos
    private double SeekWeight = 0.15;
    private double FleeWeight = 0.30;
    private double WanderWeight = 0.05;
    private double ArriveWeight = 0.50;
    private double ObstacleAvoidanceWeight = 1.0;
    private double PathFollowingWeight = 1.0;

    // Limites do movimento
    private double MaxSteeringForce = 300;
    private double MaxSpeed = 200;

    // Referências
    private final Arrow Arrow;
    private final Seek Seek;
    private final Flee Flee;
    private final Wander Wander;
    private final Arrive Arrive;
    private final ObstacleAvoidance ObstacleAvoidance;
    private final PathFollowing PathFollowing;
    private boolean PathFollowingActive = false;

    public SteeringManager(
            Arrow Arrow,
            Seek Seek,
            Flee Flee,
            Wander Wander,
            Arrive Arrive,
            ObstacleAvoidance ObstacleAvoidance,
            PathFollowing PathFollowing) {

        this.Arrow = Arrow;
        this.Seek = Seek;
        this.Flee = Flee;
        this.Wander = Wander;
        this.Arrive = Arrive;
        this.ObstacleAvoidance = ObstacleAvoidance;
        this.PathFollowing = PathFollowing;

        SetMaxSpeed(MaxSpeed);
    }

    public void Update(double DeltaTime) {

        double SteeringX = 0;
        double SteeringY = 0;
        double TotalWeight = 0;

        // Wander
        if (WanderWeight > 0 &&
                !PathFollowingActive &&
                !(Arrive.GetIsActive() && Arrive.IsInsideSlowRadius()) &&
                !(Seek.GetIsActive() && Seek.IsNearTarget())) {

            Point2D.Double Force = Wander.CalculateSteering(DeltaTime);

            SteeringX += Force.x * WanderWeight;
            SteeringY += Force.y * WanderWeight;
            TotalWeight += WanderWeight;
        }

        // Seek
        if (Seek.GetIsActive() && Seek.IsInsideRadius() && SeekWeight > 0) {
            Point2D.Double Force = Seek.CalculateSteering();

            SteeringX += Force.x * SeekWeight;
            SteeringY += Force.y * SeekWeight;
            TotalWeight += SeekWeight;
        }

        // Flee
        if (Flee.GetIsActive() && Flee.IsInsideRadius() && FleeWeight > 0) {
            Point2D.Double Force = Flee.CalculateSteering();

            SteeringX += Force.x * FleeWeight;
            SteeringY += Force.y * FleeWeight;
            TotalWeight += FleeWeight;
        }

        // Arrive
        if (Arrive.GetIsActive() && ArriveWeight > 0) {
            Point2D.Double Force = Arrive.CalculateSteering();

            SteeringX += Force.x * ArriveWeight;
            SteeringY += Force.y * ArriveWeight;
            TotalWeight += ArriveWeight;
        }

        // Obstacle Avoidance //Para tirar a modificação que tem bug de não colidir é so comentar o if e a chave
        if (!Arrive.IsInsideSlowRadius()) {
            Point2D.Double AvoidanceForce = ObstacleAvoidance.CalculateSteering();

            if ((AvoidanceForce.x != 0 || AvoidanceForce.y != 0) && ObstacleAvoidanceWeight > 0) {
                SteeringX += AvoidanceForce.x * ObstacleAvoidanceWeight;
                SteeringY += AvoidanceForce.y * ObstacleAvoidanceWeight;
                TotalWeight += ObstacleAvoidanceWeight;
            }
        }

        // Path Following
        if (PathFollowingActive && PathFollowing.HasValidPath() && PathFollowingWeight > 0) {
            Point2D.Double Force = PathFollowing.CalculateSteering();

            SteeringX += Force.x * PathFollowingWeight;
            SteeringY += Force.y * PathFollowingWeight;
            TotalWeight += PathFollowingWeight;
        }

        // Normaliza os pesos ativos
        if (TotalWeight <= 0) {
            Arrow.SetVelocity(0, 0);
            return;
        }

        SteeringX /= TotalWeight;
        SteeringY /= TotalWeight;

        // Limita a força resultante
        double SteeringLength = SteeringMath.CalculateVectorLength(
                SteeringX, SteeringY
        );

        if (SteeringLength > MaxSteeringForce) {
            SteeringX = SteeringX / SteeringLength * MaxSteeringForce;
            SteeringY = SteeringY / SteeringLength * MaxSteeringForce;
        }

        // Aplica a força à velocidade atual
        double VelocityX = Arrow.GetVelocityX() + SteeringX * DeltaTime;
        double VelocityY = Arrow.GetVelocityY() + SteeringY * DeltaTime;

        // Limita a velocidade
        double VelocityLength = SteeringMath.CalculateVectorLength(
                VelocityX, VelocityY
        );

        if (VelocityLength > MaxSpeed) {
            VelocityX = VelocityX / VelocityLength * MaxSpeed;
            VelocityY = VelocityY / VelocityLength * MaxSpeed;
        }

        Arrow.SetVelocity(VelocityX, VelocityY);
    }

    // Acesso aos pesos para a interface
    //Acesso de Seek
    public double GetSeekWeight() {
        return SeekWeight;
    }
    public void SetSeekWeight(double Weight) {
        SeekWeight = Math.max(0, Weight);
    }

    //Acesso de Flee
    public double GetFleeWeight() {
        return FleeWeight;
    }
    public void SetFleeWeight(double Weight) {
        FleeWeight = Math.max(0, Weight);
    }

    //Acesso de Wander
    public double GetWanderWeight() {
        return WanderWeight;
    }
    public void SetWanderWeight(double Weight) {
        WanderWeight = Math.max(0, Weight);
    }

    //Acesso de Arrive
    public double GetArriveWeight() {
        return ArriveWeight;
    }
    public void SetArriveWeight(double Weight) {
        ArriveWeight = Math.max(0, Weight);
    }

    // Acesso de Obstacle Avoidance
    public double GetObstacleAvoidanceWeight() {
        return ObstacleAvoidanceWeight;
    }
    public void SetObstacleAvoidanceWeight(double Weight) { //Mesmo de PathFollowing
        ObstacleAvoidanceWeight = Math.max(0, Weight);
        if (ObstacleAvoidanceWeight == 0) {ObstacleAvoidance.ClearObstacles();}}

    //Acesso de PathFollowing
    public void SetPathFollowingActive(boolean Active) {
        PathFollowingActive = Active;
    }
    public boolean GetPathFollowingActive() {
        return PathFollowingActive;
    }
    public double GetPathFollowingWeight() {
        return PathFollowingWeight;
    }
    public void SetPathFollowingWeight(double Weight) {
        PathFollowingWeight = Math.max(0, Weight);

        if (PathFollowingWeight == 0) { //Limpa os pontos caso zere o peso, para limpeza apenas disso
            PathFollowingActive = false;
            PathFollowing.ClearPath();
        }
    }

    // Velocidade máxima
    public double GetMaxSpeed() {
        return MaxSpeed;
    }

    public void SetMaxSpeed(double MaxSpeed) { //Define a velocidade para todos os comportamentos
        this.MaxSpeed = Math.max(0, MaxSpeed);

        Seek.SetMaxSpeed(this.MaxSpeed);
        Flee.SetMaxSpeed(this.MaxSpeed);
        Arrive.SetMaxSpeed(this.MaxSpeed);
        Wander.SetMaxSpeed(this.MaxSpeed);
        ObstacleAvoidance.SetMaxSpeed(this.MaxSpeed);
        PathFollowing.SetMaxSpeed(this.MaxSpeed);
    }

    // Raio de influência
    public double GetInfluenceRadius() {
        return Seek.GetInfluenceRadius();
    }

    public void SetInfluenceRadius(double InfluenceRadius) {
        Seek.SetInfluenceRadius(InfluenceRadius);
        Flee.SetInfluenceRadius(InfluenceRadius);
    }
}