package behaviours;

public class SteeringMath {
    public static double CalculateDirectionX(double PositionX, double TargetX) {
        return TargetX - PositionX;
    }

    public static double CalculateDirectionY(double PositionY, double TargetY) {
        return TargetY - PositionY;
    }

    public static double CalculateVectorLength(double VectorX, double VectorY) {
        return Math.sqrt(VectorX * VectorX + VectorY * VectorY);
    }

    public static double CalculateDistance(
            double PositionX,
            double PositionY,
            double TargetX,
            double TargetY) {

        double DistanceX = CalculateDirectionX(PositionX, TargetX);
        double DistanceY = CalculateDirectionY(PositionY, TargetY);

        return CalculateVectorLength(DistanceX, DistanceY);
    }

    public static double NormalizeX(double VectorX, double VectorY) {
        double Length = CalculateVectorLength(VectorX, VectorY);

        return Length > 0 ? VectorX / Length : 0;
    }

    public static double NormalizeY(double VectorX, double VectorY) {
        double Length = CalculateVectorLength(VectorX, VectorY);

        return Length > 0 ? VectorY / Length : 0;
    }
}
