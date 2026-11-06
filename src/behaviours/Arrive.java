
package behaviours;

import agent.Arrow;
import java.awt.geom.Point2D;

public class Arrive {

    // Configurações
    private double maxSpeed;
    private double SlowRadius = 250;
    private double StopRadius = 2;
    private double CollisionRadius = 12;

    // Referência do objeto
    private final Arrow Arrow;

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

    public Arrive(Arrow Arrow) {
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

        if (HasReachedTarget()) {
            Reset();
            return new Point2D.Double(0, 0);
        }

        DirectionX = SteeringMath.CalculateDirectionX(PositionX, TargetX);
        DirectionY = SteeringMath.CalculateDirectionY(PositionY, TargetY);

        double Distance = SteeringMath.CalculateVectorLength(DirectionX, DirectionY);

        if (Distance <= StopRadius) {
            return new Point2D.Double(0, 0);
        }

        // Normaliza a direção
        double NormalizedX = SteeringMath.NormalizeX(DirectionX, DirectionY);
        double NormalizedY = SteeringMath.NormalizeY(DirectionX, DirectionY);

        // Reduz a velocidade conforme se aproxima
        double DesiredSpeed = maxSpeed;

        if (Distance < SlowRadius) {
            DesiredSpeed = maxSpeed * (Distance / SlowRadius);
        }

        double DesiredVelocityX = NormalizedX * DesiredSpeed;
        double DesiredVelocityY = NormalizedY * DesiredSpeed;

        double SteeringX = DesiredVelocityX - currentVelocityX;
        double SteeringY = DesiredVelocityY - currentVelocityY;

        return new Point2D.Double(SteeringX, SteeringY);
    }

    //verifica se a flecha já entrou na região de desaceleração
    public boolean IsInsideSlowRadius() {

        PositionX = Arrow.GetPositionX();
        PositionY = Arrow.GetPositionY();

        double DirectionX = SteeringMath.CalculateDirectionX(
                PositionX, TargetX
        );
        double DirectionY = SteeringMath.CalculateDirectionY(
                PositionY, TargetY
        );

        double Distance = SteeringMath.CalculateVectorLength(
                DirectionX, DirectionY
        );
        return Distance <= SlowRadius;
    }

    public boolean HasReachedTarget() {
        return Collision.CheckCollision(PositionX, PositionY, TargetX, TargetY, CollisionRadius);
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
}