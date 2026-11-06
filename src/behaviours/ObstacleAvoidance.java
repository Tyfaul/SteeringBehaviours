package behaviours;

import agent.Arrow;
import agent.Obstacle;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class ObstacleAvoidance {

    private final Arrow Arrow;
    private final ArrayList<Obstacle> Obstacles;

    private double DetectionLength = 150;
    private double DetectionAngle = 45; //O dobro é o angulo do leque de visão
    private double maxSpeed;

    public ObstacleAvoidance(Arrow Arrow, ArrayList<Obstacle> Obstacles) {
        this.Arrow = Arrow;
        this.Obstacles = Obstacles;
    }

    public Point2D.Double CalculateSteering() {

        double VelocityX = Arrow.GetVelocityX();
        double VelocityY = Arrow.GetVelocityY();

        double Speed = SteeringMath.CalculateVectorLength(VelocityX, VelocityY);

        if (Speed <= 0) {return new Point2D.Double(0, 0);}

        double ForwardX = VelocityX / Speed;
        double ForwardY = VelocityY / Speed;

        double ClosestDistance = Double.MAX_VALUE;
        Obstacle ClosestObstacle = null;

        for (Obstacle Obstacle : Obstacles) {

            double DirectionX = Obstacle.GetPositionX() - Arrow.GetPositionX();
            double DirectionY = Obstacle.GetPositionY() - Arrow.GetPositionY();

            double Distance = SteeringMath.CalculateVectorLength(DirectionX, DirectionY);

            double ObstacleRadius = Obstacle.GetSize() * 0.7071;

            double Dot = (DirectionX * ForwardX + DirectionY * ForwardY) / Math.max(Distance, 0.001);

            if (Dot >= Math.cos(Math.toRadians(DetectionAngle)) &&
                    Distance <= DetectionLength + ObstacleRadius &&
                    Distance < ClosestDistance) {
                ClosestDistance = Distance;
                ClosestObstacle = Obstacle;
            }
        }

        if (ClosestObstacle == null) {return new Point2D.Double(0, 0);}

        double DirectionX = ClosestObstacle.GetPositionX() - Arrow.GetPositionX();
        double DirectionY = ClosestObstacle.GetPositionY() - Arrow.GetPositionY();

        double Cross = ForwardX * DirectionY - ForwardY * DirectionX;

        double AvoidanceX;
        double AvoidanceY;

        if (Cross >= 0) {
            AvoidanceX = ForwardY;
            AvoidanceY = -ForwardX;
        } else {
            AvoidanceX = -ForwardY;
            AvoidanceY = ForwardX;
        }

        return new Point2D.Double(AvoidanceX * maxSpeed, AvoidanceY * maxSpeed);
    }

    public void SetMaxSpeed(double MaxSpeed) {
        maxSpeed = Math.max(0, MaxSpeed);
    }
}