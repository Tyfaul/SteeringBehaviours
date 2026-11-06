package behaviours;
import agent.Arrow;

public class Arrive {

        // Configurações
        private double maxSpeed = 150;
        private double SlowRadius = 250;
        private double StopRadius = 2;

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

        private double CollisionRadius = 12;

        private boolean IsActive = false;

        public Arrive(Arrow Arrow) {
            this.Arrow = Arrow;
        }

        public void SetTarget(double TargetX, double TargetY) {
            this.TargetX = TargetX;
            this.TargetY = TargetY;
            SetActive(true);
        }

        public void Update(double DeltaTime) {

            PositionX = Arrow.GetPositionX();
            PositionY = Arrow.GetPositionY();

            // Collision
            if (HasReachedTarget()) {
                Arrow.SetVelocity(0, 0);
                Reset();
                return;
            }

            DirectionX = TargetX - PositionX;
            DirectionY = TargetY - PositionY;

            double Distance = Math.sqrt(
                    Math.pow(DirectionX, 2) +
                            Math.pow(DirectionY, 2)
            );

            // Para quando alcançar o destino
            if (Distance <= StopRadius) {
                Arrow.SetVelocity(0, 0);
                return;
            }

            // Normaliza a direção
            double NormalizedX = DirectionX / Distance;
            double NormalizedY = DirectionY / Distance;

            // Reduz a velocidade conforme se aproxima
            double DesiredSpeed = maxSpeed;

            if (Distance < SlowRadius) {
                DesiredSpeed = maxSpeed * (Distance / SlowRadius);
            }

            double DesiredVelocityX = NormalizedX * DesiredSpeed;
            double DesiredVelocityY = NormalizedY * DesiredSpeed;

            currentVelocityX = Arrow.GetVelocityX();
            currentVelocityY = Arrow.GetVelocityY();

            double SteeringX = DesiredVelocityX - currentVelocityX;
            double SteeringY = DesiredVelocityY - currentVelocityY;

            double NewVelocityX = currentVelocityX + SteeringX * DeltaTime;
            double NewVelocityY = currentVelocityY + SteeringY * DeltaTime;

            Arrow.SetVelocity(NewVelocityX, NewVelocityY);
        }

        //Collision
        public boolean HasReachedTarget() {
        return Collision.CheckCollision(
                PositionX,
                PositionY,
                TargetX,
                TargetY,
                CollisionRadius
        );
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
    }