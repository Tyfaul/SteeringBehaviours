package behaviours;

import agent.Arrow;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class PathFollowing {

    private final Arrow Arrow;
    private final ArrayList<Point> PathPoints;
    private double maxSpeed = 200;
    private int CurrentPoint = 0;

    public PathFollowing(Arrow Arrow, ArrayList<Point> PathPoints) {
        this.Arrow = Arrow;
        this.PathPoints = PathPoints;
    }

    public boolean HasValidPath() {
        return PathPoints.size() >= 2;
    }

    public Point2D.Double CalculateSteering() {

        if (PathPoints.size() < 2) return new Point2D.Double(0, 0);

        Point Target = PathPoints.get(CurrentPoint);

        double Distance = SteeringMath.CalculateDistance(Arrow.GetPositionX(), Arrow.GetPositionY(), Target.x, Target.y);

        if (Distance < 20) {
            CurrentPoint = (CurrentPoint + 1) % PathPoints.size();
            Target = PathPoints.get(CurrentPoint);
        }

        double DirectionX = SteeringMath.NormalizeX(Target.x - Arrow.GetPositionX(), Target.y - Arrow.GetPositionY());
        double DirectionY = SteeringMath.NormalizeY(Target.x - Arrow.GetPositionX(), Target.y - Arrow.GetPositionY());

        double DesiredX = DirectionX * maxSpeed;
        double DesiredY = DirectionY * maxSpeed;

        return new Point2D.Double(DesiredX - Arrow.GetVelocityX(), DesiredY - Arrow.GetVelocityY());
    }

    public void SetMaxSpeed(double MaxSpeed) {this.maxSpeed = MaxSpeed;}

    public void Reset() {CurrentPoint = 0;}

    public void ClearPath() {
        PathPoints.clear();
        CurrentPoint = 0;
    }
}