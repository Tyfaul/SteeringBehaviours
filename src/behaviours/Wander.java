package behaviours;

import agent.Arrow;
import java.awt.geom.Point2D;

public class Wander {

    // Configurações
    private double maxSpeed;
    private double WanderRadius = 70;
    private double WanderDistance = 80;
    private double WanderJitter = 10.0;

    // Referência do objeto
    private final Arrow Arrow;

    // Dados do movimento
    private double WanderAngle = 0;

    public Wander(Arrow Arrow) {
        this.Arrow = Arrow;
    }

    public Point2D.Double CalculateSteering(double DeltaTime) {

        double PositionX = Arrow.GetPositionX();
        double PositionY = Arrow.GetPositionY();

        double VelocityX = Arrow.GetVelocityX();
        double VelocityY = Arrow.GetVelocityY();

        // Atualiza o ângulo aleatoriamente
        WanderAngle += (Math.random() * 2 - 1) * WanderJitter * DeltaTime;

        // Calcula a direção atual
        double VelocityLength = SteeringMath.CalculateVectorLength(VelocityX, VelocityY);

        double ForwardX;
        double ForwardY;

        if (VelocityLength > 0.001) {
            ForwardX = SteeringMath.NormalizeX(VelocityX, VelocityY);
            ForwardY = SteeringMath.NormalizeY(VelocityX, VelocityY);
        } else {
            ForwardX = Math.cos(WanderAngle);
            ForwardY = Math.sin(WanderAngle);
        }

        // Calcula o centro do círculo
        double CircleCenterX = ForwardX * WanderDistance;
        double CircleCenterY = ForwardY * WanderDistance;

        // Calcula o deslocamento dentro do círculo
        double DisplacementX = Math.cos(WanderAngle) * WanderRadius;
        double DisplacementY = Math.sin(WanderAngle) * WanderRadius;

        // Define o destino temporário
        double TargetX = PositionX + CircleCenterX + DisplacementX;
        double TargetY = PositionY + CircleCenterY + DisplacementY;

        // Calcula a direção desejada
        double DirectionX = SteeringMath.CalculateDirectionX(PositionX, TargetX);
        double DirectionY = SteeringMath.CalculateDirectionY(PositionY, TargetY);

        double DirectionLength = SteeringMath.CalculateVectorLength(DirectionX, DirectionY);

        if (DirectionLength > 0.001) {
            double NormalizedX = SteeringMath.NormalizeX(DirectionX, DirectionY);
            double NormalizedY = SteeringMath.NormalizeY(DirectionX, DirectionY);

            DirectionX = NormalizedX;
            DirectionY = NormalizedY;
        }

        // Calcula a velocidade desejada
        double DesiredVelocityX = DirectionX * maxSpeed;
        double DesiredVelocityY = DirectionY * maxSpeed;

        // Calcula a força
        double SteeringX = DesiredVelocityX - VelocityX;
        double SteeringY = DesiredVelocityY - VelocityY;

        return new Point2D.Double(SteeringX, SteeringY);
    }

    public void Reset() {
        WanderAngle = 0;
    }

    public void SetMaxSpeed(double MaxSpeed) {
        maxSpeed = Math.max(0, MaxSpeed);
    }
}