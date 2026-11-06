# Steering Behaviors

## Sobre o projeto

Este projeto implementa uma simulação básica de Steering Behaviors em Java, utilizando um agente autônomo capaz de navegar pelo ambiente e reagir a diferentes situações.
Objetivo: demonstrar diferentes técnicas de movimentação e tomada de decisão, permitindo combinar comportamentos por meio de pesos configuráveis.
A simulação possui uma interface gráfica para visualizar os comportamentos em tempo real e ajustar seus parâmetros.

## Controles

| Tecla | Função |
|---|---|
| **S** | Ativar Seek |
| **A** | Ativar Arrive |
| **O + Click** | Ativar Obstacle Avoidance |
| **P + Click** | Ligar / Desligar Path Following |
| **F** | Ativar Flee |
| **Wander** | Sempre ativo |

Obs.: Wander permanece ativo durante toda a simulação.

## Comportamentos implementados

### 1. Seek

Seek faz o agente se movimentar em direção a um ponto de destino.
Calculando a direção entre sua posição atual e o alvo, ajustando sua velocidade para seguir.

**Exemplos:**
- Perseguir um personagem ou objeto.
- Alcançar uma posição específica.

### 2. Flee

O Flee é o oposto de Seek. Em vez de se aproximar de um alvo, o agente procura se afastar dele.
Cálculos iguais ao do Seek, apenas invertendo os sinais.

**Exemplos:**
- Fugir de inimigos.
- Evitar ameaças.

### 3. Arrive

O Arrive permite que o agente alcance um destino desacelerando gradualmente conforme a distância.
Diferentemente de Seek, Arrive reduz a velocidade, evitando ultrapassar o destino.

**Exemplos:**
- Parar próximo a um objeto.
- Criar movimentações mais suaves.

### 4. Wander

O Wander permite que o agente se movimente de maneira aparentemente aleatória e contínua.
Ele produz pequenas variações na direção de movimentação, criando um deslocamento exploratório.
Wander permanece sempre ativo durante a simulação.

**Exemplos:**
- Simular exploração.
- Criar movimentação de NPCs.

### 5. Obstacle Avoidance

O Obstacle Avoidance permite que o agente identifique e desvie de obstáculos presentes no ambiente.
O sistema utiliza um leque de detecção à frente do agente (atualmente 90º) para identificar obstáculos próximos e calcular uma força de desvio.

**Exemplos:**
- Navegação em ambientes com obstáculos.
- Desvio de objetos.

### 6. Path Following

O Path Following permite que o agente percorra um caminho de pontos.
Os pontos são definidos pelo usuário na simulação. O agente procura alcançar cada ponto sequencialmente, em loop.

**Exemplos:**
- Patrulhamento.
- Seguir trajetórias predefinidas.

## Weighted Steering

O projeto utiliza Weighted Steering para combinar diferentes comportamentos em uma única simulação. 
Cada comportamento possui um peso que define sua influência no movimento final do agente.
As forças são multiplicadas pelos pesos e combinadas para gerar a direção.
Os pesos podem ser ajustados durante a simulação, permitindo experimentar diferentes combinações.

Obs.: Um peso igual a zero desativa a influência daquele comportamento na movimentação.

**Exemplos de combinações:**

- **Seek + Obstacle Avoidance:** o agente procura alcançar um destino enquanto desvia dos obstáculos.
- **Wander + Obstacle Avoidance:** o agente explora o ambiente sem colidir com os obstáculos.
- **Path Following + Obstacle Avoidance:** o agente percorre um caminho enquanto tenta evitar obstáculos.

## Interface e configurações

A interface permite controlar diferentes parâmetros da simulação em tempo real:
Peso dos comportamentos: influencia na movimentação final do agente.
Velocidade: controla a velocidade máxima.
Raio: configura o raio de influência utilizado pelo seek e flee.
Tamanho: controla o tamanho dos obstáculos criados na simulação.
Espessura: ajusta a espessura do rastro deixado pelo agente.
Cor: permite selecionar a cor do rastro por meio de um seletor de cores.

## Tecnologias utilizadas

- Java
- Java Swing
- Java AWT
- IntelliJ IDEA

## Objetivo acadêmico

O projeto foi desenvolvido para estudar técnicas de Steering Behaviors, explorando como diferentes forças de movimentação podem ser combinadas para produzir comportamentos suaves e automáticos.

## Referências

Os seguintes materiais foram utilizados como referência para o estudo e desenvolvimento dos comportamentos implementados neste projeto.

### Referências de Vídeos do YouTube

- **Seek:** [Week 5 - Part 1 - Seek Behavior](https://www.youtube.com/watch?v=ng2-9sNXpXM)
- **Flee:** [Coding Challenge #59: Steering Behaviors](https://www.youtube.com/watch?v=4hA7G3gup-4&t=459s)
- **Arrive:** [6.3: Steering Behaviors: Arrive - The Nature of Code](https://www.youtube.com/watch?v=2CL1maXeQCI)
- **Wander:** [5.5 Wander Steering Behavior - The Nature of Code](https://www.youtube.com/watch?v=ujsR2vcJlLk)
- **Collisions:** [AI Steering Behaviors In Godot : Collisions](https://www.youtube.com/watch?v=9oG57rqi64w)
- **Path Following:** [5.7 Path Following (Steering) - Nature of Code](https://www.youtube.com/watch?v=rlZYT-uvmGQ)

### Inteligência Artificial utilizada para correção de bugs e criação de interface visual.
