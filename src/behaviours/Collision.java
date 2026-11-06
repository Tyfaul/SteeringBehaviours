package behaviours;

public class Collision { //Detecção de colisão para deletar objetos específicos
    public static boolean CheckCollision(
            double PositionX,
            double PositionY,
            double TargetX,
            double TargetY,
            double Radius) {

        double DistanceX = TargetX - PositionX;
        double DistanceY = TargetY - PositionY;

        double Distance = Math.sqrt(
                Math.pow(DistanceX, 2) +
                        Math.pow(DistanceY, 2)
        );

        return Distance <= Radius;
    }
}
