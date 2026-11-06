package behaviours;

public class Collision { //Detecção de colisão para deletar objetos específicos
    public static boolean CheckCollision(
            double PositionX,
            double PositionY,
            double TargetX,
            double TargetY,
            double Radius) {

        double Distance = SteeringMath.CalculateDistance(
                PositionX, PositionY, TargetX, TargetY
        );

        return Distance <= Radius;
    }
}
