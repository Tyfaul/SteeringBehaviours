package behaviours;

import agent.Arrow;
import java.awt.geom.Point2D;

public class Flee {

    private final Arrow Arrow;

    // Configurações
    private double maxSpeed;
    private double InfluenceRadius = 300;

    // Posição
    private double PositionX;
    private double PositionY;
    private double TargetX;
    private double TargetY;

    // Velocidade
    private double currentVelocityX;
    private double currentVelocityY;

    // Direção
    private double DirectionX;
    private double DirectionY;

    private boolean IsActive = false;

    public Flee(Arrow Arrow) {
        this.Arrow = Arrow;
    }

    public void SetTarget(double TargetX, double TargetY) {
        this.TargetX = TargetX;
        this.TargetY = TargetY;
        SetActive(true);
    }

    // Calcula a força sem alterar a velocidade do Arrow
    public Point2D.Double CalculateSteering() {

        PositionX = Arrow.GetPositionX();
        PositionY = Arrow.GetPositionY();

        currentVelocityX = Arrow.GetVelocityX();
        currentVelocityY = Arrow.GetVelocityY();

        if (!IsInsideRadius()) {
            return new Point2D.Double(0, 0);
        }

        // Inverte a direção para fugir do alvo
        DirectionX = -SteeringMath.CalculateDirectionX(PositionX, TargetX);
        DirectionY = -SteeringMath.CalculateDirectionY(PositionY, TargetY);

        double NormalizedX = SteeringMath.NormalizeX(DirectionX, DirectionY);
        double NormalizedY = SteeringMath.NormalizeY(DirectionX, DirectionY);

        double DesiredVelocityX = NormalizedX * maxSpeed;
        double DesiredVelocityY = NormalizedY * maxSpeed;

        double SteeringX = DesiredVelocityX - currentVelocityX;
        double SteeringY = DesiredVelocityY - currentVelocityY;

        return new Point2D.Double(SteeringX, SteeringY);
    }

    // Mantém a compatibilidade temporária com o sistema atual
    public void Update(double DeltaTime) {
        Point2D.Double Steering = CalculateSteering();

        double NewVelocityX = currentVelocityX + Steering.x * DeltaTime;
        double NewVelocityY = currentVelocityY + Steering.y * DeltaTime;

        Arrow.SetVelocity(NewVelocityX, NewVelocityY);
    }

    public boolean IsInsideRadius() {
        PositionX = Arrow.GetPositionX();
        PositionY = Arrow.GetPositionY();

        double Distance = SteeringMath.CalculateDistance(
                PositionX, PositionY, TargetX, TargetY
        );

        return Distance <= InfluenceRadius;
    }

    public void SetActive(boolean IsActive) {
        this.IsActive = IsActive;
    }

    public boolean GetIsActive() {
        return IsActive;
    }

    public void Reset() {
        TargetX = 0;
        TargetY = 0;
        SetActive(false);
    }

    public void SetMaxSpeed(double MaxSpeed) {
        maxSpeed = Math.max(0, MaxSpeed);
    }
    public void SetInfluenceRadius(double InfluenceRadius) {
        this.InfluenceRadius = Math.max(0, InfluenceRadius);
    }
}