package behaviours;
import agent.Arrow;

    public class Wander {

        // Configurações
        private double maxSpeed = 200;
        private double WanderRadius = 70;
        private double WanderDistance = 80;
        private double WanderJitter = 10.0; //Variação de ângulo

        // Dados do movimento
        private final Arrow Arrow;
        private double WanderAngle = 0;

        public Wander(Arrow Arrow) {
            this.Arrow = Arrow;
        }

        public void Update(double DeltaTime) {

            double PositionX = Arrow.GetPositionX();
            double PositionY = Arrow.GetPositionY();

            double VelocityX = Arrow.GetVelocityX();
            double VelocityY = Arrow.GetVelocityY();

            // Atualiza o ângulo aleatoriamente.
            WanderAngle += (Math.random() * 2 - 1) * WanderJitter * DeltaTime;

            // Calcula a direção atual.
            double VelocityLength = Math.sqrt(
                    Math.pow(VelocityX, 2) +
                            Math.pow(VelocityY, 2)
            );

            double ForwardX;
            double ForwardY;

            if (VelocityLength > 0.001) {
                ForwardX = VelocityX / VelocityLength;
                ForwardY = VelocityY / VelocityLength;
            } else {
                ForwardX = Math.cos(WanderAngle);
                ForwardY = Math.sin(WanderAngle);
            }

            // Calcula o centro do círculo.
            double CircleCenterX = ForwardX * WanderDistance;
            double CircleCenterY = ForwardY * WanderDistance;

            // Calcula o deslocamento dentro do círculo.
            double DisplacementX = Math.cos(WanderAngle) * WanderRadius;
            double DisplacementY = Math.sin(WanderAngle) * WanderRadius;

            // Define o destino temporário.
            double TargetX = PositionX + CircleCenterX + DisplacementX;
            double TargetY = PositionY + CircleCenterY + DisplacementY;

            // Calcula a direção desejada.
            double DirectionX = TargetX - PositionX;
            double DirectionY = TargetY - PositionY;

            double DirectionLength = Math.sqrt(
                    Math.pow(DirectionX, 2) +
                            Math.pow(DirectionY, 2)
            );

            if (DirectionLength > 0.001) {
                DirectionX /= DirectionLength;
                DirectionY /= DirectionLength;
            }

            // Calcula a velocidade desejada.
            double DesiredVelocityX = DirectionX * maxSpeed;
            double DesiredVelocityY = DirectionY * maxSpeed;

            // Calcula o Steering.
            double SteeringX = DesiredVelocityX - VelocityX;
            double SteeringY = DesiredVelocityY - VelocityY;

            // Atualiza a velocidade.
            double NewVelocityX = VelocityX + SteeringX * DeltaTime;
            double NewVelocityY = VelocityY + SteeringY * DeltaTime;

            Arrow.SetVelocity(NewVelocityX, NewVelocityY);
        }

        public void Reset() {
            WanderAngle = 0;
        }
    }